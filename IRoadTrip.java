import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class IRoadTrip {
    //https://howtodoinjava.com/java/collections/hashmap/java-nested-map/
    private HashMap <String, HashMap<String, Integer>> borders;
    private HashMap <String, HashMap<String, Integer>> capdist;;
    private HashMap <String, String> state_name;

    public HashMap <String, HashMap<String, Integer>> readborders(String file) throws NumberFormatException{
        HashMap <String, HashMap<String, Integer>> borders = new HashMap<>();
        HashMap <String, Integer> bordc = new HashMap<>();
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
                if (arr.length > 1){
                    String[] borderlen = arr[1].split("; ");
                    for (String b : borderlen) {
                        String cname = "";
                        String len = "";
                        for (int i = 0; i < b.length() - 2; i++){
                            //System.out.println(b + b.length());
                            //System.out.println(b.charAt(i));
                            if (Character.isDigit(b.charAt(i)) == true){
                                len += b.charAt(i);
                            }
                            else{
                                cname += b.charAt(i);
                            }
                            //System.out.println(cname +": "+ dist);

                        }
                        String c = cname.trim().replaceAll("[^\\sa-zA-Z0-9]","").trim();
                        bordc.put(c, Integer.parseInt(len));
                        //System.out.println(c +" "+ len);
                    }
                }
                borders.put(country, bordc);
            }
            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File does not exist");
            e.printStackTrace();
        }
        return borders;
    }
    public HashMap <String, HashMap<String, Integer>> readcap(String file){
        HashMap <String, HashMap<String, Integer>> capdist = new HashMap<>();
        HashMap <String, Integer> kmdistbtwcap = new HashMap<>();
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
                String idb = arr[3].trim();
                int kmdist = Integer.parseInt(arr[4]);
                //https://www.geeksforgeeks.org/hashmap-containskey-method-in-java/
                if (capdist.containsKey(ida)){
                    kmdistbtwcap.put(idb, kmdist);
                }
                else{
                   kmdistbtwcap.put(idb, kmdist);
                }
                capdist.put(ida, kmdistbtwcap);
                //System.out.println(kmdist);
                //System.out.println(ida+": "+kmdistbtwcap);
                //System.out.println(kmdistbtwcap.size());
            }
            //System.out.println(capdist.size());
            scanner.close();
        }
        catch (Exception e){
            System.out.println("File does not exist");
            e.printStackTrace();
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
                String cid = arr[2].trim();
                state_name.put(stateid, cid);
                //System.out.println(stateid+": "+cid);
            }

            scanner.close();
        }
        catch (Exception e){
            System.out.println("File does not exist");
            e.printStackTrace();
        }
        return state_name;
    }

    public IRoadTrip (String[] args) {
        if (args.length < 3){
            System.out.println("Not enough files");
        }
        borders = readborders(args[0]);
        System.out.println(borders);
        capdist = readcap(args[1]);
        System.out.println(capdist);
        state_name = readstate_name(args[2]);
        System.out.println(state_name);

    }


    public int getDistance (String country1, String country2) {
        if (borders == null || capdist == null || state_name == null) {
            System.out.println("HashMaps are null");
            return -1;
        }
        List<String> path = findPath(country1, country2);
        int totalDistance = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            String current = path.get(i);
            String next = path.get(i + 1);
            Map<String, Integer> neighbordists = borders.get(current);

            if (neighbordists.containsKey(next)) {
                totalDistance += neighbordists.get(next);
            } else {
                return -1;
            }
        }
        return totalDistance;
    }


    public List<String> findPath (String country1, String country2) {
        if (!borders.containsKey(country1) || !borders.containsKey(country2)) {
            return new ArrayList<>(); 
        }
        HashMap<String, Integer> dists = new HashMap<>();
        HashMap<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dists::get));

        for (String country : borders.keySet()) {
            dists.put(country, country.equals(country1) ? 0 : Integer.MAX_VALUE);
            previous.put(country, null);
            pq.offer(country);
        }

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(country2)) {
                break;
            }

            HashMap<String, Integer> neighbors = borders.get(current);
            for (String neighbor : neighbors.keySet()) {
                int newDistance = dists.get(current) + neighbors.get(neighbor);
                if (newDistance < dists.get(neighbor)) {
                    dists.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    pq.offer(neighbor);
                }
            }
        }
        List<String> shortestPath = new ArrayList<>();
        String current = country2;
        while (current != null) {
            shortestPath.add(0, current);
            current = previous.get(current);
        }

        return shortestPath.size() > 1 ? shortestPath : new ArrayList<>();
    }


    public void acceptUserInput() {
        // Replace with your code
        //System.out.println("IRoadTrip - skeleton");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the first country (type EXIT to quit): ");
        String c1 = scanner.nextLine();

        if (c1.equals("EXIT")){
            System.exit(0);
        }
        System.out.println("Enter the name of the second country (type EXIT to quit): ");
        String c2 = scanner.nextLine();

        //findPath(c1,c2);
        //getDistance(c1, c2);
        System.out.println(findPath(c1,c2) + ": " + getDistance(c1,c2));
        acceptUserInput();
        scanner.close();
        
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }

}

