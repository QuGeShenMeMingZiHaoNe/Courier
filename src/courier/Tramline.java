package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Tramline implements Steppable {
    public Station a;
    public Station b;
    public int tramlineID;
    public Map map;

    // record of all the cars on the tram line
    public LinkedList<Car> carsOnTramline = new LinkedList<Car>();

    // count the requirements for station a and b
//    public int requireAccessCount1 =0;
//    public int requireAccessCount2 =0;
    // when the requirements reach a certain limit then we give the traffic control right to the other station.
    private int requirementThreshold = 3;
    public int quota1 = requirementThreshold;
    public int quota2 = requirementThreshold;
    private boolean clearingTheRoad = false;
    // whom is controlling the traffic
    public Station trafficLightOccupant;
    public Car currLeavingCar;




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

        // randomly assign trafficLightOccupant to one of the station
        if(new Random().nextInt(2)==0){
            trafficLightOccupant =a;
        }else{
            trafficLightOccupant = b;
        }
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
        }

        if (c != null) {
            Collections.reverse(result);
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

            xStep += xDiff / distance;
            yStep += yDiff / distance;

            intXStep = xStep.intValue();
            intYStep = yStep.intValue();

            result.add(new Int2D(a.location.getX() + intXStep, a.location.getY() + intYStep));
        }
        result.pop();
        return result;
    }

    private int findTramLineIndexNB(Station a, Station b) {
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

        return result;
    }

    public Tramline findTramLine(Station a, Station b){
        return map.tramlines.get(findTramLineIndexNB(a,b));
    }

    public Tramline getPathGlobal(Station a, Station b) {

        // if it is neighbour
        int index = findTramLineIndexNB(a, b);
        if (index >= 0) {
            return map.tramlines.get(index);
        }

        // not neighbour

        //TODO!!!!!!!!!!!!!!!!!!!!!!

        // ERROR no connection
        System.out.println(a.stationID + "can not reach!" + b.stationID);
        return null;
    }


    public void tryOccupyTraffic(Station demander){
        if(clearingTheRoad){
            if(roadClean()){
                clearingTheRoad = false;
            }
            return;
        }
        if(demander.equals(a)){
            // give the traffic to a
            if(quota2==0||b.carPark.isEmpty()){
                trafficLightOccupant = a;
                clearingTheRoad = true;
                quota2=0;
                quota1=requirementThreshold;
            }
        }else{
            // give the traffic to b
            if(quota1==0||a.carPark.isEmpty()){
                trafficLightOccupant = b;
                clearingTheRoad = true;
                quota1=0;
                quota2=requirementThreshold;
            }
        }
    }

    public boolean roadClean(){
        return carsOnTramline.isEmpty();
    }

    public boolean isController(Station asker){
        return asker.equals(trafficLightOccupant);
    }

    public boolean okToLeave(Station asker){
        return !clearingTheRoad && trafficLightOccupant.equals(asker);
    }
    @Override
    public void step(SimState state) {

    }
}
