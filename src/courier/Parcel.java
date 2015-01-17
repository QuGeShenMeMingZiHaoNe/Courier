package courier;

/**
 * Created by daniel on 15/1/4.
 */
public class Parcel {
    protected int parcelID;
    protected double weight;
    protected Station destination;
    protected Map map;

    Parcel(int parcelID, Station destination, double weight, Map map) {
        this.parcelID = parcelID;
        this.weight = weight;
        this.destination = destination;
        this.map = map;
    }

    @Override
    public String toString() {
        return "Parcel :" + parcelID;
    }

}
