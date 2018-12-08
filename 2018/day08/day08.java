import java.util.*;
import java.io.*;

public class day08 {
    static class IntStream {
        List<Integer> input;
        int offset;

        IntStream(List<Integer> input) {
            this.input = input;
        }

        int read() throws IOException {
            if (offset == input.size()) {
                throw new IOException("Reached end of input");
            }
            return input.get(offset++);
        }

        void reset() {
            offset = 0;
        }
    }

    static class Node {
        List<Node> children = new ArrayList<Node>();
        List<Integer> metadata = new ArrayList<Integer>();

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

    static IntStream getInput(String file) {
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
        return new IntStream(list);
    }

    static Node readTree(IntStream input) throws IOException {
        int nchild = input.read();
        int nmeta = input.read();
        Node r = new Node();

        for (int i=0; i < nchild; i++) {
            r.children.add(readTree(input));
        }

        for (int i=0; i < nmeta; i++) {
            r.metadata.add(input.read());
        }
        
        return r;
    }

    public static void main(String args[]) throws Exception {
        IntStream input = getInput((args.length==0) ? "input.txt" : args[0]);
        Node root = readTree(input);

        //root.print(0);
        System.out.println(root.sumMetadata());
        System.out.println(root.value());
    }
}
