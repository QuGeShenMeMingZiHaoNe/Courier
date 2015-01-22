package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class Car extends OvalPortrayal2D implements Steppable {
    public static final int maxSpace = 5;
    public Shape shape;
    protected int carID;
    protected int spaceRemaining = maxSpace;
    protected int speed;
    protected LinkedList<Int2D> pathLocal = new LinkedList<Int2D>();
    protected ExpressCentre stationFrom;
    protected ExpressCentre stationTo;
    protected Int2D location;
    protected Map map;
    Ellipse2D.Double preciseEllipse = new Ellipse2D.Double();
    private LinkedList<Parcel> carrying = new LinkedList<Parcel>();
    private int stepCount = 0;
    private boolean hasArrived = false;
    private boolean hasLeaved = false;
    private LinkedList<ExpressCentre> globalPath;
    private int basicCarDisplaySize = 2;
    private boolean moving = true;


    public Car(int carID, Int2D location, Map map) {
        this.carID = carID;
        this.location = location;
        this.map = map;
    }

    public int getCurrLoading() {
        return maxSpace - spaceRemaining;
    }

    public LinkedList<Parcel> getCarrying() {
        return carrying;
    }

    public String toString() {
        return "Car :" + carID;
    }

    public boolean loadParcel() {
        ExpressCentre s = currStation();
        if (s.pToBeSent.size() == 0) return false;
        LinkedList<Parcel> parcelsCouldBeLoad = new LinkedList<Parcel>();
        parcelLoader(parcelsCouldBeLoad);
        carrying.addAll(parcelsCouldBeLoad);
        return true;
    }

    // unload parcel
    public void unloadParcel() {
        ExpressCentre s = currStation();

        List<Parcel> unload = parcelsToUnload(s);
        // when there is a package is unloaded and the package is the first package in the carrying list, then reset the global Path
        if (unload.size() > 0) {
            globalPath = null;
        }
        carrying.removeAll(unload);

        s.pArrived.addAll(unload);

        if (unload.size() > 0) {
            System.out.print("Log: Global parcels remaining ");
            System.out.println(map.parcelTotal -= (unload.size()));
        }
    }

    // for the parcels that have arrived the final destination
    private List<Parcel> parcelsToUnload(ExpressCentre s) {
        List<Parcel> toUnload = new LinkedList<Parcel>();
        LinkedList<Parcel> carCallerToUnload = new LinkedList<Parcel>();
        for (Parcel p : carrying) {
            if (p.destination.equals(s)) {
                // TODO: does car caller earn money??
                map.profit += p.getProfit();

                System.out.println("Log: " + this + " has unloaded" + " " + p + " with wight " + p.weight + " with time spending " + p.getTimeSpending() + "...");
                // restore the released weight to the car
                this.spaceRemaining += p.weight;

                // if the parcel is car caller
                if (p instanceof CarCaller) {
                    currStation().carCallerSema++;
                    carCallerToUnload.add(p);
                    globalPath = null;
                } else {
                    toUnload.add(p);
                }
            }
        }
        carrying.removeAll(carCallerToUnload);
        return toUnload;
    }

    // arrive carpark
    public void arriveStation() {
        // get current statition
        ExpressCentre currStation = currStation();
        moving = false;
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
            ExpressCentre targetStation = map.allStations.get(0).findStationByID(p.destination.stationID);

            // set the current station as station from, the next station as station to.
            setPathGlobal(currStation, targetStation,null,SIMULATION_MODE.BASIC);

            // calculate the path from current station to the next station
            setPathLocal(currStation, stationTo);

        }
    }

    // leave carpark
    public void leaveStation() {
        hasLeaved = true;
    }

    private void setPathLocal(ExpressCentre from, ExpressCentre to) {
        pathLocal = map.tramLines.get(0).getPathBetweenNBStations(from, to);
    }

    private void setPathGlobal(ExpressCentre from, ExpressCentre to,ExpressCentre avoidStation, SIMULATION_MODE mode) {
        TramLine tl = map.tramLines.get(0);
        ExpressCentre currStation = currStation();

        if (globalPath == null) {
                globalPath = tl.getPathGlobal(from, to,avoidStation,mode);
        }

        // if no such path exist then set the destination to currStation which will let the car stay still
//        if (globalPath == null) {
//        stationTo = currStation;
//        } else {
        stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
//        }

    }

    private ExpressCentre currStation() {
        ExpressCentre s;
//        if(map.garages.size()>0) {
//            s = map.garages.get(0).findGarageByLoc(this.location);
//            if(s != null)
//                return s;
//        }
        s = map.allStations.get(0).findStationByLoc(this.location);
        return s;
    }

    // calculate what parcel can be put into the car with the given loading weight
    private void parcelLoader(LinkedList<Parcel> parcelsCouldBeLoad) {
        boolean newParcelAdded = false;
        ExpressCentre currStation = currStation();

        for (Parcel p : currStation.pToBeSent) {
            if (p.weight <= spaceRemaining) {
                spaceRemaining -= p.weight;
                newParcelAdded = true;
                parcelsCouldBeLoad.add(p);
                System.out.println("Log: " + p + " with weight " + p.weight + " has been picked up by " + this);
                break;
            }
        }
        if (newParcelAdded) {
            currStation.pToBeSent.remove(parcelsCouldBeLoad.getLast());
            parcelLoader(parcelsCouldBeLoad);
        }
    }

    //  graphics
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double scale = 2.0;
        Color paint;

        // colour of cars change by the loading of the cars
        if (spaceRemaining >= 0.8 * maxSpace) {
            paint = Color.green;
        } else if (spaceRemaining >= 0.4 * maxSpace) {
            paint = Color.orange;
        } else {
            paint = Color.red;
        }
        Rectangle2D.Double draw = info.draw;
        final double width = draw.width * (scale) + basicCarDisplaySize;
        final double height = draw.height * (scale) + basicCarDisplaySize;

        graphics.setPaint(paint);
        // we are doing a simple draw, so we ignore the info.clip


        // we must be transient because Ellipse2D.Double is not serializable.
        // We also check to see if it's null elsewhere (because it's transient).

        if (info.precise) {
            if (preciseEllipse == null)
                preciseEllipse = new Ellipse2D.Double();    // could get reset because it's transient
            preciseEllipse.setFrame(info.draw.x - width / 2.0, info.draw.y - height / 2.0, width, height);
            if (true) graphics.fill(preciseEllipse);
            else graphics.draw(preciseEllipse);
            return;
        }

        final int x = (int) (draw.x - width / 2.0);
        final int y = (int) (draw.y - height / 2.0);
        int w = (int) (width);
        int h = (int) (height);

        // draw centered on the origin
        if (true)
            graphics.fillOval(x, y, w, h);
        else
            graphics.drawOval(x, y, w, h);
    }

    @Override
    public void step(SimState state) {
//        if (!(currStation() instanceof Garage)) {
//            Int2D d = new Int2D(1, 1);
//            map.profit -= d.distance(new Int2D(2, 2));
//        }
        ExpressCentre currStation = currStation();
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

            // if the car has not carrying any thing that it don't need to leave the station
            if (carrying.isEmpty())
                return;

            TramLine tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

            // can not find a tram Line to destination
            if (tramLine == null) {
                return;
            }

            if (!hasLeaved) {
                if (tramLine.okToLeave(currStation)) {
                    // leave the car park one by one -- FIFO
                    if (tramLine.currLeavingCars != null) {
                        return;
                    }
                    tramLine.currLeavingCars = this;
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
                if (tramLine.currLeavingCars.equals(this)) {
                    tramLine.currLeavingCars = null;
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


//        if (!carrying.isEmpty()) {
        // get the next step location
        Int2D nextStep = this.pathLocal.get(stepCount);

        // move
        while (this.location.equals(nextStep)) {
            stepCount++;
            nextStep = this.pathLocal.get(stepCount);
        }

        this.location = nextStep;
        map.mapGrid.setObjectLocation(this, nextStep);
        stepCount++;
//        }
    }
}





