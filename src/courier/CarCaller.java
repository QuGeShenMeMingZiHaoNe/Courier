package courier;

/**
 * Created by daniel on 15/1/7.
 */
public class CarCaller extends Parcel {
    CarCaller(Station destination, Map map) {
        super(map.serialCarCallerID, destination, 0, map);
        map.serialCarCallerID++;
    }

    @Override
    public String toString() {
        return "CarCaller: " + parcelID;
    }
}
