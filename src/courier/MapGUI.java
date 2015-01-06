package courier;

import sim.app.tutorial4.Tutorial4;
import sim.app.tutorial4.Tutorial4WithUI;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends GUIState {

    public Display2D display;
    public JFrame displayFrame;

    public MapGUI(SimState state) {
        super(state);
    }

    SparseGridPortrayal2D mapGridPortrayal = new SparseGridPortrayal2D();

    public static void main(String[] args)
    {
        new MapGUI().createController();
    }

    public MapGUI() { super(new Map(System.currentTimeMillis())); }

    public static String getName() { return "Courier"; }


    public void quit()
    {
        super.quit();

        if (displayFrame!=null) displayFrame.dispose();
        displayFrame = null;  // let gc
        display = null;       // let gc
    }

    public void start()
    {
        super.start();
        // set up our portrayals
        setupPortrayals();
    }

    public void load(SimState state)
    {
        super.load(state);
        // we now have new grids.  Set up the portrayals to reflect that
        setupPortrayals();
    }

    public void setupPortrayals()
    {
        // tell the portrayals what to portray and how to portray them
        mapGridPortrayal.setField(((Map)state).mapGrid);
        mapGridPortrayal.setPortrayalForClass(
                Station.class, new sim.portrayal.simple.OvalPortrayal2D(Color.BLUE,1.5) );


        mapGridPortrayal.setPortrayalForClass(
                Car.class, new sim.portrayal.simple.RectanglePortrayal2D(Color.pink));
//                {
//                    public Inspector getInspector(LocationWrapper wrapper, GUIState state)
//                    {
//                        make the inspector
//                        return new BigParticleInspector(super.getInspector(wrapper,state), wrapper, state);
//                    }
//                });

        // reschedule the displayer
        display.reset();

        // redraw the display
        display.repaint();
    }

    public void init(Controller c)
    {
        super.init(c);

        display = new Display2D(400,400,this);
        displayFrame = display.createFrame();
        c.registerFrame(displayFrame);
        displayFrame.setVisible(true);
        display.setBackdrop(Color.lightGray);
        display.attach(mapGridPortrayal,"Map");

    }

    public Object getSimulationInspectedObject()
    {
        return state;
    }


}
