package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

public class Car implements Steppable {
    public int carID;
    public double maxWeight = 50;
    public List<Parcel> carrying = new LinkedList<Parcel>();
    public int speed;
    public LinkedList<Int2D> pathLocal = new LinkedList<Int2D>();
    public Station stationFrom;
    public Station stationTo;
    public Int2D location;
    public Map map;
    public Station direction;
    private int stepCount=0;
    private boolean hasArrived = false;
    private boolean hasLeaved = false;



    public Car(int carID, Int2D location, Map map) {
        this.carID = carID;
        this.location = location;
        this.map = map;
    }


    public boolean loadParcel() {
        Station s = currStation();
        if (s.pToBeSent.size() == 0) return false;
        // TODO load package number
        Parcel p = s.pToBeSent.get(0);
        s.pToBeSent.remove(0);
        carrying.add(p);
        return true;
    }

    // unload parcel
    public void unloadParcel() {
        Station s = currStation();

        List<Parcel> unload = parcelsToUnload(s);
        carrying.removeAll(unload);
        s.pArrived.addAll(unload);
    }

    // for the parcels that have arrived the final destination
    private List<Parcel> parcelsToUnload(Station s) {
        List<Parcel> toUnload = new LinkedList<Parcel>();
        for (Parcel p : carrying) {
            if (p.destination.stationID == s.stationID) {
                toUnload.add(p);
            }
        }
        return toUnload;
    }

    // arrive carpark
    public boolean arriveStation() {
        Station currStation = currStation();
        currStation.carPark.add(this);
        pathLocal.clear();
        this.stationFrom = currStation;
        stepCount = 0;
        hasArrived = true;
        unloadParcel();
        loadParcel();

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

            // remove the car from the road into station
            Tramline tramLine = map.tramlines.get(0).findTramLine(stationFrom,stationTo);
            tramLine.carsOnTramline.remove(this);

        }
        return true;
    }

    // leave carpark
    public void leaveStation() {
        hasLeaved = true;
    }

    private void setPathLocal(Station from, Station to) {
        Tramline tl = map.tramlines.get(0);
        pathLocal = tl.getStepsNB(from, to);
    }

    private void setPathGlobal(Station from, Station to) {
        Tramline tl = map.tramlines.get(0);
        Station currStation = currStation();

        // TODO get 0 , return a station??
        tl = tl.getPathGlobal(from, to);

        if (tl.a.equals(currStation)) {
            stationTo = tl.b;
            direction = tl.b;
        } else {
            stationTo = tl.a;
            direction = tl.a;
        }
    }



    private Station currStation() {
        return map.stations.get(0).findStationByLoc(this.location);
    }

    @Override
    public void step(SimState state) {

        Station currStation = currStation();
        if (currStation != null) {
            if(!hasArrived) {
                arriveStation();
                // this return is for waite one step after arrival
                return;
            }

            Tramline tramLine = map.tramlines.get(0).findTramLine(stationFrom,stationTo);

            if (!hasLeaved) {
                if(tramLine.okToLeave(currStation)) {
                    // leave the car park one by one -- FIFO
                    if (tramLine.currLeavingCar!=null) {
                        return;
                    }
                    tramLine.currLeavingCar = this;
                    leaveStation();
                    tramLine.carsOnTramline.add(this);
                    return;
            }else{
                tramLine.tryOccupyTraffic(currStation);
                return;
            }
            }

            // delay one step of leaving the car park, Truly leave
            if(hasLeaved){
                if(tramLine.currLeavingCar.equals(this)){
                    tramLine.currLeavingCar = null;
                }
                if(tramLine.a.equals(currStation)){
                    tramLine.quota1--;
                }else{
                    tramLine.quota2--;
                }
                currStation.carPark.remove(this);
                hasArrived = false;
                hasLeaved = false;
            }
        }


        if (!carrying.isEmpty()) {
            Int2D nextStep = this.pathLocal.get(stepCount);
            this.location = nextStep;
            map.mapGrid.setObjectLocation(this, nextStep);
            stepCount++;
        }
    }
}
















