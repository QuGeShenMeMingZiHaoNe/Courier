package courier;

import java.util.LinkedList;

/**
 * Created by daniel on 15/1/28.
 */
public class ModeSelector {
    LinkedList<SIMULATION_MODE> modes;
    Number min;
    Number max;
    boolean isDouble;

    public ModeSelector(LinkedList<SIMULATION_MODE> modes) {
        this.modes = modes;
    }

    public SIMULATION_MODE getMin() {
        return modes.getFirst();
    }

    public SIMULATION_MODE getMax() {
        return modes.getLast();
    }

    public boolean contains(SIMULATION_MODE mode) {
        return modes.contains(mode);
    }

}
