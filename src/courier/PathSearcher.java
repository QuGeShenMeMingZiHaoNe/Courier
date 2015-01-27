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
        LinkedList<ExpressCentre> r1 = new AStar(map).findShortestPath(from, to, new LinkedList<ExpressCentre>());
//        LinkedList<ExpressCentre> r2 = new BreadFirstSearch(map).findShortestPath(from,to);
//
//        if(r1!=null&&r2!=null&&!r1.equals(r2)) {
//            double d1 = calPathDistance(r1);
//            double d2 = calPathDistance(r2);
//            if(d1>d2) {
//                System.out.println("Basic"+"&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(");
//                System.out.println((d1 < d2) + "" + d1 + " " + r1 + "\n" + d2 + " " + r2 + "\n");
//            }
//        }
        return r1;

    }

    // type with traffic avoiding
    public LinkedList<ExpressCentre> findAllPossiblePath(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> avoids) {
        LinkedList<ExpressCentre> r1 = new AStar(map).findShortestPath(from, to, avoids);
//        LinkedList<ExpressCentre> r2 = new BreadFirstSearch(map).findShortestPath(from,to,avoids);
//
//        if(r1!=null&&r2!=null&&!r1.equals(r2)) {
//            double d1 = calPathDistance(r1);
//            double d2 = calPathDistance(r2);
//            if(d1>d2) {
//
//                System.out.println("Avoiding"+"&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(^&*(");
//                System.out.println((d1 < d2) + "" + d1 + " " + r1 + "\n" + d2 + " " + r2 + "\n");
//            }
//        }
        return r1;
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
