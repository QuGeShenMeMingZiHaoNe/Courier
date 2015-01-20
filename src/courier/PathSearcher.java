package courier;

import java.util.Iterator;
import java.util.LinkedList;

public class PathSearcher {

    // we are using breadth first search in this path searching class

    protected LinkedList<LinkedList<ExpressCenter>> paths;
    private Map map;

    PathSearcher(Map map) {
        this.map = map;
    }

    public LinkedList<LinkedList<ExpressCenter>> findAllPossiblePath(ExpressCenter from, ExpressCenter to) {
        initPathSearch();
        LinkedList<ExpressCenter> path = new LinkedList<ExpressCenter>();
        path.add(from);
        findAllPossiblePathHelper(from, to, path);
        return paths;
    }

    private void initPathSearch() {
        this.paths = new LinkedList<LinkedList<ExpressCenter>>();
    }

    private void findAllPossiblePathHelper(ExpressCenter from, ExpressCenter to, LinkedList<ExpressCenter> result) {
        LinkedList<ExpressCenter> neighbours = from.findNeighbours();

        // if node is isolated then just return
        if (neighbours.size() == 0)
            return;

        boolean sizeIncrease = false;

        for (ExpressCenter expressCenter : neighbours) {
            if (!result.contains(expressCenter)) {
                // make a copy of result
                LinkedList<ExpressCenter> copy = (LinkedList<ExpressCenter>) result.clone();
                copy.add(expressCenter);

                // if we are at the final destination
                if (expressCenter.equals(to)) {
                    paths.add(copy);
                } else {
                    findAllPossiblePathHelper(expressCenter, to, copy);
                }
            }
        }
    }

    private double calPathDistance(LinkedList<ExpressCenter> path) {
        Iterator<ExpressCenter> iterator = path.iterator();
        ExpressCenter first = iterator.next();
        ExpressCenter second;
        double distance = 0;
        while (iterator.hasNext()) {
            second = iterator.next();
            distance = distance + first.location.distance(second.location);
            first = second;
        }
        return distance;
    }

    // insert sort
    public LinkedList<LinkedList<ExpressCenter>> sortPathByDistance(LinkedList<LinkedList<ExpressCenter>> target) {
        LinkedList<LinkedList<ExpressCenter>> result = new LinkedList<LinkedList<ExpressCenter>>();
        LinkedList<Double> distances = new LinkedList<Double>();

        int index = distances.size();
        for (LinkedList<ExpressCenter> path : target) {
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

}
