package closestpoints;

import java.util.*;

public class ClosestPoints {

    static class Point {
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{x=" + x + ", y=" + y + "}";
        }
    }


    private static double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    private static double bruteForce(Point[] p, int begin, int end) {
        double min = Double.MAX_VALUE;
        for (int i = begin; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                min = Math.min(min, distance(p[i], p[j]));
            }
        }
        return min;
    }

    // A utility function to find the distance between the closest points of
    // strip of given size. All points in strip are sorted according to
    // y coordinate. They all have an upper bound on minimum distance as d.
    // Note that this method seems to be a O(n^2) method, but it's a O(n)
    // method as the inner loop runs at most 6 times
    private static double stripClosest(List<Point> strip, double min)
    {
        strip.sort(Comparator.comparingDouble(o -> o.y));

        // Pick all points one by one and try the next points till the difference
        // between y coordinates is smaller than d.
        // This is a proven fact that this loop runs at most 6 times
        // See here: https://www.cs.mcgill.ca/~cs251/ClosestPair/ClosestPairDQ.html
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < min; j++) {
                min = Math.min(min, distance(strip.get(i), strip.get(j)));
            }
        }

        return min;
    }

    private static double closestInternal(Point[] p, int begin, int end) {
        int n = end - begin;

        // If there are 2 or 3 points, then use brute force
        if (n <= 3) {
            return bruteForce(p, begin, end);
        }

        // Find the middle point
        int mid = begin + n / 2;
        Point midPoint = p[mid];

        // Consider the vertical line passing through the middle point
        // calculate the smallest distance dl on left of middle point and
        // dr on right side
        double dl = closestInternal(p, begin, mid + 1);
        double dr = closestInternal(p, mid + 1, end);

        // Find the smaller of two distances
        double d = Math.min(dl, dr);

        // Build a list strip that contains points close (closer than d)
        // to the line passing through the middle point
        List<Point> strip = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            if (Math.abs(p[i].x - midPoint.x) < d) {
                strip.add(p[i]);
            }
        }

        // Find the closest points in strip. Return the minimum of d and closest
        // distance in strip
        return Math.min(d, stripClosest(strip, d));
    }

    private static double closest(Point[] p) {
        Arrays.sort(p, Comparator.comparingDouble(o -> o.x));
        return closestInternal(p, 0, p.length);
    }

    public static void main(String[] args) {
        /*Point[] points = {
                new Point(2, 3),
                new Point(12, 30),
                new Point(40, 50),
                new Point(5, 1),
                new Point(12, 10),
                new Point(3, 4),
        };*/

        Point[] points = {
                new Point(0, 0),
                new Point(1, 0),
                new Point(3, 0),
                new Point(3.5, 0),
                new Point(5, 0),
        };

        System.out.println("The smallest distance is " + closest(points));
    }
}
