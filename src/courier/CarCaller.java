package courier;

public class CarCaller extends Parcel {
    CarCaller(ExpressCentre from, ExpressCentre destination, Double weight, Map map) {
        super(from, destination, weight, map);

        map.serialParcelID--;
        map.parcelTotal--;
        map.serialCarCallerID++;
    }

    @Override
    public String toString() {
        return "CarCaller: " + parcelID;
    }
}
