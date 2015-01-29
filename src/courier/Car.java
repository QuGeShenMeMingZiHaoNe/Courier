package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Car extends OvalPortrayal2D implements Steppable {
    public static final int maxSpace = 5;
    protected int spaceRemaining = maxSpace;

    private final int basicCarDisplaySize = 2;
    public Shape shape;
    protected int carID;
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
    private ExpressCentre currStation;
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
        if (currStation.pToBeSent.size() == 0) return false;
        parcelLoader();
        return true;
    }


    // remove a parcels from carrying
    private void pickOut(Parcel pickOut){
        // arrive time
        pickOut.arriveTime = map.schedule.getSteps();


        // remove from carrying
        if(carrying.remove(pickOut)){
            // restore weight
            spaceRemaining += pickOut.weight;
        }

        // add into arrived parcels
        if(!(pickOut instanceof CarCaller)) {
            printParcelUnloadLog(pickOut);
            currStation.pArrived.add(pickOut);
            map.parcelTotal--;
            tryTerminate();
        } else {
            carCallerConvertParcel((CarCaller)pickOut);
            printCarCallerUnloadLog((CarCaller) pickOut);
        }
        initCarState();
    }

    //  put a parcel into a carrying
    private void putIn(Parcel putIn){

        // put into carrying
        if(carrying.add(putIn)){
            // subtract weight
            spaceRemaining -= putIn.weight;
        }

        // arrive time
        putIn.pickUpTime = map.schedule.getSteps();
    }

    private void outputFile(String write) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + map.mode + " " + map.initTime + ".output", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);

        writer.close();
    }

    private void tryTerminate(){
        if (map.autoGenParcelsModeTermination && map.autoGenParcelByStationsMax > 0) {
            return ;
        }


        // the ending of the output file
        if (map.parcelTotal == 0) {
            long timeSpendingAverage = map.parcelTimeSpendingTotal / map.parcelTotalCopy;
            outputFile("***********************************************************************************************************");
            outputFile("\nMode: " + map.mode + "\nRandom number seed: " + map.seed() + "\nCar number: " + map.initNumOfParcelsInExpressCentre + "\nParcel number: " + map.parcelTotalCopy + "\nExpressCenter: " + map.expressCentres.size() + "\n");
            outputFile("***********************************************************************************************************");
            outputFile("\nTotal Parcels Carrying Time: " + (map.parcelTimeSpendingTotal) + "\nTime Spending on Carrying in Average: " + (timeSpendingAverage));
            long finalStep = map.schedule.getSteps();
            outputFile("Time Spending On Finishing Delivery: " + finalStep + "\n");
            outputFile("***********************************************************************************************************");

            outputFile("\nThe Longer carrying Time means the car travel with the parcel for longer period.\n" +
                    "The Traffic Avoiding Mode has a longer carrying time as it has to carry the parcels\n" +
                    "with the cars when the cars need to travel longer way to avoid the traffic jam.\n" +
                    "The performance can be well represented by the Total Time Spending On Delivery\n" +
                    "The shorter the time, the better the performance is!\n");

            outputFile("***********************************************************************************************************");

            System.out.println("Finish!!!!!!!");
        }
    }

    private void printCarCallerUnloadLog(CarCaller carCaller){
        String timeSpending = carCaller.getTimeSpending();
        if (map.detailsOn) {
            System.out.println("Log: " + this + " has unloaded" + " " + carCaller + " with wight " + carCaller.weight + " and time spending " + timeSpending + " at " + currStation + "...");
            System.out.println("Log: Global parcels remaining "+map.parcelTotal);
        }
    }

    private void printParcelUnloadLog(Parcel parcel){
        String timeSpending = parcel.getTimeSpending();
        if (map.detailsOn) {
            System.out.println("Log: " + this + " has unloaded" + " " + parcel + " with wight " + parcel.weight + " and time spending " + timeSpending + " at " + currStation+  "...");
            System.out.println("Log: Global parcels remaining "+map.parcelTotal);
        }
    }

    private void carCallerConvertParcel(CarCaller carCaller){

        // find and remove from car caller pick up linkedList,
        // then add to the carrying of current car
        Parcel newP = currStation.findParcelWithWeightFromCarCallerPickUp(carCaller.weight);
        if(!newP.destination.equals(currStation)) {
            putIn(newP);
        }else{

            currStation.pArrived.add(newP);
            arriveStation();
        }
    }

    // unload parcel
    public void unloadParcel() {
        // when there is a package is unloaded and the package is the first package in the carrying list, then reset the global Path
        LinkedList<Parcel> carryingCopy = (LinkedList<Parcel>)carrying.clone();
        for (Parcel p : carryingCopy) {
            if (p.destination.equals(currStation)) {
                pickOut(p);
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

    private void firstTimeArrive(){
        moving = false;
        hasArrived = true;
        currStation = currStation();

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

    private void setAllPath(){

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

            // calculate the path from current station to the next station
            if (stationTo != null)
                setPathLocal(currStation, stationTo);
        }
    }

    private LinkedList<ExpressCentre> findTrafficJam() {
        LinkedList<ExpressCentre> avoids = new LinkedList<ExpressCentre>();
        for (ExpressCentre nb : currStation.neighbours) {
            TramLine tl = map.tramLines.getFirst().findTramLine(currStation, nb);

            tl.tryOccupyTraffic(currStation);

            if (!tl.okToLeave(currStation)) {
                avoids.add(nb);
            }
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

        if (globalPath == null) {
            globalPath = tl.getPathGlobal(from, to);
        }

        stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
    }

    private void setPathGlobal(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> avoids) {
        TramLine tl = map.tramLines.get(0);
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

                double oldPathIntensity = findPathIntensity(old, currStation);
                double newPathIntensity = findPathIntensity(globalPath, currStation);

                if (newDistance >= oldDistance || oldPathIntensity <= 0.5 * newPathIntensity) {
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

    private double findPathIntensity(LinkedList<ExpressCentre> path, ExpressCentre commonEC) {
        Iterator<ExpressCentre> iter = path.iterator();
        ExpressCentre first = iter.next();
        if (!first.equals(commonEC)) {
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

    private void printLogForParcelLoader(Parcel newP){
        if (map.detailsOn) {
            System.out.println("Log: " + this +" has picked up "+ newP + " with weight " + newP.weight);
        }
    }

    private Parcel fetchFromParcelsToBeSent(int weight){
        LinkedList<Parcel> pToBeSentCopy = (LinkedList<Parcel>)currStation.pToBeSent.clone();
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
        while ((newP = fetchFromParcelsToBeSent(spaceRemaining))!=null) {
            currStation.pToBeSent.remove(newP);
            putIn(newP);
            printLogForParcelLoader(newP);
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

    private void setUpCarCaller(){
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
    @Override
    public void step(SimState state) {
        // don't increase cost if the cars in garage
//        if (!(currStation() instanceof Garage)) {
//            Int2D d = new Int2D(1, 1);
//            map.profit -= d.distance(new Int2D(2, 2));
//        }
//        System.out.println(spaceRemaining);
//        System.out.println(carrying);

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
                if (this.carrying.isEmpty() || globalPath == null || stationTo == null) {
                    setUpCarCaller();
                    this.arriveStation();
                    return;
                }
            }



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





