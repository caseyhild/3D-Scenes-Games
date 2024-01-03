public class Camera
{
    public Ray ray;
    
    public Camera(double x, double y, double z)
    {
        ray = new Ray(new Vector3D(x, y, z), new Vector3D());
    }
    
    public void setDir(double x, double y, double z)
    {
        ray.dir = new Vector3D(x, y, z);
    }
}