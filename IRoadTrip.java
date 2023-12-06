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
    public Map <String, Map<String, Integer>> borders;
    public Map <String, Map<String, Integer>> capdist;;
    public Map <String, String> state_name;

    public static Map<String, Map<String, Integer>> readborders(String file) throws NumberFormatException{
        Map <String, Map<String, Integer>> borders = new HashMap<>();
        Map <String, Integer> bordc = new HashMap<>();
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
    public static Map<String, Map<String, Integer>> readcap(String file){
        Map <String, Map<String, Integer>> capdist = new HashMap<>();
        Map <String, Integer> kmdistbtwcap = new HashMap<>();
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
                   capdist.put(ida, kmdistbtwcap);
                }
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
    public static Map<String, String> readstate_name(String file){
        Map <String, String> state_name = new HashMap<>();
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

    public IRoadTrip (String [] args) {
        // Replace with your code

    }


    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public static void acceptUserInput() {
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
        scanner.close();
        acceptUserInput();
    }


    public static void main(String[] args) {
        //IRoadTrip a3 = new IRoadTrip(args);
        if (args.length < 3){
            System.out.println("Not enough files");
        }
        readborders(args[0]);
        readcap(args[1]);
        readstate_name(args[2]);

        //acceptUserInput();
    }

}

