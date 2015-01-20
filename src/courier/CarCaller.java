package courier;

public class CarCaller extends Parcel {
    CarCaller(ExpressCenter from, ExpressCenter destination, Map map) {
        super(map.serialCarCallerID, from, destination, 0, map);
        map.serialCarCallerID++;
    }

    @Override
    public String toString() {
        return "CarCaller: " + parcelID;
    }
}
