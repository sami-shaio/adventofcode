import java.io.*;
import java.util.*;

public class day02 {
    static List<String> getInput(String file) {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            ArrayList<String> input = new ArrayList<String>();
            String line;
            while ((line = r.readLine()) != null) {
                input.add(line);
            }
            return input;
        } catch (IOException ex) {
            return null;
        }
    }

    public static Map<Character, Integer> letterCount(String s) {
        HashMap<Character, Integer> m = new HashMap<Character, Integer>();
        int l = s.length();
        for (int i=0; i < l; i++) {
            Character c = s.charAt(i);
            Integer count = m.get(c);
            if (count == null) {
                m.put(c, 1);
            } else {
                m.put(c, count+1);
            }
        }
        
        return m;
    }

    public static int calculateChecksum(List<String> input) {
        int containsTwo = 0;
        int containsThree = 0;

        for (String s : input) {
            Map<Character, Integer> counts = letterCount(s);
            
            boolean foundTwo = false;
            boolean foundThree = false;

            for (Integer count : counts.values()) {
                switch (count) {
                    case 2:
                        if (!foundTwo) {
                            containsTwo++;
                            foundTwo = true;
                        }
                        break;
                    case 3:
                        if (!foundThree) {
                            containsThree++;
                            foundThree = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return containsTwo * containsThree;
    }

    public static List<String> getVariations(String s) {
        List<String> list = new ArrayList<String>();
        int l = s.length();

        for (int i=0; i < l; i++) {
            if (i==0) {
                list.add("_"+s.substring(1));
            } else if (i == l-1) {
                list.add(s.substring(0, l-1)+"_");
            } else {
                list.add(s.substring(0, i)+"_"+s.substring(i+1));
            }
        }

        return list;
    }

    /**
     * For each string in input store into a map all the variations of 
     * the string with "_" replacing each letter and increment the count 
     * at that key.
     * For example for "fgij" store "_gij", "f_ij", "fg_j","fgi_" into the
     * map incrementing the count at each.
     * When all strings have been processed the entries that have count==2
     * will be the correct boxes and removing "_" from the key will yield
     * the common letters between them.
     */
    public static String getCommonLetters(List<String> input) {
        Map<String, Integer> similarMap = new HashMap<String, Integer>();
        
        for (String s : input) {
            List<String> keys = getVariations(s);
            for (String k : keys) {
                Integer c = similarMap.get(k);
                if (c == null) {
                    similarMap.put(k, 1);
                } else {
                    similarMap.put(k, c+1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : similarMap.entrySet()) {
            if (entry.getValue() == 2) {
                return entry.getKey().replace("_","");
            }
        }
        
        return null;
    }

    public static void main(String args[]) {
        List<String> input = getInput((args.length==0) ? "input.txt" : args[0]);

        System.out.println(calculateChecksum(input));
        System.out.println(getCommonLetters(input));
    }
}

