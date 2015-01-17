package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

public class Car implements Steppable {
    protected int carID;
    protected double maxWeight = 50;
    protected List<Parcel> carrying = new LinkedList<Parcel>();
    protected int speed;
    protected LinkedList<Int2D> pathLocal = new LinkedList<Int2D>();
    protected Station stationFrom;
    protected Station stationTo;
    protected Int2D location;
    protected Map map;
    private int stepCount = 0;
    private boolean hasArrived = false;
    private boolean hasLeaved = false;
    private LinkedList<Station> globalPath;


    public Car(int carID, Int2D location, Map map) {
        this.carID = carID;
        this.location = location;
        this.map = map;
    }

    public String toString() {
        return "Car :" + carID;
    }


    public boolean loadParcel() {
        Station s = currStation();
        if (s.pToBeSent.size() == 0) return false;
        // TODO load package number

        Parcel p = s.pToBeSent.get(0);
        s.pToBeSent.remove(0);
        System.out.println("Log: " + p + " has been picked up by " + this);
        carrying.add(p);
        return true;
    }

    // unload parcel
    public void unloadParcel() {
        Station s = currStation();

        List<Parcel> unload = parcelsToUnload(s);
        // when there is a package is unloaded and the package is the first package in the carrying list, then reset the global Path
        if(unload.size()>0 && unload.get(0).equals(carrying.get(0))){
            globalPath = null;
        }
        carrying.removeAll(unload);

        List<Parcel> copyOfUnload = new LinkedList<Parcel>();
        copyOfUnload.addAll(unload);

        // remove car Caller
        for (Parcel p : copyOfUnload) {
            if (p instanceof CarCaller) {
                System.out.println("Log: " + this + " has unloaded" +
                        " " + p + "...");
                currStation().carCallerSema++;
                unload.remove(p);
            }
        }

        s.pArrived.addAll(unload);

        if (unload.size() > 0) {
            System.out.print("Log: Global parcels remaining ");
            System.out.println(map.parcelTotal -= (unload.size()));
        }
    }

    // for the parcels that have arrived the final destination
    private List<Parcel> parcelsToUnload(Station s) {
        List<Parcel> toUnload = new LinkedList<Parcel>();
        for (Parcel p : carrying) {
            if (p.destination.stationID == s.stationID) {
                toUnload.add(p);
                System.out.println("Log: " + this + " has unloaded" +
                        " " + p + "...");
            }
        }
        return toUnload;
    }

    // arrive carpark
    public void arriveStation() {
        // get current statition
        Station currStation = currStation();

        // remove the car from the road
        TramLine tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

        if (tramLine != null)
            tramLine.carsOnTramLine.remove(this);

        pathLocal.clear();
        this.stationFrom = currStation;
        stationTo = null;
        stepCount = 0;
        unloadParcel();
        loadParcel();
        hasArrived = true;

        // if the car has not enter the station
        if (!currStation.carPark.contains(this))
            currStation.carPark.add(this);

        // if there are something to be delivered
        if (!carrying.isEmpty()) {

            // get the parcel with highest priority
            Parcel p = carrying.get(0);

            // find the final destination of the parcel as target Station.
            Station targetStation = map.stations.get(0).findStationByID(p.destination.stationID);

            // set the current station as station from, the next station as station to.
            setPathGlobal(currStation, targetStation);

            // calculate the path from current station to the next station
            setPathLocal(currStation, stationTo);

        }
    }

    // leave carpark
    public void leaveStation() {
        hasLeaved = true;
    }

    private void setPathLocal(Station from, Station to) {
        pathLocal = map.tramLines.get(0).getStepsNB(from, to);
    }

    private void setPathGlobal(Station from, Station to) {
        TramLine tl = map.tramLines.get(0);
        Station currStation = currStation();

        if(globalPath==null) {
            globalPath = tl.getPathGlobal(from, to);
        }

        // the station is the next station to not the final destination.
        // TODO don't need null condition??
        if (tl == null) {
            stationTo = currStation;
        } else {
            stationTo = globalPath.get(globalPath.indexOf(currStation)+1);
        }

    }


    private Station currStation() {
        return map.stations.get(0).findStationByLoc(this.location);
    }


    @Override
    public void step(SimState state) {

        Station currStation = currStation();
        if (currStation != null) {
            if (!hasArrived) {
                arriveStation();
                // this return is for waite one step after arrival
                return;
            } else {
                if (this.carrying.isEmpty()) {
                    this.arriveStation();
                }
            }

            TramLine tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

            // can not find a tram Line to destination
            if (tramLine == null) {
                return;
            }

            if (!hasLeaved) {
                if (tramLine.okToLeave(currStation)) {
                    // leave the car park one by one -- FIFO
                    if (tramLine.currLeavingCar != null) {
                        return;
                    }
                    tramLine.currLeavingCar = this;
                    leaveStation();
                    tramLine.carsOnTramLine.add(this);
                    return;
                } else {
                    tramLine.tryOccupyTraffic(currStation);
                    return;
                }
            }

            // delay one step of leaving the car park, Truly leave
            if (hasLeaved) {
                if (tramLine.currLeavingCar.equals(this)) {
                    tramLine.currLeavingCar = null;
                }
                if (tramLine.a.equals(currStation)) {
                    tramLine.quota1--;
                } else {
                    tramLine.quota2--;
                }
                currStation.carPark.remove(this);
                hasArrived = false;
                hasLeaved = false;
            }
        }


        if (!carrying.isEmpty()) {
            // get the next step location
            Int2D nextStep = this.pathLocal.get(stepCount);

            // if the next step location has been occupied then waite
//            for (Car c : map.cars) {
//                if ( c.location.equals(nextStep)&&(!map.stations.get(0).isStation(nextStep)))
//                    return;
//            }

            // move
            while (this.location.equals(nextStep)) {
                stepCount++;
                nextStep = this.pathLocal.get(stepCount);
            }

            this.location = nextStep;
            map.mapGrid.setObjectLocation(this, nextStep);
            stepCount++;
        }
    }
}





