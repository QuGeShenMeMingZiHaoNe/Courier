package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.util.LinkedList;

public class Map extends SimState {
    protected LinkedList<ExpressCentre> allStations = new LinkedList<ExpressCentre>();
    protected LinkedList<ExpressCentre> expressCentres = new LinkedList<ExpressCentre>();
    protected LinkedList<Garage> garages = new LinkedList<Garage>();

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
    private int initNumOfCarsInStation = 1000;
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

        initExpressCenter();
        addGarage();
        initTramLines();
//        initCars();
        initParcels();
        initTramLineNet();
        allStations.addAll(garages);
        allStations.addAll(expressCentres);
    }

    private void initExpressCenter() {
        addExpressCentre("A", new Int2D(10, 40));
        addExpressCentre("B", new Int2D(40, 50));
        addExpressCentre("C", new Int2D(50, 66));
        addExpressCentre("D", new Int2D(90, 50));
        addExpressCentre("E", new Int2D(40, 60));
        addExpressCentre("F", new Int2D(99, 99));
    }



    private void addExpressCentre(String name, Int2D loc) {
        ExpressCentre expressCentre = new ExpressCentre(name, serialStationID, loc, this);
        expressCentres.add(expressCentre);
        schedule.scheduleRepeating(expressCentre);
        mapGrid.setObjectLocation(expressCentre, loc);
        serialStationID++;
    }


    // add garage with number of cars
    private void addGarage() {
        Int2D loc = new Int2D(90, 90);
        Garage g = new Garage("garage", 99, loc, this);
        mapGrid.setObjectLocation(g, loc);

        Car car;
        for (int i = 0; i < initNumOfCarsInStation; i++) {
            car = new Car(serialCarID, loc, this);
            cars.add(car);
            serialCarID++;
            schedule.scheduleRepeating(car);
            mapGrid.setObjectLocation(car, loc);
        }
        addTramLine(g, expressCentres.get(1));
        garages.add(g);
    }

    private void initTramLines() {
        addTramLine(expressCentres.get(0), expressCentres.get(1));
        addTramLine(expressCentres.get(1), expressCentres.get(2));
        addTramLine(expressCentres.get(2), expressCentres.get(3));
        addTramLine(expressCentres.get(3), expressCentres.get(0));
        addTramLine(expressCentres.get(1), expressCentres.get(4));
        addTramLine(expressCentres.get(3), expressCentres.get(4));
    }

    private void addTramLine(ExpressCentre a, ExpressCentre b) {
        tramLines.add(new TramLine(a, b, serialTramLineID, this));
        serialTramLineID++;
    }

    private void initCars() {
        Car car;
        for (ExpressCentre s : allStations) {
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
        for (ExpressCentre s : expressCentres) {
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

    public void addParcel(ExpressCentre currExpressCentre) {
        int next;
        do {
            next = random.nextInt(expressCentres.size());
        }
        while (!(expressCentres.get(next).stationID != currExpressCentre.stationID && currExpressCentre.reachable(expressCentres.get(next))));

        currExpressCentre.pToBeSent.add(new Parcel(serialParcelID, currExpressCentre, expressCentres.get(next), getNextInt(Car.maxSpace), this));
        serialParcelID++;
    }

    private void initTramLineNet() {
        for (ExpressCentre s : allStations)
            tramLineNet.addNode(s);
        for (TramLine tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramLineID);
        }
    }

}