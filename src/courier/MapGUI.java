package courier;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MapGUI extends GUIState {

    private Display2D display;
    private JFrame displayFrame;
    private SparseGridPortrayal2D mapGridPortrayal = new SparseGridPortrayal2D();
    private NetworkPortrayal2D tramLinePortrayal = new NetworkPortrayal2D();


    public MapGUI(SimState state) {
        super(state);
    }

    public MapGUI() {
        super(new Map(System.currentTimeMillis()));
    }

    public static void main(String[] args) {
        new MapGUI().createController();
    }

    public static String getName() {
        return "Courier: ";
    }


    public void quit() {
        super.quit();

        if (displayFrame != null) displayFrame.dispose();
        displayFrame = null;  // let gc
        display = null;       // let gc
    }

    public void start() {
        super.start();
        // set up our portrayals
        setupPortrayals();
    }

    public void load(SimState state) {
        super.load(state);
        // we now have new grids.  Set up the portrayals to reflect that
        setupPortrayals();
    }


    public void setupPortrayals() {
        Map map = ((Map) state);
        // tell the portrayals what to portray and how to portray them
        mapGridPortrayal.setField(map.mapGrid);

        tramLinePortrayal.setField(new SpatialNetwork2D(map.mapGrid, map.tramLineNet));
        tramLinePortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());

        // reschedule the displayer
        display.reset();

        // redraw the display
        display.repaint();

    }

    public Object getSimulationInspectedObject() {
        return state;
    }

    public Inspector getInspector() {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }

    public void init(Controller c) {
        super.init(c);

        if (Map.readTestSetting) {
            try {
                new TestReader().read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        display = new Display2D(1000, 700, this);
        displayFrame = display.createFrame();

        c.registerFrame(displayFrame);

        displayFrame.setVisible(true);
        display.setBackdrop(Color.LIGHT_GRAY);

        display.attach(mapGridPortrayal, "Map");
        display.attach(tramLinePortrayal, "Tram Line");

    }

}
