public class Cube extends Shape
{
    public Vector3D pos;
    private Vector3D size;

    public Cube()
    {
        super(new Vector3D(255, 255, 255));
        pos = new Vector3D();
        size = new Vector3D();
    }

    public Cube(double x, double y, double z, double xs, double ys, double zs, Vector3D color)
    {
        super(new Vector3D(color));
        pos = new Vector3D(x, y, z);
        size = new Vector3D(xs, ys, zs);
    }

    public double getDist(Vector3D p)
    {
        Vector3D v = Vector3D.sub(Vector3D.abs(Vector3D.sub(p, pos)), size);
        return Vector3D.max(v, new Vector3D()).mag() + Math.min(Math.max(v.x, Math.max(v.y, v.z)), 0);
    }
}