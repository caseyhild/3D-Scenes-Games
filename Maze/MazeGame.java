import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
public class MazeGame extends JFrame implements Runnable
{  
    private static final long serialVersionUID = 1L;
    private int level = 1;
    private int mapWidth = 2 * level + 3;
    private int mapHeight = 2 * level + 3;
    private int finishX = 0;
    private int finishY = 0;
    private boolean showMinimap = false;
    private int minimapType = 1;
    private int[][] minimap;
    private int timer = 1;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private int[] pixels;
    private int[][] map;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private ArrayList<Texture> textures;
    private Vector player;
    private Camera camera;
    private Screen screen;
    public MazeGame()
    {
        //initial map and location
        camera = new Camera(1.5, 1.5, 1, 0, 0, -0.66);//coordinates from topleft of map, facing down
        Maze m = new Maze();
        m.setLevel(level);
        m.newMaze();
        mapWidth = m.getWidth();
        mapHeight = m.getHeight();
        map = new int[mapWidth][mapHeight];
        map = m.getMaze();
        camera.setPos(1.5, 1.5);
        double turnLeft = 0;
        if(map[1][2] == 0)
            turnLeft = Math.PI/2.0;
        camera.turnLeft(turnLeft);
        floorMap = new int[mapWidth][mapHeight];
        ceilingMap = new int[mapWidth][mapHeight];
        player = new Vector(1.5, 1.5);
        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        //list of the available textures to use
        textures = new ArrayList<Texture>();
        textures.add(Texture.fakebricks);
        textures.add(Texture.bricks);
        textures.add(Texture.grass);
        textures.add(Texture.black);
        textures.add(Texture.coolpattern);
        //starting floor, ceiling, and finish location
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                floorMap[mapX][mapY] = 3;
                ceilingMap[mapX][mapY] = 4;
                if(map[mapX][mapY] == -1)
                {
                    floorMap[mapX][mapY] = 5;
                    finishX = mapX;
                    finishY = mapY;
                    map[mapX][mapY] = 0;
                }
            }
        }
        //recognizes when key is pressed
        addKeyListener(camera);
        //send info to screen class to be drawn
        screen = new Screen(map, floorMap, ceilingMap, mapWidth, mapHeight, textures, 640, 480);
        screen.setLevel(level);
        //setting minimap
        minimap = new int[mapWidth][mapHeight];
        if(showMinimap == true)
        {
            screen.setMinimap(minimapType, minimap);
        }
        //setting up the window
        setSize(640, 480);
        setResizable(false);
        setTitle("Maze Game");
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
        //draws the text at beginning of each level
        g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 20)); 
        FontMetrics f = g.getFontMetrics();
        g.setColor(new Color(255, 255, 255));
        g.drawString("Level " + level, 10, 10 + 25 + f.getAscent()/2);
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
                player = new Vector(camera.xPos, camera.yPos);
                Maze m = new Maze();
                if(m.getWidth() != mapWidth || m.getHeight() != mapHeight)
                {
                    resetMinimap(2 * level + 3, 2 * level + 3);
                    screen.resetMinimap(2 * level + 3, 2 * level + 3);
                }
                mapWidth = m.getWidth();
                mapHeight = m.getHeight();
                //checks if in finish square
                if((int) camera.xPos == finishX && (int) camera.yPos == finishY)
                {
                    //resets floor of finish square
                    floorMap[finishX][finishY] = 3;
                    //goes to next level
                    level++;
                    screen.setLevel(level);
                    m.setLevel(level);
                    timer = 1;
                    //creates a new maze
                    m.newMaze();
                    mapWidth = m.getWidth();
                    mapHeight = m.getHeight();
                    map = new int[mapWidth][mapHeight];
                    floorMap = new int[mapWidth][mapHeight];
                    ceilingMap = new int[mapWidth][mapHeight];
                    map = m.getMaze();
                    //resets all floor and ceiling
                    for(int mapX = 0; mapX < mapWidth; mapX++)
                    {
                        for(int mapY = 0; mapY < mapHeight; mapY++)
                        {
                            floorMap[mapX][mapY] = 3;
                            ceilingMap[mapX][mapY] = 4;
                        }
                    }
                    //sets new finish square 
                    for(int mapX = 0; mapX < mapWidth; mapX++)
                    {
                        for(int mapY = 0; mapY < mapHeight; mapY++)
                        {
                            if(map[mapX][mapY] == -1)
                            {
                                floorMap[mapX][mapY] = 5;
                                finishX = mapX;
                                finishY = mapY;
                                map[mapX][mapY] = 0;
                            }
                        }
                    }
                    //resets player position and direction facing
                    camera.setPos(1.5, 1.5);
                    camera.setDir(1, 0);
                    camera.setPlane(0, -0.66);
                    camera.resetTurn();
                    double turnLeft = 0;
                    if(map[1][2] == 0)
                        turnLeft = Math.PI/2.0;
                    camera.turnLeft(turnLeft);
                }
                else if(showMinimap == true)
                {
                    for(int mapX = 0; mapX < minimap[0].length; mapX++)
                    {
                        for(int mapY = 0; mapY < minimap.length; mapY++)
                        {
                            if(minimap[mapX][mapY] == 2)
                                minimap[mapX][mapY] = 1;
                            if((int) camera.xPos == mapX && (int) camera.yPos == mapY)
                                minimap[mapX][mapY] = 2;
                            if(Math.abs((int) camera.xPos - mapX) == 1 && Math.abs((int) camera.yPos - mapY) == 1 && minimap[mapX][mapY] == 1 && minimap[mapX + 1][mapY] == 1)
                                minimap[(int) camera.xPos][mapY] = 1;
                            if(Math.abs((int) camera.xPos - mapX) == 1 && Math.abs((int) camera.yPos - mapY) == 1 && minimap[mapX][mapY] == 1 && minimap[mapX][mapY + 1] == 1)
                                minimap[mapX][(int) camera.yPos] = 1;
                            if(Math.abs((int) camera.xPos - mapX) == 1 && Math.abs((int) camera.yPos - mapY) == 1 && minimap[mapX][mapY] == 1 && minimap[mapX - 1][mapY] == 1)
                                minimap[(int) camera.xPos][mapY] = 1;
                            if(Math.abs((int) camera.xPos - mapX) == 1 && Math.abs((int) camera.yPos - mapY) == 1 && minimap[mapX][mapY] == 1 && minimap[mapX][mapY - 1] == 1)
                                minimap[mapX][(int) camera.yPos] = 1;
                        }
                    }
                    screen.resetMinimap(2 * level + 3, 2 * level + 3);
                    screen.setMinimap(minimapType, minimap);
                }
                //handles all of the logic restricted time
                camera.update(map);
                screen.updateMazeGame(camera, pixels, map, floorMap, ceilingMap);
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }

    public void resetMinimap(int w, int h)
    {
        minimap = new int[w][h];
    }

    public static void main(String [] args)
    {
        MazeGame mg = new MazeGame();
    }
}