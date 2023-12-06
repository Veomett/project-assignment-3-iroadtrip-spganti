import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import static java.lang.Integer.MAX_VALUE;

public class IRoadTrip2 {
    private Graph countryGraph;
    private Map<String, String> countryCodesMap;
    private Map<String, String> fixedCountriesMap;

    public IRoadTrip2 (String [] args) {
        countryGraph= new Graph();
        countryCodesMap = new HashMap<String, String>();
        fixedCountriesMap = createFixedCountries();
        if (args.length != 3 ||
                ! (loadGraph(args[0]) &&
                        loadCodes(args[2]) &&
                        loadDistances(args[1])))
            usage();
        //countryGraph.print();
        System.out.println("Distance between United States and Canada" + getDistance("United States of America", "Canada"));
        System.out.println("Distance Between Canada and Mexico" + getDistance("Canada", "Mexico"));
    }

    private Map<String, String> createFixedCountries() {
        Map<String, String> fixedCountries = new HashMap<String, String>();
        fixedCountries.put("German Federal Republic", "Germany");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "Macedonia");
        fixedCountries.put("Bosnia-Herzegovina", "Bosnia and Herzegovina");
        fixedCountries.put("Bahamas", "Bahamas, The");
        fixedCountries.put("Zambia.", "Zambia");
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("United States", "United States of America");
        fixedCountries.put("Greenland).", "Greenland");
        fixedCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of the", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Republic of the", "Republic of the Congo");
        fixedCountries.put("Gambia, The", "The Gambia");
        fixedCountries.put("Gambia", "The Gambia");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "North Macedonia");
        fixedCountries.put("Italy.", "Italy");
        fixedCountries.put("East Timor", "Timor-Leste");
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Korea, People's Republic of", "North Korea");
        fixedCountries.put("Korea, South", "South Korea");
        fixedCountries.put("Korea, Republic of", "South Korea");
        fixedCountries.put("UAE", "United Arab Emirates");
        fixedCountries.put("Turkey (Turkiye)", "Turkey");
        fixedCountries.put("Botswana.", "Botswana");
        fixedCountries.put("Myanmar (Burma)", "Burma");
        fixedCountries.put("Vietnam, Democratic Republic of", "Vietnam");
        fixedCountries.put("Cambodia (Kampuchea)", "Cambodia");
        fixedCountries.put("Sri Lanka (Ceylon)", "Sri Lanka");
        fixedCountries.put("Kyrgyz Republic", "Kyrgyzstan");
        fixedCountries.put("Yemen (Arab Republic of Yemen)", "Yemen");
        fixedCountries.put("Turkey (Ottoman Empire)", "Turkey");
        fixedCountries.put("Iran (Persia)", "Iran");
        fixedCountries.put("Zimbabwe (Rhodesia)", "Zimbabwe");
        fixedCountries.put("Tanzania/Tanganyika", "Tanzania");
        fixedCountries.put("Congo", "Republic of the Congo");
        fixedCountries.put("Burkina Faso (Upper Volta)", "Burkina Faso");
        fixedCountries.put("Belarus (Byelorussia)", "Belarus");
        fixedCountries.put("Russia (Soviet Union)", "Russia");
        fixedCountries.put("Italy/Sardinia", "Italy");
        return fixedCountries;
    }

    private boolean loadGraph(String filename){
        String currentLine;
        String[] borderCountries;
        String countryName;
        try {
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                String[] lineCountries = currentLine.split(" = ", 2);
                countryName = lineCountries[0];
                if (fixedCountriesMap.containsKey(countryName)){
                    //System.out.println("We will fix country " + countryName);
                    countryName = fixedCountriesMap.get(countryName);
                }
                //System.out.println("We got country " + countryName);
                countryGraph.addCountry(countryName);
                if (lineCountries.length > 1) {
                    borderCountries = lineCountries[1].split(" km; | km, | km|km |; | [(]| [)]");
                } else {
                    borderCountries = new String[0];
                }
                if (borderCountries.length > 0) {
                    for (int i = 0; i < borderCountries.length; i++) {
                        String neighborName = borderCountries[i].replaceAll("\\d| \\d|,", "");
                        if (fixedCountriesMap.containsKey(neighborName)){
                            //System.out.println("Fixing country " + neighborName);
                            neighborName = fixedCountriesMap.get(neighborName);
                        }
                        countryGraph.addCountry(neighborName);
                        countryGraph.addEdge(countryName, neighborName, MAX_VALUE);
                        //System.out.println("border country: " + neighborName);
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private boolean loadCodes(String filename){
        String[] codeArray;
        String currentLine;
        String countryName;
        String countryCode;
        try {
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                codeArray = currentLine.trim().split("\\t");
                if (codeArray[codeArray.length-1].startsWith("2020")){
                    countryName = codeArray[2];
                    if(fixedCountriesMap.containsKey(countryName)){
                        //System.out.println("Fixing country " + countryName);
                        countryName = fixedCountriesMap.get(countryName);
                    }
                    countryCode = codeArray[1];
                    //System.out.println(countryName + " has code " + countryCode);
                    countryCodesMap.put(countryCode, countryName);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private boolean loadDistances(String filename){
        String[] distancesArray;
        String currentLine;
        String country1;
        String country2;
        Integer distance;
        try {
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);
            scanner.nextLine(); // don't need first line
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                distancesArray = currentLine.trim().split(",");
                country1 = countryCodesMap.get(distancesArray[1]);
                country2 = countryCodesMap.get(distancesArray[3]);
                distance = Integer.parseInt(distancesArray[4]);
                if(country1 != null && country2 != null && countryGraph.hasCountry(country1) && countryGraph.areAdjacent(country1, country2)){
                    countryGraph.replaceEdge(country1, country2, distance);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getDistance (String country1, String country2) {
        if (countryGraph.hasCountry(country1) && countryGraph.areAdjacent(country1, country2))
        {
            if (countryGraph.adjacencyList.get(country1).get(country2) < MAX_VALUE){
                return countryGraph.adjacencyList.get(country1).get(country2);
            }
        }
        return -1;
    }


    public List<String> findPath (String country1, String country2) {

        List<String> pathList = new ArrayList<>();
        Map<String, String> dijkstraTable = countryGraph.dijkstra(country1, country2);
        if (dijkstraTable == null){
            System.out.println("Returning empty path");
            return pathList;
        }
        String curCountry = dijkstraTable.get(country2);
        String subsequentCountry = country2;

        while (curCountry != null){
            //System.out.println(curCountry + " --> " + subsequentCountry + " (" + getDistance(curCountry, subsequentCountry) + " km.)");
            pathList.add(0, curCountry + " --> " + subsequentCountry + " (" + getDistance(curCountry, subsequentCountry) + " km.)");
            subsequentCountry = dijkstraTable.get(subsequentCountry);
            curCountry = dijkstraTable.get(subsequentCountry);
        }

        return pathList;

    }


    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String country1 = scanner.nextLine().trim();

            if (country1.equalsIgnoreCase("EXIT")) {
                break;  // Exit the loop if the user types EXIT
            }

            if (!countryGraph.hasCountry(country1)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String country2 = scanner.nextLine().trim();

            if (country2.equalsIgnoreCase("EXIT")) {
                break;  // Exit the loop if the user types EXIT
            }

            if (!countryGraph.hasCountry(country2)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            // Process the pair of valid country names
            System.out.println("Route from " + country1 + " to " + country2 + ":");
            List<String> path = findPath(country1, country2);
            if (path.size() > 0) {
                printPath(path);
            }

            System.out.println("Exiting the program.");
        }
    }

    private void printPath(List<String> path) {
        for (String step : path)
            System.out.println("* " + step);
    }
    public static void main(String[] args) {
        IRoadTrip2 a3 = new IRoadTrip2(args);

        a3.acceptUserInput();
    }

    public class Graph {
        private Map<String, Map<String, Integer>> adjacencyList;
        public Graph() {
            this.adjacencyList = new HashMap<>();
        }

        public void addCountry(String country) {
            if (! this.hasCountry(country))
                adjacencyList.put(country, new HashMap<>());
        }

        public boolean hasCountry(String country) {
            return adjacencyList.containsKey(country);
        }

        public void addEdge(String country1, String country2, int distance) {
            adjacencyList.get(country1).put(country2, distance);
            adjacencyList.get(country2).put(country1, distance);
        }
        public void replaceEdge(String country1, String country2, int distance) {
            adjacencyList.get(country1).replace(country2, distance);
            adjacencyList.get(country2).replace(country1, distance);
        }


        public boolean areAdjacent(String country1, String country2){
            return (hasCountry(country1) && adjacencyList.get(country1).containsKey(country2));}

        public Map<String, String> dijkstra(String country1, String country2){
            PriorityQueue<Node> costMinHeap = new PriorityQueue<>();
            Map<String, Integer> curCosts = new HashMap<String, Integer>();
            Map<String, String> dijkstraPath = new HashMap<String, String>();
            Set<String> finalizedSet = new HashSet<String> ();
            for (String country: countryGraph.adjacencyList.keySet()){
                if (country.equals(country1)){
                    curCosts.put(country1, 0);
                    //System.out.println("Destination is " + country1);
                    if (countryGraph.adjacencyList.get(country1) != null){
                        for (String neighbor: countryGraph.adjacencyList.get(country1).keySet()){
                            curCosts.put(neighbor, getDistance(country1, neighbor));
                            costMinHeap.add(new Node(neighbor, getDistance(country1, neighbor)));
                            //System.out.println("Putting in neighbor " + neighbor + " with distance " + getDistance(country1, neighbor));
                            dijkstraPath.put(neighbor, country1);
                        }
                    }
                }
            }

            while (!finalizedSet.contains(country2) && !costMinHeap.isEmpty()){
                Node curNode = costMinHeap.poll();
                //System.out.println("Pulling country " + curNode.country);
                //Integer dist = curNode.distance;  // don't need?
                if (curNode != null && !finalizedSet.contains(curNode.country) && countryGraph.adjacencyList.get(curNode.country) != null){
                    finalizedSet.add(curNode.country);
                    for (String neighbor : countryGraph.adjacencyList.get(curNode.country).keySet()){
                        Integer neighborDist = getDistance(curNode.country, neighbor);
                        if (neighborDist != -1 && !curCosts.containsKey(neighbor)){ // neighbor not yet initialized
                            curCosts.put(neighbor, curCosts.get(curNode.country) + neighborDist);
                            dijkstraPath.put(neighbor, curNode.country);
                            Node insertedNode = new Node(neighbor, curCosts.get(neighbor));
                            costMinHeap.add(insertedNode);
                        }
                        else if (neighborDist != -1 && curCosts.get(curNode.country) + neighborDist < curCosts.get(neighbor)){
                            curCosts.replace(neighbor, curCosts.get(curNode.country) + neighborDist);
                            dijkstraPath.put(neighbor, curNode.country);
                            Node insertedNode = new Node(neighbor, curCosts.get(neighbor));
                            costMinHeap.add(insertedNode);
                        }
                    }
                }
            }
            if(finalizedSet.contains(country2) && (curCosts.get(country2)!=null)){
                System.out.println("Total cost is " + curCosts.get(country2));
                return dijkstraPath;
            }
            else{
                Map<String, String> emptyPath = new HashMap<String, String>();
                System.out.println("Returning empty path");
                return emptyPath;
            }
        }
        public void print(){
            for (String country : adjacencyList.keySet()){
                System.out.println("Country " + country +" has neighbors ");
                for (String neighbor : adjacencyList.get(country).keySet()){
                    System.out.print(neighbor + " " + adjacencyList.get(country).get(neighbor) + " ");
                }
                System.out.println("\n");
            }
        }
    }
    public static class Node implements Comparable<Node>{
        String country;
        Integer distance;
        Node(String c, Integer d){
            country = c;
            distance = d;
        }
        @Override
        public int compareTo(Node n1)
        {
            return this.distance - n1.distance;
        }
    }
    public void usage() {
        System.out.println("Please provide three arguments, in order:");
        System.out.println("1: borders = land boundaries file");
        System.out.println("2: capdist = Distances between capitals, eg. what's found in http://ksgleditsch.com/data-5.html");
        System.out.println("3: state_name = Names of countries, eg. http://www.ksgleditsch.com/statelist.html > [list of independent states]");
    }

}