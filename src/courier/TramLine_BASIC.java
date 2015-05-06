package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class TramLine_BASIC implements Steppable {
    // clear the road when the traffic control right was swapped
    public boolean clearingTheRoad = false;
    protected ExpressCentre a;
    protected ExpressCentre b;
    protected int tramLineID;
    protected Map map;
    // record of all the cars on the tram line
    protected LinkedList<Car_BASIC> carsOnTramLine = new LinkedList<Car_BASIC>();
    // whom is controlling the traffic
    protected ExpressCentre trafficLightOccupant = null;
    protected Car_BASIC currLeavingCars;
    // when the requirements reach a certain limit then we give the traffic control right to the other station.
//    private int maximumCarLeavingBeforeRedLight = (int) Math.round(0.1 * map.initNumOfCarsInGarage);
    public static int maximumCarLeavingBeforeRedLight = 1;
    protected int quota1 = maximumCarLeavingBeforeRedLight;
    protected int quota2 = maximumCarLeavingBeforeRedLight;
    private String line;


    public TramLine_BASIC(String line, ExpressCentre a, ExpressCentre b, Map map) {
        if (a.stationID < b.stationID) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
        this.map = map;
        this.tramLineID = map.serialTramLineID;
        this.line = line;

        // randomly assign trafficLightOccupant to one of the station
        if (new Random().nextInt(2) == 0) {
            trafficLightOccupant = a;
        } else {
            trafficLightOccupant = b;
        }
        map.serialTramLineID++;
    }


    public LinkedList<Car_BASIC> getCarsOnTramLine() {
        return carsOnTramLine;
    }

    public ExpressCentre getTrafficLightOccupant() {
        return trafficLightOccupant;
    }

    // return each coordinates of the path from neighbour station a to b
    public LinkedList<Int2D> getPathBetweenNBStations(ExpressCentre a, ExpressCentre b) {
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        if (a.equals(b)) {
            return result;
        }
        ExpressCentre c = null;

        if (a.stationID < b.stationID) {
            result = buildPath(a, b);
        } else if (a.stationID != b.stationID) {
            c = a;
            a = b;
            b = c;
            result = buildPath(a, b);
        }

        if (c != null) {
            Collections.reverse(result);
        }

        return result;
    }

    // the helper function of getPathBetweenNBStations
    protected LinkedList<Int2D> buildPath(ExpressCentre a, ExpressCentre b) {
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        int xDiff, yDiff, intXStep, intYStep;
        xDiff = b.location.getX() - a.location.getX();
        yDiff = b.location.getY() - a.location.getY();
        Double xStep, yStep;
        xStep = 0.0;
        yStep = 0.0;

        Double distance = a.location.distance(b.location);

        result.add(a.location);
        while (!(result.get(result.size() - 1).getX() == b.location.getX() && result.get(result.size() - 1).getY() == b.location.getY())) {

            xStep += xDiff / distance;
            yStep += yDiff / distance;

            intXStep = xStep.intValue();
            intYStep = yStep.intValue();

            result.add(new Int2D(a.location.getX() + intXStep, a.location.getY() + intYStep));
        }
//        result.pop();
        if (!result.contains(b.location))
            result.add(b.location);
        return result;
    }

    // return the index of given tram line in map.tramlines
    public int findTramLineIndexByNB(ExpressCentre a, ExpressCentre b) {
        int result = -1;

        // if a== b
        if (a.equals(b))
            return result;

        ExpressCentre c;
        TramLine_BASIC temp;
        if (a.stationID < b.stationID) {
            for (int i = 0; i < map.tramLines.size(); i++) {
                temp = map.tramLines.get(i);
                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
                    return i;
                }
            }
        } else {
            c = a;
            a = b;
            b = c;
            for (int i = 0; i < map.tramLines.size(); i++) {
                temp = map.tramLines.get(i);
                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
                    return i;
                }
            }
        }

        return result;
    }

//    // return the index of given tram line in map.tramlines
//    public boolean tramLineHasBeenAdded(ExpressCentre a, ExpressCentre b) {
//        boolean result = false;
//
//        // if a== b
//        if (a.equals(b))
//            return result;
//
//        ExpressCentre c;
//        TramLine_BASIC temp;
//        if (a.stationID < b.stationID) {
//            for (int i = 0; i < map.tramLines.size(); i++) {
//                temp = map.tramLines.get(i);
//                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
//                    return true;
//                }
//            }
//        } else {
//            c = a;
//            a = b;
//            b = c;
//            for (int i = 0; i < map.tramLines.size(); i++) {
//                temp = map.tramLines.get(i);
//                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
//                    return true;
//                }
//            }
//        }
//
//        return result;
//    }

    // find the given tram line in map.tramlines
    public TramLine_BASIC findTramLine(ExpressCentre a, ExpressCentre b) {

        if (a == null || b == null) return null;
        if (a.equals(b)) return null;

        int index = findTramLineIndexByNB(a, b);
        if (index >= 0) {
            return map.tramLines.get(index);
        }
        return null;
    }

    // return the next tram line of the path from a to b,
    public LinkedList<ExpressCentre> getPathGlobal(ExpressCentre a, ExpressCentre b) {
        // not neighbour

        // TODO move this path searcher into constructor;
        PathSearcher pathSearcher = new PathSearcher(map);

        // find path using a star search
        return pathSearcher.findAllPossiblePath(a, b);
    }

    public LinkedList<ExpressCentre> getPathGlobal(ExpressCentre a, ExpressCentre b, LinkedList<ExpressCentre> avoids) {
        // not neighbour

        // TODO move this path searcher into constructor;
        PathSearcher pathSearcher = new PathSearcher(map);

        // find path using breadth first search
        return pathSearcher.findAllPossiblePath(a, b, avoids);

    }

    // try to get the traffic control, release the control if the holder station has ran out quota
    // or the current holder's car park is empty.
    public void tryOccupyTraffic(ExpressCentre demander) {

        if (clearingTheRoad) {
            if (roadClear()) {
                clearingTheRoad = false;
            }
//            return;
        }

        if (demander.equals(trafficLightOccupant))
            return;

        if (demander.equals(a)) {
            // give the traffic to a
            if (quota2 <= 0 || b.carPark.isEmpty() || noCarIsComing(b, a)) {
                trafficLightOccupant = a;
                if (!roadClear())
                    clearingTheRoad = true;
                quota2 = 0;
                quota1 = maximumCarLeavingBeforeRedLight;
            }
        } else {
            // give the traffic to b
            if (quota1 <= 0 || a.carPark.isEmpty() || noCarIsComing(a, b)) {
                trafficLightOccupant = b;
                if (!roadClear())
                    clearingTheRoad = true;
                quota1 = 0;
                quota2 = maximumCarLeavingBeforeRedLight;
            }
        }
    }

    // the holder station of tram line has no car want to come into asker station
    protected boolean noCarIsComing(ExpressCentre from, ExpressCentre to) {
//        if (to.carPark == null) return true;

        for (Car_BASIC c : from.carPark) {
            if (c.stationTo != null && c.stationTo.equals(to)){
                if(!map.getRefugeeIslandOn()){
                    return false;
                }else{
                    if(c.carParkTicket){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // check if there is any car on the road.
    public boolean roadClear() {
        return carsOnTramLine.isEmpty();
    }

    // return if the asker is the controller
    public boolean isController(ExpressCentre asker) {
        return asker.equals(trafficLightOccupant);
    }

    // return the condition of the car to leave the station
    public boolean okToLeave(ExpressCentre asker) {
        if (trafficLightOccupant == null)
            return true;
        return !clearingTheRoad && (trafficLightOccupant.equals(asker));
    }

    @Override
    public void step(SimState state) {
    }
}
