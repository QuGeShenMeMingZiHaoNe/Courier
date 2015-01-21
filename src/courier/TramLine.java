package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class TramLine implements Steppable {
    protected ExpressCentre a;
    protected ExpressCentre b;
    protected int tramLineID;
    protected Map map;

    // record of all the cars on the tram line
    protected LinkedList<Car> carsOnTramLine = new LinkedList<Car>();
    // whom is controlling the traffic
    protected ExpressCentre trafficLightOccupant;
    protected Car currLeavingCar;
    // when the requirements reach a certain limit then we give the traffic control right to the other station.
    private int maximunCarLeavingBeforeRedLight = (int) Math.round(0.1*map.initNumOfCarsInStation);
    protected int quota1 = maximunCarLeavingBeforeRedLight;
    protected int quota2 = maximunCarLeavingBeforeRedLight;
    // clear the road when the traffic control right was swapped
    private boolean clearingTheRoad = false;
    private String line;



    public TramLine(String line, ExpressCentre a, ExpressCentre b, int tramLineID, Map map) {
        if (a.stationID < b.stationID) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
        this.map = map;
        this.tramLineID = tramLineID;
        this.line = line;

        // randomly assign trafficLightOccupant to one of the station
        if (new Random().nextInt(2) == 0) {
            trafficLightOccupant = a;
        } else {
            trafficLightOccupant = b;
        }
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
//        if (result != null)
//            result.pop();

        return result;
    }

    // the helper function of getPathBetweenNBStations
    private LinkedList<Int2D> buildPath(ExpressCentre a, ExpressCentre b) {
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
    private int findTramLineIndexByNB(ExpressCentre a, ExpressCentre b) {
        int result = -1;

        // if a== b
        if (a.equals(b))
            return result;

        ExpressCentre c;
        TramLine temp;
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

    // find the given tram line in map.tramlines
    public TramLine findTramLine(ExpressCentre a, ExpressCentre b) {

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

        // a and b are neighbour
        int index = findTramLineIndexByNB(a, b);
        if (index >= 0) {
            LinkedList<ExpressCentre> result = new LinkedList<ExpressCentre>();
            result.add(a);
            result.add(b);
            return result;
        }

        // not neighbour

        // TODO move this path searcher into constructor;
        PathSearcher pathSearcher = new PathSearcher(map);
        // find path using breadth first search
        LinkedList<LinkedList<ExpressCentre>> paths = pathSearcher.findAllPossiblePath(a, b);
        // sort path by distance in ascending order
        if (paths.size() > 1)
            paths = pathSearcher.sortPathByDistance(paths);
        // return the one with the lowest distance
        if (paths.size() > 0) {
            return paths.getFirst();
        } else {
            return null;
        }
    }

    // try to get the traffic control, release the control if the holder station has ran out quota
    // or the current holder's car park is empty.
    public void tryOccupyTraffic(ExpressCentre demander) {
        if (clearingTheRoad) {
            if (roadClear()) {
                clearingTheRoad = false;
            }
            return;
        }
        if (demander.equals(a)) {
            // give the traffic to a
            if (quota2 <= 0 || b.carPark.isEmpty() || noCarIsComing(b, a)) {
                trafficLightOccupant = a;
                clearingTheRoad = true;
                quota2 = 0;
                quota1 = maximunCarLeavingBeforeRedLight;
            }
        } else {
            // give the traffic to b
            if (quota1 <= 0 || a.carPark.isEmpty() || noCarIsComing(a, b)) {
                trafficLightOccupant = b;
                clearingTheRoad = true;
                quota1 = 0;
                quota2 = maximunCarLeavingBeforeRedLight;
            }
        }
    }

    // the holder station of tram line has no car want to come into asker station
    private boolean noCarIsComing(ExpressCentre to, ExpressCentre from) {
//        if (to.carPark == null) return true;

        for (Car c : from.carPark) {
            if (c.stationTo != null)
                if (c.stationTo.equals(to))
                    return false;
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
        return !clearingTheRoad && trafficLightOccupant.equals(asker);
    }


    @Override
    public void step(SimState state) {

    }
}
