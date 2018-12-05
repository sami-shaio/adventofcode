import java.io.*;
import java.util.*;
import java.util.regex.*;

// dumbest brute force algorithm. but it works
public class day04 {
    static Pattern inputPattern = Pattern.compile("\\[(\\d+)-(\\d+)-(\\d+) (\\d\\d):(\\d\\d)\\] (.*)");

    static class LogEntry implements Comparator<LogEntry> {
        int hour;
        int minute;
        String key;
        String text;
        int id;
        boolean asleep;

        LogEntry(String year, String month, String day, String hour, String minute, String text) {
            this.hour = Integer.parseInt(hour);
            this.minute = Integer.parseInt(minute);
            this.key = year+"."+month+"."+day+"."+hour+"."+minute;
            setText(text);
        }

        public int compare(LogEntry e1, LogEntry e2) {
            return e1.key.compareTo(e2.key);
        }

        void setText(String text) {
            int idx = text.indexOf('#');
            if (idx != -1) {
                id = Integer.parseInt(text.substring(idx+1, text.indexOf(' ', idx+1)));
            } else if (text.startsWith("falls")) {
                asleep = true;
            }
            this.text = text;
        }
    }
            
    static List<LogEntry> getInput(String file) {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            ArrayList<LogEntry> input = new ArrayList<LogEntry>();
            String line;
            while ((line = r.readLine()) != null) {
                LogEntry e = parseInput(line);
                if (e != null) {
                    input.add(e);
                }
            }
            input.sort(new LogEntry("", "", "", "0", "0", ""));
            return input;
        } catch (IOException ex) {
            return null;
        }
    }

    public static int findSleepy(List<LogEntry> input) {
        Map<Integer, Integer> counters = new HashMap<Integer, Integer>();
        
        int currentId = -1;
        int sleepStart = 0;

        for (LogEntry e : input) {
            if (e.id > 0) {
                currentId = e.id;
            } else if (e.asleep) {
                sleepStart = e.minute;
            } else {
                int sleepMinutes = e.minute - sleepStart;

                Integer c = counters.get(currentId);
                if (c == null) {
                    counters.put(currentId, sleepMinutes);
                } else {
                    counters.put(currentId, c + sleepMinutes);
                }
            }
        }

        int max = 0;
        int id = 0;

        for (Map.Entry<Integer, Integer> entry : counters.entrySet()) {
            if (entry.getValue() > max) {
                id = entry.getKey();
                max = entry.getValue();
            }
        }

        System.out.println("sleepy: " + id+" "+max);
        return id;
    }

    public static int[] findMinute(int id, List<LogEntry> input) {
        int minutes[] = new int[60];

        int currentId = -1;
        int sleepStart = 0;

        for (LogEntry e : input) {
            if (e.id > 0) {
                currentId = e.id;
                continue;
            }
            if (currentId != id) {
                continue;
            }
            if (e.asleep) {
                sleepStart = e.minute;
                minutes[sleepStart]++;
                System.out.println("start "+sleepStart);
            } else if (sleepStart > 0) {
                System.out.println("end "+e.minute);
                for (int i = sleepStart+1; i < e.minute; i++) {
                    minutes[i]++;
                }
            }
        }
        
        int max = 0;
        int minute = 0;
        
        for (int i = 0; i < minutes.length; i++) {
            System.out.println(i+": "+minutes[i]);
            if (minutes[i] > max) {
                max = minutes[i];
                minute = i;
            }
        }

        int r[] = new int[2];
        r[0] = minute;
        r[1] = max;

        return r;
    }

    public static int[] findStrategy2(List<LogEntry> input) {
        int r[] = new int[2];
        
        int id = 0;
        int max = 0;
        int minute = 0;

        for (LogEntry e : input) {
            if (e.id > 0) {
                int er[] = findMinute(e.id, input);
                if (er[1] > max) {
                    id = e.id;
                    max = er[1];
                    minute = er[0];
                }
            }
        }

        r[0] = id;
        r[1] = minute;

        return r;
    }

    public static LogEntry parseInput(String line) {
        Matcher m = inputPattern.matcher(line);
        if (! m.find()) {
            System.err.println("Bad input: "+line);
            return null;
        }

        return new LogEntry(m.group(1), m.group(2),m.group(3),m.group(4),m.group(5),m.group(6));
    }

    public static void main(String args[]) {
        List<LogEntry> input = getInput((args.length==0) ? "input.txt" : args[0]);

        for (LogEntry e : input) {
            System.out.println(e.key+" "+e.id+" "+e.asleep+" "+e.text);
        }

        int guard = findSleepy(input);
        int r[] = findMinute(guard, input);
        int r2[] = findStrategy2(input);

        System.out.println("guard: "+guard+" minute: "+r[0]+" "+(guard * r[0]));
        System.out.println("stategy2: "+r2[0]+" "+r2[1]+" "+(r2[0]*r2[1]));
    }
}
