package courier;

import javax.xml.stream.Location;
import java.util.Iterator;
import java.util.LinkedList;

public class PathSearcher {

    // we are using breadth first search in this path searching class

    protected LinkedList<LinkedList<ExpressCentre>> paths;
    private Map map;
    private final double MAX_DISTANCE = 999999999;
    private double currMinDistance;

    PathSearcher(Map map) {
        this.map = map;
    }

    public LinkedList<LinkedList<ExpressCentre>> findAllPossiblePath(ExpressCentre from, ExpressCentre to) {
        initPathSearch();
        LinkedList<ExpressCentre> path = new LinkedList<ExpressCentre>();
        path.add(from);
        findAllPossiblePathHelper(from, to, path,0);
        return paths;
    }

    private void initPathSearch() {
        this.paths = new LinkedList<LinkedList<ExpressCentre>>();
        currMinDistance = MAX_DISTANCE;
    }

    private void findAllPossiblePathHelper(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> result, double distance) {
        LinkedList<ExpressCentre> neighbours = from.findNeighbours();

        // if node is isolated then just return
        if (neighbours.size() == 0)
            return;

        for (ExpressCentre expressCentre : neighbours) {
            if (!result.contains(expressCentre)) {
                distance += expressCentre.location.distance(result.getLast().location);

                if(!(distance>currMinDistance)) {
                    // make a copy of result
                    LinkedList<ExpressCentre> copy = (LinkedList<ExpressCentre>) result.clone();
                    copy.add(expressCentre);

                    // if we are at the final destination
                    if (expressCentre.equals(to)) {
                        paths.add(copy);
                        currMinDistance = calPathDistance(copy);
//                        currMinDistance = 0;
//                        return;
                    } else {
                        findAllPossiblePathHelper(expressCentre, to, copy,distance);
                    }

                }
            }
        }
    }

    public double calPathDistance(LinkedList<ExpressCentre> path) {
        Iterator<ExpressCentre> iterator = path.iterator();
        ExpressCentre first = iterator.next();
        ExpressCentre second;
        double distance = 0;
        while (iterator.hasNext()) {
            second = iterator.next();
            distance = distance + first.location.distance(second.location);
            first = second;
        }
        return distance;
    }

    // insert sort
    public LinkedList<LinkedList<ExpressCentre>> sortPathByDistance(LinkedList<LinkedList<ExpressCentre>> target) {
        LinkedList<LinkedList<ExpressCentre>> result = new LinkedList<LinkedList<ExpressCentre>>();
        LinkedList<Double> distances = new LinkedList<Double>();

        int index = distances.size();
        for (LinkedList<ExpressCentre> path : target) {
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
