/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.app.balls3d;

import sim.util.*;

public class Band implements java.io.Serializable, sim.util.Valuable
    {
    private static final long serialVersionUID = 1;

    public double laxDistance;
    public double strength;
    
    public Band(double laxDistance, double strength)
        { this.laxDistance = laxDistance; this.strength = strength; }

    // Bean Properties for our Inspector 
    public void setStrength(double val) { if (val > 0) strength = val; }
    public double getStrength() { return strength; }
    public void setLaxDistance(double val) { if (val >= 0) laxDistance = val; }
    public double getLaxDistance() { return laxDistance; }

    public double doubleValue() { return strength; }
        
    public String toString() { return "" + strength + " (" + laxDistance + ")"; }
    }
