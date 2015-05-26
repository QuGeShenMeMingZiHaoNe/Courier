package courier;

import sim.engine.SimState;

public class Test {
    private static  int s1 = 1168243636;
    private static int job = 0;


    public Test(){

    }
    public static void main(String[] args) {
        Map.testType = "BusyLVL";
        testBusyLVL();

        Map.testType = "OPickUp";
        testOPickUpWithBUsyLVL();

        Map.testType = "CarLoad";
        testCarLoad();

        Map.testType = "NumCar";
        testNumCar();

        Map.testType = "TestConLVL";
        testConLVL();

        Map.testType = "IslandCarPark";
        testIslandCarPark();

        System.exit(0);

    }

    public static void testBusyLVL() {
        {
            // Basic
            for (int lvl = 6; lvl <= 10; lvl++) {
                Map.expressCenterBusyLevel = lvl;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(lvl);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }

            // Avoid
            Map.mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
            for (int lvl = 6; lvl <= 10; lvl++) {
                Map.expressCenterBusyLevel = lvl;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(lvl);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }
        }
    }

    public static void testOPickUpWithBUsyLVL(){
        Map.smartLoadingOn = true;
        testBusyLVL();
    }

    public static void testCarLoad(){
        {
            // Basic
            for (int carLoad = 20; carLoad <= 120; carLoad+=20) {
                Map.carMaxSpace = carLoad;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(carLoad);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }

            // Avoid
            Map.mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
            for (int carLoad = 20; carLoad <= 120; carLoad+=20) {
                Map.carMaxSpace = carLoad;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(carLoad);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }
        }
    }

    public static void testNumCar(){
        {
            // Basic
            for (int numCar = 30; numCar <= 280; numCar+=50) {
                Map.initNumOfCarsInGarage = numCar;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(numCar);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }

            // Avoid
            Map.mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
            for (int numCar = 30; numCar <= 280; numCar+=50) {
                Map.initNumOfCarsInGarage = numCar;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(numCar);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }
        }
    }

    public static void testConLVL(){
            // Basic
            for (int conLVL = 5; conLVL <= 10; conLVL++) {
                Map.congestionLevel = conLVL;
                SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
                state.setJob(conLVL);
                state.start();
                do
                    if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
                state.finish();
            }

            // Avoid
            Map.mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
        for (int conLVL = 5; conLVL <= 10; conLVL++) {
            Map.congestionLevel = conLVL;
            SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
            state.setJob(conLVL);
            state.start();
            do
                if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
            state.finish();
        }
    }

    public static void testIslandCarPark(){
        // Basic
        Map.refugeeIslandOn = true;
        for (int carPark = 1; carPark <= 6; carPark++) {
            Map.refugeeCarParkNum = carPark;
            SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
            state.setJob(carPark);
            state.start();
            do
                if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
            state.finish();
        }

        // Avoid
        Map.mode = SIMULATION_MODE.AVOID_TRAFFIC_JAM;
        for (int carPark = 1; carPark <= 6; carPark++) {
            Map.refugeeCarParkNum = carPark;
            SimState state = new Map(s1); // MyModel is our SimState subclass state.nameThread();
            state.setJob(carPark);
            state.start();
            do
                if (!state.schedule.step(state)) break; while (state.schedule.getSteps() < 99999999);
            state.finish();
        }
    }



}
