//Smrithi Panuganti CS 245
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

/*
I created a Graph class with several set/get methods. I basically made an adjacency list using
a nested hashmap. For each vertex/country (src) as the key of hashmap, the value is a hashmap that contains
all it's neighboring countries (dest) with the weight of the edge being the distance between capitals. I made
sure to only add the vertex of the source and destination if they weren't already in the graph. The getNeighbors()
function returns a default value (empty hashmap) of a hashmap for the key/vertex.

https://www.softwaretestinghelp.com/java-graph-tutorial/
https://www.javatpoint.com/java-graph
*/
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

    /*
    I moved the findPath() function into the Graph class rather than to keep it in the IRoadTrip class to
    make it easier to make the graph. For the findPath function I implemented alot of code from the following
    sites for how to program a djikstra's algorithm. I also used comparator for the queues so that the were 
    PriorityQueus for which country should be the priority to visit first. I also created a hashSet to store which
    countries had been visited. I used the PriorityQueue to create an accurate path for what order as well as which
    countries should be traveled to between the two main countries. I also used the poll() method of queues to see which
    country I was currently looking at. By checking the visited hashSet, I made sure that I was not adding already
    visited countries to the queue. Also used several hashMaps to add onto the graph and keep track of which countries
    were in the adjacency list for the country/vertex I was currently visiting.

    https://www.geeksforgeeks.org/queue-poll-method-in-java/
    https://www.w3schools.com/java/java_hashset.asp
    https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
    https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
    https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/
    */

    public List<String> findPath(String src, String dest) {
        HashMap<String, Integer> distances = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        HashSet<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        HashMap<String, Integer> edgeDistances = new HashMap<>();

        for (String country : adjacencyList.keySet()) {
            distances.put(country, Integer.MAX_VALUE);
            prev.put(country, null);
        }

        distances.put(src, 0);
        queue.add(src);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(dest)) {
                String temp = dest;
                while (prev.get(temp) != null) {
                    path.add(0, temp);
                    temp = prev.get(temp);
                }
                path.add(0, src);
                for(int i = 0; i < path.size() - 1; i++) {
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

                    if (!visited.contains(neighborCountry)){
                        int dist = distances.get(current) + distanceToNeighbor;

                        if (dist < distances.get(neighborCountry)) {
                            distances.put(neighborCountry, dist);
                            prev.put(neighborCountry, current);
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
    private HashMap <String, HashMap<String, Integer>> borders;
    private HashMap <String, HashMap<String, Integer>> capdist;;
    private HashMap <String, String> state_name;
    private HashMap<String, String> fixedCountries;
    private Graph graph;

    /*
    I initialized all the class' hashmaps and graphs above. I also created a hashmap that was specifically
    for fixing the countries names. I got all the messed up country names from when Professor Veomett showed
    the function in class. This function basically puts in an alternative name for a country as the key of the
    hashmap, and the value is the correct name of the country.
    */
    
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
    /*
    I created a function specifically for reading the borders.txt file. I had the actually file reading in a 
    try-catch statement so that it would catch the exceptions, for example if the file did not exist. I used a
    nested HashMap. The inner hashmap had all the neighbors of the country as the keys nd the length of how much 
    heir borders were shared as the value. For the outer hashmap, the original country was the key and the inner
    hashmap was the value. I split the strings for the outer hashmap using the '=' sign, but for the inner hashmap
    I got the values by splitting the string and then parsing through each separate neighboring country string to 
    separate the characters and the digits so that the distance and the country name would be separated. I also 
    sure that the country name was changed if it was part of the countries that needed their names fixed. Then I 
    closed the file. The links below are the resources I used for this function and the rest of the functions that
    read files state_name.tsv and capdist.csv.

    https://howtodoinjava.com/java/collections/hashmap/java-nested-map/
    https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Using%20the%20Java%20BufferedRedaer%20class,a%20file%20line%20by%20line.
    https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
    https://www.baeldung.com/java-remove-punctuation-from-string
    */

    public HashMap <String, HashMap<String, Integer>> readborders(String file) throws NumberFormatException{
        HashMap <String, HashMap<String, Integer>> borders = new HashMap<>();
        try {
            File f = new File(file);
            Scanner scanner = new Scanner(f);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] arr = line.split(" = ");
                String country = arr[0].trim();
                HashMap <String, Integer> bordc = new HashMap<>();
                if (fixedCountries.containsKey(country)) {
                    country = fixedCountries.get(country);
                }
                if (arr.length > 1) {
                    String[] borderCountries = arr[1].split("; ");
                    for (String b : borderCountries) {
                        String neighbor = "";
                        String dist = "";
                        for (int i = 0; i < b.length() - 2; i++) {

                            if (Character.isDigit(b.charAt(i)) == true) {
                                dist += b.charAt(i);
                            }
                            else {
                                neighbor += b.charAt(i);
                            }
                            if (fixedCountries.containsKey(neighbor)) {
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
        catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            System.exit(0);
        }
        return borders;
    }
    /*
    The readcap function read the capdist.csv file and stored the needed contents into a nested hashmap. Similarly
    to what I did before, I split the string at the ','. The inner hashmap stored the ids of the other countries as the key 
    and the distance in capitals between the country with ida and the country with idb as the value. The outer hashmap stored 
    the country with ida as the key and the inner hashmap as the value. For this function I used the .getOrDefault() method
    in the hashmap class. It basically just returns a default value for ida which would be making a new hashmap if the ida 
    hasn't been a key prevly.  
    
    https://www.geeksforgeeks.org/hashmap-getordefaultkey-defaultvalue-method-in-java-with-examples/
    */

    public HashMap <String, HashMap<String, Integer>> readcap(String file) {
        HashMap <String, HashMap<String, Integer>> capdist = new HashMap<>();
        try {
            File f = new File(file);
            Scanner scanner = new Scanner(f);
            String line = scanner.nextLine();
            while (scanner.hasNextLine()) {
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
        catch (Exception e) {
            System.out.println("File does not exist");
            System.exit(0);
        }
        return capdist;
    }
    /*
    The readstate_name function reads the state_name.tsv file and stores the countryName and its country
    ID into a hashmap, with the country name being the key and the ID being the value. I made sure to only
    get the most recent names of the countries by checking if the latest date contained "2020" in it. I also
    checked if the country name had to be fixed if it was one of the keys in the fixedcountries() hashmap.
    */
    public HashMap <String, String> readstate_name(String file) {
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
                if (date.indexOf("2020") != -1){
                    if (fixedCountries.containsKey(country)) {
                        countryName = fixedCountries.get(country);
                    }
                    else {
                        countryName = country;
                    }
                    state_name.put(countryName, stateid);
                }
            }

            scanner.close();
        }
        catch (Exception e) {
            System.out.println("File does not exist");
            System.exit(0);
        }
        return state_name;
    }

    public IRoadTrip (String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough files");
        }
        fixedCountries = createFixedCountries();
        borders = readborders(args[0]);
        capdist = readcap(args[1]);
        state_name = readstate_name(args[2]);
        fixedCountries = createFixedCountries();

        graph = createGraph();
    }
    /*
    I created a Hashmap specifically for the distances, and made sure that they hashmap was equal to 
    the hashmap containing the country's capital and its distance between every other countries' capital.
    I made sure to get the distance between capitals by first getting the ID of the countries name using the
    state_name hashmap. I also made sure to initialize in the distances hashmap that in the country hashmap,
    the country itself will always have a distance of 0. I initialized the integer being returned "dist" as -1,
    to be set later in the function. I used poll() and PriorityQueue again to check the order in which the countries
    were being visited so that I knew which distance between which countries to return. 

    https://www.baeldung.com/java-comparator-comparable
    https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
    https://www.geeksforgeeks.org/comparator-interface-java/
    https://stackoverflow.com/questions/28521459/java-comparator-with-relative-distance
    */

    public int getDistance (String country1, String country2) {
        HashMap<String, Integer> distances = capdist.get(state_name.get(country1));
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        distances.put(state_name.get(country1), 0);
        queue.add(country1);

        int dist = -1;

        while (!queue.isEmpty()) {
            String current = queue.poll();
            dist = distances.get(state_name.get(country2));
        }
        return dist;
    }
    /*
    I created another nested hashmap for creating a graph. This time the outer hashmap was a Map,entry<>,
    so it was easier to use the ket values. I parsed through the entries of the borders hashmap and got the
    key of entry map, then added to the graph as a vertex. The inner hashmap contained the bordering country's (bc)
    values. I parsed through the the entry set of the bordering countries and set the weight as the distance that
    the neighboring countries share a border. I checked if the capdist contained the country ID of the country and
    the neighbor country, then set the weight to the distance between capitals rather than the distance of borders.
    I then added the country, neighboring country, and weight to create a weighted edge on the graph.

    https://www.geeksforgeeks.org/map-entry-interface-java-example/
    https://www.baeldung.com/java-map-entry
    */
    public Graph createGraph() {
        Graph graph = new Graph();

        for (Map.Entry<String, HashMap<String, Integer>> entry : borders.entrySet()) {
            String country = entry.getKey();
            graph.addVertex(country);

            HashMap<String, Integer> bc = entry.getValue();
            for (Map.Entry<String, Integer> neighbor : bc.entrySet()) {
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

    /*
    The acceptUserInput function that asks the user to input two countries to find the shortest path between
    them using djikstras algorithm. In this case I made sure to put the if statements into a while loop so that
    the function would keep running until the system exited because the user decided to exit the program. I also
    had this function printing the output of the program. Since the path is returned as a list, I iterated through
    the list to print the correct path with the correct formatting. I called both the getDistance function and 
    findPath() method to get the needed values such as the distance between countries in the path and the actual 
    path itself.
    */

    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
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
            List<String> path = graph.findPath(c1, c2);
            int dist = 0;
            System.out.println("Route from " + c1 + " to " + c2 + ":");
            for (int i = 0; i < path.size() - 1; i++){
                dist = getDistance(path.get(i), path.get(i + 1));
                System.out.println("* " + path.get(i) + " --> " + path.get(i + 1) + " (" + dist + " km.)");
            }
        }

        
    }
    /*
    I used my main function to create a IRoadTrip object that passes the command line arguments (file names)
    as the parameters. I also created a Graph object called graph using the IRoadTrip object. I then called the
    acceptUserInput() method so that the user could input which two countries to find the path between.
    */
    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        Graph graph = a3.createGraph();
        a3.acceptUserInput();
    }

}

