package courier;

public class CarCaller extends Parcel {
    CarCaller(ExpressCentre from, ExpressCentre destination, Map map) {
        super(map.serialCarCallerID, from, destination, 0, map);
        map.serialCarCallerID++;
    }

    @Override
    public String toString() {
        return "CarCaller: " + parcelID;
    }
}
