package courier;

import java.util.LinkedList;

public class AStar {
    Map map;
    LinkedList<SearchNode> closeSet;
    LinkedList<SearchNode> openSet;
    LinkedList<SearchNode> allStations;

    AStar(Map map) {
        this.map = map;
        closeSet = new LinkedList<SearchNode>();
        openSet = new LinkedList<SearchNode>();
        allStations = new LinkedList<SearchNode>();
        initAllStations();
    }


    private void initAllStations() {
        for (int i = 0; i < map.allStations.size(); i++) {
            allStations.add(new SearchNode(map.allStations.get(i)));
        }
    }

    private SearchNode findStation(ExpressCentre find) {
        for (SearchNode sn : allStations) {
            if (sn.station.equals(find))
                return sn;
        }
        // this will never happen
        return null;
    }


    public LinkedList<ExpressCentre> findShortestPath(ExpressCentre from, ExpressCentre goal, LinkedList<ExpressCentre> avoidsNB) {

        // initialize linkedLists
        closeSet.clear();
        openSet.clear();
        allStations.clear();
        initAllStations();

        // if from or to is either isolated then will be impossible to the to one of them
        if (goal.neighbours.isEmpty() || from.neighbours.isEmpty())
            return null;

//        LinkedList<ExpressCentre> returnPath = new LinkedList<ExpressCentre>();
        SearchNode currSearchNode = findStation(from);
        openSet.add(currSearchNode);
        currSearchNode.gScore = 0.0;
        currSearchNode.fScore = currSearchNode.gScore + currSearchNode.station.location.distance(goal.location);

        while (!openSet.isEmpty()) {
            currSearchNode = findStationWithLowestFScore();
            if (currSearchNode.station.equals(goal))
                return reconstructPath(goal, from);

            closeSet.add(currSearchNode);
            openSet.remove(currSearchNode);

            for (ExpressCentre nb : currSearchNode.station.neighbours) {
                SearchNode nbSearchNode = findStation(nb);

                // if the node has already been search then we will not search it again
                if (closeSet.contains(nbSearchNode))
                    continue;

                if(avoidsNB.contains(nb)&&currSearchNode.station.equals(from)) {
                    nbSearchNode.cameFrom = currSearchNode;
                    nbSearchNode.gScore = 9999999999999.9;
                    nbSearchNode.fScore = nbSearchNode.gScore + nb.location.distance(goal.location);
                    continue;
                }

                double tempGScore = currSearchNode.gScore + currSearchNode.station.location.distance(nb.location);

                // TODO dangerous at the second condition could be null
                if (!openSet.contains(nbSearchNode) || tempGScore < nbSearchNode.gScore) {
                    nbSearchNode.cameFrom = currSearchNode;
                    nbSearchNode.gScore = tempGScore;
                    nbSearchNode.fScore = nbSearchNode.gScore + nb.location.distance(goal.location);

                    if (!openSet.contains(nbSearchNode))
                        openSet.add(nbSearchNode);
                }
            }
        }

        return null;
    }

    private LinkedList<ExpressCentre> reconstructPath(ExpressCentre current, ExpressCentre from) {
        LinkedList<ExpressCentre> path = new LinkedList<ExpressCentre>();
        path.add(current);
        SearchNode currentSearchNode = findStation(current);

        while (currentSearchNode.station != from) {
            currentSearchNode = currentSearchNode.cameFrom;
            path.addFirst(currentSearchNode.station);
        }

        return path;
    }

    private SearchNode findStationWithLowestFScore() {
        SearchNode lowestNode = openSet.getFirst();
        double lowestFScore = lowestNode.fScore;
        for (SearchNode sn : openSet) {
            if (sn.fScore < lowestFScore) {
                lowestNode = sn;
                lowestFScore = sn.fScore;
            }
        }
        return lowestNode;
    }

    private class SearchNode {
        protected ExpressCentre station;
        protected Double gScore;
        protected Double fScore;
        protected SearchNode cameFrom;


        SearchNode(ExpressCentre station) {
            this.station = station;
        }

        @Override
        public String toString() {
            return station.toString();
        }
    }
}