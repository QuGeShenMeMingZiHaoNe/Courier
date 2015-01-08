package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 15/1/4.
 */
public class Station implements Steppable {
    public int stationID;
    public List<Car> carPark = new LinkedList<Car>();
    public Int2D location;
    public List<Parcel> pToBeSent = new LinkedList<Parcel>();
    public List<Parcel> pArrived = new LinkedList<Parcel>();
    public Map map;
    private String name;
    public CarCaller carCaller;


    public Station(String name, int stationID, Int2D location, Map map) {
        this.name = name;
        this.stationID = stationID;
        this.location = location;
        this.map = map;
    }

    @Override
    public String toString() {
        return "Station: " + name;
    }

    public Boolean isStation(Int2D loc) {
        for (Station s : map.stations) {
            if (s.location.equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public Station findStationByLoc(Int2D loc) {
        for (Station s : map.stations) {
            if (s.location.equals(loc)) {
                return s;
            }
        }
        return null;
    }

    public Station findStationByID(int id) {
        for (Station s : map.stations) {
            if (s.stationID == id) {
                return s;
            }
        }
        return null;
    }

    //
    public LinkedList<Station> findNeighbours() {
        LinkedList<Station> result = new LinkedList<Station>();
        for (Station s : map.stations) {
            if (!s.equals(this)) {
                if (map.tramLines.get(0).findTramLine(this, s) != null)
                    result.add(s);
            }
        }
        return result;
    }

    // find all reachable station except the one whom called findNeighbours()
    public LinkedList<Station> findAllReachableStations(Station root) {
        LinkedList<Station> neighbours = this.findNeighbours();
        LinkedList<Station> temp, result;
        // does not contains the root node
        while (neighbours.contains(root))
            neighbours.remove(root);

        int size = neighbours.size();
        result = (LinkedList<Station>) neighbours.clone();
        int previous = 0;

        while (previous < size) {
            previous = size;
            for (Station nb : neighbours) {
                temp = nb.findNeighbours();
                result.removeAll(temp);
                result.addAll(temp);
                while (result.contains(root))
                    result.remove(root);
            }
            neighbours = (LinkedList<Station>) result.clone();
            size = neighbours.size();
        }
        return result;
    }

    public boolean reachable(Station b) {
        return this.findAllReachableStations(new Station("null", -1, new Int2D(-1, -1), map)).contains(b);
    }

    private void callCar(){
        Station station = findStationWithFreeCar();
        if(station!=null){
            station.pToBeSent.add(new CarCaller(this,map));
        }
    }

    private Station findStationWithFreeCar(){
        for(Station s:map.stations){
            if(s.carPark.size()>0 && s.pToBeSent.size()==0){
                for(Car c : s.carPark){
                    if (c.carrying.size()==0){
                        return s;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void step(SimState state) {
        // if the car park is empty, has package to be sent, and the car caller is empty
        if(this.pToBeSent.size()>0 && this.carPark.size()==0 && carCaller == null)
            callCar();
    }

}
