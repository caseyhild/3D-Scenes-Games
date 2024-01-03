public class Sphere extends Shape
{
    public Vector3D pos;
    private double radius;

    public Sphere()
    {
        super(new Vector3D(255, 255, 255));
        pos = new Vector3D();
        radius = 0;
    }

    public Sphere(double x, double y, double z, double r, Vector3D color)
    {
        super(new Vector3D(color));
        pos = new Vector3D(x, y, z);
        radius = r;
    }

    public double getDist(Vector3D p)
    {
        return Vector3D.sub(p, pos).mag() - radius;
    }
}