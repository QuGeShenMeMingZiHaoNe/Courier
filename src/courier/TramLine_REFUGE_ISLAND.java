package courier;

import sim.app.networktest.NetworkTest;
import sim.portrayal.DrawInfo2D;
import sim.util.Int2D;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class TramLine_REFUGE_ISLAND extends TramLine_BASIC {
    private int stationDisplaySize = 3;

    public Font nodeFont = new Font("Station", Font.BOLD | Font.ROMAN_BASELINE, stationDisplaySize - 1);

    protected LinkedList<RefugeeIsland> refugee_islands_local = new LinkedList<RefugeeIsland>();

    public TramLine_REFUGE_ISLAND(String line, ExpressCentre a, ExpressCentre b, int tramLineID, Map map, int numOfIsland) {
        super(line, a, b, tramLineID, map);
        addRefugeeIsland(a,b,numOfIsland);
    }

    private void addRefugeeIsland(ExpressCentre a, ExpressCentre b,int numOfIsland){
        int count = numOfIsland;
        while (count>0){
            Int2D loc = new Int2D(b.location.x+(a.location.x-b.location.x)/(count+1),b.location.y+(a.location.y-b.location.y)/(count+1));
            RefugeeIsland island = new RefugeeIsland("REFUGEE ISLAND", map.serialRefugeeIsland,loc,map);
            map.serialRefugeeIsland++;
            count --;
            refugee_islands_local.add(island);
            map.refugee_islands.add(island);
            map.mapGrid.setObjectLocation(island, loc);
        }

    }
//
//    // returns the path from station a to station b with islands in between
//    private LinkedList<ExpressCentre> calPathWithIslands(ExpressCentre a, ExpressCentre b){
//
//        if(a.location.getX()<b.location.getX()){
//
//        }else {
//
//        }
//
//        return
//    }
//
//
//    @Override
//    // return each coordinates of the path from neighbour station a to b
//    public LinkedList<Int2D> getPathBetweenNBStations(ExpressCentre a, ExpressCentre b) {
//        LinkedList<Int2D> result = new LinkedList<Int2D>();
//
//        if (a.equals(b)) {
//            return result;
//        }
//        ExpressCentre c = null;
//
//        if (a.stationID < b.stationID) {
//            result = buildPath(a, b);
//        } else if (a.stationID != b.stationID) {
//            c = a;
//            a = b;
//            b = c;
//            result = buildPath(a, b);
//        }
//
//        if (c != null) {
//            Collections.reverse(result);
//        }
//
//        return result;
//    }

}
