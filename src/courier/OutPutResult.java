package courier;

import java.io.*;
import java.text.SimpleDateFormat;

public class OutPutResult {
    Map map;

    OutPutResult(Map map) {
        this.map = map;
    }

    public void writeResult() {
        long timeSpendingAverageSincePickUp = map.parcelTimeSpendingTotalSincePickUp / map.parcelTotalCopy;
        long timeSpendingAverageSinceGen = map.parcelTimeSpendingTotalSinceGen / map.parcelTotalCopy;
        outputFile("***********************************************************************************************************");
        outputFile("\nMode: " + map.mode);
        outputFile("\nOptimizedPickUp: "+ map.optimizedPickUp);
        outputFile("\nRefugee Island On: " + (map.numOfRefugeIsland>0));
        if(map.numOfRefugeIsland>0){
            outputFile("\nRefugee Island carPark available : " + map.refugeCarParkNum);
            outputFile("\nNumber Of Refugee Between Two Stations : " + map.numOfRefugeIsland);
        }
        outputFile("\nRandom number seed: " + map.seed());
        outputFile("\nNumber Of ExpressCenters: " + map.expressCentres.size());
        outputFile("\nNumber Of Parcels: " + map.parcelTotalCopy);
        outputFile("\nNumber Of Cars: " + map.getInitNumOfCarsInStation());
        outputFile("\nMaximum Car carrying weight: " + map.carMaxSpace);
        outputFile("\nMap size: " + map.getMapSize_300_2000());
        outputFile("\nThe degree of busyLevel in Express Centre: " + map.expressCentres.getFirst().busyLevel);
        outputFile("\nCongestion Level: " + map.getCongestionLevel_1_10() + "\n");
        outputFile("***********************************************************************************************************");
        outputFile("\nSum Of All Parcels Delivering Time: " + (map.parcelTimeSpendingTotalSincePickUp));
        outputFile("\nAverage Parcel Delivering Time Since Pick Up: " + (timeSpendingAverageSincePickUp));
        outputAvgSincePickUp(timeSpendingAverageSincePickUp+"");
        outputFile("\nAverage Parcel Delivering Time Since Generate: "+ (timeSpendingAverageSinceGen));
        outputAvgSinceGen(timeSpendingAverageSinceGen+"");

        // sd of pick up
        double sumSincePickUp = 0;
        for(Parcel p: map.parcelArrive){
            sumSincePickUp += (p.timeSpendingSincePickUp-timeSpendingAverageSincePickUp)*(p.timeSpendingSincePickUp-timeSpendingAverageSincePickUp);
        }
        double sdSincePickUp = Math.sqrt(sumSincePickUp/map.parcelTotalCopy);
        outputSDPickUp(sdSincePickUp+"");


        // sd of generate
        double sumSinceGen = 0;
        for(Parcel p: map.parcelArrive){
            sumSinceGen += (p.timeSpendingSinceGenerate-timeSpendingAverageSinceGen)*(p.timeSpendingSinceGenerate-timeSpendingAverageSinceGen);
        }
        double sdSinceGen = Math.sqrt(sumSinceGen/map.parcelTotalCopy);
        outputSDGen(sdSinceGen + "");



        outputFile("\nstandard deviation of Deliver time since pick up: " + sdSincePickUp);
        outputFile("\nstandard deviation of Deliver time since generate: " + sdSinceGen);
        long finalStep = map.schedule.getSteps();
        outputFile("\nSystem Time Of Finishing Delivery All Parcels: " + finalStep);
        outputFinishTime(finalStep+"");
//        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
//            outputFile("\nTime Saved By Path Changing Path (compare to old path): " + map.pathImprovement + "\n");
//        } else {
            outputFile("\n");
//        }
        outputFile("***********************************************************************************************************");

        System.out.println("Finish!!!!!!!");
//        System.exit(0);


        map.finish();
    }

    private void outputFile(String write) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PrintWriter writer = null;
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }

        File dir = new File("src/courier/testResult/");
        dir.mkdir();
        dir = new File("src/courier/testResult/" + map.testType +"/");
        dir.mkdir();
        try {
            if(map.numOfRefugeIsland>0) {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/"+ mode + " Island " + map.initTime + "_C_" + map.getInitNumOfCarsInStation() + "_P_" + map.initNumOfParcelsInExpressCentre + "_TCL_" + map.getCongestionLevel_1_10() + "_B_" + ExpressCentre.busyLevel + "_S_" + map.seed() + ".out", true)));
            }else {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/"+ mode + " " + map.initTime + "_C_" + map.getInitNumOfCarsInStation() + "_P_" + map.initNumOfParcelsInExpressCentre + "_TCL_" + map.getCongestionLevel_1_10() + "_B_" + ExpressCentre.busyLevel + "_S_" + map.seed() + ".out", true)));
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }

    private void outputAvgSincePickUp(String write){
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/" +  map.testType + "_" + mode + "_AvgPickUp"+".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }

    private void outputAvgSinceGen(String write){
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/" +  map.testType+ "_" + mode + "_AvgGen"+".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }

    private void outputFinishTime(String write){
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/" +  map.testType + "_" + mode + "_FinishTime"+".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }

    private void outputSDPickUp(String write){
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/" +  map.testType + "_" + mode + "_SDPickUp"+".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }

    private void outputSDGen(String write){
        String mode;
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            mode = "AVOID";
        } else {
            mode = "BASIC";
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/testResult/" + map.testType +"/" +  map.testType + "_" + mode + "_SDGen"+".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);
        writer.close();
    }
}
