public class Sphere extends Shape
{
    public Vector3D pos;
    public int radius;
    public Sphere()
    {
        super(0);
        pos = new Vector3D();
        radius = 0;
    }

    public Sphere(int x, int y, int z, int r, int c)
    {
        super(c);
        pos = new Vector3D(x, y, z);
        radius = r;
    }

    public double getxyDist(Vector3D v)
    {
        return Math.sqrt((v.x - pos.x) * (v.x - pos.x) + (v.y - pos.y) * (v.y - pos.y));
    }
}