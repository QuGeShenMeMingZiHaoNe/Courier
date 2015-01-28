package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Car extends OvalPortrayal2D implements Steppable {
    public static final int maxSpace = 5;
    private final int basicCarDisplaySize = 2;
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
    private boolean moving = true;
    private boolean alterPath = false;
//    private LinkedList<ExpressCentre> refusedAlterPath = new LinkedList<ExpressCentre>();


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
//        if (unload.size() > 0) {
//        }
        carrying.removeAll(unload);

        s.pArrived.addAll(unload);

        if (unload.size() > 0) {
            if (map.detailsOn) {
                System.out.print("Log: Global parcels remaining ");
                System.out.println(map.parcelTotal);
            }
        }
    }

    // for the parcels that have arrived the final destination
    private List<Parcel> parcelsToUnload(ExpressCentre s) {
        ExpressCentre currStation = currStation();
        List<Parcel> toUnload = new LinkedList<Parcel>();
        LinkedList<Parcel> carCallerToUnload = new LinkedList<Parcel>();
        LinkedList<Parcel> carCallerPickUp = new LinkedList<Parcel>();
        for (Parcel p : carrying) {
            if (p.destination.equals(s)) {
                // TODO: does car caller earn money??
                map.profit += p.getProfit();

                // if the package is the first package
                if (carrying.indexOf(p) == 0)
                    initCarState();

                p.arriveTime = map.schedule.getSteps();

                // restore the released weight to the car
                this.spaceRemaining += p.weight;

                // if the parcel is car caller
                if (p instanceof CarCaller) {
//                    currStation.carCallerSema++;

                    // find and remove from car caller pick up linkedList,
                    // then add to the carrying of current car
                    carCallerPickUp.add(currStation.findParcelWithWeightFromCarCallerPickUp(p.weight));
                    carCallerToUnload.add(p);
                    initCarState();
                    String timeSpending = p.getTimeSpending();
                    if (map.detailsOn) {
                        System.out.println("Log: " + this + " has unloaded" + " " + p + " with wight " + p.weight + " and time spending " + timeSpending + " at " + currStation() + "...");
                    }
                } else {
                    map.parcelTotal--;
                    toUnload.add(p);
                    String timeSpending = p.getTimeSpending();

                    if (map.detailsOn) {
                        System.out.println("Log: " + this + " has unloaded" + " " + p + " with wight " + p.weight + " and time spending " + timeSpending + " at " + currStation() + "...");
                    }
                }
            }
        }
        carrying.removeAll(carCallerToUnload);
        carrying.addAll(carCallerPickUp);
        return toUnload;
    }

    // arrive carpark
    public void arriveStation() {
        // get current statition
        ExpressCentre currStation = currStation();

        if (!hasArrived) {
            moving = false;
            hasArrived = true;
            currStation.lastVisitTime = map.schedule.getSteps();
            // remove the car from the road
            TramLine tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

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


        loadParcel();

        // if there are something to be delivered
        if (!carrying.isEmpty()) {

            // get the parcel with highest priority
            Parcel p = carrying.get(0);

            // find the final destination of the parcel as target Station.
            ExpressCentre targetStation = p.destination;


            switch (map.mode) {
                // set the current station as station from, the next station as station to.
                case BASIC:
                    setPathGlobal(currStation, targetStation);
                    break;
                case AVOID_TRAFFIC_JAM:
                    if (globalPath == null) {
                        setPathGlobal(currStation, targetStation);
                    }
                    LinkedList<ExpressCentre> avoids = findTrafficJam();
//                    avoids.addAll(refusedAlterPath);
                    if (avoids.size() < currStation.neighbours.size()) {
                        setPathGlobal(currStation, targetStation, avoids);
                    }

                    // when we can not find the path by avoiding traffic jam, we still set the path by
                    break;
            }

//            System.out.println(globalPath + ""+stationTo);
            // calculate the path from current station to the next station
            if (stationTo != null)
                setPathLocal(currStation, stationTo);

        }
    }

    private LinkedList<ExpressCentre> findTrafficJam() {
        LinkedList<ExpressCentre> avoids = new LinkedList<ExpressCentre>();
        ExpressCentre currStation = currStation();
        for (ExpressCentre nb : currStation.neighbours) {
            TramLine tl = map.tramLines.getFirst().findTramLine(currStation, nb);

            tl.tryOccupyTraffic(currStation);

            if (!tl.okToLeave(currStation)) {
//                if(tl.clearingTheRoad){
//                    if(!tl.trafficLightOccupant.equals(currStation)&&(tl.visited>(map.tramLineVisitedTotal/map.numOfTramLineExceptGarage)))

                if (tl.visited > (map.tramLineVisitedTotal / map.numOfTramLineExceptGarage))
                    avoids.add(nb);
//                }
            }

//            if(!tl.carsOnTramLine.isEmpty()&&!tl.trafficLightOccupant.equals(currStation))
        }
        return avoids;
    }


    // leave carpark
    public void leaveStation() {
        hasLeaved = true;
    }

    private void setPathLocal(ExpressCentre from, ExpressCentre to) {
        pathLocal = map.tramLines.get(0).getPathBetweenNBStations(from, to);
    }

    private void setPathGlobal(ExpressCentre from, ExpressCentre to) {
        TramLine tl = map.tramLines.get(0);
        ExpressCentre currStation = currStation();

        if (globalPath == null) {
            globalPath = tl.getPathGlobal(from, to);
        }

        stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
    }

    private void setPathGlobal(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> avoids) {
        TramLine tl = map.tramLines.get(0);
        ExpressCentre currStation = currStation();
        LinkedList<ExpressCentre> old;

        if (!(globalPath == null)) {
            old = (LinkedList<ExpressCentre>) globalPath.clone();
        } else {
            old = null;
        }

        if (globalPath == null) {
            globalPath = tl.getPathGlobal(from, to, avoids);

        }

        if (alterPath) {
            globalPath = tl.getPathGlobal(from, to, avoids);

            if (globalPath != null) {
                ExpressCentre commonEC = findFirstCommentStation(globalPath, old);
                double oldDistance = calPathDistanceBetween(old, currStation, commonEC);

                double newDistance = calPathDistanceBetween(globalPath, currStation, commonEC);

                double oldPathIntensity = findPathIntensity(old,currStation);
                double newPathIntensity = findPathIntensity(globalPath,currStation);

//                System.out.println("\n"+visitedPathOld/ globalPath.size()+"\n"+visitedPathNew/ old.size()+"\n");
//                if(oldPathIntensity<0.4*newPathIntensity)
//                    System.out.println("*()_()*()&*()&*()&*()&*()&*()&*())))))))))))****************************************************");

                if (newDistance >= oldDistance || oldPathIntensity<=0.5*newPathIntensity) {
//                        refusedAlterPath.add(globalPath.get(1));
                    globalPath = old;
                } else {
                    System.out.println("\n\n\n\n\n\nLog: old distance " + oldDistance + "  new distance " + newDistance + "\n" + "successfully alter " + "\n between " + currStation + " " + commonEC);
                    System.out.println("Parcels Total: " + map.parcelTotal);
                }

            }
        }

        if (globalPath == null && old != null) {
            globalPath = old;
        }

        if (globalPath != null) {
            stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
        }
    }

//    private int findPathVisited(LinkedList<ExpressCentre> path) {
//        Iterator<ExpressCentre> iter = path.iterator();
//        ExpressCentre first = iter.next();
//        ExpressCentre second;
//        int visited = 0;
//        TramLine tl;
//        while (iter.hasNext()) {
//            second = iter.next();
//            tl = map.tramLines.getFirst().findTramLine(first, second);
//            visited += tl.visited;
//            first = second;
//        }
//        return visited;
//    }

    private double findPathIntensity(LinkedList<ExpressCentre> path,ExpressCentre commonEC) {
        Iterator<ExpressCentre> iter = path.iterator();
        ExpressCentre first = iter.next();
        if(!first.equals(commonEC)) {
            while (iter.hasNext()) {
                first = iter.next();
                if (first.equals(commonEC))
                    break;
            }
        }
        ExpressCentre second;
        double intensity = 0;
        TramLine tl;
        while (iter.hasNext()) {
            second = iter.next();
            tl = map.tramLines.getFirst().findTramLine(first, second);
            intensity += tl.carsOnTramLine.size();
            first = second;
        }
        return intensity;
    }


    private ExpressCentre findFirstCommentStation(LinkedList<ExpressCentre> newPath, LinkedList<ExpressCentre> oldPath) {
        LinkedList<ExpressCentre> copyNew = (LinkedList<ExpressCentre>) newPath.clone();
        copyNew.remove(0);

        LinkedList<ExpressCentre> copyOld = new LinkedList<ExpressCentre>();
        int index = oldPath.indexOf(newPath.getFirst());
        while (index < oldPath.size()) {
            copyOld.add(oldPath.get(index));
            index++;
        }

        for (ExpressCentre ec : copyNew) {
            if (copyOld.contains(ec))
                return ec;
        }

        System.out.println("log: NEVER GETS HERE");
        // never come to this state
        return null;
    }

    private double calPathDistanceBetween(LinkedList<ExpressCentre> path, ExpressCentre a, ExpressCentre b) {
        double distance = 0;
        ExpressCentre currStation = currStation();
        // return directly if station a and b
        if (a.equals(b))
            return distance;

        for (int index = path.indexOf(a); index < path.size(); index++) {
            if (index == path.indexOf(a)) {
                TramLine tl = map.tramLines.getFirst().findTramLine(a, path.get(index + 1));
                if (!tl.carsOnTramLine.isEmpty()) {
                    distance += tl.carsOnTramLine.getLast().location.distance(currStation.location);
                    distance += a.location.distance(path.get(index + 1).location);
                } else {
//                    System.out.println("fdsafdsafdsafsafdsafasfsdafdsafdsafsad!@#$%^&*()^&%$$#@!@!@#$%^&*(*)_(*&*^%$#@");
//                    System.out.println(this+" "+a+" "+b);
                    distance += path.get(index).location.distance(path.get(index + 1).location);
                }
            } else {
                distance += path.get(index).location.distance(path.get(index + 1).location);
            }

            if (path.get(index + 1).equals(b)) {
                return distance;
            }
        }
        return distance;
    }

    private ExpressCentre currStation() {
        ExpressCentre s;
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
                p.pickUpTime = map.schedule.getSteps();
                if (map.detailsOn) {
                    System.out.println("Log: " + p + " with weight " + p.weight + " has been picked up by " + this);
                }
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

    @Override
    public void step(SimState state) {
        // don't increase cost if the cars in garage
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
                // TODO globalPath can be set with out using arriveStation
                // TODO car caller can pick multiple parcels
                if (this.carrying.isEmpty() || globalPath == null || stationTo == null) {
                    if(!map.callCarToPickUpParcels.isEmpty()){
                        Parcel p = map.callCarToPickUpParcels.pollFirst();
                        if(p.from.equals(currStation)) {
                            p.from.pToBeSent.add(p);
                        }else {
                            p.from.pToBeSentForCarCallerPickUp.add(p);
                            this.carrying.add(new CarCaller(currStation, p.from, p.weight, map));
                        }
                    }
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

            // prepare for leaving
            if (!hasLeaved) {

                if (tramLine.okToLeave(currStation)) {
                    // leave the car park one by one -- FIFO
                    if (tramLine.currLeavingCars != null) {
                        return;
                    }
                    tramLine.currLeavingCars = this;
                    leaveStation();
                    tramLine.carsOnTramLine.add(this);

                    // tramline vistited ++
                    if (!map.garages.contains(currStation)) {
                        map.numOfTramLineExceptGarage++;
                        tramLine.visited++;
                    }

//                    refusedAlterPath = new LinkedList<ExpressCentre>();
                    return;
                } else {
                    tramLine.tryOccupyTraffic(currStation);
                    if (map.mode == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
                        if (!tramLine.okToLeave(currStation) && (tramLine.trafficLightOccupant == currStation)) {
//                        initCarState();
                            alterPath = true;
                            arriveStation();
                            alterPath = false;
                        }
                    }
                    return;
                }
            }

            // delay one step of leaving the car park, Truly leave
            if (hasLeaved) {
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
        }


        if (!carrying.isEmpty()) {
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
        }
    }
}





