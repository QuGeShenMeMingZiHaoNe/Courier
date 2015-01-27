package courier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        return "Parcel: " + parcelID + " From " + from + " To " + destination;
    }

    public String getTimeSpending() {
        timeSpending = map.schedule.getSteps() - pickUpTime;

        if (map.testModeOn && !(this instanceof CarCaller)) {
            map.parcelTimeSpendingTotal += this.timeSpending;
            if (map.detailsOn) {
                outputFile(this + " DELIVERED FROM " + from + " TO " + destination + "\nRELEASE TIME " + (pickUpTime) + " ARRIVED TIME " + (arriveTime) + " TIME SPENDING " + (timeSpending) + "\nPARCEL REMAINING " + map.parcelTotal + "...\n");
            }

            // the ending of the output file
            if (map.parcelTotal == 0) {
                long timeSpendingAverage = map.parcelTimeSpendingTotal / map.parcelTotalCopy;
                outputFile("\n\n\n\nTotal spending time: " + (map.parcelTimeSpendingTotal) + "\nTime Spending Average: " + (timeSpendingAverage));
                outputFile("Mode: " + map.mode + "\nCar number: " + map.initNumOfParcelsInGarage + "\nParcel number: " + map.parcelTotalCopy + "\nExpressCenter: " + map.expressCentres.size());
                System.out.println("Finish!!!!!!!");
            }
        }
        return String.valueOf(timeSpending);
    }

    private void outputFile(String write) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + map.mode + " " + map.initTime + ".output", true)));
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
