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

    public Station(String name,int stationID,Int2D location){
        this.name = name;
        this.stationID = stationID;
        this.location = location;
    }

    @Override
    public void step(SimState state) {

    }

    public String toString() { return "Station: "+name;}
}
