package courier;

import java.util.Iterator;
import java.util.LinkedList;

public class PathSearcher {

    private Map map;

    PathSearcher(Map map) {
        this.map = map;
    }

    // basic type
    public LinkedList<ExpressCentre> findAllPossiblePath(ExpressCentre from, ExpressCentre to) {
        return new AStar(map).findShortestPath(from, to, new LinkedList<ExpressCentre>());
//        return new BreadFirstSearch(map).findShortestPath(from,to);

    }

    // type with traffic avoiding
    public LinkedList<ExpressCentre> findAllPossiblePath(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> avoids) {
        return new AStar(map).findShortestPath(from, to, avoids);
//        return new BreadFirstSearch(map).findShortestPath(from,to,avoids);
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
}
