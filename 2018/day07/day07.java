import java.io.*;
import java.util.*;
import java.util.regex.*;

public class day07 {
    static Pattern inputPattern = Pattern.compile("Step (.) must be finished before step (.) can begin.");
    static Map<String, Node> map = new HashMap<String, Node>();
    static List<Node> list = new ArrayList<Node>();

    static class Node implements Comparator<Node> {
        String label;
        List<String> dependencies = new ArrayList<String>();
        boolean finished;

        Node(String label) {
            this.label = label;
        }

        public int compare(Node e1, Node e2) {
            return e1.label.compareTo(e2.label);
        }

        static void addDependency(String node, String label) {
            Node n = map.get(node);
            if (n == null) {
                map.put(node, n = new Node(node));
            }
            n.dependencies.add(label);
            n = map.get(label);
            if (n == null) {
                map.put(label, new Node(label));
            }
        }

        boolean isFinished() {
            return finished;
        }

        boolean available() {
            if (finished) {
                return false;
            } else {
                for (String l : dependencies) {
                    Node n = map.get(l);

                    if (! n.isFinished()) {
                        return false;
                    }
                }

                return true;
            }
        }

        void output(StringBuilder output) {
            finished = true;
            output.append(label);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(label+":");
            for (String s : dependencies) {
                sb.append(" "+s);
            }
            return sb.toString();
        }
    }
            
    static void getInput(String file) {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                Matcher m = inputPattern.matcher(line);
                if (! m.find()) {
                    System.err.println("Bad input: "+line);
                    return;
                }

                Node.addDependency(m.group(2), m.group(1));
            }
            for (Node n : map.values()) {
                list.add(n);
            }
            list.sort(new Node("foo"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static String buildOutput() {
        StringBuilder sb = new StringBuilder();
        boolean foundAvailable;

        do {
            foundAvailable = false;

            for (Node n : list) {
                if (n.available()) {
                    n.output(sb);
                    foundAvailable = true;
                    break;
                }
            }
        } while (foundAvailable);

        return sb.toString();
    }

    public static void main(String args[]) {
        getInput((args.length==0) ? "input.txt" : args[0]);

        System.out.println(buildOutput());
    }
}
