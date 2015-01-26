package courier;

import java.io.*;
import java.text.SimpleDateFormat;

public class Parcel {
    protected int parcelID;
    protected double weight;
    protected ExpressCentre from;
    protected ExpressCentre destination;
    protected Map map;
    protected long pickUpTime;
    protected long arriveTime;
    protected long timeSpending;

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
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        timeSpending = map.schedule.getSteps() - pickUpTime;
        if(map.testModeOne && !(this instanceof CarCaller)) {
            outputFile(this + " DELIVERED FROM " + from + " TO " + destination + "\nRELEASE TIME " + (pickUpTime) + " ARRIVED TIME " + (arriveTime) + " TIME SPENDING " + (timeSpending) + "\nPARCEL REMAINING " + map.parcelTotal+"...\n");
                map.parcelTimeSpendingTotal+=this.timeSpending;
            // the ending of the output file
            if(map.parcelTotal == 0) {
                map.lastParcelArrivedTime = this.arriveTime;
                long timeSpendingAverage = map.parcelTimeSpendingTotal/map.parcelTotalCopy;
                outputFile("\n\n\n\nTotal spending time: "+ (map.parcelTimeSpendingTotal) + "\nTime Spending Average: "+ (timeSpendingAverage));
                outputFile("Mode: "+map.mode+" Car number: "+map.initNumOfParcelsInStation+" Parcel number: "+map.parcelTotalCopy+" ExpressCenter: "+map.expressCentres.size());
            }
        }
        return String.valueOf(timeSpending);
    }

    private void outputFile(String write){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/"+ map.mode+ map.initTime+".output", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);

            writer.close();
    }

    // TODO: better simulation as the distance does not == car step
    private double getMinimumCost() {
        PathSearcher ps = new PathSearcher(map);
        // find the minimum cost base on the shortest path

        double minimumCost = from.location.distance(destination.location);
        return minimumCost;
    }

    public double getProfit() {
        return getMinimumCost() * map.profitMargin;
    }

}
