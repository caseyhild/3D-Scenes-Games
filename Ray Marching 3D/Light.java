import java.util.*;
public class Light
{
    public Vector3D pos;

    public Light()
    {
        pos = new Vector3D();
    }

    public Light(double x, double y, double z)
    {
        pos = new Vector3D(x, y, z);
    }

    public double getLight(Vector3D p, ArrayList<Shape> shapes)
    {
        Vector3D l = Vector3D.normalize(Vector3D.sub(pos, p));
        Ray ray = new Ray();
        Vector3D n = ray.getNormal(p, shapes);
        double dif = Math.max(0, Math.min(Vector3D.dotProduct(n, l), 1));
        double d = new Ray(Vector3D.add(p, Vector3D.mult(n, 3 * ray.SURFACE_DIST)), l).march(shapes);
        if(d < Vector3D.sub(pos, p).mag())
            dif *= 0.1;
        return dif;
    }
}