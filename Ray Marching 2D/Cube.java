public class Cube extends Shape
{
    private Vector3D size;

    public Cube()
    {
        pos = new Vector3D();
        size = new Vector3D();
    }

    public Cube(double x, double y, double z, double xs, double ys, double zs)
    {
        pos = new Vector3D(x, y, z);
        size = new Vector3D(xs, ys, zs);
    }

    public double getDist(Ray ray)
    {
        Vector3D dist = Vector3D.abs(Vector3D.sub(ray.pos, pos));
        dist.sub(size);
        if(dist.x < 0)
            dist.x = 0;
        if(dist.y < 0)
            dist.y = 0;
        if(dist.z < 0)
            dist.z = 0;
        return dist.mag();
    }
}