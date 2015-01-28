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
    protected static final int MAX_PACKAGES = 10;
    public String name;
    protected int stationID;
    protected List<Car> carPark = new LinkedList<Car>();
    protected Int2D location;
    protected LinkedList<Parcel> pToBeSent = new LinkedList<Parcel>();
    protected LinkedList<Parcel> pToBeSentForCarCallerPickUp = new LinkedList<Parcel>();
    protected LinkedList<Parcel> pArrived = new LinkedList<Parcel>();
    protected LinkedList<ExpressCentre> neighbours = new LinkedList<ExpressCentre>();
    protected Map map;
    // number of car caller of a station can have;
    protected int carCallerSema = 1;
    protected long lastVisitTime = 0;
    private int stationDisplaySize = 5;
    public Font nodeFont = new Font("Station", Font.BOLD | Font.ROMAN_BASELINE, stationDisplaySize - 1);
    // busy indicates how busy the station is, the number should between 1000 and 0,
    // the bigger the number, the more busy it is
    private int busy = 990;
    private int count = 0;
    private long sequence = 300;

    public ExpressCentre(String name, int stationID, Int2D location, Map map) {
        this.name = name;
        this.stationID = stationID;
        this.location = location;
        this.map = map;
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

    //
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

//    private void callCar() {
//        ExpressCentre expressCentre = findStationWithFreeCar();
//        if (expressCentre != null) {
//            carCallerSema--;
//            expressCentre.pToBeSent.add(new CarCaller(expressCentre, this, map));
//            if (map.detailsOn)
//                System.out.println("Log: " + this + " has put a CarCaller in" + expressCentre + "...");
//        }
//    }
//
//    private ExpressCentre findStationWithFreeCar() {
//        LinkedList<ExpressCentre> result = new LinkedList<ExpressCentre>();
//        for (ExpressCentre s : map.allStations) {
//            if (s.carPark.size() > 0 && s.pToBeSent.size() == 0 && this.reachable(s)) {
//                for (Car c : s.carPark) {
//                    if (c.getCarrying().size() == 0) {
//                        result.add(s);
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public Parcel findParcelWithWeightFromCarCallerPickUp(double weight){

        for(Parcel p : pToBeSentForCarCallerPickUp){
            if(p.weight == weight) {
                pToBeSentForCarCallerPickUp.remove(p);
                return p;
            }
        }
        return null;
    }

    @Override
    public void step(SimState state) {
//      if the car park is empty, has package to be sent, and the car caller is empty
        if (map.schedule.getSteps() - lastVisitTime > sequence) {
            if (reachableByGarage()) {
                if (this.pToBeSent.size() > 0 && this.carPark.size() == 0){
//                    callCar();
                    if(map.callCarToPickUpParcels.size()<=map.initNumOfCarsInStation) {
                            map.callCarToPickUpParcels.add(this.pToBeSent.pop());
                    }
                }

                if (pToBeSent.size() < MAX_PACKAGES && genParcelOrNot() && !map.testModeOn) {
                    map.addRandomParcel(this);
                    map.parcelTotal++;
                }
            }
        }
    }

    private boolean genParcelOrNot() {
        Random random = new Random();
        // this number indicate how busy the station is
        int j = random.nextInt(Math.abs(1000 - busy));

        // if i is smaller than busy then generalize a parcles
        if (1 > j)
            return true;
        else
            return false;
    }

    @Override
    public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double diamx = info.draw.width * NetworkTest.DIAMETER / 2;
        double diamy = info.draw.height * NetworkTest.DIAMETER / 2;

        graphics.setColor(Color.blue);
        graphics.fillOval((int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2), (int) (diamx), (int) (diamy));
        graphics.setFont(nodeFont.deriveFont(nodeFont.getSize2D() * (float) info.draw.width));
        graphics.setColor(Color.black);
        graphics.drawString("S:" + name + " C:" + carPark.size() + " P:" + pToBeSent.size() + " A:" + pArrived.size(), (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));
    }

}
