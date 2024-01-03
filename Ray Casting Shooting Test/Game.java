import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
public class Game extends JFrame implements Runnable
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
    private int mapWidth = map.length;
    private int mapHeight = map[0].length;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private ArrayList<Texture> textures;
    private ArrayList<Sprite> sprites;
    private ArrayList<Enemy> enemy;
    private boolean enemyDead;
    private boolean playerDead;
    private Vector player;
    private Camera camera;
    private Screen screen;
    public Game()
    {
        //initial map and location
        camera = new Camera(1.5, 1.5, 1, 0, 0, -0.66, false);//coordinates from topleft of map, facing down
        floorMap = new int[mapWidth][mapHeight];
        ceilingMap = new int[mapWidth][mapHeight];
        //list of all sprites
        sprites = new ArrayList<Sprite>();
        enemy = new ArrayList<Enemy>();
        player = new Vector(1.5, 1.5);
        playerDead = false;
        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        //list of the available textures to use
        textures = new ArrayList<Texture>();
        textures.add(Texture.fakebricks);
        textures.add(Texture.bricks);
        textures.add(Texture.gray);
        textures.add(Texture.black);
        //starting floor, ceiling, and finish location
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                floorMap[mapX][mapY] = 3;
                ceilingMap[mapX][mapY] = 4;
            }
        }
        //enemy starting locations
        enemy.add(new Enemy(mapWidth - 1.5, mapHeight - 1.5, Texture.ball));
        enemyDead = false;
        //recognizes when key is pressed
        addKeyListener(camera);
        //send info to screen class to be drawn
        screen = new Screen(map, floorMap, ceilingMap, mapWidth, mapHeight, textures, width, height);
        screen.setEnemyPos(enemy.get(0).xPos, enemy.get(0).yPos);
        //setting up the window
        setSize(width, height);
        setResizable(false);
        setTitle("Game");
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
        if(playerDead)
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    pixels[y * width + x] = rgbNum(255, 0, 0);
                }
            }
        }
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        if(playerDead)
        {
            g.setFont(new Font("Verdana", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("You Died", width/2 - fm.stringWidth("You Died")/2, height/2 - fm.getAscent()/2);
        }
        else
        {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(width/2 - 2, height/2 - 11, 4, 8);
            g.fillRect(width/2 - 2, height/2 + 3, 4, 8);
            g.fillRect(width/2 - 11, height/2 - 2, 8, 4);
            g.fillRect(width/2 + 3, height/2 - 2, 8, 4);
        }
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
                if(enemy.size() > 0)
                {
                    screen.setEnemyPos(enemy.get(0).xPos, enemy.get(0).yPos);
                    enemyDead = screen.isDead();
                }
                if(!enemyDead)
                {
                    Vector dir = Vector.sub(new Vector(enemy.get(0).xPos, enemy.get(0).yPos), new Vector(player.getX(), player.getY()));
                    if(dir.mag() < 0.5)
                    {
                        //System.out.println("You died");
                        camera.setPos(1.5, 1.5);
                        camera.setDir(1, 0);
                        camera.setPlane(0, -0.66);
                        screen.setDead(true);
                        playerDead = true;
                    }
                    dir.normalize();
                    dir.mult(0.05);
                    if(map[(int) (enemy.get(0).xPos - dir.getX())][(int) (enemy.get(0).yPos - dir.getX())] == 0)
                    {
                        enemy.get(0).setX(enemy.get(0).xPos - dir.getX());
                        enemy.get(0).setY(enemy.get(0).yPos - dir.getY());
                    }
                }
                else if(enemy.size() > 0)
                {
                    double randomX = 0;
                    double randomY = 0;
                    do
                    {
                        randomX = Math.random() * 11 + 2;
                        randomY = Math.random() * 11 + 2;
                    }while(Math.sqrt((randomX - player.getX()) * (randomX - player.getX()) + (randomY - player.getY()) * (randomY - player.getY())) < 5);
                    enemy.get(0).setX(randomX);
                    enemy.get(0).setY(randomY);
                    screen.setDead(false);
                }
                //creates all the sprites
                sprites.clear();
                for(int i = 0; i < enemy.size(); i++)
                {
                    sprites.add(new Sprite(enemy.get(i).xPos, enemy.get(i).yPos, enemy.get(i).texture));
                }
                screen.setSprites(sprites);
                //handles all of the logic restricted time
                camera.update(map);
                screen.updateGame(camera, pixels, map, floorMap, ceilingMap);
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
        Game g = new Game();
    }
}