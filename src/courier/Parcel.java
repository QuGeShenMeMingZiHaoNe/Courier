package courier;

/**
 * Created by daniel on 15/1/4.
 */
public class Parcel {
    public int parcelID;
    public double weight;
    public Station destination;
    public Map map;

    Parcel(int parcelID, Station destination, double weight, Map map) {
        this.parcelID = parcelID;
        this.weight = weight;
        this.destination = destination;
        this.map = map;
    }

}
