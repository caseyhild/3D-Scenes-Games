public class Plane extends Shape
{
    private double pos;
    private String axis;

    public Plane()
    {
        super(new Vector3D(255, 255, 255));
        pos = 0;
        axis = "xy";
    }

    public Plane(double p, String a, Vector3D color)
    {
        super(new Vector3D(color));
        pos = p;
        axis = a;
    }

    public double getDist(Vector3D p)
    {
        if(axis.equals("xy"))
            return p.z - pos;
        else if(axis.equals("xz"))
            return p.y - pos;
        else if(axis.equals("yz"))
            return p.x - pos;
        else
            return 0;
    }
}