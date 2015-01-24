package courier;

import java.io.*;
import java.net.InetSocketAddress;

public class TestReader {

    public void read() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/src/courier/TestSetting.data")));
        try {
            StringBuilder sb = new StringBuilder();
            String line ;

            while (((line =br.readLine()) != null) && !line.contains("//")) {
                sb.append(line);
                sb.append(System.lineSeparator());
//                line = br.readLine();

                String obj = line.split(":")[0];
                int value = Integer.parseInt(line.split(":")[1]);
                createObj(obj,value);
            }
            String everything = sb.toString();
            System.out.println(everything);
        } finally {
            br.close();
        }
}
    public void createObj(String obj, int value){

    }

}
