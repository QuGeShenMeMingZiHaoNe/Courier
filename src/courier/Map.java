package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.util.LinkedList;

/**
 * Created by daniel on 15/1/4.
 */
public class Map extends SimState {
    public LinkedList<Station> stations = new LinkedList<Station>();
    public LinkedList<Parcel> parcels = new LinkedList<Parcel>();
    public LinkedList<Tramline> tramlines = new LinkedList<Tramline>();
    public LinkedList<Car> cars = new LinkedList<Car>();
    public Network tramlineNet = new Network(false);
    private int serialStationID = 1;
    private int serialParcelID = 1;
    private int serialTramlineID = 1;
    private int serialCarID = 1;
    public int serialTrafficLightID = 1;
    private int initNumOfCarsInStation = 5;
    private int initNumOfParcelsInStation = 1000;
    private int smallPackageSize = 1;
    private int mediumPackageSize = 3;
    private int largePackageSize = 8;
    private int gridWidth = 100;
    private int gridHeight = 100;
    public SparseGrid2D mapGrid = new SparseGrid2D(gridWidth, gridHeight);

    public Map(long seed) {
        super(seed);
    }

    public static void main(String[] args) {
        doLoop(Map.class, args);
        System.exit(0);
    }

    public void start() {
        super.start();

        // clear the buddies
        tramlineNet.clear();

        initStations();
        initTramlines();
        initCars();
        initParcels();
        initeTramlineNet();
    }

    private void initStations() {
        Station station = new Station("A", serialStationID, new Int2D(10, 10), this);
        stations.add(station);
        mapGrid.setObjectLocation(station, new Int2D(10, 10));
        serialStationID++;

        station = new Station("B", serialStationID, new Int2D(15, 70), this);
        stations.add(station);
        mapGrid.setObjectLocation(station, new Int2D(15, 70));
        serialStationID++;
    }

    private void initTramlines() {
        tramlines.add(new Tramline(stations.get(0), stations.get(1), serialTramlineID, this));
        serialTramlineID++;
    }

    private void initCars() {
        Car car;
        for (Station s : stations) {
            for (int i = 0; i < initNumOfCarsInStation; i++) {
                car = new Car(serialCarID, s.location, this);
                cars.add(car);
                serialCarID++;
                schedule.scheduleRepeating(car);
                mapGrid.setObjectLocation(car, s.location);
            }
        }
    }

    private void initParcels() {
        Parcel p;
        int next;
        for (Station s : stations) {
            for (int i = 0; i < initNumOfParcelsInStation; i++) {
                do {
                    next = random.nextInt(stations.size());
                } while (stations.get(next).stationID == s.stationID);

                p = new Parcel(serialParcelID, stations.get(next), smallPackageSize, this);
                serialParcelID++;
                s.pToBeSent.add(p);
            }
        }
    }

    private void initeTramlineNet() {
        for (Station s : stations)
            tramlineNet.addNode(s);
        for (Tramline tl : tramlines) {
            tramlineNet.addEdge(tl.a, tl.b, tl.tramlineID);
        }
    }

}
