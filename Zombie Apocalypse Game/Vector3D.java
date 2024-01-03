public class Vector3D
{
    private double x;
    private double y;
    private double z;

    public Vector3D()
    {
        x = 0; 
        y = 0;
        z = 0;
    }

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3D(Vector3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
    
    public double getZ()
    {
        return z;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
    
    public void setZ(double z)
    {
        this.z = z;
    }

    public void add(Vector3D v)
    {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public static Vector3D add(Vector3D v1, Vector3D v2)
    {
        Vector3D v = new Vector3D();
        v.setX(v1.x + v2.x);
        v.setY(v1.y + v2.y);
        v.setZ(v1.z + v2.z);
        return v;
    }

    public void sub(Vector3D v)
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public static Vector3D sub(Vector3D v1, Vector3D v2)
    {
        Vector3D v = new Vector3D();
        v.setX(v1.x - v2.x);
        v.setY(v1.y - v2.y);
        v.setZ(v1.z + v2.z);
        return v;
    }

    public void mult(double n)
    {
        x *= n;
        y *= n;
        z *= n;
    }

    public static Vector3D mult(Vector3D v1, double n)
    {
        Vector3D v = new Vector3D();
        v.setX(v1.x * n);
        v.setY(v1.y * n);
        v.setZ(v1.z * n);
        return v;
    }

    public void div(double n)
    {
        x /= n;
        y /= n;
        z /= n;
    }

    public static Vector3D div(Vector3D v1, double n)
    {
        Vector3D v = new Vector3D();
        v.setX(v1.x/n);
        v.setY(v1.y/n);
        v.setZ(v1.z/n);
        return v;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize()
    {
        if(mag() > 0)
            div(mag());
    }

    public static Vector3D normalize(Vector3D v1)
    {
        Vector3D v = new Vector3D();
        if(v1.mag() > 0)
        {
            v.setX(v1.x/v1.mag());
            v.setY(v1.y/v1.mag());
            v.setZ(v1.z/v1.mag());
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

    public static Vector3D limit(Vector3D v1, double max)
    {
        Vector3D v = new Vector3D();
        if(v1.mag() >= max)
        {
            v = normalize(v1);
            v = mult(v1, max);
        }
        else
            v = v1;
        return v;
    }

    public double dist(Vector3D v)
    {
        return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2) + Math.pow(y - v.y, 2));
    }

    public boolean equals(Vector3D v)
    {
        if(x == v.x && y == v.y && z == v.z)
            return true;
        else
            return false;
    }

    public String toString()
    {
        return x + " " + y;
    }
}