import java.util.*;
import java.io.*;

public class day08 {
    static class Node {
        List<Node> children = new ArrayList<Node>();
        List<Integer> metadata = new ArrayList<Integer>();
        int start;

        int sumMetadata() {
            int sum = 0;
            for (Integer m : metadata) {
                sum += m;
            }
            for (Node n : children) {
                sum += n.sumMetadata();
            }

            return sum;
        }

        int value() {
            if (children.size() == 0) {
                return sumMetadata();
            } else {
                int v = 0;
                for (Integer m : metadata) {
                    if (m == 0 || m > children.size()) {
                        continue;
                    }

                    v += children.get(m - 1).value();
                }

                return v;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Node:");
            for (Integer m : metadata) {
                sb.append(" "+m);
            }
            return sb.toString();
        }

        void print(int spaces) {
            for (int i =0; i < spaces; i++) {
                System.out.print(" ");
            }
            System.out.println(this);
            for (Node n : children) {
                n.print(spaces+5);
            }
        }
    }

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

    static Node readTree(List<Integer> input, int start) {
        int nchild = input.get(start);
        int nmeta = input.get(start+1);
        Node r = new Node();
        Node child = null;

        r.start = start + 2;
        for (int i=0; i < nchild; i++) {
            r.children.add(child = readTree(input, (i==0) ? r.start : child.start));
        }

        for (int i=0; i < nmeta; i++) {
            r.metadata.add(input.get((child == null) ? r.start+i : child.start+i));
        }
        
        r.start = (child == null) ? r.start+nmeta : child.start + nmeta;

        return r;
    }

    public static void main(String args[]) {
        List<Integer> input = getInput((args.length==0) ? "input.txt" : args[0]);
        List<Integer> metadata = new ArrayList<Integer>();
        Node root = readTree(input, 0);

        //root.print(0);
        System.out.println(root.sumMetadata());
        System.out.println(root.value());
    }
}
