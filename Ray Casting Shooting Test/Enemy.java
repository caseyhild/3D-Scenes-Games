public class Enemy
{
    public double xPos;
    public double yPos;
    public Texture texture;
    public Enemy(double x, double y, Texture tex)
    {
        //creates an enemy with coordinates x and y and a texture tex
        xPos = x;
        yPos = y;
        texture = tex;
    }
    
    public void setX(double x)
    {
        //sets x position
        xPos = x;
    }
    
    public void setY(double y)
    {
        //sets y position
        yPos = y;
    }
}