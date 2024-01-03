public class Vector
{
    private double x = 0;
    private double y = 0;
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
    
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void add(Vector v)
    {
        this.x += v.x;
        this.y += v.y;
    }

    public static Vector add(Vector v1, Vector v2)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x + v2.x;
        v.y = v1.y + v2.y;
        return v;
    }

    public void sub(Vector v)
    {
        this.x -= v.x;
        this.y -= v.y;
    }

    public static Vector sub(Vector v1, Vector v2)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x - v2.x;
        v.y = v1.y - v2.y;
        return v;
    }

    public void mult(double n)
    {
        this.x *= n;
        this.y *= n;
    }

    public static Vector mult(Vector v1, double n)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x * n;
        v.y = v1.y * n;
        return v;
    }

    public void div(double n)
    {
        this.x /= n;
        this.y /= n;
    }

    public static Vector div(Vector v1, double n)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x/n;
        v.y = v1.y/n;
        return v;
    }

    public double mag()
    {
        double mag = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        return mag;
    }

    public void normalize()
    {
        if(mag() > 0)
            this.div(this.mag());
    }

    public static Vector normalize(Vector v1)
    {
        Vector v = new Vector(0, 0);
        v.x = v1.x/v1.mag();
        v.y = v1.y/v1.mag();
        return v;
    }

    public void limit(double max)
    {
        if(this.mag() >= max)
        {
            this.normalize();
            this.mult(max);
        }
    }

    public static Vector limit(Vector v1, double max)
    {
        Vector v = new Vector(0, 0);
        if(v1.mag() >= max)
        {
            v = normalize(v1);
            v = mult(v1, max);
        }
        return v;
    }

    public double dist(Vector v)
    {
        double d = Math.abs(Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2)));
        return d;
    }
    
    public static double dist(Vector v1, Vector v2)
    {
        double d = Math.abs(Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2)));
        return d;
    }
    
    public String toString()
    {
        return x + " , " + y;
    }
}