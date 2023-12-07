//Smrithi Panuganti CS 245
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

//https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
//https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/
class Graph {
    private HashMap<String, HashMap<String, Integer>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyList.put(vertex, new HashMap<>());
    }

    public void addWeightedEdge(String src, String dest, int weight) {
        if (!adjacencyList.containsKey(src)) {
            addVertex(src);
        }
        if (!adjacencyList.containsKey(dest)) {
            addVertex(dest);
        }
        adjacencyList.get(src).put(dest, weight);
        adjacencyList.get(dest).put(src, weight);
    }

    public HashMap<String, Integer> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new HashMap<>());
    }

    public List<String> findPath(String startVertex, String endVertex) {
        HashMap<String, Integer> distances = new HashMap<>();
        HashMap<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        HashSet<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        HashMap<String, Integer> edgeDistances = new HashMap<>();

        for (String country : adjacencyList.keySet()) {
            distances.put(country, Integer.MAX_VALUE);
            previous.put(country, null);
        }

        distances.put(startVertex, 0);
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(endVertex)) {
                String temp = endVertex;
                while (previous.get(temp) != null) {
                    path.add(0, temp);
                    temp = previous.get(temp);
                }
                path.add(0, startVertex);
                for (int i = 0; i < path.size() - 1; i++) {
                    String country1 = path.get(i);
                    String country2 = path.get(i + 1);
                    int distance = adjacencyList.get(country1).get(country2);
                    edgeDistances.put(country1 + " --> " + country2, distance);
                }
                break;
            }

            if (!visited.contains(current)) {
                visited.add(current);

                for (Map.Entry<String, Integer> neighbor : adjacencyList.get(current).entrySet()) {
                    String neighborCountry = neighbor.getKey();
                    int distanceToNeighbor = neighbor.getValue();

                    if (!visited.contains(neighborCountry)) {
                        int newDistance = distances.get(current) + distanceToNeighbor;

                        if (newDistance < distances.get(neighborCountry)) {
                            distances.put(neighborCountry, newDistance);
                            previous.put(neighborCountry, current);
                            queue.add(neighborCountry);
                        }
                    }
                }
            }
        }
        return path;
    }

}
public class IRoadTrip {
    //https://howtodoinjava.com/java/collections/hashmap/java-nested-map/
    private HashMap <String, HashMap<String, Integer>> borders;
    private HashMap <String, HashMap<String, Integer>> capdist;;
    private HashMap <String, String> state_name;
    private HashMap<String, String> fixedCountries;
    private Graph roadTripGraph;
    
    public HashMap<String, String> createFixedCountries() {
        HashMap<String, String> fixedCountries = new HashMap<String, String>();
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("Italy/Sardinia", "Italy");
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Korea, People's Republic of", "North Korea");
        fixedCountries.put("Korea, South", "South Korea");
        fixedCountries.put("Korea, Republic of", "South Korea");
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
        return fixedCountries;
    }

    public HashMap <String, HashMap<String, Integer>> readborders(String file) throws NumberFormatException{
        HashMap <String, HashMap<String, Integer>> borders = new HashMap<>();
        //https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Using%20the%20Java%20BufferedRedaer%20class,a%20file%20line%20by%20line.
        //https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        //https://www.baeldung.com/java-remove-punctuation-from-string
        try {
            File f = new File(file);
            Scanner scanner = new Scanner(f);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] arr = line.split(" = ");
                String country = arr[0].trim();
                HashMap <String, Integer> bordc = new HashMap<>();
                if (fixedCountries.containsKey(country)){
                    country = fixedCountries.get(country);
                }
                if (arr.length > 1){
                    String[] borderCountries = arr[1].split("; ");
                    for (String b : borderCountries) {
                        String neighbor = "";
                        String dist = "";
                        for(int i = 0; i < b.length() - 2; i++){

                            if (Character.isDigit(b.charAt(i)) == true){
                                dist += b.charAt(i);
                            }
                            else{
                                neighbor += b.charAt(i);
                            }
                            if (fixedCountries.containsKey(neighbor)){
                                neighbor = fixedCountries.get(neighbor);

                            }

                        }
                        String c = neighbor.trim().replaceAll("[^\\sa-zA-Z0-9]","").trim();
                        bordc.put(c, Integer.parseInt(dist));

                    }
                    borders.put(country, bordc);

                }

            }
            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File does not exist");
            System.exit(0);
        }
        return borders;
    }
    public HashMap <String, HashMap<String, Integer>> readcap(String file){
        HashMap <String, HashMap<String, Integer>> capdist = new HashMap<>();
        
        //https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Using%20the%20Java%20BufferedRedaer%20class,a%20file%20line%20by%20line.
        //https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        try {
            File f = new File(file);
            Scanner scanner = new Scanner(f);
            String line = scanner.nextLine();
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] arr = line.split(",");
                String ida = arr[1].trim();
                
                HashMap<String, Integer> kmdistbtwcap = capdist.getOrDefault(ida, new HashMap<>());

                String idb = arr[3].trim();
                int kmdist = Integer.parseInt(arr[4]);
                kmdistbtwcap.put(idb, kmdist);
                
                capdist.put(ida, kmdistbtwcap);

            }
            scanner.close();
        }
        catch (Exception e){
            System.out.println("File does not exist");
            System.exit(0);
        }
        return capdist;
    }
    public HashMap <String, String> readstate_name(String file){
        HashMap <String, String> state_name = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(file));
            String line = scanner.nextLine();
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] arr = line.split("\t");
                String stateid = arr[1].trim();
                String country = arr[2].trim();
                String date = arr[4].trim();
                String countryName = country;
                if(date.indexOf("2020") != -1){
                    if (fixedCountries.containsKey(country)){
                        countryName = fixedCountries.get(country);
                    }
                    else{
                        countryName = country;
                    }
                    state_name.put(countryName, stateid);
                }
            }

            scanner.close();
        }
        catch (Exception e){
            System.out.println("File does not exist");
            System.exit(0);
        }
        return state_name;
    }

    public IRoadTrip (String[] args) {
        if (args.length < 3){
            System.out.println("Not enough files");
        }
        fixedCountries = createFixedCountries();
        borders = readborders(args[0]);
        capdist = readcap(args[1]);
        state_name = readstate_name(args[2]);
        fixedCountries = createFixedCountries();

        roadTripGraph = createGraphFromBordersAndCapDist();
    }


    public int getDistance (String country1, String country2) {
        HashMap<String, Integer> distances = capdist.get(state_name.get(country1));
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        HashSet<String> visited = new HashSet<>();
        distances.put(state_name.get(country1), 0);
        queue.add(country1);

        int newDistance = -1;

        while (!queue.isEmpty()) {
            String current = queue.poll();
            newDistance = distances.get(state_name.get(country2));

            if (current.equals(state_name.get(country2))) {
                System.out.println(distances.get(country2));
                return distances.get(country2);
            }
        }
        return newDistance;
    }
    public Graph createGraphFromBordersAndCapDist() {
        Graph graph = new Graph();

        for (Map.Entry<String, HashMap<String, Integer>> entry : borders.entrySet()) {
            String country = entry.getKey();
            graph.addVertex(country);

            HashMap<String, Integer> borderingCountries = entry.getValue();
            for (Map.Entry<String, Integer> neighbor : borderingCountries.entrySet()) {
                String neighborCountry = neighbor.getKey();
                int weight = neighbor.getValue();

                if (capdist.containsKey(state_name.get(country)) && capdist.get(state_name.get(country)).containsKey(state_name.get(neighborCountry))) {

                    weight = capdist.get(state_name.get(country)).get(state_name.get(neighborCountry));
                }

                graph.addWeightedEdge(country, neighborCountry, weight);
            }
        }
        return graph;
    }


    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String c1 = scanner.nextLine().trim();
            if (c1.equals("EXIT")){
                scanner.close();
                System.exit(0);
            }
            else if (!borders.containsKey(c1)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                acceptUserInput();
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String c2 = scanner.nextLine().trim();

            if (c2.equals("EXIT")){
                scanner.close();
                System.exit(0);
            }
            else if (!borders.containsKey(c2)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                acceptUserInput();
            }
            List<String> path = roadTripGraph.findPath(c1, c2);
            int dist = 0;
            int totaldist = 0;
            for (int i = 0; i < path.size() - 1; i++){
                dist = getDistance(path.get(i), path.get(i + 1));
                System.out.println("* " + path.get(i) + " --> " + path.get(i + 1) + " (" + dist + " km.)");
            }
        }

        
    }
    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        Graph roadTripGraph = a3.createGraphFromBordersAndCapDist();
        a3.acceptUserInput();
    }

}

