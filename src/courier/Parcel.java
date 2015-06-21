package courier;

public class Parcel {
    protected int parcelID;
    protected double weight;
    protected ExpressCentre from;
    protected ExpressCentre destination;
    protected Map map;
    protected long pickUpTime;
    protected long arriveTime;
//    protected long timeSpending;
    protected long generateTime;
    protected long timeSpendingSincePickUp;
    protected long timeSpendingSinceGenerate;

    Parcel(ExpressCentre from, ExpressCentre destination, double weight, Map map) {
        from.generatingParcel = true;

        this.parcelID = map.serialParcelID;
        this.weight = weight;
        this.destination = destination;
        this.from = from;
        this.map = map;
        this.generateTime = map.schedule.getSteps();
        map.serialParcelID++;
        map.parcelTotal++;
        from.generatingParcel = false;

    }

    Parcel(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Parcel: " + parcelID + " From " + from + " To " + destination;
    }

    public String getTimeSpendingSincePickUp() {
        timeSpendingSincePickUp = map.schedule.getSteps() - pickUpTime;
        if(map.longestDeliverTimeSincePickUp < timeSpendingSincePickUp){
            map.longestDeliverTimeSincePickUp = timeSpendingSincePickUp;
        }
        if (!(this instanceof CarCaller)) {
            map.parcelTimeSpendingTotalSincePickUp += timeSpendingSincePickUp;
        }
        return String.valueOf(timeSpendingSincePickUp);
    }

    public String getTimeSpendingSinceGen() {
        timeSpendingSinceGenerate = map.schedule.getSteps() - generateTime;
        if(map.longestDeliverTimeSinceGenerate < timeSpendingSinceGenerate){
            map.longestDeliverTimeSinceGenerate = timeSpendingSinceGenerate;
        }
        if (!(this instanceof CarCaller)) {
            map.parcelTimeSpendingTotalSinceGen += timeSpendingSinceGenerate;
        }
        return String.valueOf(timeSpendingSinceGenerate);
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
