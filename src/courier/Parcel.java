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

    Parcel(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Parcel: " + parcelID + " From " + from + " To " + destination;
    }

    public String getTimeSpending() {
        timeSpending = map.schedule.getSteps() - pickUpTime;

        if (!(this instanceof CarCaller)) {
            map.parcelTimeSpendingTotal += this.timeSpending;

        }
        return String.valueOf(timeSpending);
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

    // add parcels with fixed destination and number
    public void addFixedLocParcel(ExpressCentre currExpressCentre) {
        int i = map.expressCentres.indexOf(currExpressCentre);
        int j = 1;
        int next;

        // add "initNumOfParcelsInExpressCentre" numbers of parcels
        for (int k = 0; k < map.initNumOfParcelsInExpressCentre; k++) {
            if (currExpressCentre.reachableByGarage()) {
                if (currExpressCentre.neighbours.containsAll(map.garages) && currExpressCentre.neighbours.size() == map.garages.size()) {
                    return;
                }
                do {
                    next = (i + j) % (map.expressCentres.size());
                    j++;
                }
                while (!(!map.expressCentres.get(next).equals(currExpressCentre) && currExpressCentre.reachable(map.expressCentres.get(next))));

                // dynamically add packages
                if (j % 2 == 0) {
                    currExpressCentre.pToBeSent.addFirst(new Parcel(map.serialParcelID, currExpressCentre, map.expressCentres.get(next), getNextInt(Car.maxSpace), map));
                } else {
                    currExpressCentre.pToBeSent.add(new Parcel(map.serialParcelID, currExpressCentre, map.expressCentres.get(next), getNextInt(Car.maxSpace), map));
                }
                map.serialParcelID++;
                map.parcelTotal++;


                // in order to increase the randomization of package we add some extra packages
                if (next % 3 == 1) {
                    for (int f = 0; f < 10; f++) {
                        currExpressCentre.pToBeSent.add(new Parcel(map.serialParcelID, currExpressCentre, map.expressCentres.get(next), getNextInt(Car.maxSpace), map));
                        map.serialParcelID++;
                        map.parcelTotal++;
                    }
                }

            }
        }
    }

    public void addRandomParcel(ExpressCentre currExpressCentre) {
        if (currExpressCentre.hasNeighbour()) {
            if (currExpressCentre.neighbours.containsAll(map.garages) && currExpressCentre.neighbours.size() == map.garages.size()) {
                return;
            }
            int next;
            do {
                next = map.random.nextInt(map.expressCentres.size());
            }
            while (!(!map.expressCentres.get(next).equals(currExpressCentre) && currExpressCentre.reachable(map.expressCentres.get(next))));

            currExpressCentre.pToBeSent.add(new Parcel(map.serialParcelID, currExpressCentre, map.expressCentres.get(next), getNextInt(Car.maxSpace), map));
            map.serialParcelID++;
            map.parcelTotal++;
        }
    }

    // return a number beyond limit and greater than 0
    private int getNextInt(int limit) {
        int result;
        do {
            result = map.random.nextInt(limit);
        } while (result == 0);
        return result;
    }

}
