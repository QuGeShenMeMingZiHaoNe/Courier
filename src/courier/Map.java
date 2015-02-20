package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Map extends SimState {

    private static final int gridWidth = 2800;
    private static final int gridHeight = 1800;
    private static final Int2D centre = new Int2D(gridWidth / 2, gridHeight / 2);

    public static int initNumOfParcelsInExpressCentre = 200;
    public static int initNumOfCarsInGarage = 100;
    // Simulation mode, basic mod means set a destination without changing,
    // AVOID_TRAFFIC_JAM mode will recalculate the path if it come to red light
    public static int distanceToCentre = 300;
    //    public static SIMULATION_MODE mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
    public static SIMULATION_MODE mode = SIMULATION_MODE.BASIC;
    public static boolean testModeOn = true;
    public static boolean detailsOn = false;
    public static boolean readTestSetting = false;
    public double modePicker = 0;
    public SparseGrid2D mapGrid = new SparseGrid2D(gridWidth, gridHeight);
    public double profit = 0;
    //    public double retainedProfit = 0;
    public double profitMargin = 2.45;
    protected int autoGenParcelByStationsMax;
    protected boolean autoGenParcelsModeTermination = testModeOn;
    protected int parcelTotal = 0;
    protected int serialCarCallerID = 1;
    protected LinkedList<ExpressCentre> allStations = new LinkedList<ExpressCentre>();
    protected LinkedList<ExpressCentre> expressCentres = new LinkedList<ExpressCentre>();
    protected LinkedList<Garage> garages = new LinkedList<Garage>();
    protected LinkedList<Parcel> parcels = new LinkedList<Parcel>();
    protected LinkedList<TramLine_BASIC> tramLines = new LinkedList<TramLine_BASIC>();
    protected LinkedList<Car_BASIC> cars = new LinkedList<Car_BASIC>();
    protected LinkedList<Parcel> callCarToPickUpParcels = new LinkedList<Parcel>();
    protected Network tramLineNet = new Network(false);
    protected long parcelTimeSpendingTotal = 0;
    protected int parcelTotalCopy;
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd-hh-mm");
    protected String initTime = format.format(new Date()).toString();
    protected long startTime;
    protected int serialParcelID = 1;
    private int serialStationID = 1;
    private int serialTramLineID = 1;
    private int serialCarID = 1;
    protected long pathImprovement = 0;


    public Map(long seed) {
        super(seed);
    }

    public static void main(String[] args) {
        doLoop(Map.class, args);
        System.exit(0);
    }

    public SIMULATION_MODE getMode() {
        return mode;
    }

    // **********************************Functions**For**Display**BEGIN**************************************** //
    // **********************************Functions**For**Display**BEGIN**************************************** //

    public int getNumOfParcelsInEachStations() {
        return initNumOfParcelsInExpressCentre;
    }

    public void setNumOfParcelsInEachStations(int val) {
        if (val > 0)
            initNumOfParcelsInExpressCentre = val;
    }

    public int getInitNumOfCarsInStation() {
        return initNumOfCarsInGarage;
    }

    public void setInitNumOfCarsInStation(int val) {
        if (val > 0)
            initNumOfCarsInGarage = val;
    }

    public int getMapSize_300_2000() {
        return distanceToCentre;
    }


    public void setMapSize_300_2000(int val) {
        if (val > 300)
            distanceToCentre = val;
    }

    public int getCongestionLevel_1_10() {
        return 11 - TramLine_BASIC.maximumCarLeavingBeforeRedLight;
    }

    public void setCongestionLevel_1_10(int val) {
        if (val >= 1 && val <= 10) {
            TramLine_BASIC.maximumCarLeavingBeforeRedLight = 11 - val;
        }
    }

//    public Object domCongestionLevel_1_10() {
//        return new sim.util.Interval(0.0, 10.0);
//    }


    public int getExpressCentreBusyLevel_1_999() {
        return ExpressCentre.busy;
    }


    public void setExpressCentreBusyLevel_1_999(int val) {
        if (val > 0 && val < 999)
            ExpressCentre.busy = val;
    }


    public double getModePicker_BASIC_AVOID() {
        return modePicker;
    }

    public void setModePicker_BASIC_AVOID(double val) {
        if (val >= 0 && val < 1) {
            modePicker = 0;
            mode = (SIMULATION_MODE.BASIC);
        } else {
            modePicker = 2;
            mode = (SIMULATION_MODE.AVOID_TRAFFIC_JAM);
        }
    }

    public Object domModePicker_BASIC_AVOID() {
        return new sim.util.Interval(0.0, 2.0);
    }

    public boolean getTestModeOn() {
        return testModeOn;
    }

    public void setTestModeOn(boolean on) {
        testModeOn = on;
    }

    public boolean getDetailsOn() {
        return detailsOn;
    }

    public void setDetailsOn(boolean on) {
        detailsOn = on;
    }

    // **********************************Functions**For**Display**END*************************************** //
    // **********************************Functions**For**Display**END*************************************** //


    public void start() {
        super.start();

        // clear the buddies
        tramLineNet.clear();

        // init Express Centres
        initExpressCenter();
        allStations.addAll(expressCentres);

        // init garage
        addGarage();
        allStations.addAll(garages);

        initCars();

        // init tramlines
        initTramLines();

        if (testModeOn) {
            autoGenParcelByStationsMax = initNumOfParcelsInExpressCentre * expressCentres.size();
        } else {
            autoGenParcelByStationsMax = 999999999;
        }

        initTramLineNet();

        parcelTotalCopy = autoGenParcelByStationsMax;
        startTime = this.schedule.getSteps();
    }

    private void initExpressCenter() {
        InitExpressCentre i = new InitExpressCentre(this);
        i.initExpressCentre();
//        addExpressCentre("A", new Int2D(gridWidth/2-10, gridHeight/2-20));
//        addExpressCentre("B", new Int2D(gridWidth/2-200,gridHeight/2+200));
//        addExpressCentre("C", new Int2D(gridWidth/2+50,gridHeight/2+60));
//        addExpressCentre("D", new Int2D(gridWidth/2-70, gridHeight/2-80));
//        addExpressCentre("E", new Int2D(gridWidth/2-90,gridHeight/2+90));
//        addExpressCentre("F", new Int2D(gridWidth/2+60,gridHeight/2+20));
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
        Int2D loc = new Int2D(80, 80);
        Garage g = new Garage("garage", serialStationID, loc, this);
        serialStationID++;
        mapGrid.setObjectLocation(g, loc);

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

    private void initCars() {
        Car_BASIC car;
        for (int i = 0; i < initNumOfCarsInGarage; i++) {
            Int2D loc = garages.get(random.nextInt(garages.size())).location;

            if (mode.equals(SIMULATION_MODE.BASIC))
                car = new Car_BASIC(serialCarID, loc, this);

            else if (mode.equals(SIMULATION_MODE.AVOID_TRAFFIC_JAM))
                car = new Car_AVOID(serialCarID, loc, this);

            else {
                car = null;
            }

            cars.add(car);
            serialCarID++;
            schedule.scheduleRepeating(car);
            mapGrid.setObjectLocation(car, loc);
        }
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

//        addTramLine("line",expressCentres.get(2), expressCentres.get(0));

    }

    public void addTramLine(String line, String a, String b) {

        ExpressCentre ec1 = expressCentres.getFirst().findStationByName(a);
        ExpressCentre ec2 = expressCentres.getFirst().findStationByName(b);

        if (ec1 != null && ec2 != null) {
            if (tramLines.getFirst().findTramLineIndexByNB(ec1, ec2) < 0) {
                ec1.neighbours.add(ec2);
                ec2.neighbours.add(ec1);
                if (ec1 != null && ec2 != null) {
                    TramLine_BASIC tl = new TramLine_BASIC(line, ec1, ec2, serialTramLineID, this);
                    tramLines.add(tl);
                }
                serialTramLineID++;
            }
        }
    }

    public void addTramLine(String line, ExpressCentre a, ExpressCentre b) {
        a.neighbours.add(b);
        b.neighbours.add(a);
        TramLine_BASIC tl = new TramLine_BASIC(line, a, b, serialTramLineID, this);
        tramLines.add(tl);
        serialTramLineID++;
    }


    private void initRandomParcels() {
        for (ExpressCentre s : expressCentres) {
            // if the station is not isolated
            if (s.reachableByGarage()) {
                for (int i = 0; i < initNumOfParcelsInExpressCentre; i++) {
                    // add a parcel to current station;
                    new Parcel(this).addRandomParcel(s);
                }
            }
        }
    }

    private void initFixedLocParcels() {
        for (ExpressCentre s : expressCentres) {
            // if the station is not isolated
            if (s.reachableByGarage()) {
                new Parcel(this).addFixedLocParcel(s);
            }
        }
    }

    private void initTramLineNet() {
        for (ExpressCentre s : allStations)
            tramLineNet.addNode(s);
        for (TramLine_BASIC tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramLineID);
        }
    }

}