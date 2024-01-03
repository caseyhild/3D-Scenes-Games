public class Shot
{
    public Vector initialPos;
    public Vector pos;
    public Vector dir;
    
    public Shot(double xPos, double yPos, double xDir, double yDir)
    {
        initialPos = new Vector(xPos, yPos);
        pos = new Vector(xPos, yPos);
        dir = new Vector(xDir, yDir);
    }
}