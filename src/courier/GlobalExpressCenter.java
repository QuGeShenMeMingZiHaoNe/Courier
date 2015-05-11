package courier;

import sim.engine.SimState;
import sim.util.Int2D;

import java.util.LinkedList;

public class GlobalExpressCenter extends ExpressCentre {
    protected LinkedList<Parcel> callCarToPickUpParcels = new LinkedList<Parcel>();

    private int restoreTime = 5000;
    private int lastTimeVisit =0;

    public GlobalExpressCenter(String name, Int2D location, Map map) {
        super(name, location, map);
    }

    @Override
    public void step(SimState state) {
        if(lastVisitTime>restoreTime){
            lastVisitTime=0;
                returnParcels();
        }
        lastVisitTime++;
    }

    private void returnParcels(){
        if(callCarToPickUpParcels.isEmpty()){
            return;
        }
        LinkedList<Parcel> copy = (LinkedList<Parcel>) callCarToPickUpParcels.clone();
        for(Parcel p: copy) {
            callCarToPickUpParcels.remove(p);
            p.from.pToBeSent.addFirst(p);
            System.out.println("Parcel been restored: "+p);
        }
    }
}
