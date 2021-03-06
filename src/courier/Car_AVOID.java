package courier;

import sim.engine.SimState;
import sim.util.Int2D;

import java.util.Iterator;
import java.util.LinkedList;

public class Car_AVOID extends Car_BASIC {
    public Car_AVOID(int carID, Int2D location, Map map) {
        super(carID, location, map);
    }

    private void tryLeaveStation(TramLine_BASIC tramLine) {

        if (stationTo instanceof RefugeeIsland && ((RefugeeIsland) stationTo).carParkAvailable <= 0) {
            return;
        }

        // allow to leave
        if (tramLine.okToLeave(currStation)) {
            if (tramLine.trafficLightOccupant == null)
                tramLine.tryOccupyTraffic(currStation);
            // leave the car park one by one -- FIFO
            if (tramLine.currLeavingCars != null) {
                return;
            }
            tramLine.currLeavingCars = this;
            tramLine.carsOnTramLine.add(this);
            leaveStation();
//                    refusedAlterPath = new LinkedList<ExpressCentre>();
        } else {
            // not allow to leave, then ask to leave
            tramLine.tryOccupyTraffic(currStation);
            if (!tramLine.okToLeave(currStation) && (tramLine.trafficLightOccupant == currStation)) {
                alterPath = true;
                if (!carParkTicket.equals("NULL")) {
                    ((RefugeeIsland) (map.expressCentres.getFirst().findStationByName(carParkTicket))).carParkAvailable++;
                    carParkTicket = "NULL";
                }
                arriveStation();
                alterPath = false;
            }
        }
    }

    public void optimizeUnloadParcel() {
        if (globalPath == null || carrying.isEmpty())
            return;
        LinkedList<Parcel> carryingCopy = (LinkedList<Parcel>) carrying.clone();
        for (Parcel p : carryingCopy) {
            if (!globalPath.contains(p.destination)) {
                carrying.remove(p);
                currStation.pToBeSent.add(0, p);
                this.spaceRemaining += p.weight;
            }
        }
        optimizedLoadParcel();
    }


    @Override
    public void arriveStation() {

        if (!hasArrived) {
            // unloadParcel method is in the first time arrive method
            firstTimeArrive();
        }
        pickupGlobalList();
        if (map.optimizedPickUp) {
            optimizedLoadParcel();
        }
        loadParcelBasic();
        // set both global and local path
        setAllPath();
    }

    private void setAllPath() {

        // if there are something to be delivered
        if (!carrying.isEmpty()) {

            // get the parcel with highest priority
            Parcel p = carrying.get(0);

            // find the final destination of the parcel as target Station.
            ExpressCentre targetStation = p.destination;


            if (globalPath == null) {
                setPathGlobal(currStation, targetStation);
            }
            LinkedList<ExpressCentre> avoids = findTrafficJam();
//                    avoids.addAll(refusedAlterPath);
            if (avoids.size() < currStation.neighbours.size()) {
                setPathGlobal(currStation, targetStation, avoids);
            }


            // calculate the path from current station to the next station
            if (stationTo != null)
                setPathLocal(currStation, stationTo);
        }
    }

    private LinkedList<ExpressCentre> findTrafficJam() {
        LinkedList<ExpressCentre> avoids = new LinkedList<ExpressCentre>();

        int indexCurr = -1;
        if (globalPath != null) {
            indexCurr = globalPath.indexOf(currStation);
        }

//        if(indexCurr>1){
//            avoids.add(globalPath.get(indexCurr - 1));
//        }

        for (ExpressCentre nb : currStation.neighbours) {
            TramLine_BASIC tl = map.tramLines.getFirst().findTramLine(currStation, nb);

            //test dont go back to where it came from
            if (indexCurr >= 1 && nb.equals(globalPath.get(indexCurr - 1))) {
                avoids.add(nb);
                continue;
            } else {
                tl.tryOccupyTraffic(currStation);
            }

            if (!tl.okToLeave(currStation)) {
                avoids.add(nb);
            }

//            if(globalPath!=null&&globalPath.indexOf(currStation)>0){
//                avoids.add(globalPath.get(globalPath.indexOf(currStation) - 1));
//            }
        }
//        if(avoids.size()==currStation.neighbours.size()){
//            initCarState();
//        }
        return avoids;
    }


    private void setPathGlobal(ExpressCentre from, ExpressCentre to, LinkedList<ExpressCentre> avoids) {
        TramLine_BASIC tl = map.tramLines.get(0);
        LinkedList<ExpressCentre> old;

        if (!(globalPath == null)) {
            old = (LinkedList<ExpressCentre>) globalPath.clone();
        } else {
            old = null;
        }

        if (globalPath == null) {
            globalPath = tl.getPathGlobal(from, to, avoids);

        }

        if (alterPath) {
            globalPath = tl.getPathGlobal(from, to, avoids);

            if (globalPath != null) {
                ExpressCentre commonEC = findFirstCommentStation(globalPath, old);

                double oldDistance = calPathDistanceBetween(old, currStation, commonEC);
                double newDistance = calPathDistanceBetween(globalPath, currStation, commonEC);

                double oldPathIntensity = findPathIntensity(old);
                double newPathIntensity = findPathIntensity(globalPath);

//                if (newDistance >= oldDistance) {
                if (newDistance >= oldDistance || oldPathIntensity <= 0.5 * newPathIntensity) {
//                        refusedAlterPath.add(globalPath.get(1));
                    globalPath = old;
                } else {
                    map.pathImprovement += (oldDistance - newDistance);
                    if (map.getDetailsOn()) {
                        System.out.println("\n\n\n\n\n\nLog: old distance " + oldDistance + "  new distance " + newDistance + "\n" + "successfully alter " + "\n between " + currStation + " " + commonEC);
                        System.out.println("Parcels Total: " + map.parcelTotal);
                    }
                }

            }
        }

        if (globalPath == null && old != null) {
            globalPath = old;
        }

        if (globalPath != null) {
            stationTo = globalPath.get(globalPath.indexOf(currStation) + 1);
        }

//        System.out.println(globalPath.equals(globalPath) + " " +map.optimizedPickUp);

        if (!globalPath.equals(old) && map.optimizedPickUp) {
//            System.out.println("fdasfsafsafs");
            optimizeUnloadParcel();
        }
    }

    private double findPathIntensity(LinkedList<ExpressCentre> path) {
        Iterator<ExpressCentre> iter = path.iterator();
        ExpressCentre first = iter.next();
        int x = 1;
        if (!first.equals(currStation)) {
            while (iter.hasNext()) {
                first = iter.next();
                if (first.equals(currStation))
                    break;
                x++;
            }
        }
        double diff = path.size() - x;
        ExpressCentre second;
        double intensity = 0;
        TramLine_BASIC tl;
        double count = 0;
        while (iter.hasNext()) {
            second = iter.next();
            tl = map.tramLines.getFirst().findTramLine(first, second);
            intensity += tl.carsOnTramLine.size() * ((diff - count) / diff);
            count++;
            first = second;
        }
        return intensity;
    }

    private ExpressCentre findFirstCommentStation(LinkedList<ExpressCentre> newPath, LinkedList<ExpressCentre> oldPath) {
        LinkedList<ExpressCentre> copyNew = (LinkedList<ExpressCentre>) newPath.clone();
        copyNew.remove(0);

        LinkedList<ExpressCentre> copyOld = new LinkedList<ExpressCentre>();
        int index = oldPath.indexOf(newPath.getFirst());
        while (index < oldPath.size()) {
            copyOld.add(oldPath.get(index));
            index++;
        }

        for (ExpressCentre ec : copyNew) {
            if (copyOld.contains(ec))
                return ec;
        }

        System.out.println("log: NEVER GETS HERE");
        // never come to this state
        return null;
    }

    private double calPathDistanceBetween(LinkedList<ExpressCentre> path, ExpressCentre a, ExpressCentre b) {
        double distance = 0;
        // return directly if station a and b

        for (int index = path.indexOf(a); index < path.size(); index++) {
            // look one step forward
            if (index == path.indexOf(a)) {
                // if it is old path
                if (!path.equals(globalPath)) {
                    TramLine_BASIC tl = map.tramLines.getFirst().findTramLine(a, path.get(index + 1));
                    // car coming
                    Car_BASIC car = tl.carsOnTramLine.getLast();
                    if (car.stationTo.equals(currStation)) {
                        double carDistance = car.location.distance(currStation.location);
                        distance += carDistance;

//                        double dif = globalPath.get(globalPath.indexOf(currStation)+1).location.distance(currStation.location);
//                        if(carDistance<dif){
//                            distance = 0;
//                            return distance;
//                        }
                        distance += a.location.distance(path.get(index + 1).location);


                    } else {
                        // car leaving
                        car = tl.carsOnTramLine.getLast();
                        double carDistance = car.stationTo.location.distance(car.location);
                        carDistance += car.stationTo.location.distance(currStation.location);
                        distance += carDistance;

                        distance += a.location.distance(path.get(index + 1).location);
                    }
                    // if the cars in the station is over the threshold of traffic light
                    // test
                    int countCompetitors = 0;
                    if (currStation.carPark.size() > TramLine_BASIC.maximumCarLeavingBeforeRedLight) {
                        for (Car_BASIC competitors : currStation.carPark) {
                            if (competitors.globalPath != null) {
                                // +1 zai zhe li
                                if (competitors.globalPath.get(competitors.globalPath.indexOf(currStation()) + 1).equals(b)) {
                                    countCompetitors++;
                                }
                            }
                        }
                    }
                    if (countCompetitors > TramLine_BASIC.maximumCarLeavingBeforeRedLight) {
                        distance += a.location.distance(b.location) * 2 * (countCompetitors / TramLine_BASIC.maximumCarLeavingBeforeRedLight);
                    }
                } else {
                    distance += path.get(index).location.distance(path.get(index + 1).location);
                }
            } else {
                distance += path.get(index).location.distance(path.get(index + 1).location);
            }

            if (path.get(index + 1).equals(b)) {
                return distance;
            } else {
                // the more station it has to go through the worse prediction it gets
                if (index <= path.size() - 2)
                    distance += path.get(index + 1).location.distance(path.get(index + 2).location);
            }
        }
        return distance;
    }


    public void step(SimState state) {
        // don't increase cost if the cars in garage
//        if (!(currStation() instanceof Garage)) {
//            Int2D d = new Int2D(1, 1);
//            map.profit -= d.distance(new Int2D(2, 2));
//        }

        if (stepping) {
            oneStep();
            return;
        }

        currStation = currStation();
        if (currStation != null) {
            if (!hasArrived) {
                arriveStation();
                // this return is for waite one step after arrival
                return;
            } else {
                // TODO globalPath can be set with out using arriveStation
                // TODO car caller can pick multiple parcels
                // if the car has not carrying any thing that it don't need to leave the station
                if (this.carrying.isEmpty()) {
                    setUpCarCaller();
                    this.arriveStation();
                    return;
                }

                // this is for catching the situation that both pToBeSent and global parcel boards are empty
                if (globalPath == null || stationTo == null) {
                    this.arriveStation();
                    return;
                }
            }


            TramLine_BASIC tramLine = map.tramLines.get(0).findTramLine(stationFrom, stationTo);

            // can not find a tram Line to destination
            if (tramLine == null) {
                return;
            }

            // prepare for leaving
            if (!hasLeaved) {
                tryLeaveStation(tramLine);
                return;
            }

            // delay one step of leaving the car park, Truly leave
            if (hasLeaved) {
                afterLeaving(tramLine);
                stepping = true;
            }
        }

        // travel on the tramline
        oneStep();

    }
}
