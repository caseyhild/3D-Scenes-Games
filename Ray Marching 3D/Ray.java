import java.util.*;
public class Ray
{
    public Vector3D pos;
    public Vector3D dir;
    public Shape shapeHit;
    public final int MAX_STEPS = 100;
    public final double SURFACE_DIST = 0.01;
    public final double MAX_DIST = 350;

    public Ray()
    {
        pos = new Vector3D();
        dir = new Vector3D();
        shapeHit = null;
    }

    public Ray(Vector3D p, Vector3D d)
    {
        pos = new Vector3D(p);
        dir = new Vector3D(d);
        dir.normalize();
        shapeHit = null;
    }

    public double march(ArrayList<Shape> shapes)
    {
        double distFromOrigin = 0;
        for(int i = 0; i < MAX_STEPS; i++)
        {
            Vector3D p = Vector3D.add(pos, Vector3D.mult(dir, distFromOrigin));
            double distToScene = getDist(p, shapes);
            distFromOrigin += distToScene;
            if(distToScene < SURFACE_DIST || distFromOrigin > MAX_DIST)
                break;
        }
        return distFromOrigin;
    }

    public double getDist(Vector3D p, ArrayList<Shape> shapes)
    {
        if(shapes.isEmpty())
            return MAX_DIST;
        double dist = shapes.get(0).getDist(p);
        shapeHit = shapes.get(0);
        for(int i = 1; i < shapes.size(); i++)
        {
            if(shapes.get(i).getDist(p) < dist)
            {
                dist = Math.min(dist, shapes.get(i).getDist(p));
                shapeHit = shapes.get(i);
            }
        }
        return dist;
    }

    public double smoothMin(double a, double b, double k)
    {
        double h = Math.max(0, Math.min(0.5 + 0.5 * (b - a)/k, 1));
        return mix(b, a, h) - k * h * (1 - h);
    }

    public double mix(double x, double y, double a)
    {
        return x * (1 - a) + y * a;
    }

    public Vector3D getNormal(Vector3D p, ArrayList<Shape> shapes)
    {
        double d = getDist(p, shapes);
        Vector3D n = new Vector3D(
                d - getDist(Vector3D.sub(p, new Vector3D(0.01, 0, 0)), shapes),
                d - getDist(Vector3D.sub(p, new Vector3D(0, 0.01, 0)), shapes),
                d - getDist(Vector3D.sub(p, new Vector3D(0, 0, 0.01)), shapes));
        return Vector3D.normalize(n);
    }
}