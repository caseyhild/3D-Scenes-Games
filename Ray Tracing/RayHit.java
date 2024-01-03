public class RayHit
{
    public Vector3D pos;
    public double dist;
    public Vector3D normal;
    public Shape shape;
    public RayHit()
    {
        pos = new Vector3D();
        dist = Double.MAX_VALUE;
        normal = new Vector3D();
        shape = null;
    }
}