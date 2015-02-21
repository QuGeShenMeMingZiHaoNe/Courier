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
        outputFile("\nRandom number seed: " + map.seed());
        outputFile("\nNumber Of ExpressCenters: " + map.expressCentres.size());
        outputFile("\nNumber Of Parcels: " + map.parcelTotalCopy);
        outputFile("\nNumber Of Cars: " + map.getInitNumOfCarsInStation());
        outputFile("\nMaximum Car carrying weight: "+Car_BASIC.maxSpace);
        outputFile("\nMap size: " + map.getMapSize_300_2000());
        outputFile("\nThe degree of busy in Express Centre: " + map.expressCentres.getFirst().busy);
        outputFile("\nCongestion Level: " + map.getCongestionLevel_1_10() + "\n");
        outputFile("***********************************************************************************************************");
        outputFile("\nTotal Parcels Delivering Time: " + (map.parcelTimeSpendingTotal));
        outputFile("\nAverage Parcel Delivering Time : " + (timeSpendingAverage));
        long finalStep = map.schedule.getSteps();
        outputFile("\nFinish Delivery All Parcels: " + finalStep);
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
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + mode + " " + map.initTime + "_C_" + map.getInitNumOfCarsInStation() + "_P_" + map.initNumOfParcelsInExpressCentre + "_TCL_" + map.getCongestionLevel_1_10() + "_B_" + ExpressCentre.busy + "_S_" + map.seed() + ".out", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);

        writer.close();
    }
}
