import java.awt.image.*;
import java.util.*;
public class Screen
{
    private int width;
    private int height;
    private Camera camera;

    public Screen(Camera cam, int w, int h)
    {
        camera = cam;
        width = w;
        height = h;
    }

    public void update(int frame, ArrayList<Shape> shapes, Light light, BufferedImage bufferedImage)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                Vector2D uv = new Vector2D(x, height - 1 - y);
                uv.sub(new Vector2D(width/2, height/2));
                uv.div(height);
                camera.setDir(uv.x, uv.y, 1);
                camera.ray.dir.rotateX3D(30, new Vector3D());
                camera.ray.dir.rotateY3D(45, new Vector3D());
                int max_bounces = 2;
                int bounces = 0;
                double energy = 1;
                Vector3D color = new Vector3D();
                Vector3D cameraPos = new Vector3D(camera.ray.pos);
                boolean planeHitFirst = false;
                for(int i = 0; i <= max_bounces; i++)
                {
                    bounces = i;
                    double d = camera.ray.march(shapes);
                    Vector3D p = Vector3D.add(camera.ray.pos, Vector3D.mult(camera.ray.dir, d));
                    Vector3D n = camera.ray.getNormal(p, shapes);
                    double dif = light.getLight(p, shapes);
                    int r = Math.max(0, Math.min((int) (dif * camera.ray.shapeHit.color.x), 255));
                    int g = Math.max(0, Math.min((int) (dif * camera.ray.shapeHit.color.y), 255));
                    int b = Math.max(0, Math.min((int) (dif * camera.ray.shapeHit.color.z), 255));
                    if(d >= camera.ray.MAX_DIST)
                    {
                        color.add(new Vector3D(energy * 128, energy * 192, energy * 255));
                        break;
                    }
                    else
                    {
                        color.add(new Vector3D(energy * r, energy * g, energy * b));
                        if(camera.ray.shapeHit instanceof Plane && i == 0)
                        {
                            planeHitFirst = true;
                            break;
                        }
                    }
                    energy *= 0.5;
                    camera.ray.pos = Vector3D.add(p, Vector3D.mult(n, 3 * camera.ray.SURFACE_DIST));
                    camera.ray.dir = new Vector3D(n);
                }
                if(planeHitFirst)
                {
                    color.add(new Vector3D(energy * 128, energy * 192, energy * 255));
                    bounces++;
                }
                camera.ray.pos = new Vector3D(cameraPos);
                color.div(bounces + 1);
                bufferedImage.setRGB(x, y, rgbNum((int) color.x, (int) color.y, (int) color.z));
            }
        }
    }

    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        return r * 65536 + g * 256 + b;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        return color/65536;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        return color % 65536/256;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        return color % 65536 % 256;
    }
}