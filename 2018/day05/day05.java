import java.io.*;

public class day05 {
    static String inputString;

    static char[] getInput(String file) {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            inputString = r.readLine().trim();
            return inputString.toCharArray();
        } catch (IOException ex) {
            return null;
        }
    }

    public static boolean isPair(char a, char b) {
        if (b > a) {
            return ((int)b - 32) == ((int)a);
        } else {
            return ((int)a - 32) == ((int)b);
        }
    }

    public static boolean doConversions(char input[]) {
        boolean found = false;
        char c1 = '-';
        char c2 = '-';
        int idx1 = 0;
        int idx2 = 0;
        int i = 0;

        while (i < input.length) {
            if (input[i] == '-') {
                i++;
                continue;
            } 

            if (c1 == '-') {
                c1 = input[i];
                idx1 = i++;
                continue;
            } else if (c2 == '-') {
                c2 = input[i];
                idx2 = i;
            }
            
            if (c1 != '-' && c2 != '-') {
                if (isPair(c1, c2)) {
                    input[idx1] = '-';
                    input[idx2] = '-';
                    found = true;
                    c1 = '-';
                    c2 = '-';               
                    i = idx2+1;
                } else {
                    c1 = c2;
                    idx1 = idx2;
                    c2 = '-';
                    i = idx2+1;
                }
            }
        }

        return found;
    }

    public static int runConversions(char input[], char rem) {
        boolean converted = false;

        if (rem != '-') {
            for (int i=0; i < input.length; i++) {
                if (input[i] == rem || input[i] == (char)((int)rem+32)) {
                    input[i] = '-';
                }
            }
        }

        do {
            converted = doConversions(input);
        } while (converted);

        int count = 0;
        for (int i=0; i < input.length; i++) {
            if (input[i] != '-') {
                count++;
            }
        }

        return count;
    }
        
    public static int findMin() {
        int min = -1;

        for (int i = 65; i <= 90; i++) {
            int v = runConversions(inputString.toCharArray(), (char)i);
            if (min == -1) {
                min = v;
            } else if (v < min) {
                min = v;
            }
        }

        return min;
    }
        
    public static String cleanString(char input[], boolean includeSpace) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < input.length; i++) {
            if (includeSpace || input[i] != '-') {
                sb.append(input[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        char[] input = getInput((args.length==0) ? "input.txt" : args[0]);

        System.out.println(runConversions(input, '-'));
        System.out.println(findMin());
    }
}
