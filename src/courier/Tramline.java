package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by daniel on 15/1/4.
 */
public class Tramline implements Steppable {
    public Station a;
    public Station b;
    public int tramlineID;
    public Map map;

    public Tramline() {
    }

    public Tramline(Station a, Station b, int tramlineID, Map map) {
        if (a.stationID < b.stationID) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
        this.map = map;
        this.tramlineID = tramlineID;
    }

    // return each step of path from a to b
    public LinkedList<Int2D> getStepsNB(Station a, Station b) {
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        if (a.equals(b)) {
            System.out.println("Attempting to find the route to itself");
            return null;
        }
        Station c = null;

        if (a.stationID < b.stationID) {
            result = buildPath(a, b);
        } else {
            c = a;
            a = b;
            b = c;
            result = buildPath(a, b);
//            while(result.get(0).equals(a))
//                result.pop();
        }

        if (c != null) {
            Collections.reverse(result);
//            while(result.get(0).equals(b))
//                result.pop();
        }
        result.pop();
        return result;
    }

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

//            if (Math.abs(result.get(result.size()-1).getX()-b.location.getX())<1)

            xStep += xDiff / distance;
            yStep += yDiff / distance;

            intXStep = xStep.intValue();
            intYStep = yStep.intValue();

            result.add(new Int2D(a.location.getX() + intXStep, a.location.getY() + intYStep));
//            // set x step
//            if (result.get(result.size()-1).getX() < b.location.getX()) {
//                xStep += 1;
//            } else if (result.get(result.size()-1).getX() > b.location.getX()) {
//                xStep += -1;
//            } else {
//                xStep += 0;
//            }
//
//            // set y step
//            if (result.get(result.size()-1).getY() < b.location.getY()) {
//                yStep += 1;
//            } else if (result.get(result.size()-1).getY() > b.location.getY()) {
//                yStep += -1;
//            } else {
//                yStep += 0;
//            }
//            result.add(new Int2D(a.location.x+xStep,a.location.y+yStep));

        }
        result.pop();
        return result;
    }

    private int findTramlineIndexNB(Station a, Station b) {
        int result = -1;
        Station c;
        Tramline temp;

        if (a.stationID < b.stationID) {
            for (int i = 0; i < map.tramlines.size(); i++) {
                temp = map.tramlines.get(i);
                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
                    return i;
                }
            }
        } else {
            c = a;
            a = b;
            b = c;
            for (int i = 0; i < map.tramlines.size(); i++) {
                temp = map.tramlines.get(i);
                if (temp.a.stationID == a.stationID && temp.b.stationID == b.stationID) {
                    return i;
                }
            }
        }

        // ERROR
        System.out.println("No such tramline!");
        return result;
    }

    public Tramline getPathGlobal(Station a, Station b) {

        // if it is neighbour
        int index = findTramlineIndexNB(a, b);
        if (index >= 0) {
            return map.tramlines.get(index);
        }

        // not neighbour

        //TODO!!!!!!!!!!!!!!!!!!!!!!

        // ERROR no connection
        System.out.println(a.stationID + "can not reach!" + b.stationID);
        return null;
    }

    @Override
    public void step(SimState state) {

    }
}
