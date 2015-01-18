package courier;

import java.text.SimpleDateFormat;

/**
 * Created by daniel on 15/1/4.
 */
public class Parcel {
    protected int parcelID;
    protected double weight;
    protected Station from;
    protected Station destination;
    protected Map map;
    protected long releaseTime = System.currentTimeMillis();

    Parcel(int parcelID, Station from, Station destination, double weight, Map map) {
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

    public String getTimeSpending(){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return (sdf.format(System.currentTimeMillis()-releaseTime));
    }


}
