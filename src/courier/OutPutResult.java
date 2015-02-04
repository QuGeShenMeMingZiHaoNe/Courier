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
        }else{
            outputFile("\n");
        }
        outputFile("***********************************************************************************************************");

        outputFile("\nThe Longer carrying Time means the car travel with the parcel for longer period.\n" +
                "The Traffic Avoiding Mode has a longer carrying time as it has to carry the parcels\n" +
                "with the cars when the cars need to travel longer way to avoid the traffic jam.\n" +
                "The performance can be well represented by the Total Time Spending On Delivery\n" +
                "The shorter the time, the better the performance is!\n");

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
