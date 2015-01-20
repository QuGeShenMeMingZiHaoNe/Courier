package courier;

import sim.engine.SimState;
import sim.util.Int2D;

/**
 * Created by daniel on 15/1/20.
 */
public class Garage extends ExpressCentre {
    protected static final int MAX_PACKAGES = 0;

    public Garage(String name, int stationID, Int2D location, Map map) {
        super(name, stationID, location, map);
    }

    @Override
    public void step(SimState state) {

    }

    public boolean isGarage(Int2D loc) {
        for (Garage g : map.garages) {
            if (g.location.equals(loc))
                return true;
        }
        return false;
    }

    public Garage findGarageByLoc(Int2D loc) {
        for (Garage g : map.garages) {
            if (g.location.equals(loc))
                return g;
        }
        return null;
    }
}
