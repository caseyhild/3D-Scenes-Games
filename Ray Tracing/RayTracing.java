import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
public class RayTracing extends JFrame implements Runnable
{  
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private int frame;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private int[] pixels;
    private Screen screen;
    private Camera camera;
    private Sphere[] spheres;
    private Plane floor;
    private Light light;

    public RayTracing()
    {
        //set size of screen
        width = 600;
        height = 600;
        //set starting frame
        frame = 0;
        //setting up the screen
        pixels = new int[width * height];
        spheres = new Sphere[3];
        spheres[0] = new Sphere(0, 3, 6, 1, rgbNum(255, 0, 0));
        spheres[1] = new Sphere(4, 4, 8, 2, rgbNum(0, 255, 0));
        spheres[2] = new Sphere(-2, 6, 12, 3, rgbNum(0, 0, 255));
        floor = new Plane(0, rgbNum(192, 192, 192));
        light = new Light(1000, 1000, 0);
        //fix this
        camera = new Camera(0, 3, 0);
        screen = new Screen(width, height, spheres, floor, light);
        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        //setting up the window
        setSize(width, height);
        setResizable(false);
        setTitle("Ray Tracing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.gray);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    private synchronized void start()
    {
        //starts game
        running = true;
        thread.start();
    }

    private synchronized void stop()
    {
        //stops game
        running = false;
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void render()
    {
        //draws the window
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        bs.show();
    }

    public void run()
    {
        //main game loop
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running)
        {
            //updates time
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            frame++;
            while(delta >= 1)//Make sure update is only happening 60 times a second
            {
                //updates game
                screen.update(camera, pixels);
                delta--;
            }
            render();//displays to the screen unrestricted time
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

    public static void main(String [] args)
    {
        RayTracing r = new RayTracing();
    }
}
