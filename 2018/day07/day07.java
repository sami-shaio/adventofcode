import java.io.*;
import java.util.*;
import java.util.regex.*;

public class day07 {
    static Pattern inputPattern = Pattern.compile("Step (.) must be finished before step (.) can begin.");
    static Map<String, Node> map = new HashMap<String, Node>();
    static List<Node> list = new ArrayList<Node>();
    static List<Worker> workers = new ArrayList<Worker>();
    static int timeDelay = 60;

    static class Worker implements Comparator<Worker> {
        Node task;
        int startTime;

        Worker() {
        }

        public int compare(Worker e1, Worker e2) {
            if (e1.task == null) {
                return -1;
            } else if (e2.task == null) {
                return 1;
            } else {
                return e1.task.label.compareTo(e2.task.label);
            }
        }

        void assign(Node task, int time) {
            this.task = task;
            this.startTime = time;
            task.assigned = true;
        }

        boolean isDone(int time, StringBuilder result) {
            if (task == null) {
                return false;
            }

            if ((startTime + task.duration()) == time) {
                task.output(result);
                task = null;
                startTime = 0;
                return true;
            } else {
                return false;
            }
        }

        boolean isAssigned() {
            return task != null;
        }
    }

    static class Node implements Comparator<Node> {
        String label;
        List<String> dependencies = new ArrayList<String>();
        boolean finished;
        boolean assigned;

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

        int duration() {
            return (label.charAt(0) - 'A') + 1 + timeDelay;
        }

        boolean isFinished() {
            return finished;
        }

        boolean available() {
            if (finished || assigned) {
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
        
        boolean inProgress() {
            return assigned;
        }

        void output(StringBuilder output) {
            if (! finished) {
                finished = true;
                output.append(label);
            }
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

    static int scheduledOutput(int nworkers, int delay) {
        StringBuilder result = new StringBuilder();

        timeDelay = delay;
        for (int i=0; i < nworkers; i++) {
            workers.add(new Worker());
        }

        boolean workLeft;
        int time = 0;

        do {
            workLeft = false;

            int assignedWorkers = 0;

            for (Worker w : workers) {
                if (w.isAssigned() && !w.isDone(time, result)) {
                    workLeft = true;
                    assignedWorkers++;
                }
            }

            workers.sort(workers.get(0));

            for (Node n : list) {
                if (assignedWorkers == workers.size()) {
                    break;
                }

                if (!n.inProgress() && n.available()) {
                    workLeft = true;
                    boolean assignedWorker = false;
                    for (Worker w : workers) {
                        if (! w.isAssigned()) {
                            w.assign(n, time);
                            assignedWorkers++;
                            assignedWorker = true;
                            break;
                        }
                    }
                    if (assignedWorker) {
                        workers.sort(workers.get(0));                   
                    }
                }
            }
            time++;
        } while (workLeft);

        return time - 1;
    }

    public static void main(String args[]) {
        getInput((args.length==0) ? "input.txt" : args[0]);

        System.out.println(buildOutput());

        for (Node l : list) {
            l.finished = false;
        }

        System.out.println(scheduledOutput(((args.length > 1) ? Integer.parseInt(args[1]) : 5), 
                                           ((args.length >= 2) ? Integer.parseInt(args[2]) : 60)));
    }
}
