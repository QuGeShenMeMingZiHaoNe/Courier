package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.util.LinkedList;

public class Map extends SimState {
    protected LinkedList<Station> stations = new LinkedList<Station>();
    protected LinkedList<Parcel> parcels = new LinkedList<Parcel>();
    protected LinkedList<TramLine> tramLines = new LinkedList<TramLine>();
    protected LinkedList<Car> cars = new LinkedList<Car>();
    protected Network tramLineNet = new Network(false);
    protected int parcelTotal = 0;
    protected int serialCarCallerID = 1;
    protected int initNumOfParcelsInStation = 3;
    private int serialStationID = 1;
    private int serialParcelID = 1;
    private int serialTramLineID = 1;
    private int serialCarID = 1;
    private int initNumOfCarsInStation = 1;
    private int gridWidth = 180;
    private int gridHeight = 180;
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
        initTramLines();
        initCars();
        initParcels();
        initTramLineNet();
    }

    private void initStations() {
        addStation("A", new Int2D(10, 40));
        addStation("B", new Int2D(40, 50));
        addStation("C", new Int2D(50, 66));
        addStation("D", new Int2D(90, 50));
        addStation("E", new Int2D(40, 60));
        addStation("F", new Int2D(99, 99));
    }

    private void addStation(String name, Int2D loc) {
        Station station = new Station(name, serialStationID, loc, this);
        stations.add(station);
        schedule.scheduleRepeating(station);
        mapGrid.setObjectLocation(station, loc);
        serialStationID++;
    }

    private void initTramLines() {
        addTramLine(stations.get(0), stations.get(1));
        addTramLine(stations.get(1), stations.get(2));
        addTramLine(stations.get(2), stations.get(3));
        addTramLine(stations.get(3), stations.get(0));
        addTramLine(stations.get(1), stations.get(4));
        addTramLine(stations.get(3), stations.get(4));
    }

    private void addTramLine(Station a, Station b) {
        tramLines.add(new TramLine(a, b, serialTramLineID, this));
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
        int next;
        for (Station s : stations) {
            // if the station is not isolated
            if (s.findNeighbours().size() > 0) {
                for (int i = 0; i < initNumOfParcelsInStation; i++) {
                    // add a parcel to current station;
                    addParcel(s);
                    parcelTotal++;
                }
            }
        }
    }

    // return a number beyond limit and greater than 0
    private int getNextInt(int limit) {
        int result;
        do {
            result = random.nextInt(limit);
        } while (result == 0);
        return result;
    }

    public void addParcel(Station currStation) {
        int next;
        do {
            next = random.nextInt(stations.size());
        } while (!(stations.get(next).stationID != currStation.stationID && currStation.reachable(stations.get(next))));

        currStation.pToBeSent.add(new Parcel(serialParcelID, currStation, stations.get(next), getNextInt(cars.getFirst().maxSpace), this));
        serialParcelID++;
    }

    private void initTramLineNet() {
        for (Station s : stations)
            tramLineNet.addNode(s);
        for (TramLine tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramLineID);
        }
    }

}