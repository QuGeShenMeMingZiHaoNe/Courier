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


    public Station() {
    }

    ;

    public Station(String name, int stationID, Int2D location, Map map) {
        this.name = name;
        this.stationID = stationID;
        this.location = location;
        this.map = map;
    }

    @Override
    public void step(SimState state) {

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
                map.tramLines.get(0).findTramLine(this, s);
                result.add(s);
            }
        }
        return result;
    }

    // find all reachable station except the one whom called findNeighbours()
    public LinkedList<Station> findAllReachableStations(Station root) {
        LinkedList<Station> neighbours = this.findNeighbours();
        LinkedList<Station> temp;
        // does not contains the root node
        while (neighbours.contains(root))
            neighbours.remove(root);

        int size = neighbours.size();
        int previous = 0;

        // TODO when nb = 0

        while (previous < size) {
            previous = size;
            for (Station nb : neighbours) {
                temp = nb.findNeighbours();
                neighbours.removeAll(temp);
                neighbours.addAll(temp);
                while (neighbours.contains(root))
                    neighbours.remove(root);
            }
            size = neighbours.size();
        }
        return neighbours;
    }
}
