public class Camera
{
    public Vector3D pos;
    public Vector3D dir;
    public int FOV;
    
    public Camera(int xPos, int yPos, int zPos, int xDir, int yDir, int zDir, int fov)
    {
        pos = new Vector3D(xPos, yPos, zPos);
        dir = new Vector3D(xDir, yDir, zDir);
        FOV = fov;
    }
}