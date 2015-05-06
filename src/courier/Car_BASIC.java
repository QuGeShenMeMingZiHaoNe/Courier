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

public class Car_BASIC extends OvalPortrayal2D implements Steppable {
    protected static final int maxSpace = 100;
    protected int spaceRemaining = maxSpace;

    protected final int basicCarDisplaySize = 2;
    protected Shape shape;
    protected int carID;
    protected int speed;
    protected LinkedList<Int2D> pathLocal = new LinkedList<Int2D>();
    protected ExpressCentre stationFrom;
    protected ExpressCentre stationTo;
    protected Int2D location;
    protected Map map;
    protected Ellipse2D.Double preciseEllipse = new Ellipse2D.Double();
    protected LinkedList<Parcel> carrying = new LinkedList<Parcel>();
    protected int stepCount = 0;
    protected boolean hasArrived = false;
    protected boolean hasLeaved = false;
    protected LinkedList<ExpressCentre> globalPath;
    protected boolean moving = true;
    protected boolean alterPath = false;
    protected ExpressCentre currStation;
    protected boolean stepping = false;
    protected boolean carParkTicket = false;
//    private LinkedList<ExpressCentre> refusedAlterPath = new LinkedList<ExpressCentre>();


    public Car_BASIC(int carID, Int2D location, Map map) {
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

    public long getAverageDeliverTime() {
        return map.parcelTimeSpendingTotal / (map.parcelArrivedTotal + 1);
    }


    public String toString() {
        return "Car :" + carID;
    }


    public boolean loadParcel() {
        if (currStation.pToBeSent.size() == 0) return false;
        if (spaceRemaining > 0)
            parcelLoader();
        return true;
    }


    // remove a parcels from carrying
    private void pickOut(Parcel pickOut) {
        // arrive time
        pickOut.arriveTime = map.schedule.getSteps();


        // remove from carrying
        if (carrying.remove(pickOut)) {
            // restore weight
            spaceRemaining += pickOut.weight;
        }

        // add into arrived parcels
        if (!(pickOut instanceof CarCaller)) {
            printParcelUnloadLog(pickOut);
            currStation.pArrived.add(pickOut);
            map.parcelTotal--;
            tryTerminate();
        } else {
            carCallerConvertParcel((CarCaller) pickOut);
            printCarCallerUnloadLog((CarCaller) pickOut);
        }
        initCarState();
    }

    //  put a parcel into a carrying
    private void putIn(Parcel putIn) {

        // put into carrying
        if (carrying.add(putIn)) {
            // subtract weight
            spaceRemaining -= putIn.weight;
        }

        // arrive time
        putIn.pickUpTime = map.schedule.getSteps();
    }


    private void tryTerminate() {
        if (map.autoGenParcelsModeTermination && map.autoGenParcelByStationsMax > 0) {
            return;
        }
        // the ending of the output file
        if (map.parcelTotal == 0) {
            new OutPutResult(map).writeResult();
        }
    }

    private void printCarCallerUnloadLog(CarCaller carCaller) {
        String timeSpending = carCaller.getTimeSpending();
        if (map.detailsOn) {
            System.out.println("Log: " + this + " has unloaded" + " " + carCaller + " with wight " + carCaller.weight + " and time spending " + timeSpending + " at " + currStation + "...");
            System.out.println("Log: Global parcels remaining " + map.parcelTotal);
        }
    }

    private void printParcelUnloadLog(Parcel parcel) {
        String timeSpending = parcel.getTimeSpending();
        if (map.detailsOn) {
            System.out.println("Log: " + this + " has unloaded" + " " + parcel + " with wight " + parcel.weight + " and time spending " + timeSpending + " at " + currStation + "...");
            System.out.println("Log: Global parcels remaining " + map.parcelTotal);
        }
    }

    private void carCallerConvertParcel(CarCaller carCaller) {

        // find and remove from car caller pick up linkedList,
        // then add to the carrying of current car
        Parcel newP = currStation.findParcelWithWeightFromCarCallerPickUp(carCaller.weight);
        if (!newP.destination.equals(currStation)) {
            putIn(newP);
        } else {

            currStation.pArrived.add(newP);
            arriveStation();
        }
    }

    // unload parcel
    public void unloadParcel() {
        // when there is a package is unloaded and the package is the first package in the carrying list, then reset the global Path
        LinkedList<Parcel> carryingCopy = (LinkedList<Parcel>) carrying.clone();
        for (Parcel p : carryingCopy) {
            if (p.destination.equals(currStation)) {
                pickOut(p);
                map.parcelArrivedTotal++;
                // if the package is the first package
                if (carrying.indexOf(p) == 0)
                    initCarState();
            }
        }
    }

    // arrive carpark
    public void arriveStation() {

        if (!hasArrived) {
            firstTimeArrive();
        }
        // unloadParcel method is in the first time arrive method
        loadParcel();
        // set both global and local path
        setAllPath();
    }

    protected void firstTimeArrive() {
        moving = false;
        hasArrived = true;
        currStation = currStation();

        currStation.lastVisitTime = map.schedule.getSteps();
        // remove the car from the road
        TramLine_BASIC tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

        // if the car has not enter the station
//            if (!currStation.carPark.contains(this))
        currStation.carPark.add(this);

        if (tramLine != null) {
            tramLine.carsOnTramLine.remove(this);
        }

        pathLocal.clear();

        stationFrom = currStation;
        stationTo = null;

        stepCount = 0;
        unloadParcel();
    }

    private void setAllPath() {

        // if there are something to be delivered
        if (!carrying.isEmpty()) {

            // get the parcel with highest priority
            Parcel p = carrying.get(0);

            // find the final destination of the parcel as target Station.
            ExpressCentre targetStation = p.destination;
            setPathGlobal(currStation, targetStation);

            // calculate the path from current station to the next station
            if (stationTo != null)
                setPathLocal(currStation, stationTo);
        }
    }


    // leave carpark
    public void leaveStation() {
        hasLeaved = true;

        if(currStation instanceof RefugeeIsland){
            ((RefugeeIsland) currStation).carLeaveCarPark();
            carParkTicket = false;
        }
    }

    protected void setPathLocal(ExpressCentre from, ExpressCentre to) {
        pathLocal = map.tramLines.get(0).getPathBetweenNBStations(from, to);
    }

    protected void setPathGlobal(ExpressCentre from, ExpressCentre to) {
        TramLine_BASIC tl = map.tramLines.get(0);

        if (globalPath == null) {
            globalPath = tl.getPathGlobal(from, to);
        }

        stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
    }


    protected ExpressCentre currStation() {
        ExpressCentre s;
        s = map.allStations.get(0).findStationByLoc(this.location);
        return s;
    }

    private void printLogForParcelLoader(Parcel newP) {
        if (map.detailsOn) {
            System.out.println("Log: " + this + " has picked up " + newP + " with weight " + newP.weight);
        }
    }

    private Parcel fetchFromParcelsToBeSent(int weight) {
        LinkedList<Parcel> pToBeSentCopy = (LinkedList<Parcel>) currStation.pToBeSent.clone();
        for (Parcel p : pToBeSentCopy) {
            if (p.weight <= weight) {
                currStation.pToBeSent.remove(p);
                return p;
            }
        }
        return null;
    }

    // calculate what parcel can be put into the car with the given loading weight
    private void parcelLoader() {
        Parcel newP;
        newP = fetchFromParcelsToBeSent(spaceRemaining);
        while (newP != null) {
            putIn(newP);
            printLogForParcelLoader(newP);
            newP = fetchFromParcelsToBeSent(spaceRemaining);
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
            graphics.fill(preciseEllipse);
            return;
        }

        final int x = (int) (draw.x - width / 2.0);
        final int y = (int) (draw.y - height / 2.0);
        int w = (int) (width);
        int h = (int) (height);

        // draw centered on the origin
        graphics.fillOval(x, y, w, h);
    }

    private void initCarState() {
        globalPath = null;
        stationTo = null;
    }

    protected void setUpCarCaller() {
        if (!map.callCarToPickUpParcels.isEmpty()) {
            Parcel newP = map.callCarToPickUpParcels.pollFirst();
            // if the asker is the current station
            if (newP.from.equals(currStation)) {
                putIn(newP);
            } else {
                newP.from.pToBeSentForCarCallerPickUp.add(newP);
                CarCaller carCaller = new CarCaller(currStation, newP.from, newP.weight, map);
                putIn(carCaller);
            }
        }
    }

    protected void oneStep() {

        if (this.location == pathLocal.getLast() || stepCount == pathLocal.size()) {
            stepping = false;
            return;
        }

        // get the next step location
        Int2D nextStep = this.pathLocal.get(stepCount);

        // move
        // TODO get ride of duplicated data
        while (this.location.equals(nextStep)) {
            stepCount++;
            if (stepCount >= pathLocal.size()) {
                stepping = false;
                return;
            }
            nextStep = this.pathLocal.get(stepCount);

        }

        this.location = nextStep;
        map.mapGrid.setObjectLocation(this, nextStep);
        stepCount++;

    }

    private void tryLeaveStation(TramLine_BASIC tramLine) {

        if(stationTo instanceof RefugeeIsland && ((RefugeeIsland) stationTo).carParkAvailable<=0){
            return;
        }


//        System.out.println("got a ticket "+ tramLine.okToLeave(currStation)+System.currentTimeMillis());


        // allow to leave
        if (tramLine.okToLeave(currStation)) {
            if (tramLine.trafficLightOccupant == null)
                tramLine.tryOccupyTraffic(currStation);
            // leave the car park one by one -- FIFO

            if (tramLine.currLeavingCars != null) {
                return;
            }

            tramLine.currLeavingCars = this;

            if(stationTo instanceof RefugeeIsland) {
                ((RefugeeIsland) stationTo).carParkAvailable--;
            }

            tramLine.carsOnTramLine.add(this);
            leaveStation();
//                    refusedAlterPath = new LinkedList<ExpressCentre>();
        } else {
            // not allow to leave, then ask to leave
            tramLine.tryOccupyTraffic(currStation);
        }


    }



    protected void afterLeaving(TramLine_BASIC tramLine) {
        //                if (tramLine.currLeavingCars.equals(this)) {
        tramLine.currLeavingCars = null;
//                }
        if (tramLine.a.equals(currStation)) {
            tramLine.quota1--;
        } else {
            tramLine.quota2--;
        }
        currStation.carPark.remove(this);
        hasArrived = false;
        hasLeaved = false;
    }

    @Override
    public void step(SimState state) {
        // don't increase cost if the cars in garage
//        if (!(currStation() instanceof Garage)) {
//            Int2D d = new Int2D(1, 1);
//            map.profit -= d.distance(new Int2D(2, 2));
//        }

        if (stepping) {
            oneStep();
            return;
        }

        currStation = currStation();
        if (currStation != null) {
            if (!hasArrived) {
                arriveStation();
                // this return is for waite one step after arrival
                return;
            } else {
                // TODO globalPath can be set with out using arriveStation
                // TODO car caller can pick multiple parcels
                // if the car has not carrying any thing that it don't need to leave the station
                if (this.carrying.isEmpty()) {
                    setUpCarCaller();
                    this.arriveStation();
                    return;
                }

                // this is for catching the situation that both pToBeSent and global parcel boards are empty
                if (globalPath == null || stationTo == null) {
                    this.arriveStation();
                    return;
                }
            }


            TramLine_BASIC tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

            // can not find a tram Line to destination
            if (tramLine == null) {
                return;
            }

            // prepare for leaving
            if (!hasLeaved) {
                tryLeaveStation(tramLine);
                return;
            }

            // delay one step of leaving the car park, Truly leave
            if (hasLeaved) {
                afterLeaving(tramLine);
                stepping = true;

            }
        }

        // travel on the tramline

    }
}





