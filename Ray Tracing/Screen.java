public class Screen
{
    private int width;
    private int height;
    private Sphere[] spheres;
    private Plane floor;
    private Light light;
    public Screen(int w, int h, Sphere[] s, Plane f, Light l)
    {
        width = w;
        height = h;
        spheres = s;
        floor = f;
        light = l;
    }

    public void update(Camera camera, int[] pixels)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                Vector3D dir = new Vector3D(2.0 * x/width - 1, 2.0 * (height - y)/height - 1, 1);
                dir.normalize();
                dir.z = -dir.z;
                Ray ray = new Ray(camera.pos, dir, new Vector3D(1, 1, 1));
                RayHit bestHit = ray.trace(floor, spheres);
                if(bestHit.shape != null)
                {
                    int c = bestHit.shape.color;
                    if(bestHit.shape instanceof Sphere)
                    {
                        int c1 = rgbNum((int) (bestHit.normal.x * 128 + 128), (int) (bestHit.normal.y * 128 + 128), (int) (bestHit.normal.z * 128 + 128));
                        c = rgbNum((3 * getR(c) + getR(c1))/4, (3 * getG(c) + getG(c1))/4, (3 * getB(c) + getB(c1))/4);
                    }
                    ray = new Ray(bestHit.pos, Vector3D.normalize(Vector3D.sub(light.pos, bestHit.pos)), new Vector3D());
                    ray.dir.z = -ray.dir.z;
                    ray.pos.add(Vector3D.mult(ray.dir, 0.001));
                    Shape s = bestHit.shape;
                    bestHit = ray.trace(floor, spheres);
                    if(s instanceof Sphere)
                    {
                        double dist = Math.sqrt((light.pos.x - ray.pos.x) * (light.pos.x - ray.pos.x) + (light.pos.y - ray.pos.y) * (light.pos.y - ray.pos.y)) - ((Sphere) s).getxyDist(light.pos);
                        double shade = 1 - (dist + ((Sphere) s).radius)/(2 * ((Sphere) s).radius);
                        c = rgbNum((int) (shade * getR(c)), (int) (shade * getG(c)), (int) (shade * getB(c)));
                    }
                    if(bestHit.shape instanceof Sphere && s instanceof Plane)
                        c = rgbNum(0, 0, 0);
                    // if(s instanceof Plane)
                    // {
                    // c = rgbNum((int) (bestHit.normal.x * 128 + 128), (int) (bestHit.normal.y * 128 + 128), (int) (bestHit.normal.z * 128 + 128));
                    // Vector3D result = new Vector3D();
                    // for (int i = 0; i < 2; i++)
                    // {
                    // RayHit hit = ray.trace(floor, spheres);
                    // Vector3D specular = new Vector3D(0.6, 0.6, 0.6);
                    // if(hit.shape != null)// && !(hit.shape instanceof Plane))
                    // {
                    // ray.pos = Vector3D.add(hit.pos, Vector3D.mult(hit.normal, 0.001));
                    // ray.dir = hit.normal;
                    // ray.energy = new Vector3D(ray.energy.x * specular.x, ray.energy.y * specular.y, ray.energy.z * specular.z);
                    // result.add(new Vector3D(ray.energy.x * getR(hit.shape.color), ray.energy.y * getG(hit.shape.color), ray.energy.z * getB(hit.shape.color)));
                    // result.add(new Vector3D(ray.energy.x * (int) (hit.normal.x * 128 + 128), ray.energy.y * (int) (hit.normal.y * 128 + 128), ray.energy.z * (int) (hit.normal.z * 128 + 128)));
                    // }
                    // else
                    // {
                    // //ray.energy = new Vector3D(ray.energy.x * specular.x, ray.energy.y * specular.y, ray.energy.z * specular.z);
                    // result.add(new Vector3D(ray.energy.x * 64, ray.energy.y * 64, ray.energy.z * 64));
                    // break;
                    // }
                    // }
                    // c = rgbNum((int) result.x, (int) result.y, (int) result.z);
                    // }
                    pixels[y * width + x] = c;
                }
                else
                    pixels[y * width + x] = rgbNum(128, 128, 128);
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