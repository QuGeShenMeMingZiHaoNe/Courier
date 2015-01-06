package courier;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

import java.util.LinkedList;
import java.util.List;

public class Car implements Steppable {
    public int carID;
    public double maxWeight = 50;
    public List<Parcel> carrying = new LinkedList<Parcel>();
    public int speed;
    public LinkedList<Int2D> pathLocal = new LinkedList<Int2D>();
    public Station nextStation;
    public Int2D location;
    public Map map;
    private int step =0;


    public Car(int carID, Int2D location,Map map){
        this.carID = carID;
        this.location = location;
        this.map = map;
    }


    public boolean loadParcel(){
        Station s = currStation();
        if(s.pToBeSent.size()==0) return false;
        // TODO load package number
        Parcel p = s.pToBeSent.get(0);
        s.pToBeSent.remove(0);
        carrying.add(p);
        return true;
    }

    // unload parcel
    public void unloadParcel(){
        Station s = currStation();

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
        Station s = currStation();
        s.carPark.add(this);
        pathLocal.clear();
        nextStation=null;
        unloadParcel();
        loadParcel();
        return true;
    }

    private boolean setPathLocal(Station from,Station to){
        Tramline tl = map.tramlines.get(0);
        pathLocal = tl.getStepsNB(from,to);
        return true;
    }

    private boolean setPathGlobal(Station from, Station to){
        Tramline tl = map.tramlines.get(0);
        Station currStation = currStation();

        // TODO get 0 , return a station??
        tl = tl.getPathGlobal(from, to);

        if(tl.a.equals(currStation)){
            nextStation = tl.b;
        }else{
            nextStation = tl.a;
        }
        return true;
    }

    // leave carpark
    public boolean leaveStation(){
        if(!carrying.isEmpty()) {
            // get the top parcel
            Parcel p = carrying.get(0);

            Station targetStation = map.stations.get(0);
            targetStation = targetStation.findStationByID(p.destination.stationID);

            Station currStation = currStation();

            setPathGlobal(currStation,targetStation);
            setPathLocal(currStation, nextStation);

            currStation.carPark.remove(this);
        }
        return true;
    }

    private Station currStation(){
        return map.stations.get(0).findStationByLoc(this.location);
    }

    @Override
    public void step(SimState state) {
//        Map map = ((Map)state);
//        SparseGrid2D mapGrid = map.mapGrid;

        Station currStation = currStation();
        if(currStation!=null){
            arriveStation();
            leaveStation();
        }
        if(!carrying.isEmpty()) {
            Int2D nextStep = this.pathLocal.pop();
            this.location = nextStep;
            ((Map) state).mapGrid.setObjectLocation(this, nextStep);
        }
    }
}
















