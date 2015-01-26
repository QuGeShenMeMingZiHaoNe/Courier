package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class Map extends SimState {
    public static final int initNumOfParcelsInStation = 200;
    public static final int initNumOfCarsInStation = 100;
    private static final int gridWidth = 2800;
    private static final int gridHeight = 1800;
    private static final Int2D centre = new Int2D(gridWidth / 2, gridHeight / 2);
    private static final int distanceToCentre = 300;
    // Simulation mode, basic mod means set a destination without changing,
    // AVOID_TRAFFIC_JAM mode will recalculate the path if it come to red light
    public final SIMULATION_MODE mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
    public final boolean testModeOne = true;
    public SparseGrid2D mapGrid = new SparseGrid2D(gridWidth, gridHeight);
    public double profit = 0;
    //    public double retainedProfit = 0;
    public double profitMargin = 2.45;
    protected int parcelTotal = 0;
    protected int serialCarCallerID = 1;
    protected LinkedList<ExpressCentre> allStations = new LinkedList<ExpressCentre>();
    protected LinkedList<ExpressCentre> expressCentres = new LinkedList<ExpressCentre>();
    protected LinkedList<Garage> garages = new LinkedList<Garage>();
    protected LinkedList<Parcel> parcels = new LinkedList<Parcel>();
    protected LinkedList<TramLine> tramLines = new LinkedList<TramLine>();
    protected LinkedList<Car> cars = new LinkedList<Car>();
    protected Network tramLineNet = new Network(false);
    private int serialStationID = 1;
    private int serialParcelID = 1;
    private int serialTramLineID = 1;
    private int serialCarID = 1;

    protected long firstParcelReleasedTime;
    protected long lastParcelArrivedTime;
    protected long parcelTimeSpendingTotal = 0;
    protected int parcelTotalCopy;

    protected String initTime ;


    public Map(long seed) {
        super(seed);
    }

    public static void main(String[] args) {
        doLoop(Map.class, args);
        System.exit(0);
    }

    public void start() {
        super.start();

        // init the output test file
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        initTime = sdf.format(System.currentTimeMillis());
//        File testOutput = new File("src/courier/"+ mode+initTime +".output");
//        testOutput.delete();

        // clear the buddies
        tramLineNet.clear();

        // init Express Centres
        initExpressCenter();
        allStations.addAll(expressCentres);

        // init garage
        addGarage();
        allStations.addAll(garages);

        // init tramlines
        initTramLines();

        if(testModeOne)
            initFixedLocParcels();

//        initRandomParcels();

        initTramLineNet();

        parcelTotalCopy = parcelTotal;
        firstParcelReleasedTime = this.schedule.getSteps();
    }

    private void initExpressCenter() {
        InitExpressCentre i = new InitExpressCentre(this);
        i.initExpressCentre();
//        addExpressCentre("A", new Int2D(10, 40));
//        addExpressCentre("B", new Int2D(40, 50));
//        addExpressCentre("C", new Int2D(50, 66));
//        addExpressCentre("D", new Int2D(90, 50));
//        addExpressCentre("E", new Int2D(40, 60));
//        addExpressCentre("F", new Int2D(99, 99));
    }


    public void addExpressCentre(String name, Int2D loc) {
        if (loc.distance(centre) <= distanceToCentre) {
            Boolean b = true;
            for (ExpressCentre e : expressCentres) {
                if (e.location.equals(loc)) {
                    b = false;
                    System.out.println(name + " and " + e.name + " has same location");
                }
            }
            if (b) {
                ExpressCentre expressCentre = new ExpressCentre(name, serialStationID, loc, this);
                expressCentres.add(expressCentre);
                schedule.scheduleRepeating(expressCentre);
                mapGrid.setObjectLocation(expressCentre, loc);
                serialStationID++;
            }
        }
    }


    // add garage with number of cars
    private void addGarage() {
        Int2D loc = new Int2D(90, 90);
        Garage g = new Garage("garage", serialStationID, loc, this);
        serialStationID++;
        mapGrid.setObjectLocation(g, loc);

        Car car;
        for (int i = 0; i < initNumOfCarsInStation; i++) {
            car = new Car(serialCarID, loc, this);
            cars.add(car);
            serialCarID++;
            schedule.scheduleRepeating(car);
            mapGrid.setObjectLocation(car, loc);
        }
        ExpressCentre closestEC = expressCentres.getFirst();
        double distance = 999999999;
        double tempDistance;
        for (ExpressCentre ec : expressCentres) {
            tempDistance = loc.distance(ec.location);
            if (tempDistance < distance) {
                distance = tempDistance;
                closestEC = ec;
            }
        }
        addTramLine("garage", g, closestEC);
        garages.add(g);
    }

    private void initTramLines() {
        InitTramLine init = new InitTramLine(this);
        init.initTramLine();
//        addTramLine("line",expressCentres.get(0), expressCentres.get(1));
//        addTramLine("line",expressCentres.get(1), expressCentres.get(2));
//        addTramLine("line",expressCentres.get(2), expressCentres.get(3));
//        addTramLine("line",expressCentres.get(3), expressCentres.get(0));
//        addTramLine("line",expressCentres.get(1), expressCentres.get(4));
//        addTramLine("line",expressCentres.get(3), expressCentres.get(4));
    }

    public void addTramLine(String line, String a, String b) {

        ExpressCentre ec1 = expressCentres.getFirst().findStationByName(a);
        ExpressCentre ec2 = expressCentres.getFirst().findStationByName(b);

        if (ec1 != null && ec2 != null) {
            if (tramLines.getFirst().findTramLineIndexByNB(ec1, ec2) < 0) {
                ec1.neighbours.add(ec2);
                ec2.neighbours.add(ec1);
                if (ec1 != null && ec2 != null) {
                    TramLine tl = new TramLine(line, ec1, ec2, serialTramLineID, this);
                    tramLines.add(tl);
                }
                serialTramLineID++;
            }
        }
    }

    public void addTramLine(String line, ExpressCentre a, ExpressCentre b) {
        a.neighbours.add(b);
        b.neighbours.add(a);
        tramLines.add(new TramLine(line, a, b, serialTramLineID, this));
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

    private void initRandomParcels() {
        for (ExpressCentre s : expressCentres) {
            // if the station is not isolated
            if (s.hasNeighbour()) {
                for (int i = 0; i < initNumOfParcelsInStation; i++) {
                    // add a parcel to current station;
                    addRandomParcel(s);
                    parcelTotal++;
                }
            }
        }
    }

    private void initFixedLocParcels() {
        for (ExpressCentre s : expressCentres) {
            // if the station is not isolated
            if (s.hasNeighbour()) {
                addFixedLocParcel(s);
            }
        }
    }



    // add parcels with fixed destination and number
    public void addFixedLocParcel(ExpressCentre currExpressCentre) {
        int i = expressCentres.indexOf(currExpressCentre);
        int j = 1;
        int next = (i+j)%(expressCentres.size());

        // add "initNumOfParcelsInStation" numbers of parcels
        for (int k = 0; k < initNumOfParcelsInStation; k++) {
            if (currExpressCentre.hasNeighbour()) {
                if (currExpressCentre.neighbours.containsAll(garages) && currExpressCentre.neighbours.size() == garages.size()) {
                    return;
                }
                do {
                    j++;
                    next = (i+j)%(expressCentres.size());
                }
                while (!(!expressCentres.get(next).equals(currExpressCentre) && currExpressCentre.reachable(expressCentres.get(next))));

                currExpressCentre.pToBeSent.add(new Parcel(serialParcelID, currExpressCentre, expressCentres.get(next), getNextInt(Car.maxSpace), this));
                serialParcelID++;
                parcelTotal++;
            }
        }
    }

    public void addRandomParcel(ExpressCentre currExpressCentre) {
        if (currExpressCentre.hasNeighbour()) {
            if (currExpressCentre.neighbours.containsAll(garages) && currExpressCentre.neighbours.size() == garages.size()) {
                return;
            }
            int next;
            do {
                next = random.nextInt(expressCentres.size());
            }
            while (!(!expressCentres.get(next).equals(currExpressCentre) && currExpressCentre.reachable(expressCentres.get(next))));

            currExpressCentre.pToBeSent.add(new Parcel(serialParcelID, currExpressCentre, expressCentres.get(next), getNextInt(Car.maxSpace), this));
            serialParcelID++;
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

    private void initTramLineNet() {
        for (ExpressCentre s : allStations)
            tramLineNet.addNode(s);
        for (TramLine tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramLineID);
        }
    }

}