import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
public class RayCastingJumping extends JFrame implements Runnable
{  
    private static final long serialVersionUID = 1L;
    private int width = 640;
    private int height = 480;
    private int frame = 0;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private int[] pixels;
    private static int[][] map = 
        {
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
        };
    private static int[][] map2 = 
        {
            {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3}
        };
    private int mapWidth = map.length;
    private int mapHeight = map[0].length;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private ArrayList<Texture> textures;
    private Vector player = new Vector(1.5, 1.5);
    private double playerHeight = 0.5;
    private int num;
    private Vector[] position;
    private Vector[] velocity;
    private Vector[] acceleration;
    private int[] ballSize;
    private int[] color;
    private Camera camera;
    private Screen screen;
    private Texture t;
    public RayCastingJumping()
    {
        //3D Points
//        FileOutput outFile = new FileOutput("3DPoints.txt");
//        CreatePoints c = new CreatePoints(outFile);
        //initial map and location
        t = new Texture();
        camera = new Camera(player.getX(), player.getY(), 1, 0, 0, -0.66, true);//coordinates from topleft of map, facing down
        floorMap = new int[mapWidth][mapHeight];
        ceilingMap = new int[mapWidth][mapHeight];
        player = new Vector(1.5, 1.5);
        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        //list of the available textures to use
        textures = new ArrayList<Texture>();
        //texture at position 0 in arraylist is made
        //so the player can walk through it
        textures.add(Texture.fakebricks);
        textures.add(Texture.bricks);
        textures.add(Texture.gray);
        textures.add(Texture.circles);
        //makes fakebricks texture so player can walk through it
        if(textures.contains(Texture.fakebricks))
        {
            for(int i = 0; i < textures.size(); i++)
            {
                if(textures.get(i).equals(Texture.fakebricks))
                    camera.setNonSolid(i);
            }
        }
        //starting floor and ceiling locations
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                floorMap[mapX][mapY] = 3;
                ceilingMap[mapX][mapY] = 3;
            }
        }
        //for collisions texture
        // num = 6;
        // position = new Vector[num];
        // velocity = new Vector[num];
        // acceleration = new Vector[num];
        // ballSize = new int[num];
        // color = new int[num];
        // color[0] = rgbNum(255, 0, 0);
        // color[1] = rgbNum(255, 128, 0);
        // color[2] = rgbNum(255, 255, 0);
        // color[3] = rgbNum(0, 255, 0);
        // color[4] = rgbNum(0, 0, 255);
        // color[5] = rgbNum(192, 0, 192);
        // for(int i = 0; i < num; i++)
        // {
        // ballSize[i] = 6;
        // }
        // for(int i = 0; i < num; i++)
        // {
        // position[i] = new Vector(Math.random() * 64, Math.random() * 64);
        // velocity[i] = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
        // velocity[i].normalize();
        // acceleration[i] = new Vector(0, 0);
        // }
        //recognizes when key is pressed
        addKeyListener(camera);
        //send info to screen class to be drawn
        screen = new Screen(map, map2, floorMap, ceilingMap, mapWidth, mapHeight, textures, width, height);
        //setting up the window
        setSize(width, height);
        setResizable(false);
        setTitle("Ray Casting Jumping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
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
        //updates everything
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running)
        {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while(delta >= 1)//Make sure update is only happening 60 times a second
            {
                frame++;
                player = new Vector(camera.xPos, camera.yPos);
                //updates textures
                t.update("circles", frame);
                textures.clear();
                //texture at position 0 in arraylist is made
                //so the player can walk through it
                textures.add(Texture.fakebricks);
                textures.add(Texture.bricks);
                textures.add(Texture.gray);
                textures.add(Texture.circles);
                //updates screen and camera
                camera.update(map, map2);
                playerHeight = camera.playerHeight;
                screen.updateGame(camera, pixels, map, map2, floorMap, ceilingMap, playerHeight);
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }
    
    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        int rgb = r * 65536 + g * 256 + b;
        return rgb;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        int r = color/65536;
        return r;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        color -= color/65536 * 65536;
        int g = color/256;
        return g;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        color -= color/65536 * 65536;
        color -= color/256 * 256;
        int b = color;
        return b;
    }

    public static void main(String [] args)
    {
        RayCastingJumping r = new RayCastingJumping();
    }
}
