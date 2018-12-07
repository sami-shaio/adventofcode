import java.io.*;
import java.util.*;
import java.math.*;

public class day06 {
    static final boolean DEBUG = false;

    static class Point {
        static final int NOID = Integer.MAX_VALUE;
        int x, y;
        Point neighbor;
        int id;
        boolean visited;
        
        Point(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        int distance(Point b) {
            int d = Math.abs((x - b.x)) + Math.abs((y - b.y));
            return d;
        }
    }

    static List<Point> getInput(String file) {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            List<Point> list = new ArrayList<Point>();
            String line;
            int id = 0;
            while ((line = r.readLine()) != null) {
                String pair[] = line.split(",");
                list.add(new Point(++id * -1, Integer.parseInt(pair[0].trim()), Integer.parseInt(pair[1].trim())));
            }

            return list;
        } catch (IOException ex) {
            return null;
        }
    }

    static Point[][] buildMatrix(List<Point> input) {
        int maxx = 0;
        int maxy = 0;

        for (Point p : input) {
            if (p.x > maxx) {
                maxx = p.x;
            }
            if (p.y > maxy) {
                maxy = p.y;
            }
        }

        Point[][] matrix = new Point[maxx+1][maxy+1];

        Map<Integer, Integer> distances = new HashMap<Integer, Integer>();

        for (int r=0; r < matrix[0].length; r++) {
            for (int c=0; c < matrix.length; c++) {
                Point np = new Point(0, c, r);
                matrix[c][r] = np;
                int min = Integer.MAX_VALUE;
                for (Point point : input) {
                    if (point.x == c && point.y == r) {
                        matrix[c][r] = np = point;
                        break;
                    }
                            
                    int d = np.distance(point);
                    Integer count = distances.get(d);
                    if (count == null) {
                        distances.put(d, 1);
                    } else {
                        distances.put(d, count+1);
                    }
                    if (d < min) {
                        min = d;
                        np.id = point.id * -1;
                    }
                }
                if (np.id > 0 && distances.get(min) > 1) {
                    np.id = Point.NOID;
                }
                distances.clear();
            }
        }

        return matrix;
    }

    public static int calculateArea(int id, Point[][] matrix) {
        int count = 0;
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (Math.abs(id) == Math.abs(matrix[r][c].id)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static Set<Integer> calculateInfinite(Point[][] matrix) {
        Set<Integer> infinite = new HashSet<Integer>();

        for (int c = 0; c < matrix.length; c++) {
            if (matrix[c][0].id < Point.NOID) {
                infinite.add(matrix[c][0].id);
            } else if (matrix[c][matrix[0].length - 1].id < Point.NOID) {
                infinite.add(matrix[c][matrix[0].length - 1].id);
            }
        }

        for (int c = 0; c < matrix[0].length; c++) {
            if (matrix[0][c].id < Point.NOID) {
                infinite.add(Math.abs(matrix[0][c].id));
            } else if (matrix[matrix.length - 1][c].id < Point.NOID) {
                infinite.add(Math.abs(matrix[matrix.length - 1][c].id));
            }
        }
        
        return infinite;
    }

    static int calculateMaxArea(List<Point> points, Point[][] matrix) {
        Set<Integer> infinite = calculateInfinite(matrix);
        int max = 0;

        for (Point p : points) {
            if (infinite.contains(p.id * -1)) {
                continue;
            }
            int a = calculateArea(p.id, matrix);
            if (a > max) {
                max = a;
            }
        }
        
        return max;
    }

    public static void main(String args[]) {
        List<Point> points = getInput((args.length==0) ? "input.txt" : args[0]);
        Point[][] matrix = buildMatrix(points);

        if (DEBUG) {
            System.out.println(matrix.length+"x"+matrix[0].length);
            for (int r=0; r < matrix[0].length; r++) {
                for (int c=0; c < matrix.length; c++) {
                    Point p = matrix[c][r];
                    if (p.id == Point.NOID) {
                        System.out.print("   . ");
                    } else {
                        System.out.print(String.format("%4d ",p.id));
                    }
                }
                System.out.println("");
            }
        }

        System.out.println("max area: "+ calculateMaxArea(points, matrix));
    }
}

    
