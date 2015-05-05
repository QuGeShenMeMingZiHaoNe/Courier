package courier;

import sim.util.Int2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

    Map map;

    public DataReader(Map map) {
        this.map = map;
    }

    public void initExpressCenter() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/src/courier/data/ExpressCenters.data")));
        try {
            String line;
            while ((line = br.readLine()) != null && !line.contains("//")) {
                String[] split = line.split(",");
                map.addExpressCentre(split[0], new Int2D(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        } finally {
            br.close();
        }
    }

    public void initTramLine() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/src/courier/data/TramLines.data")));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                map.addTramLine(split[0], split[1], split[2]);
            }
        } finally {
            br.close();
        }
    }

}
