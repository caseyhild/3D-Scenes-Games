public class Ray
{
    public Vector3D pos;
    public Vector3D dir;
    public boolean hit;

    public Ray(Vector3D p, Vector3D d)
    {
        pos = new Vector3D(p.x, p.y, p.z);
        dir = new Vector3D(d.x, d.y, d.z);
        hit = false;
    }

    public void march(Shape[] shapes, Camera camera, int mapSize)
    {
        if(shapes.length == 0)
            return;
        double dist = shapes[0].getDist(this);
        for(int i = 1; i < shapes.length; i++)
        {
            if(shapes[i].getDist(this) < dist)
                dist = shapes[i].getDist(this);
        }
        dir.normalize();
        dir.mult(0.05);
        pos.add(Vector3D.mult(dir, Math.max(dist, 0.1)));
        if(dist <= 0)
            hit = true;
        if(pos.x <= 0.5)
        {
            pos.x = 0.5;
            hit = true;
        }
        if(pos.x >= mapSize - 0.5)
        {
            pos.x = mapSize - 0.5;
            hit = true;
        }
        if(pos.y <= 0.5)
        {
            pos.y = 0.5;
            hit = true;
        }
        if(pos.y >= mapSize - 0.5)
        {
            pos.y = mapSize - 0.5;
            hit = true;
        }
    }
}