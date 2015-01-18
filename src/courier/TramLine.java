package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class TramLine implements Steppable {
    protected Station a;
    protected Station b;
    protected int tramLineID;
    protected Map map;

    // record of all the cars on the tram line
    protected LinkedList<Car> carsOnTramLine = new LinkedList<Car>();
    // whom is controlling the traffic
    protected Station trafficLightOccupant;
    protected Car currLeavingCar;
    // when the requirements reach a certain limit then we give the traffic control right to the other station.
    private int requirementThreshold = 1;
    protected int quota1 = requirementThreshold;
    protected int quota2 = requirementThreshold;
    // clear the road when the traffic control right was swapped
    private boolean clearingTheRoad = false;


    public TramLine(Station a, Station b, int tramLineID, Map map) {
        if (a.stationID < b.stationID) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
        this.map = map;
        this.tramLineID = tramLineID;

        // randomly assign trafficLightOccupant to one of the station
        if (new Random().nextInt(2) == 0) {
            trafficLightOccupant = a;
        } else {
            trafficLightOccupant = b;
        }
    }

    // return each coordinates of the path from neighbour station a to b
    public LinkedList<Int2D> getStepsNB(Station a, Station b) {
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        if (a.equals(b)) {
            return result;
        }
        Station c = null;

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

    // the helper function of getStepsNB
    private LinkedList<Int2D> buildPath(Station a, Station b) {
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
    private int findTramLineIndexNB(Station a, Station b) {
        int result = -1;

        // if a== b
        if (a.equals(b))
            return result;

        Station c;
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
    public TramLine findTramLine(Station a, Station b) {

        if (a == null || b == null) return null;
        if (a.equals(b)) return null;

        int index = findTramLineIndexNB(a, b);
        if (index >= 0) {
            return map.tramLines.get(index);
        }
        return null;
    }

    // return the next tram line of the path from a to b,
    public LinkedList<Station> getPathGlobal(Station a, Station b) {

        // a and b are neighbour
        int index = findTramLineIndexNB(a, b);
        if (index >= 0) {
            LinkedList<Station> result = new LinkedList<Station>();
            result.add(a);
            result.add(b);
            return result;
        }

        // not neighbour
//        if (index == -1) {
//            LinkedList<LinkedList<Station>> branches = new LinkedList<LinkedList<Station>>();
//            LinkedList<LinkedList<Station>> compareBranches = new LinkedList<LinkedList<Station>>();
//            LinkedList<Station> nbs = a.findNeighbours();
//
//            for (Station s : nbs) {
//
//                LinkedList<Station> neighbours = new LinkedList<Station>();
//                neighbours.add(s);
//                neighbours.addAll(s.findAllReachableStations(a));
//                branches.add(neighbours);
//            }
//            // select all the branches that can reach station b (final destination)
//            for (LinkedList<Station> branch : branches) {
//                // TODO better function
//                if (branch.contains(b)) {
//                    compareBranches.add(branch);
//                }
//            }
//            int whichBranch = -1;
//            int minTreeSize = 999999999;
//
//            for (LinkedList<Station> branch : compareBranches) {
//                if (branch.size() < minTreeSize) {
//                    whichBranch = compareBranches.indexOf(branch);
//                }
//            }
//
//            if (whichBranch >= 0) {
//                return findTramLine(a, compareBranches.get(whichBranch).get(0));
//            } else {
//                return null;
//            }
//        }

        // not neighbour

        // TODO move this path searcher into constructor;
        PathSearcher pathSearcher = new PathSearcher(map);
        // find path using breadth first search
        LinkedList<LinkedList<Station>> paths = pathSearcher.findAllPossiblePath(a, b);
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

    // try to get the traffic control, if the quota of the other end station has ran out
    // or the current holder's car park is empty.
    public void tryOccupyTraffic(Station demander) {
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
                quota1 = requirementThreshold;
            }
        } else {
            // give the traffic to b
            if (quota1 <= 0 || a.carPark.isEmpty() || noCarIsComing(a, b)) {
                trafficLightOccupant = b;
                clearingTheRoad = true;
                quota1 = 0;
                quota2 = requirementThreshold;
            }
        }
    }

    // the holder station of tram line has no car want to come into asker station
    private boolean noCarIsComing(Station b, Station a) {
        if (b.carPark == null) return true;

        for (Car c : b.carPark) {
            if (c.stationTo != null)
                if (c.stationTo.equals(a))
                    return false;
        }
        return true;
    }

    // check if there is any car on the road.
    public boolean roadClear() {
        return carsOnTramLine.isEmpty();
    }

    // return if the asker is the controller
    public boolean isController(Station asker) {
        return asker.equals(trafficLightOccupant);
    }

    // return the condition of the car to leave the station
    public boolean okToLeave(Station asker) {
        return !clearingTheRoad && trafficLightOccupant.equals(asker);
    }


    @Override
    public void step(SimState state) {

    }
}
