package courier;

import sim.app.networktest.NetworkTest;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ExpressCentre extends OvalPortrayal2D implements Steppable {
    protected static final int MAX_PACKAGES = 999999999;
    public String name;
    protected int stationID;
    protected List<Car_BASIC> carPark = new LinkedList<Car_BASIC>();
    protected Int2D location;
    protected LinkedList<Parcel> pToBeSent = new LinkedList<Parcel>();
    protected LinkedList<Parcel> pToBeSentForCarCallerPickUp = new LinkedList<Parcel>();
    protected LinkedList<Parcel> infoFromGlobalExpressCenter = new LinkedList<Parcel>();
    protected LinkedList<Parcel> pArrived = new LinkedList<Parcel>();
    protected LinkedList<ExpressCentre> neighbours = new LinkedList<ExpressCentre>();
    protected Map map;
    protected Boolean reachableByGarage;
    // number of car caller of a station can have;
    protected long lastVisitTime = 0;
    private int stationDisplaySize = 5;
    public Font nodeFont = new Font("Station", Font.BOLD | Font.ROMAN_BASELINE, stationDisplaySize - 1);
    // busyLevel indicates how busyLevel the station is, the number should between 1000 and 0,
    // the bigger the number, the more busyLevel it is
    public static int busyLevel;
    private int count = 0;
    private long visitSequence = 1000;
    protected int maxGlobalParcel = 3;
    protected boolean generatingParcel = false;
    int testCount = 0;


    public ExpressCentre(String name, Int2D location, Map map) {
        this.name = name;
        this.stationID = map.serialStationID;
        this.location = location;
        this.map = map;
        map.serialStationID++;
        busyLevel = map.expressCenterBusyLevel;
    }


    public double getProfit() {
        return map.profit;
    }

    public List<Parcel> getpToBeSent() {
        return pToBeSent;
    }

    @Override
    public String toString() {
        return "Station: " + name;
    }

    public Boolean isStation(Int2D loc) {
        for (ExpressCentre s : map.allStations) {
            if (s.location.equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public ExpressCentre findStationByLoc(Int2D loc) {
        for (ExpressCentre s : map.allStations) {
            if (s.location.equals(loc)) {
                return s;
            }
        }
        return null;
    }

    public ExpressCentre findStationByName(String inName) {
        for (ExpressCentre s : map.allStations) {
            if (s.name.equals(inName)) {
                return s;
            }
        }
        return null;
    }

    public ExpressCentre findStationByID(int id) {
        for (ExpressCentre s : map.allStations) {
            if (s.stationID == id) {
                return s;
            }
        }
        return null;
    }

    public LinkedList<ExpressCentre> findNeighbours() {
        return (LinkedList<ExpressCentre>) neighbours.clone();
    }

    public Boolean hasNeighbour() {
        return neighbours.size() > 0;
    }

    public boolean reachableByGarage() {
        return !(new PathSearcher(map).findAllPossiblePath(this, map.garages.getFirst()) == null);
    }

    public boolean reachable(ExpressCentre b) {
        return !(new PathSearcher(map).findAllPossiblePath(this, b) == null);
    }

    public Parcel findParcelWithWeightFromCarCallerPickUp(double weight) {

        for (Parcel p : pToBeSentForCarCallerPickUp) {
            if (p.weight == weight) {
                pToBeSentForCarCallerPickUp.remove(p);
                return p;
            }
        }
        return null;
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
                    currExpressCentre.pToBeSent.addFirst(new Parcel(currExpressCentre, map.expressCentres.get(next), getNextInt(map.carMaxSpace), map));
                } else {
                    currExpressCentre.pToBeSent.add(new Parcel(currExpressCentre, map.expressCentres.get(next), getNextInt(map.carMaxSpace), map));
                }


                // in order to increase the randomization of package we add some extra packages
                if (next % 3 == 1) {
                    for (int f = 0; f < 10; f++) {
                        currExpressCentre.pToBeSent.add(new Parcel(currExpressCentre, map.expressCentres.get(next), getNextInt(map.carMaxSpace), map));
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

            currExpressCentre.pToBeSent.add(new Parcel(currExpressCentre, map.expressCentres.get(next), getNextInt(5), map));
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

    @Override
    public void step(SimState state) {
        // if reachableByGarage is null
//        if (reachableByGarage == null) {
//            reachableByGarage = reachableByGarage();
//        }

//            if(reachableByGarage){

        // generated parcels
        if (!generatingParcel && pToBeSent.size() < MAX_PACKAGES && genParcelOrNot() && map.autoGenParcelByStationsMax > 0) {
            addRandomParcel(this);
            map.autoGenParcelByStationsMax--;

            return;
        }

//      if the car park is empty, has package to be sent, and the car caller is empty
        // TODO we only check for one garage!! What if we have more garages
        if (map.schedule.getSteps() - lastVisitTime > visitSequence || !map.garages.getFirst().carPark.isEmpty()) {
            // put parcel into global list to let other cars to pickup
            if (this.pToBeSent.size() > 0 && this.carPark.size() == 0 && maxGlobalParcel > 0) {
                if (map.gec.callCarToPickUpParcels.size() <= map.initNumOfCarsInGarage) {
                    maxGlobalParcel--;
                    Parcel p = this.pToBeSent.pop();
                    map.gec.callCarToPickUpParcels.add(p);
                    infoFromGlobalExpressCenter.add(p);
                }
            }
        }
    }

    private boolean genParcelOrNot() {
        Random random = new Random();
        // this number indicate how busyLevel the station is
        int j = random.nextInt((10 - busyLevel) * 25 + 1);

        // if i is smaller than busyLevel then generalize a parcles
        if (1 > j)
            return true;
        else
            return false;

    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double diamx = info.draw.width * NetworkTest.DIAMETER / 2;
        double diamy = info.draw.height * NetworkTest.DIAMETER / 2;

        graphics.setColor(Color.BLUE);
        graphics.fillOval((int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2), (int) (diamx), (int) (diamy));
        graphics.setFont(nodeFont.deriveFont(nodeFont.getSize2D() * (float) info.draw.width));
        graphics.setColor(Color.black);
        graphics.drawString("S:" + name + " C:" + carPark.size() + " P:" + pToBeSent.size() + " A:" + pArrived.size(), (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));
    }

}
