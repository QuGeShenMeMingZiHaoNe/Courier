package courier;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by daniel on 15/1/17.
 */
public class PathSearcher {

    // we are using breadth first search in this path searching class

    protected LinkedList<LinkedList<Station>> paths;
    private Map map;

    PathSearcher(Map map) {
        this.map = map;
    }

    public LinkedList<LinkedList<Station>> findAllPossiblePath(Station from, Station to) {
        initPathSearch();
        LinkedList<Station> path = new LinkedList<Station>();
        path.add(from);
        findAllPossiblePathHelper(from, to, path);
        return paths;
    }

    private void initPathSearch() {
        this.paths = new LinkedList<LinkedList<Station>>();
    }

    private void findAllPossiblePathHelper(Station from, Station to, LinkedList<Station> result) {
        LinkedList<Station> neighbours = from.findNeighbours();

        // if node is isolated then just return
        if (neighbours.size() == 0)
            return;

        boolean sizeIncrease = false;

        for (Station station : neighbours) {
            if (!result.contains(station)) {
                // make a copy of result
                LinkedList<Station> copy = (LinkedList<Station>) result.clone();
                copy.add(station);

                // if we are at the final destination
                if (station.equals(to)) {
                    paths.add(copy);
                } else {
                    findAllPossiblePathHelper(station, to, copy);
                }
            }
        }
    }

    private double calPathDistance(LinkedList<Station> path) {
        Iterator<Station> iterator = path.iterator();
        Station first = iterator.next();
        Station second;
        double distance = 0;
        while (iterator.hasNext()) {
            second = iterator.next();
            distance = distance + first.location.distance(second.location);
            first = second;
        }
        return distance;
    }

    // insert sort
    public LinkedList<LinkedList<Station>> sortPathByDistance(LinkedList<LinkedList<Station>> target) {
        LinkedList<LinkedList<Station>> result = new LinkedList<LinkedList<Station>>();
        LinkedList<Double> distances = new LinkedList<Double>();

        int index = distances.size();
        for (LinkedList<Station> path : target) {
            double currDistance = calPathDistance(path);
            for (double distance : distances) {
                if (distance > currDistance) {
                    index = distances.indexOf(distance);
                    break;
                }
            }
            distances.add(index, currDistance);
            result.add(index, path);
        }
        return result;
    }

//    insert in the order of distance
//    private void insertPath(LinkedList<LinkedList<Station>> result, LinkedList<Double> distances){
//
//    }

//    private LinkedList<Integer> rankByDistance(LinkedList<Double> distances) {
//        LinkedList<Integer> result = new LinkedList<Integer>();
//    }

//
//    private class ComparablePath implements Comparable<LinkedList<Station>>{
//
//        @Override
//        public int compareTo(LinkedList<Station> o) {
//            int comparedSize = o.size();
//        }
//
//        public int compareTo(House o) {
//            int comparedSize = o.size;
//            if (this.size > comparedSize) {
//                return 1;
//            } else if (this.size == comparedSize) {
//                return 0;
//            } else {
//                return -1;
//            }
//        }

//    }

}
