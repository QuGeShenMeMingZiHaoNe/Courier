package courier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class OutPutResult {
    Map map;

    OutPutResult(Map map) {
        this.map = map;
    }

    public void writeResult() {
        long timeSpendingAverage = map.parcelTimeSpendingTotal / map.parcelTotalCopy;
        outputFile("***********************************************************************************************************");
        outputFile("\nMode: " + map.mode);
        outputFile("\nRefugee Island On: " + map.getRefugeeIslandOn());
        if(map.getRefugeeIslandOn()){
            outputFile("\nRefugee Island carPark available : " + map.carParkAvailable);
            outputFile("\nNumber Of Refugee Between Two Stations : " + map.numOfRefugeeIsland);
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
        outputFile("\nSum Of All Parcels Delivering Time: " + (map.parcelTimeSpendingTotal));
        outputFile("\nAverage Parcel Delivering Time For One Parcel : " + (timeSpendingAverage));
        long finalStep = map.schedule.getSteps();
        outputFile("\nSystem Time Of Finishing Delivery All Parcels: " + finalStep);
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            outputFile("\nTime Saved By Path Changing Path (compare to old path): " + map.pathImprovement + "\n");
        } else {
            outputFile("\n");
        }
        outputFile("***********************************************************************************************************");

        System.out.println("Finish!!!!!!!");
        System.exit(0);
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
        try {
            if(map.getRefugeeIslandOn()) {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + mode + " Island " + map.initTime + "_C_" + map.getInitNumOfCarsInStation() + "_P_" + map.initNumOfParcelsInExpressCentre + "_TCL_" + map.getCongestionLevel_1_10() + "_B_" + ExpressCentre.busyLevel + "_S_" + map.seed() + ".out", true)));
            }else {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + mode + " " + map.initTime + "_C_" + map.getInitNumOfCarsInStation() + "_P_" + map.initNumOfParcelsInExpressCentre + "_TCL_" + map.getCongestionLevel_1_10() + "_B_" + ExpressCentre.busyLevel + "_S_" + map.seed() + ".out", true)));
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);

        writer.close();
    }
}
