//Using the Graham Scan Algorithm to compute the convex hull of a set of points in the 2-D plane
//importing necessary util packages for the program to run
import java.util.*;

//declaring a class to store and implement required functionality for points in 2-D space
class Point2D implements Comparable<Point2D>
{
    //declaring several comparators capable of sorting 2-D points according to x-coordinate, y-coordinate, r value, polar coordinates, angle and distance
    public static final Comparator<Point2D> X_ORDER = new XOrder();
    public static final Comparator<Point2D> Y_ORDER = new YOrder();
    public static final Comparator<Point2D> R_ORDER = new ROrder();
    public final Comparator<Point2D> POLAR_ORDER = new PolarOrder();
    public final Comparator<Point2D> ATAN2_ORDER = new Atan2Order();
    public final Comparator<Point2D> DISTANCE_TO_ORDER = new DistanceToOrder();

    //declaring variables to store x and y coordinates of points
    private final double x;
    private final double y;

    //declaring a constructor to build 2-D points
    public Point2D(double x, double y)
    {
        if (Double.isInfinite(x) || Double.isInfinite(y))
            throw new IllegalArgumentException("Coordinates must be finite");
        if (Double.isNaN(x) || Double.isNaN(y))
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        if (x == 0.0)
            x = 0.0; // convert -0.0 to +0.0
        if (y == 0.0)
            y = 0.0; // convert -0.0 to +0.0
        this.x = x;
        this.y = y;
    }

    //declaring functions to compute and return x-coordinate, y-coordinate, r-value and angle from x-axis
    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }

    public double r()
    {
        return Math.sqrt(x * x + y * y);
    }

    public double theta()
    {
        return Math.atan2(y, x);
    }

    //declaring functions to compute angle between two points
    private double angleTo(Point2D other)
    {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.atan2(dy, dx);
    }

    //declaring function to determine the collinearity of points
    public static int ccw(Point2D a, Point2D b, Point2D c)
    {
        double area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (area2 < 0)
            return -1;
        else if (area2 > 0)
            return +1;
        else
            return 0;
    }

    //declaring a function to compute the area of region between points A, B and C
    public static double area2(Point2D a, Point2D b, Point2D c)
    {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    //declaring a function to compute distance between points
    public double distanceTo(Point2D other)
    {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //declaring a function to compute squared distance between two points
    public double distanceSquaredTo(Point2D other)
    {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    //declaring a function to compare two 2-D points, based on y-coordinate value first and then x-coordinate value
    public int compareTo(Point2D other)
    {
        if (this.y < other.y)
            return -1;
        if (this.y > other.y)
            return +1;
        if (this.x < other.x)
            return -1;
        if (this.x > other.x)
            return +1;
        return 0;
    }

    //implementing comparators to order points based on x-coordinate, y-coordinate, r-value, angle polar coordinates and distance
    private static class XOrder implements Comparator<Point2D>
    {
        public int compare(Point2D p, Point2D q)
        {
            if (p.x < q.x)
                return -1;
            if (p.x > q.x)
                return +1;
            return 0;
        }
    }

    private static class YOrder implements Comparator<Point2D>
    {
        public int compare(Point2D p, Point2D q)
        {
            if (p.y < q.y)
                return -1;
            if (p.y > q.y)
                return +1;
            return 0;
        }
    }

    private static class ROrder implements Comparator<Point2D>
    {
        public int compare(Point2D p, Point2D q)
        {
            double delta = (p.x * p.x + p.y * p.y) - (q.x * q.x + q.y * q.y);
            if (delta < 0)
                return -1;
            if (delta > 0)
                return +1;
            return 0;
        }
    }

    private class Atan2Order implements Comparator<Point2D>
    {
        public int compare(Point2D q1, Point2D q2)
        {
            double angle1 = angleTo(q1);
            double angle2 = angleTo(q2);
            if (angle1 < angle2)
                return -1;
            else if (angle1 > angle2)
                return +1;
            else
                return 0;
        }
    }

    private class PolarOrder implements Comparator<Point2D>
    {
        public int compare(Point2D q1, Point2D q2)
        {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;

            if (dy1 >= 0 && dy2 < 0)
                return -1; // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0)
                return +1; // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0)
            { // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0)
                    return -1;
                else if (dx2 >= 0 && dx1 < 0)
                    return +1;
                else
                    return 0;
            } else
                return -ccw(Point2D.this, q1, q2); // both above or below
        }
    }

    private class DistanceToOrder implements Comparator<Point2D>
    {
        public int compare(Point2D p, Point2D q)
        {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if (dist1 < dist2)
                return -1;
            else if (dist1 > dist2)
                return +1;
            else
                return 0;
        }
    }

    //declaring an equals function for 2-D points
    public boolean equals(Object other)
    {
        if (other == this)
            return true;
        if (other == null)
            return false;
        if (other.getClass() != this.getClass())
            return false;
        Point2D that = (Point2D) other;
        return this.x == that.x && this.y == that.y;
    }

    //declaring a toString function for 2-D points
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

}

//this is the main class that implements the Graham Scan algorithm
public class GrahamScan
{
    //creating a stack of 2-D points to store the convex hull
    private Stack<Point2D> hull = new Stack<Point2D>();

    //creating a function to perform Graham Scan on a list of 2-D points
    public GrahamScan(Point2D[] pts)
    {

        //creating a copy of the points array
        int N = pts.length;
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++)
            points[i] = pts[i];

        //sorting this copy of the points array based on y-coordinate first, and then x-coordinate (if y-coordinates are same for both points)
        Arrays.sort(points);

        //sorting the copy of the points array now based on polar-coordinates
        Arrays.sort(points, 1, N, points[0].POLAR_ORDER);

        //since p[0] is the first extreme point, push it on the hull stack
        hull.push(points[0]);

        //corner case checking: ensure that all points are not equal. If all points are equal, return
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1]))
                break;
        if (k1 == N)
            return;

        //use the collinearity function to choose a point which is not collinear with the first hull point, and the first distinct point apart from the hull point
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (Point2D.ccw(points[0], points[k1], points[k2]) != 0)
                break;

        //point k2-1 is the second extreme point in this case, so push it to the hull
        hull.push(points[k2 - 1]);

        //while checking collinearity at each step, expand the convex hull
        for (int i = k2; i < N; i++)
        {
            Point2D top = hull.pop();
            while (Point2D.ccw(hull.peek(), top, points[i]) <= 0)
            {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points[i]);
        }

        //make sure the hull is convex by asserting the isConvex function
        assert isConvex();
    }

    //declare a function to return an iterable object containing all the points chosen to be on the convex hull
    public Iterable<Point2D> hull()
    {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : hull)
            s.push(p);
        return s;
    }

    //declare a function to check the convexity of the created hull
    private boolean isConvex()
    {
        int N = hull.size();
        if (N <= 2)
            return true;

        Point2D[] points = new Point2D[N];
        int n = 0;
        for (Point2D p : hull())
        {
            points[n++] = p;
        }

        for (int i = 0; i < N; i++)
        {
            if (Point2D.ccw(points[i], points[(i + 1) % N], points[(i + 2) % N]) <= 0)
            {
                return false;
            }
        }
        return true;
    }

    //declare a main function to test the Graham Scan algorithm
    public static void main(String[] args)
    {
        //declare a scanner to read from the standard input
        Scanner sc = new Scanner(System.in);

        //read in the number of 2-D points that the user will be entering and create an array for points
        System.out.println("Enter the number of points: ");
        int N = sc.nextInt();
        Point2D[] points = new Point2D[N];

        //also create a HashMap which can store the indices of the points as values with the points themselves being keys
        HashMap<Point2D,Integer> pointIndices = new HashMap<Point2D, Integer>();
        for (int i = 0; i < N; i++)
        {
            System.out.println("Enter the x and y coordinates: <x> <y>");
            int x = sc.nextInt();
            int y = sc.nextInt();
            //read in one point at a time and add to the array and also add it to the hashmap along with its index
            points[i] = new Point2D(x, y);
            pointIndices.put(points[i],new Integer(i));
        }

        //call graham scan on the points array
        GrahamScan gs = new GrahamScan(points);

        //print out the number of sides in the convex hull, which is equivalent to the hull size
        if(gs.hull.size()>2) {
            System.out.println(gs.hull.size());
        }
        else{
            System.out.println(1);
        }
        //create an iterator over the convex hull and print out all the sides
        //each side consists of two consecutive points in the hull stack
        //one more side consists of the first and last points in the hull stack
        Iterator<Point2D> hullIt = gs.hull().iterator();
        Point2D previous = null;
        Point2D first = null;
        Point2D current = null;

        while(hullIt.hasNext()){
            current = hullIt.next();
            if(previous!=null){
                System.out.println(pointIndices.get(previous)+" "+pointIndices.get(current));
            }
            else{
                first = current;
            }
            previous = current;
        }
        if(gs.hull.size()>2) {
            System.out.println(pointIndices.get(first) + " " + pointIndices.get(current));
        }
        //close the scanner
        sc.close();
    }

}