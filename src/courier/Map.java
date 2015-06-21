package courier;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Int2D;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Map extends SimState {

    private static final int gridWidth = 2800;
    private static final int gridHeight = 1800;
    private static final Int2D centre = new Int2D(gridWidth / 2, gridHeight / 2);

    public static String testType;
    public static boolean detailsOn = false;
    public static boolean readTestSetting = false;
    public int modePicker = 1;
    public SparseGrid2D mapGrid = new SparseGrid2D(gridWidth, gridHeight);
    public double profit = 0;
    //    public double retainedProfit = 0;
    public double profitMargin = 2.45;
    protected int autoGenParcelByStationsMax = 0;
    protected boolean testModeOn = true;
    protected boolean autoGenParcelsModeTermination = testModeOn;
    protected int parcelTotal = 0;
    protected int parcelArrivedTotal = 0;
    protected LinkedList<ExpressCentre> allStations = new LinkedList<ExpressCentre>();
    protected LinkedList<ExpressCentre> expressCentres = new LinkedList<ExpressCentre>();
    protected LinkedList<Garage> garages = new LinkedList<Garage>();
    protected LinkedList<RefugeeIsland> refugee_islands = new LinkedList<RefugeeIsland>();
    protected LinkedList<Double> tramLineHasInit = new LinkedList<Double>();
    protected LinkedList<Parcel> parcelArrive = new LinkedList<Parcel>();
    protected LinkedList<TramLine_BASIC> tramLines = new LinkedList<TramLine_BASIC>();
    protected LinkedList<Car_BASIC> cars = new LinkedList<Car_BASIC>();
    protected Network tramLineNet = new Network(false);
    protected long parcelTimeSpendingTotalSincePickUp = 0;
    protected long parcelTimeSpendingTotalSinceGen = 0;
    protected int parcelTotalCopy;
    private SimpleDateFormat format = new SimpleDateFormat("dd-hh-mm-ss");
    protected String initTime = format.format(new Date()).toString();
    protected long startTime;
    protected int serialCarCallerID = 1;
    protected int serialParcelID = 1;
    protected int serialStationID = 1;
    protected int serialTramLineID = 1;
    private int serialCarID = 1;
    protected long pathImprovement = 0;
    private int carMax = 9999;
    protected long longestDeliverTimeSincePickUp;
    protected long longestDeliverTimeSinceGenerate;
    protected GlobalExpressCenter gec = new GlobalExpressCenter("Global Express Center", new Int2D(0, 0), this);

    /****************************
     * test parameters
     ****************************/
    public static int initNumOfParcelsInExpressCentre = 1500;
    // Simulation mode, basic mod means set a destination without changing,
    // AVOID_TRAFFIC_JAM mode will recalculate the path if it come to red light
    public static int distanceToCentre = 300;

    //     public static SIMULATION_MODE mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
    public static SIMULATION_MODE mode = SIMULATION_MODE.BASIC;

    // test one 6-7-8-9-10 def 10
    protected static int expressCenterBusyLevel = 8;

    public static boolean optimizedPickUp = true;
//    public static boolean optimizedPickUp = false;

    // test two 20-40-60-80-100-120 def 80
    protected static int carMaxSpace = 80;

    //  test three 50-150-250-350-450-550 def 150
    protected static int initNumOfCarsInGarage = 150;

    // test four 5-6-7-8-9-10
    protected static int congestionLevel = 8;

//    public static boolean refugeIslandOn = false;
    //    public static boolean refugeIslandOn = true;

    // test five num of carpark in island 1-2-3-4-5-6-7
    protected static int refugeCarParkNum = 2;

    public static int numOfRefugeIsland = 0;


    /****************************
     * test parameters
     ****************************/


    public Map(long seed) {
        super(seed);
    }

    public static void main(String[] args) {
//        args = new String[2];
//        args[0] = "-seed";
//        args[1] = String.valueOf(1168243636);
//
        doLoop(Map.class, args);
//        Map map = new Map(4);
//        map.start();
//        while (true) {
//            map.schedule.step(map);
//            if (map.parcelTotal == 0) {
//                new OutPutResult(map).writeResult();
//            }
//        }
//        System.out.println(map.schedule.getSteps());
//        System.exit(0);
    }

    // **********************************Functions**For**Display**BEGIN**************************************** //
    // **********************************Functions**For**Display**BEGIN**************************************** //


    public SIMULATION_MODE getMode() {
        return mode;
    }

    public int getModePicker_1_BASIC_2_AVOID() {
        return modePicker;
    }

    public void setModePicker_1_BASIC_2_AVOID(int val) {
        if (val <= 1) {
            modePicker = 1;
            mode = (SIMULATION_MODE.BASIC);
        } else {
            modePicker = 2;
            mode = (SIMULATION_MODE.AVOID_TRAFFIC_JAM);
        }

    }


    public int getNumOfParcelsInEachStations() {
        return initNumOfParcelsInExpressCentre;
    }

    public int getNumOfRefugeeIsland_0_1() {
        return numOfRefugeIsland;
    }

    public void setNumOfRefugeeIsland_0_1(int val) {
        if (val >= 0 && val <= 1) {
            numOfRefugeIsland = val;
        }
    }

    public void setRefugeeCarParkNum_2_7(int val) {
        if (val >= 2 && val <= 7 && numOfRefugeIsland > 0) {
            refugeCarParkNum = val;
        }
    }

    public int getRefugeeCarParkNum_2_7() {
        if (numOfRefugeIsland > 0)
            return refugeCarParkNum;
        else
            return 0;
    }

    public void setNumOfParcelsInEachStations(int val) {
        if (val > 0)
            initNumOfParcelsInExpressCentre = val;
    }

    public int getInitNumOfCarsInStation() {
        return initNumOfCarsInGarage;
    }

    public void setInitNumOfCarsInStation(int val) {
        if (val > 0 && val < carMax)
            initNumOfCarsInGarage = val;
    }

    public int getMapSize_300_2000() {
        return distanceToCentre;
    }


    public void setMapSize_300_2000(int val) {
        if (val >= 300)
            distanceToCentre = val;
    }

    public int getCongestionLevel_1_10() {
        return congestionLevel;
    }

    public void setCongestionLevel_1_10(int val) {
        if (val >= 1 && val <= 10) {
            TramLine_BASIC.maximumCarLeavingBeforeRedLight = 11 - val;
        }
    }


//    public Object domCongestionLevel_1_10() {
//        return new sim.util.Interval(0.0, 10.0);
//    }


    public int getExpressCentreBusyLevel_1_10() {
        return expressCenterBusyLevel;
    }


    public void setExpressCentreBusyLevel_1_10(int val) {
        if (val > 0 && val <= 10)
            expressCenterBusyLevel = val;
    }


//    public double getModePicker_BASIC_AVOID() {
//        return modePicker;
//    }
//
//    public void setModePicker_BASIC_AVOID(double val) {
//        if (val >=0 && val <= 1) {
//            modePicker = 0;
//            mode = (SIMULATION_MODE.BASIC);
//        } else if (val>1 && val <= 2){
//            modePicker = 1;
//            mode = (SIMULATION_MODE.AVOID_TRAFFIC_JAM);
//        }
//
//    }

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


//        addExpressCentre("A", new Int2D(gridWidth/2-10, gridHeight/2-20));
//        addExpressCentre("B", new Int2D(gridWidth/2-200,gridHeight/2+200));
//        addExpressCentre("C", new Int2D(gridWidth/2+50,gridHeight/2+60));
//        addExpressCentre("D", new Int2D(gridWidth/2-70, gridHeight/2-80));
//        addExpressCentre("E", new Int2D(gridWidth/2-90,gridHeight/2+90));
//        addExpressCentre("F", new Int2D(gridWidth/2+60,gridHeight/2+20));

        // init Express Centres
        DataReader reader = new DataReader(this);
        try {
            reader.initExpressCenter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        allStations.addAll(expressCentres);


//        addTramLine("line",expressCentres.get(0), expressCentres.get(1));
//        addTramLine("line",expressCentres.get(1), expressCentres.get(2));
//        addTramLine("line",expressCentres.get(2), expressCentres.get(3));
//        addTramLine("line",expressCentres.get(3), expressCentres.get(0));
//        addTramLine("line",expressCentres.get(1), expressCentres.get(4));
//        addTramLine("line",expressCentres.get(3), expressCentres.get(4));
//
//        addTramLine("line",expressCentres.get(2), expressCentres.get(0));

        // init tramlines
        try {
            reader.initTramLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // init garage
        addGarage();
        allStations.addAll(garages);

        scheduleExpressCenters();

        initCars();

        if (testModeOn) {
            autoGenParcelByStationsMax = initNumOfParcelsInExpressCentre * expressCentres.size();
        } else {
            autoGenParcelByStationsMax = 999999999;
        }

        initTramLineNet();

        parcelTotalCopy = autoGenParcelByStationsMax;
        startTime = this.schedule.getSteps();
        schedule.scheduleRepeating(gec);
        System.out.println(modePicker);
        if (modePicker < 0.5) {
            mode = SIMULATION_MODE.BASIC;
        } else {
            mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
        }
    }

//    private void initExpressCenter() {
//        InitExpressCentre i = new InitExpressCentre(this);
//        i.initExpressCentre();
////        addExpressCentre("A", new Int2D(gridWidth/2-10, gridHeight/2-20));
////        addExpressCentre("B", new Int2D(gridWidth/2-200,gridHeight/2+200));
////        addExpressCentre("C", new Int2D(gridWidth/2+50,gridHeight/2+60));
////        addExpressCentre("D", new Int2D(gridWidth/2-70, gridHeight/2-80));
////        addExpressCentre("E", new Int2D(gridWidth/2-90,gridHeight/2+90));
////        addExpressCentre("F", new Int2D(gridWidth/2+60,gridHeight/2+20));
//    }


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
                ExpressCentre expressCentre = new ExpressCentre(name, loc, this);
                expressCentres.add(expressCentre);
            }
        }
    }

    private void scheduleExpressCenters() {

        LinkedList<ExpressCentre> expressCentresCopy = (LinkedList<ExpressCentre>) expressCentres.clone();

        for (ExpressCentre ec : expressCentresCopy) {
            // if reachableByGarage is null
            if (ec.reachableByGarage == null) {
                ec.reachableByGarage = ec.reachableByGarage();
                if (!ec.reachableByGarage) {
                    expressCentres.remove(ec);
                    continue;
                }
            }
            schedule.scheduleRepeating(ec);
            mapGrid.setObjectLocation(ec, ec.location);
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
            if (tempDistance < distance && ec.reachable(expressCentres.getLast())) {
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

            else
                car = null;

            cars.add(car);
            serialCarID++;
            schedule.scheduleRepeating(car);
            mapGrid.setObjectLocation(car, loc);
        }
    }

//    private void initTramLines() {
//        InitTramLine init = new InitTramLine(this);
//        init.initTramLine();
////        addTramLine("line",expressCentres.get(0), expressCentres.get(1));
////        addTramLine("line",expressCentres.get(1), expressCentres.get(2));
////        addTramLine("line",expressCentres.get(2), expressCentres.get(3));
////        addTramLine("line",expressCentres.get(3), expressCentres.get(0));
////        addTramLine("line",expressCentres.get(1), expressCentres.get(4));
////        addTramLine("line",expressCentres.get(3), expressCentres.get(4));
//
////        addTramLine("line",expressCentres.get(2), expressCentres.get(0));
//
//    }

    // an id for a pair of express center
    private double genPairID(int a, int b) {
        return a * Math.PI + b / Math.PI;
    }

    public void addTramLine(String line, String a, String b) {

        ExpressCentre ec1 = expressCentres.getFirst().findStationByName(a);
        ExpressCentre ec2 = expressCentres.getFirst().findStationByName(b);
        ExpressCentre temp;
        if (ec1 == null || ec2 == null) {
            return;
        }
        if (ec1.stationID > ec2.stationID) {
            temp = ec1;
            ec1 = ec2;
            ec2 = temp;
        }
        double uniquePairID = genPairID(ec1.stationID, ec2.stationID);

        if (ec1 != null && ec2 != null) {
            if (!tramLineHasInit.contains(uniquePairID)) {
                tramLineHasInit.add(uniquePairID);
                if (numOfRefugeIsland > 0) {
                    addTramLinesWithIsland(line, ec1, ec2);
                } else {
                    ec1.neighbours.add(ec2);
                    ec2.neighbours.add(ec1);
                    TramLine_BASIC tl = new TramLine_BASIC(line, ec1, ec2, this);
                    tramLines.add(tl);
                }
                serialTramLineID++;
            }
        }
    }


    public void addTramLine(String line, ExpressCentre a, ExpressCentre b) {
        if (numOfRefugeIsland > 0 && !(a instanceof Garage) && !(b instanceof Garage)) {
            addTramLinesWithIsland(line, a, b);
        } else {
            a.neighbours.add(b);
            b.neighbours.add(a);
            TramLine_BASIC tl = new TramLine_BASIC(line, a, b, this);
            tramLines.add(tl);
        }
    }

    private void addTramLinesWithIsland(String line, ExpressCentre a, ExpressCentre b) {
        ExpressCentre start;
        start = a;

        int count = numOfRefugeIsland;
        while (count > 0) {
            Int2D loc = new Int2D(a.location.x + (b.location.x - a.location.x) * (this.numOfRefugeIsland - count + 1) / (this.numOfRefugeIsland + 1), a.location.y + (b.location.y - a.location.y) * (this.numOfRefugeIsland - count + 1) / (this.numOfRefugeIsland + 1));
            RefugeeIsland island = new RefugeeIsland("RI: " + serialStationID, loc, this);
            count--;

            refugee_islands.add(island);
            allStations.add(island);
            mapGrid.setObjectLocation(island, loc);
            start.neighbours.add(island);
            island.neighbours.add(start);
            TramLine_BASIC tl = new TramLine_BASIC(line, start, island, this);
            tramLines.add(tl);
            start = island;
        }
        start.neighbours.add(b);
        b.neighbours.add(start);
        TramLine_BASIC tl = new TramLine_BASIC(line, start, b, this);
        tramLines.add(tl);


    }


    private void initTramLineNet() {
        for (ExpressCentre s : allStations)
            tramLineNet.addNode(s);
        for (TramLine_BASIC tl : tramLines) {
            tramLineNet.addEdge(tl.a, tl.b, tl.tramLineID);
        }
    }

}