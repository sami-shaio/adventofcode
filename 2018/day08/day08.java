import java.util.*;
import java.io.*;

public class day08 {
    static List<Integer> getInput(String file) {
        List<Integer> list = new ArrayList<Integer>();

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                String p[] = line.split(" ");
                if (p != null) {
                    int i = 0;
                    for (String s : p) {
                        list.add(Integer.parseInt(p[i++]));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // return offset into next node, accumulata metadata in metadata
    static int sumMetadata(List<Integer> input, int start, List<Integer> metadata) {
        int nchild = input.get(start);
        int nmeta = input.get(start+1);

        start += 2;

        for (int i=0; i < nchild; i++) {
            start = sumMetadata(input, start, metadata);
        }

        for (int i=0; i < nmeta; i++) {
            metadata.add(input.get(start+i));
        }

        return start + nmeta;
    }

    public static void main(String args[]) {
        List<Integer> input = getInput((args.length==0) ? "input.txt" : args[0]);
        List<Integer> metadata = new ArrayList<Integer>();

        sumMetadata(input, 0, metadata);
        int sum = 0;
        for (Integer m : metadata) {
            sum += m;
        }
        System.out.println(sum);
    }
}
