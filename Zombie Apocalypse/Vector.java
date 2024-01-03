public class Vector
{
    private double x;
    private double y;

    public Vector()
    {
        x = 0; 
        y = 0;
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Vector(Vector v)
    {
        x = v.x;
        y = v.y;
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
        x += v.x;
        y += v.y;
    }

    public static Vector add(Vector v1, Vector v2)
    {
        Vector v = new Vector();
        v.setX(v1.x + v2.x);
        v.setY(v1.y + v2.y);
        return v;
    }

    public void sub(Vector v)
    {
        x -= v.x;
        y -= v.y;
    }

    public static Vector sub(Vector v1, Vector v2)
    {
        Vector v = new Vector();
        v.setX(v1.x - v2.x);
        v.setY(v1.y - v2.y);
        return v;
    }

    public void mult(double n)
    {
        x *= n;
        y *= n;
    }

    public static Vector mult(Vector v1, double n)
    {
        Vector v = new Vector();
        v.setX(v1.x * n);
        v.setY(v1.y * n);
        return v;
    }

    public void div(double n)
    {
        x /= n;
        y /= n;
    }

    public static Vector div(Vector v1, double n)
    {
        Vector v = new Vector();
        v.setX(v1.x/n);
        v.setY(v1.y/n);
        return v;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize()
    {
        if(mag() > 0)
            div(mag());
    }

    public static Vector normalize(Vector v1)
    {
        Vector v = new Vector();
        if(v1.mag() > 0)
        {
            v.setX(v1.x/v1.mag());
            v.setY(v1.y/v1.mag());
        }
        return v;
    }

    public void limit(double max)
    {
        if(mag() >= max)
        {
            normalize();
            mult(max);
        }
    }

    public static Vector limit(Vector v1, double max)
    {
        Vector v = new Vector();
        if(v1.mag() >= max)
        {
            v = normalize(v1);
            v = mult(v1, max);
        }
        else
            v = v1;
        return v;
    }

    public double dist(Vector v)
    {
        return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
    }

    public boolean equals(Vector v)
    {
        if(x == v.x && y == v.y)
            return true;
        else
            return false;
    }

    public String toString()
    {
        return x + " " + y;
    }
}