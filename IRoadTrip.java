import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class IRoadTrip {
    //https://howtodoinjava.com/java/collections/hashmap/java-nested-map/
    private Map <String, Map<String, Integer>> borders;
    private Map <String, Map<String, Integer>> capdist;;
    private Map <String, String> state_name;

    private Map<String, Map<String, Integer>> readborders(String file){
        Map <String, Map<String, Integer>> borders = new HashMap<>();
        //https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Using%20the%20Java%20BufferedRedaer%20class,a%20file%20line%20by%20line.
        //https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        try {
            Scanner scanner = new Scanner(new File(args[0]));

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] arr = line.split("=");
                String country = arr[0].trim();
                String[] borderlen = arr[1].split(":");
                Map <String, Integer> bordc = new HashMap<>();
                for (String b : borderlen) {
                    String[] borderarr = b.trim().split("\\s+");
                    String cname = borderarr[0];
                    int len = Integer.parseInt(borderarr[1].replaceAll("[^0-9]", ""));
                    bordc.put(cname, len);
                }
                borders.put(country, bordc);
            }
            scanner.close();
        }
        catch (Exception e){
            System.out.print("");
        }
        return borders;
    }
    private Map<String, Map<String, Integer>> readcap(String file){
        Map <String, Map<String, Integer>> capdist = new HashMap<>();
        //https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Using%20the%20Java%20BufferedRedaer%20class,a%20file%20line%20by%20line.
        //https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
        try {
            Scanner scanner = new Scanner(new File(args[0]));

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] arr = line.split("=");
                String country = arr[0].trim();
                String[] borderlen = arr[1].split(":");
                Map <String, Integer> bordc = new HashMap<>();
                for (String b : borderlen) {
                    String[] borderarr = b.trim().split("\\s+");
                    String cname = borderarr[0];
                    int len = Integer.parseInt(borderarr[1].replaceAll("[^0-9]", ""));
                    bordc.put(cname, len);
                }
                borders.put(country, bordc);
            }
            scanner.close();
        }
        catch (Exception e){
            System.out.print("");
        }
    }

    public IRoadTrip (String [] args) {
        // Replace with your code
        if (args.length < 3){
            System.out.println("Not enough files");
        }
        readborders(args[0]);


    }


    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
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

        findPath(c1,c2);
        getDistance(c1, c2);
        scanner.close();
        acceptUserInput();
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

