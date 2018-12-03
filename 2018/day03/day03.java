import java.io.*;
import java.util.*;

public class day03 {
    static List<List<Integer>> fabric = new ArrayList<List<Integer>>();

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

    static int[] parseClaim(String claim) {
        int idx = claim.indexOf('@');
        int idx2 = claim.indexOf(':');

        String pair1 = claim.substring(idx+1, idx2).trim();
        String pair2 = claim.substring(idx2+1).trim();

        idx = pair1.indexOf(',');

        int x = Integer.parseInt(pair1.substring(0, idx));
        int y = Integer.parseInt(pair1.substring(idx+1));

        idx = pair2.indexOf('x');

        int w = Integer.parseInt(pair2.substring(0, idx));
        int h = Integer.parseInt(pair2.substring(idx+1));

        int v[] = new int[5];
        v[0] = x;
        v[1] = y;
        v[2] = w;
        v[3] = h;
        v[4] = Integer.parseInt(claim.substring(1, claim.indexOf('@')).trim());

        return v;
    }

    static void addClaim(String claim) {
        int[] v = parseClaim(claim);

        int x = v[0];
        int y = v[1];
        int w = v[2];
        int h = v[3];

        for (int i = 0; i <= x+w; i++) {
            if (fabric.size() < i) {
                fabric.add(new ArrayList<Integer>());
            }
        }

        for (int i = x; i < x+w; i++) {
            List<Integer> col = fabric.get(i);
            for (int j = 0; j <= y+h; j++) {
                if (col.size() < j) {
                    col.add(0);
                }
            }
            for (int j = y; j < y+h; j++) {
                Integer c = col.get(j);
                col.set(j, c+1);
            }
        }
    }

    static int calculateOverlap() {
        int overlap = 0;

        for (List<Integer> col : fabric) {
            for (Integer c : col) {
                if (c > 1) {
                    overlap++;
                }
            }
        }

        return overlap;
    }

    static int calculateFreeClaim(List<String> input) {
        for (String s : input) {
            int claim[] = parseClaim(s);

            boolean isFree = true;
            for (int i=claim[0]; i < claim[0]+claim[2] && isFree; i++) {
                List<Integer> col = fabric.get(i);
                for (int j = claim[1]; j < claim[1]+claim[3] && isFree; j++) {
                    Integer c = col.get(j);
                    if (c != 1) {
                        isFree = false;
                    }
                }
            }
            if (isFree) {
                return claim[4];
            }
        }

        return -1;
    }

    public static void main(String args[]) {
        List<String> input = getInput((args.length==0) ? "input.txt" : args[0]);

        for (String s : input) {
            addClaim(s);
        }
        
        System.out.println(calculateOverlap());
        System.out.println(calculateFreeClaim(input));
    }
}
