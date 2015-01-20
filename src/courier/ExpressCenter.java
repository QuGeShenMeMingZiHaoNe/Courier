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

public class ExpressCenter extends OvalPortrayal2D implements Steppable {
    protected static final int MAX_PACKAGES = 10;
    protected int stationID;
    protected List<Car> carPark = new LinkedList<Car>();
    protected Int2D location;
    protected List<Parcel> pToBeSent = new LinkedList<Parcel>();
    protected List<Parcel> pArrived = new LinkedList<Parcel>();
    protected Map map;
    // number of car caller of a station can have;
    protected int carCallerSema = 1;
    protected boolean addingParcels = false;
    private int stationDisplaySize = 5;
    public Font nodeFont = new Font("Station", Font.BOLD | Font.ROMAN_BASELINE, stationDisplaySize - 1);
    private String name;
    // busy indicates how busy the station is, the number should between 100 and 0,
    // the bigger the number, the more busy it is
    private int busy = 99;

    public ExpressCenter(String name, int stationID, Int2D location, Map map) {
        this.name = name;
        this.stationID = stationID;
        this.location = location;
        this.map = map;
    }

    public List<Parcel> getpToBeSent() {
        return pToBeSent;
    }

    @Override
    public String toString() {
        return "Station: " + name;
    }

    public Boolean isStation(Int2D loc) {
        for (ExpressCenter s : map.allStations) {
            if (s.location.equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public ExpressCenter findStationByLoc(Int2D loc) {
        for (ExpressCenter s : map.allStations) {
            if (s.location.equals(loc)) {
                return s;
            }
        }
        return null;
    }

    public ExpressCenter findStationByID(int id) {
        for (ExpressCenter s : map.allStations) {
            if (s.stationID == id) {
                return s;
            }
        }
        return null;
    }

    //
    public LinkedList<ExpressCenter> findNeighbours() {
        LinkedList<ExpressCenter> result = new LinkedList<ExpressCenter>();
        for (ExpressCenter s : map.allStations) {
            if (!s.equals(this)) {
                if (map.tramLines.get(0).findTramLine(this, s) != null)
                    result.add(s);
            }
        }
        return result;
    }

    // find all reachable station
    public LinkedList<ExpressCenter> findAllReachableStations() {
        LinkedList<ExpressCenter> neighbours = this.findNeighbours();
        LinkedList<ExpressCenter> temp, result;

        int size = neighbours.size();
        result = (LinkedList<ExpressCenter>) neighbours.clone();
        int previous = 0;

        while (previous < size) {
            previous = size;
            for (ExpressCenter nb : neighbours) {
                temp = nb.findNeighbours();
                result.removeAll(temp);
                result.addAll(temp);
            }
            neighbours = (LinkedList<ExpressCenter>) result.clone();
            size = neighbours.size();
        }
        return result;
    }

    public boolean reachable(ExpressCenter b) {
        return this.findAllReachableStations().contains(b);
    }

    private void callCar() {
        ExpressCenter expressCenter = findStationWithFreeCar();
        if (expressCenter != null) {
            carCallerSema--;
            expressCenter.pToBeSent.add(new CarCaller(expressCenter, this, map));
            System.out.println("Log: " + this + " has put a CarCaller in" + expressCenter + "...");
        }
    }

    private ExpressCenter findStationWithFreeCar() {
        for (ExpressCenter s : map.allStations) {
            if (s.carPark.size() > 0 && s.pToBeSent.size() == 0 && this.reachable(s)) {
                for (Car c : s.carPark) {
                    if (c.getCarrying().size() == 0) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void step(SimState state) {
        // if the car park is empty, has package to be sent, and the car caller is empty
        if (this.pToBeSent.size() > 0 && this.carPark.size() == 0 && carCallerSema > 0 && findNeighbours().size() > 0)
            callCar();

        if (pToBeSent.size() < MAX_PACKAGES && genParcelOrNot() && findNeighbours().size() > 0) {
            map.addParcel(this);
            map.parcelTotal++;
        }
    }

    private boolean genParcelOrNot() {
        Random random = new Random();
        // this number indicate how busy the station is
        int j = random.nextInt(Math.abs(100 - busy));

        // if i is smaller than busy then generalize a parcles
        if (1 > j)
            return true;
        else
            return false;
    }

    @Override
    public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        double diamx = info.draw.width * NetworkTest.DIAMETER / 1.5;
        double diamy = info.draw.height * NetworkTest.DIAMETER / 1.5;

        graphics.setColor(Color.blue);
        graphics.fillOval((int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2), (int) (diamx), (int) (diamy));
        graphics.setFont(nodeFont.deriveFont(nodeFont.getSize2D() * (float) info.draw.width));
        graphics.setColor(Color.black);
        graphics.drawString("S:" + name + " C:" + carPark.size() + " P:" + pToBeSent.size(), (int) (info.draw.x - diamx / 2), (int) (info.draw.y - diamy / 2));
    }

}
