package courier;

import com.sun.tools.classfile.Synthetic_attribute;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.SparseField;
import sim.util.Int2D;

import javax.xml.stream.Location;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by daniel on 15/1/4.
 */
public class Tramline implements Steppable {
    public Station a;
    public Station b;
    public int tramlineID;

    public Tramline(Station a, Station b, int tramlineID){
        if(a.stationID>=b.stationID) {
            this.a = a;
            this.b = b;
        }else{
            this.a = b;
            this.b = a;
        }
        this.tramlineID = tramlineID;
    }

    private LinkedList<Int2D> getPath(Station a, Station b){
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        if(a.equals(b)){
            return null;
        }

        Station c = null;

        if(a.stationID<b.stationID){
            buildPath(a,b);
        } else {
            c = a;
            a = b;
            b = c;
            buildPath(a,b);
        }

        if(c != null){
            Collections.reverse(result);
        }

        return result;
    }

    private LinkedList<Int2D> buildPath(Station a, Station b){
        LinkedList<Int2D> result = new LinkedList<Int2D>();

        int xStep,yStep;
        xStep = -1;
        yStep = -1;


        while(xStep!=0 && yStep!=0) {
            // set x step
            if (a.location.getX() < b.location.getX()) {
                xStep = 1;
            } else if (a.location.getX() > b.location.getX()) {
                xStep = -1;
            } else {
                xStep = 0;
            }

            // set y step
            if (a.location.getY() < b.location.getY()) {
                xStep = 1;
            } else if (a.location.getY() > b.location.getY()) {
                xStep = -1;
            } else {
                xStep = 0;
            }
            result.add(new Int2D(xStep,yStep));
        }

        return result;
    }

    private int findTramlineIndex(Station a, Station b, LinkedList<Tramline> tramlines){
        int result = -1;
        Station c;
        Tramline temp;

        if(a.stationID < b.stationID){
            for(int i=0; i<tramlines.size();i++){
                temp = tramlines.get(i);
                if(temp.a.stationID == a.stationID && temp.b.stationID == b.stationID){
                    return i;
                }
            }
        }else{
            c=a;
            a=b;
            b=c;
            for(int i=0; i<tramlines.size();i++){
                temp = tramlines.get(i);
                if(temp.a.stationID == a.stationID && temp.b.stationID == b.stationID){
                    return i;
                }
            }
        }

        // ERROR
        System.out.println("No such tramline!");
        return result;
    }

    public Tramline getTramline(Station a, Station b, LinkedList<Tramline> tramlines){
        int index = findTramlineIndex(a, b, tramlines);
        if(index>=0){
            return tramlines.get(index);
        }

        // ERROR
        System.out.println("No such tramline!");
        return null;
    }

    @Override
    public void step(SimState state) {

    }
}
