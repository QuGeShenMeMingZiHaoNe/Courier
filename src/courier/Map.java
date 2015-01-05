package courier;

import sim.app.wcss.tutorial13.Student;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.grid.IntGrid2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;
import sun.awt.image.ImageWatched;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by daniel on 15/1/4.
 */
public class Map extends SimState {
    LinkedList<Station> stations = new LinkedList<Station>();
    LinkedList<Parcel> parcels = new LinkedList<Parcel>();
    LinkedList<Tramline> tramlines = new LinkedList<Tramline>();
    LinkedList<Car> cars = new LinkedList<Car>();

    private int serialStationID =1;
    private int serialParcelID=1;
    private int serialTramlineID=1;
    private int serialCarID=1;
    private int initNumOfCarsInStation = 1;
    private int initNumOfParcelsInStation = 3;

    public Map(long seed) {
        super(seed);
    }
//
//
//        public IntGrid2D map = new IntGrid2D(100,100);
//
//        public Network tramline = new Network(false);
//
//        public void start()
//        {
//            super.start();
//
//            // clear the yard
//            map.clear();
//
//            // clear the buddies
//            tramline.clear();
//
//            // add some students to the yard
//            for(int i = 0; i < numStudents; i++)
//            {
//                Student student = new Student();
//                yard.setObjectLocation(student,
//                        new Double2D(yard.getWidth() * 0.5 + random.nextDouble() - 0.5,
//                                yard.getHeight() * 0.5 + random.nextDouble() - 0.5));
//
//                buddies.addNode(student);
//                schedule.scheduleRepeating(student);
//            }
//
//            // define like/dislike relationships
//            Bag students = buddies.getAllNodes();
//            for(int i = 0; i < students.size(); i++)
//            {
//                Object student = students.get(i);
//
//                // who does he like?
//                Object studentB = null;
//                do
//                {
//                    studentB = students.get(random.nextInt(students.numObjs));
//                } while (student == studentB);
//                double buddiness = random.nextDouble();
//                buddies.addEdge(student, studentB, new Double(buddiness));
//
//                // who does he dislike?
//                do
//                {
//                    studentB = students.get(random.nextInt(students.numObjs));
//                } while (student == studentB);
//                buddiness = random.nextDouble();
//                buddies.addEdge(student, studentB, new Double( -buddiness));
//            }
//        }
//
//        public static void main(String[] args)
//        {
//            doLoop(Students.class, args);
//            System.exit(0);
//        }
//    }

    private void initStations(){
        stations.add(new Station("A",serialStationID,new Int2D(10,10)));
        serialStationID++;
        stations.add(new Station("B",serialStationID,new Int2D(90,90)));
        serialStationID++;
    }

    private void initTramlines(){
//        int next;
//        for(Station s :stations){
//
//            do
//            {
//               next = random.nextInt(stations.size());
//            } while ((next-1)>=0 && (next-1)!=stations.indexOf(s));
//
//            new Tramline(s,stations.get(next));
//        }

        tramlines.add( new Tramline(stations.get(0),stations.get(1),serialTramlineID));
        serialTramlineID++;
    }

    private void initCars(){
        Car car;
        for(Station s : stations){
            for(int i =0; i<initNumOfCarsInStation;i++){
                car = new Car(serialCarID,s.location);
                s.carPark.add(car);
                cars.add(car);
                serialCarID++;
            }
        }
    }

    private void initParcels(){
        Parcel p;
        int next;
        for(Station s : stations){
            for(int i =0; i<initNumOfParcelsInStation;i++){
                do
                {
                    next = random.nextInt(stations.size());
                } while (stations.get(next).stationID != s.stationID);

                p = new Parcel(serialParcelID,stations.get(next).stationID,1);
                serialParcelID++;
                s.pToBeSent.add(p);
            }
        }
    }

}
