package courier;

public class CarCaller extends Parcel {
    CarCaller(ExpressCentre from, ExpressCentre destination, Double weight, Map map) {
        super(map.serialCarCallerID, from, destination, weight, map);
        map.serialCarCallerID++;
    }

    @Override
    public String toString() {
        return "CarCaller: " + parcelID;
    }
}
