package courier;

/**
 * Created by daniel on 15/1/4.
 */
public class Parcel {
    public int parcelID;
    public double weight;
    public int destination;

    Parcel(int parcelID,int destination, double weight){
        this.parcelID = parcelID;
        this.weight = weight;
        this.destination = destination;
    }

}
