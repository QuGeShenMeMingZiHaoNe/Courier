package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 15/1/4.
 */
public class Station  implements Steppable {
    private String name;
    public int stationID;
    public List<Car> carPark = new LinkedList<Car>();
    public Int2D location;
    public List<Parcel> pToBeSent = new LinkedList<Parcel>();
    public List<Parcel> pArrived = new LinkedList<Parcel>();
    public Map map;

    public  Station(){};
    public Station(String name,int stationID,Int2D location, Map map){
        this.name = name;
        this.stationID = stationID;
        this.location = location;
        this.map = map;
    }

    @Override
    public void step(SimState state) {

    }

    @Override
    public String toString() { return "Station: "+name;}

    public Boolean isStation(Int2D loc){
    	for(Station s: map.stations){
    		if(s.location.equals(loc)){
                return true;
            }
    	}
        return false;
    }

    public Station findStationByLoc(Int2D loc){
        for(Station s: map.stations){
            if(s.location.equals(loc)){
                return s;
            }
        }
        return null;
    }

    public Station findStationByID(int id){
        for(Station s: map.stations){
            if(s.stationID == id){
                return s;
            }
        }
        return null;
    }
}
