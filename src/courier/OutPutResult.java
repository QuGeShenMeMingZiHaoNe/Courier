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
        outputFile("\nMode: " + map.mode + "\nRandom number seed: " + map.seed() + "\nCar number: " + map.getInitNumOfCarsInStation() + "\nParcel number: " + map.parcelTotalCopy + "\nExpressCenter: " + map.expressCentres.size() + "\n");
        outputFile("***********************************************************************************************************");
        outputFile("\nTotal Parcels Delivering Time: " + (map.parcelTimeSpendingTotal) + "\nAverage Parcel Delivering Time : " + (timeSpendingAverage));
        long finalStep = map.schedule.getSteps();
        outputFile("Finish Delivery All Parcels: " + finalStep);
        if (map.getMode() == SIMULATION_MODE.AVOID_TRAFFIC_JAM) {
            outputFile("Time Saveing by path changing( compare with old path): " + map.pathImprovement + "\n");
        } else {
            outputFile("\n");
        }
        outputFile("***********************************************************************************************************");

        outputFile("\nThe degree of busy in Express Centre: " + map.expressCentres.getFirst().busy + "\n");

        outputFile("***********************************************************************************************************");

        System.out.println("Finish!!!!!!!");
    }

    private void outputFile(String write) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("src/courier/" + map.mode + " " + map.initTime + ".output", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(write);

        writer.close();
    }
}
