package courier;

import sim.app.networktest.NetworkTest;
import sim.portrayal.DrawInfo2D;
import sim.util.Int2D;

import java.awt.*;

public class RefugeeIsland extends ExpressCentre  {
    public RefugeeIsland(String name, int stationID, Int2D location, Map map) {
        super(name, stationID, location, map);
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double diamx = info.draw.width * NetworkTest.DIAMETER / 2;
        double diamy = info.draw.height * NetworkTest.DIAMETER / 2;

        graphics.setColor(Color.BLUE);
        graphics.fillOval((int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2), (int) (diamx), (int) (diamy));
        graphics.setFont(nodeFont.deriveFont(nodeFont.getSize2D() * (float) info.draw.width));
        graphics.setColor(Color.black);
        graphics.drawString("RI:" + this.stationID + " C:" + carPark.size() + " P:" + pToBeSent.size() + " A:" + pArrived.size(), (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));
    }
}
