package courier;

import sim.app.networktest.NetworkTest;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.util.Int2D;

import java.awt.*;

public class RefugeeIsland extends ExpressCentre {

    public static int carParkAvailable;

    public RefugeeIsland(String name, Int2D location, Map map) {
        super(name, location, map);
        carParkAvailable = map.carParkAvailable;
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double diamx = info.draw.width * NetworkTest.DIAMETER / 2;
        double diamy = info.draw.height * NetworkTest.DIAMETER / 2;

        graphics.setColor(new Color(250, 222, 68));
        graphics.fillOval((int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2), (int) (diamx), (int) (diamy));
        graphics.setFont(nodeFont.deriveFont(nodeFont.getSize2D() * (float) info.draw.width));
        graphics.setColor(Color.black);
//        graphics.drawString("RI:" + this.stationID + " C:" + carPark.size() , (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));
        graphics.drawString("S:" + name + " C:" + carPark.size() + " P:" + pToBeSent.size() + " A:" + pArrived.size(), (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));

    }

    public boolean askForCarPark(){
        if(carParkAvailable>0) {
            carParkAvailable--;
            return true;
        }else{
            return false;
        }
    }

    public void carLeaveCarPark(){
        carParkAvailable++;
    }

    @Override
    public void step(SimState state) {

    }

}
