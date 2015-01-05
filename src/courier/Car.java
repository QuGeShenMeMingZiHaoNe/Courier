package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

public class Car implements Steppable {
    public int carID;
    public double maxWeight = 50;
    public List<Parcel> carrying = new LinkedList<Parcel>();
    public int speed;
    public LinkedList<Station> pathLocal;
    public LinkedList<Station> pathGlobal;
    public Int2D location;
    public Map map;

    public Car(int carID, Int2D location,Map map){
        this.carID = carID;
        this.location = location;
        this.map = map;
    }


    public boolean loadParcel(){
        Station s = new Station();
        s = s.findStationByLoc(this.location);

        if(s.pToBeSent.size()==0) return false;
        // TODO load package number
        Parcel p = s.pToBeSent.get(0);
        s.pToBeSent.remove(0);
        carrying.add(p);
        return true;
    }

    // unload parcel
    public void unloadParcel(){
        Station s = new Station();
        s = s.findStationByLoc(this.location);

        List<Parcel> unload= parcelsToUnload(s);
        carrying.removeAll(unload);
        s.pArrived.addAll(unload);
    }

    // for the parcels that have arrived the final destination
    private List<Parcel> parcelsToUnload(Station s){
        List<Parcel> toUnload = new LinkedList<Parcel>();
        for(Parcel p :carrying){
            if(p.destination.stationID == s.stationID){
                toUnload.add(p);
            }
        }
        return toUnload;
    }

    // arrive carpark
    public boolean arriveStation(){
        Station s = new Station();
        s = s.findStationByLoc(this.location);
        s.carPark.add(this);
        pathLocal.clear();
        pathGlobal.clear();
        unloadParcel();
        loadParcel();
        return true;
    }

    private boolean setPathLocal(Station from,Station to){
        Tramline tl = new Tramline();
        tl.getStepsNB(from,to);
        return true;
    }

    private boolean setPathGlobal(Station from, Station to){
        Tramline tl = new Tramline();
        tl.getPathGlobal(from,to);
        return true;
    }

    // leave carpark
    public boolean leaveStation(){
        if(!carrying.isEmpty()) {
            // get the top parcel
            Parcel p = carrying.get(0);

            Station targetStation = new Station();
            targetStation = targetStation.findStationByID(p.destination.stationID);

            Station currStation = targetStation.findStationByLoc(this.location);

            setPathGlobal(currStation,targetStation);
            setPathLocal(currStation,pathGlobal.get(0));

            currStation.carPark.remove(this);
        }
        return true;
    }

        @Override
    public void step(SimState state) {
            Map map = (Map) state;
            IntGrid2D mapGrid = map.mapGrid;

    }
}
















