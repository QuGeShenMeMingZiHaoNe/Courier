package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 15/1/4.
 */
public class Car implements Steppable {
    public int carID;
    public double maxWeight = 50;
    public List<Parcel> carrying = new LinkedList<Parcel>();
    public int speed;
    public Station nextStation;
    public Int2D loc;

    public Car(int carID, Int2D loc){
        this.carID = carID;
        this.loc = loc;
    }

    @Override
    public void step(SimState state) {

    }

    public boolean load(List<Parcel> parcelsToBeSent){
        if(parcelsToBeSent.size()==0) return false;
        Parcel p = parcelsToBeSent.get(0);
        parcelsToBeSent.remove(0);
        carrying.add(p);
        return true;
    }

    public void unload(Station s){
        // unload parcel
        List<Parcel> unload= parcelsToUnload(s);
        carrying.removeAll(unload);
        s.pArrived.addAll(unload);
    }

    // for the parcels that have arrived the final destination
    public List<Parcel> parcelsToUnload(Station s){
        List<Parcel> toUnload = new LinkedList<Parcel>();
        for(Parcel p :carrying){
            if(p.destination == s.stationID){
                toUnload.add(p);
            }
        }
        return toUnload;
    }

    // arrive carpark
    public boolean arriveStation(Station s){
        s.carPark.add(this);
        nextStation = null;
        return true;
    }

    // leave carpark
    public boolean leaveStation(Station leave,Station target){
        leave.carPark.remove(this);
        nextStation = target;
        return true;
    }
}
