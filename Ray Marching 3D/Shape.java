public abstract class Shape
{
    public Vector3D color;
    
    public Shape(Vector3D color)
    {
        this.color = color;
    }
    
    abstract double getDist(Vector3D p);
}