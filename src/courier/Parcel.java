package courier;

import java.text.SimpleDateFormat;

public class Parcel {
    protected int parcelID;
    protected double weight;
    protected ExpressCentre from;
    protected ExpressCentre destination;
    protected Map map;
    protected long releaseTime = System.currentTimeMillis();

    Parcel(int parcelID, ExpressCentre from, ExpressCentre destination, double weight, Map map) {
        this.parcelID = parcelID;
        this.weight = weight;
        this.destination = destination;
        this.from = from;
        this.map = map;

    }

    @Override
    public String toString() {
        return "Parcel :" + parcelID;
    }

    public String getTimeSpending() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return (sdf.format(System.currentTimeMillis() - releaseTime));
    }


}
