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
    private HashMap<String, String> fixedCountries;
    
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
                //String countryName = country;
                if (fixedCountries.containsKey(country)){
                    country = fixedCountries.get(country);
                }
                if (arr.length > 1){
                    String[] borderCountries = arr[1].split("; ");
                    for (String b : borderCountries) {
                        String neighbor = "";
                        String dist = "";
                        for(int i = 0; i < b.length() - 2; i++){
                            //System.out.println(b + b.length());
                            //System.out.println(b.charAt(i));
                            if (Character.isDigit(b.charAt(i)) == true){
                                dist += b.charAt(i);
                            }
                            else{
                                neighbor += b.charAt(i);
                            }

                            if (fixedCountries.containsKey(neighbor)){
                                neighbor = fixedCountries.get(neighbor);
                                //System.out.println(neighbor);
                            }

                        }
                        String c = neighbor.trim().replaceAll("[^\\sa-zA-Z0-9]","").trim();
                        bordc.put(c, Integer.parseInt(dist));

                        //System.out.println(c +" "+ dist);
                    }
                }
                borders.put(country, bordc);
                //System.out.println(borders.get("Yemen"));
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
                //System.out.println(ida + " --> " + idb +"   "+ kmdist);
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
                String country = arr[2].trim();
                String date = arr[4].trim();
                //System.out.println(country);
                String countryName = country;
                if(date.indexOf("2020") != -1){
                    if (fixedCountries.containsKey(country)){
                        countryName = fixedCountries.get(country);
                        //System.out.println(countryName);
                    }
                    state_name.put(stateid, country);
                    //System.out.println(stateid+": "+country);
                }
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
        fixedCountries = createFixedCountries();
        //System.out.println(fixedCountries);
        borders = readborders(args[0]);
        //System.out.println(borders);
        capdist = readcap(args[1]);
        //System.out.println(capdist);
        state_name = readstate_name(args[2]);
        //System.out.println(state_name);
        fixedCountries = createFixedCountries();
        //System.out.println(fixedCountries);

    }


    public int getDistance (String country1, String country2) {
        HashMap<String, Integer> bordersCountry1 = borders.get(country1);
        System.out.println(bordersCountry1);

        return capdist.get(state_name.get(country1)).get(state_name.get(country2));
    }


    public List<String> findPath (String country1, String country2) {
    List<String> path = new ArrayList<>();
    //System.out.println(borders.get(country1));

    HashMap<String, Integer> bordersCountry1 = borders.get(country1);

    if (!bordersCountry1.containsKey(country2)) {
        return path;
    }
    PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(border -> bordersCountry1.get(border)));
    HashMap<String, Integer> distances = new HashMap<>();
    HashMap<String, String> previous = new HashMap<>();

    for (String border : borders.keySet()) {
        if (border.equals(country1)) {
            distances.put(border, 0);
        } 
        else {
            distances.put(border, Integer.MAX_VALUE);
        }
        queue.add(border);
    }

    while (!queue.isEmpty()) {
        String currentCountry = queue.poll();

        if (currentCountry.equals(country2)) {
            // Reconstruct path
            while (previous.containsKey(currentCountry)) {
                path.add(0, currentCountry + " --> " + previous.get(currentCountry) +
                        " (" + borders.get(currentCountry).get(previous.get(currentCountry)) + " km)");
                currentCountry = previous.get(currentCountry);
            }
            path.add(0, country1);
            break;
        }

        for (String neighbor : borders.get(currentCountry).keySet()) {
            int alt = distances.get(currentCountry) + borders.get(currentCountry).get(neighbor);
            if (alt < distances.get(neighbor)) {
                distances.put(neighbor, alt);
                previous.put(neighbor, currentCountry);
                queue.remove(neighbor);
                queue.add(neighbor);
            }
        }
    }

    return path;
    }


    public void acceptUserInput() {
        // Replace with your code
        //System.out.println("IRoadTrip - skeleton");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the first country (type EXIT to quit): ");
        String c1 = scanner.nextLine().trim();
        if (c1.equals("EXIT")){
            System.exit(0);
        }
        else if (!borders.containsKey(c1)) {
            System.out.println("Invalid country name. Please enter a valid country name.");
            acceptUserInput();
        }

        System.out.print("Enter the name of the second country (type EXIT to quit): ");
        String c2 = scanner.nextLine().trim();

        if (c2.equals("EXIT")){
            System.exit(0);
        }
        else if (!borders.containsKey(c2)) {
            System.out.println("Invalid country name. Please enter a valid country name.");
            acceptUserInput();
        }


        //findPath(c1,c2);
        //getDistance(c1, c2);
        System.out.println(findPath(c1,c2) + ": (" + getDistance(c1,c2) + ")");
        acceptUserInput();
        scanner.close();
        
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }

}

