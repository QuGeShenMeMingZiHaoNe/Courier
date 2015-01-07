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
    public LinkedList<TramLine> tramLines = new LinkedList<TramLine>();
    public LinkedList<Car> cars = new LinkedList<Car>();
    public Network tramLineNet = new Network(false);
    public int serialTrafficLightID = 1;
    private int serialStationID = 1;
    private int serialParcelID = 1;
    private int serialTramLineID = 1;
    private int serialCarID = 1;
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
        tramLineNet.clear();

        initStations();
        initTramlines();
        initCars();
        initParcels();
        initTramLineNet();
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

        station = new Station("C", serialStationID, new Int2D(55, 70), this);
        stations.add(station);
        mapGrid.setObjectLocation(station, new Int2D(55, 70));
        serialStationID++;
    }

    private void initTramlines() {
        tramLines.add(new TramLine(stations.get(0), stations.get(1), serialTramLineID, this));
        serialTramLineID++;
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
        boolean isolated = true;
        for (Station s : stations) {

            // to check if a station is isolated
            for (Station s2 : stations) {
                if (tramLines.get(0).findTramLine(s, s2) != null) {
                    isolated = false;
                    break;
                }
            }

            if (!isolated) {
                for (int i = 0; i < initNumOfParcelsInStation; i++) {
                    do {
                        next = random.nextInt(stations.size());
//                    System.out.println(next + " " +(stations.get(next).stationID != s.stationID) + " "+(s.reachable(stations.get(next))));
                    } while (!(stations.get(next).stationID != s.stationID && s.reachable(stations.get(next))));

                    p = new Parcel(serialParcelID, stations.get(next), smallPackageSize, this);
                    serialParcelID++;
                    s.pToBeSent.add(p);
                }
                isolated = true;
            }
        }
    }

    private void initTramLineNet() {
        for (Station s : stations)
            tramLineNet.addNode(s);
        for (TramLine tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramlineID);
        }
    }

}
