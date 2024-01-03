public class Shot
{
    public Vector3D initialPos;
    public Vector3D pos;
    public Vector3D dir;
    
    public Shot(double xPos, double yPos, double zPos, double xDir, double yDir, double zDir)
    {
        initialPos = new Vector3D(xPos, yPos, zPos);
        pos = new Vector3D(xPos, yPos, zPos);
        dir = new Vector3D(xDir, yDir, zDir);
    }
}