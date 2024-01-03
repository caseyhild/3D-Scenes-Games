import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
public class Game extends JFrame implements Runnable, MouseListener, MouseMotionListener, KeyListener
{  
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private int frame;
    private String gameState;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private int[] pixels;
    private static int[][] map = 
        {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    private int mapWidth = map.length;
    private int mapHeight = map[0].length;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private ArrayList<Texture> textures;
    private File folder;
    private ArrayList<PointsFile> files;
    private ArrayList<ArrayList<Vector3D>> points;
    private Vector player;
    private boolean playerDead;
    private int score;
    private ArrayList<Integer> scoresList;
    private ArrayList<String> names;
    private String name;
    private int remainder;
    private int time;
    private int cursorPos;
    private boolean highlightBox;
    private int timer;
    private Camera camera;
    private Screen screen;
    private int mouseX;
    private int mouseY;
    private boolean mousePressed;
    private boolean keyPressed;
    private boolean keyReleased;
    private boolean keyTyped;
    private KeyEvent key;
    private KeyEvent oldKey;

    public Game() throws IOException
    {
        //set size of screen
        width = 640;
        height = 480;
        //set starting frame
        frame = 0;
        //set initial game state
        gameState = "menu";
        //delete all existing files
        folder = new File("3DPoints");
        File[] filelist = folder.listFiles();
        if(filelist == null)
            filelist = new File[0];
        for(int i = 0; i < filelist.length; i++)
        {
            filelist[i].delete();
        }
        //Create 3D Points files
        CreatePoints c = new CreatePoints("3DPoints");
        files = new ArrayList<PointsFile>();
        readFile("tree.txt");
        readFile("sphere.txt");
        readFile("zombie.txt");
        //Add structures made of points
        points = new ArrayList<ArrayList<Vector3D>>();
        for(int i = 0; i < files.size(); i++)
            points.add(new ArrayList<Vector3D>());
        for(int i = 0; i < 100; i++)
        {
            points.get(0).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
            if(points.get(0).get(i).getX() > mapWidth/3 - 0.5 && points.get(0).get(i).getX() < 2 * mapWidth/3 + 0.5 && points.get(0).get(i).getY() > mapHeight/3 - 0.5 && points.get(0).get(i).getY() < 2 * mapHeight/3 + 0.5)
            {
                points.get(0).remove(i);
                i--;
            }
            else
            {
                for(int j = 0; j < points.get(0).size() - 1; j++)
                {
                    if(points.get(0).get(j).dist(points.get(0).get(i)) <= 2)
                    {
                        points.get(0).remove(i);
                        i--;
                    }
                }
            }
        }
        for(int i = 0; i < 3; i++)
        {
            points.get(2).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
            if(points.get(2).get(i).getX() > mapWidth/3 - 0.5 && points.get(2).get(i).getX() < 2 * mapWidth/3 + 0.5 && points.get(2).get(i).getY() > mapHeight/3 - 0.5 && points.get(2).get(i).getY() < 2 * mapHeight/3 + 0.5)
            {
                points.get(2).remove(i);
                i--;
            }
        }
        //initial map and location
        camera = new Camera(mapWidth/2 + 0.5, mapHeight/2 + 0.5, 1, 0, 0, -0.66);//coordinates from topleft of map, facing down
        floorMap = new int[mapWidth][mapHeight];
        ceilingMap = new int[mapWidth][mapHeight];
        //player
        player = new Vector(mapWidth/2 + 0.5, mapHeight/2 + 0.5);
        playerDead = false;
        //scores
        score = 0;
        scoresList = new ArrayList<Integer>();
        names = new ArrayList<String>();
        Scanner scores = new Scanner(new File("scores.txt"));
        while(scores.hasNext())
        {
            names.add(scores.next());
            scoresList.add(scores.nextInt());
        }
        if(scoresList.size() > 0)
            sort(scoresList, names, 0, scoresList.size() - 1);
        //name input
        name = "";
        remainder = -1;
        time = 0;
        cursorPos = 0;
        highlightBox = false;
        timer = 60000;
        //what will be displayed to the user and each pixel of that image
        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        //list of the available textures to use
        textures = new ArrayList<Texture>();
        textures.add(Texture.bricks);
        textures.add(Texture.gray);
        textures.add(Texture.grass);
        textures.add(Texture.black);
        //starting floor, ceiling, and finish location
        for(int mapX = 0; mapX < mapWidth; mapX++)
        {
            for(int mapY = 0; mapY < mapHeight; mapY++)
            {
                if(mapX >= mapWidth/3 && mapX < 2 * mapWidth/3 && mapY >= mapHeight/3 && mapY < 2 * mapHeight/3)
                {
                    floorMap[mapX][mapY] = 2;
                    ceilingMap[mapX][mapY] = 2;
                    if(map[mapX][mapY] > 0)
                        map[mapX][mapY] = 1;
                }
                else
                {
                    floorMap[mapX][mapY] = 3;
                    ceilingMap[mapX][mapY] = 4;
                    if(map[mapX][mapY] > 0)
                        map[mapX][mapY] = 4;
                }
            }
        }
        //keyboard input
        addKeyListener(camera);
        addKeyListener(this);
        keyPressed = false;
        keyReleased = false;
        keyTyped = false;
        key = null;
        oldKey = null;
        //mouse input
        addMouseListener(this);
        addMouseMotionListener(this);
        mouseX = 0;
        mouseY = 0;
        mousePressed = false;
        //send info to screen class to be drawn
        screen = new Screen(map, floorMap, ceilingMap, mapWidth, mapHeight, textures, files, width, height);
        screen.setPoints(points);
        screen.updateGame(camera, pixels, map, floorMap, ceilingMap, frame);
        //setting up the window
        setSize(width, height);
        setResizable(false);
        setTitle("Game");
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

    private void render() throws IOException
    {
        //draws the window
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if(gameState.equals("menu"))
        {
            g.setColor(new Color(128, 128, 128));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Verdana", Font.PLAIN, 50));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("Zombie Apocalypse", width/2 - fm.stringWidth("Zombie Apocalypse")/2, height/3 + fm.getAscent()/2);
            if(mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 17 * height/30 && mouseY <= 13 * height/20)
                g.setColor(new Color(32, 32, 32));
            else
                g.setColor(new Color(64, 64, 64));
            g.fillRoundRect(7 * width/24, 17 * height/30, 5 * width/12, height/12, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(7 * width/24, 17 * height/30, 5 * width/12, height/12, 10, 10);
            g.setFont(new Font("Verdana", Font.PLAIN, 30));
            fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("PLAY", width/2 - fm.stringWidth("PLAY")/2, 73 * height/120 + fm.getAscent()/2 - 2);
            if(mousePressed && mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 17 * height/30 && mouseY <= 13 * height/20)
                gameState = "game";
            if(mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 2 * height/3 && mouseY <= 3 * height/4)
                g.setColor(new Color(32, 32, 32));
            else
                g.setColor(new Color(64, 64, 64));
            g.fillRoundRect(7 * width/24, 2 * height/3, 5 * width/12, height/12, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(7 * width/24, 2 * height/3, 5 * width/12, height/12, 10, 10);
            g.setFont(new Font("Verdana", Font.PLAIN, 30));
            fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("HOW TO PLAY", width/2 - fm.stringWidth("HOW TO PLAY")/2, 17 * height/24 + fm.getAscent()/2 - 2);
            if(mousePressed && mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 2 * height/3 && mouseY <= 3 * height/4)
                gameState = "howtoplay";
            if(mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 23 * height/30 && mouseY <= 51 * height/60)
                g.setColor(new Color(32, 32, 32));
            else
                g.setColor(new Color(64, 64, 64));
            g.fillRoundRect(7 * width/24, 23 * height/30, 5 * width/12, height/12, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(7 * width/24, 23 * height/30, 5 * width/12, height/12, 10, 10);
            g.setFont(new Font("Verdana", Font.PLAIN, 30));
            fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("HIGH SCORES", width/2 - fm.stringWidth("HIGH SCORES")/2, 97 * height/120 + fm.getAscent()/2 - 2);
            if(mousePressed && mouseX >= 7 * width/24 && mouseX <= 17 * width/24 && mouseY >= 23 * height/30 && mouseY <= 51 * height/60)
                gameState = "highscores";
        }
        else if(gameState.equals("game"))
        {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            g.setColor(new Color(0, 0, 0));
            g.fillRect(width/2 - 2, height/2 - 11, 4, 8);
            g.fillRect(width/2 - 2, height/2 + 3, 4, 8);
            g.fillRect(width/2 - 11, height/2 - 2, 8, 4);
            g.fillRect(width/2 + 3, height/2 - 2, 8, 4);
            g.setFont(new Font("Verdana", Font.PLAIN, 15));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("Score: " + score, width - 10 - fm.stringWidth("Score: " + score), 40 + fm.getAscent()/2 - 2);
            if(scoresList.size() > 0)
                g.drawString("High Score: " + scoresList.get(scoresList.size() - 1), width - 10 - fm.stringWidth("High Score: " + scoresList.get(scoresList.size() - 1)), 60 + fm.getAscent()/2 - 2);
            else
                g.drawString("High Score: 0", width - 10 - fm.stringWidth("High Score: 0"), 60 + fm.getAscent()/2 - 2);
        }
        else if(gameState.equals("howtoplay"))
        {
            g.setColor(new Color(128, 128, 128));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Verdana", Font.PLAIN, 40));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("HOW TO PLAY", width/2 - fm.stringWidth("HOW TO PLAY")/2, height/6 + fm.getAscent()/2);
            g.setFont(new Font("Verdana", Font.PLAIN, 15));
            fm = g.getFontMetrics();
            g.drawString("Use the up/down arrow keys to move", width/2 - fm.stringWidth("Use the up/down arrow keys to move")/2, height/3 + fm.getAscent()/2);
            g.drawString("forward and backward.", width/2 - fm.stringWidth("forward and backward.")/2, 9 * height/24 + fm.getAscent()/2);
            g.drawString("Use the left/right arrow keys to turn.", width/2 - fm.stringWidth("Use the left/right arrow keys to turn.")/2, 11 * height/24 + fm.getAscent()/2);
            g.drawString("Press space to shoot.", width/2 - fm.stringWidth("Press space to shoot.")/2, 13 * height/24 + fm.getAscent()/2);
            if(mouseX >= 7 * width/8 && mouseX <= 31 * width/32 && mouseY >= 5 * height/64 && mouseY <= height/8)
                g.setColor(new Color(32, 32, 32));
            else
                g.setColor(new Color(64, 64, 64));
            g.fillRoundRect(7 * width/8, 5 * height/64, 3 * width/32, 3 * height/64, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(7 * width/8, 5 * height/64, 3 * width/32, 3 * height/64, 10, 10);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Verdana", Font.PLAIN, 18));
            fm = g.getFontMetrics();
            g.drawString("BACK", 59 * width/64 - fm.stringWidth("BACK")/2, 13 * height/128 - 2 + fm.getAscent()/2);
            if(mousePressed && mouseX >= 7 * width/8 && mouseX <= 31 * width/32 && mouseY >= 5 * height/64 && mouseY <= height/8)
                gameState = "menu";
        }
        else if(gameState.equals("highscores"))
        {
            g.setColor(new Color(128, 128, 128));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Verdana", Font.PLAIN, 40));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("HIGH SCORES", width/2 - fm.stringWidth("HIGH SCORES")/2, height/6 + fm.getAscent()/2);
            g.setFont(new Font("Verdana", Font.PLAIN, 20));
            fm = g.getFontMetrics();
            for(int i = 0; i < 10; i++)
            {
                g.setColor(new Color(255, 255, 255));
                g.drawString((i + 1) + ". ", width/4 + 5, height/3 + fm.getAscent()/2 + i * 30 - 16);
                g.setColor(new Color(0, 0, 0));
                g.drawRect(width/4, height/3 + i * 30 - 30, width/2, 30);
            }
            g.setColor(new Color(255, 255, 255));
            for(int i = 0; i < Math.min(scoresList.size(), 10); i++)
            {
                g.drawString(names.get(names.size() - 1 - i), width/4 + 5 + fm.stringWidth((i + 1) + ". "), height/3 + fm.getAscent()/2 + i * 30 - 16);
                if(scoresList.get(scoresList.size() - 1 - i) == 1)
                    g.drawString(scoresList.get(scoresList.size() - 1 - i) + " point", 3 * width/4 - 5 - fm.stringWidth(scoresList.get(scoresList.size() - 1 - i) + " point"), height/3 + fm.getAscent()/2 + i * 30 - 16);
                else
                    g.drawString(scoresList.get(scoresList.size() - 1 - i) + " points", 3 * width/4 - 5 - fm.stringWidth(scoresList.get(scoresList.size() - 1 - i) + " points"), height/3 + fm.getAscent()/2 + i * 30 - 16);
            }
            if(mouseX >= 7 * width/8 && mouseX <= 31 * width/32 && mouseY >= 5 * height/64 && mouseY <= height/8)
                g.setColor(new Color(32, 32, 32));
            else
                g.setColor(new Color(64, 64, 64));
            g.fillRoundRect(7 * width/8, 5 * height/64, 3 * width/32, 3 * height/64, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(7 * width/8, 5 * height/64, 3 * width/32, 3 * height/64, 10, 10);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Verdana", Font.PLAIN, 18));
            fm = g.getFontMetrics();
            g.drawString("BACK", 59 * width/64 - fm.stringWidth("BACK")/2, 13 * height/128 - 2 + fm.getAscent()/2);
            if(mousePressed && mouseX >= 7 * width/8 && mouseX <= 31 * width/32 && mouseY >= 5 * height/64 && mouseY <= height/8)
                gameState = "menu";
        }
        else if(gameState.equals("lose"))
        {
            g.setColor(new Color(255, 0, 0));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Verdana", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(255, 255, 255));
            g.drawString("You Lose!", width/2 - fm.stringWidth("You Lose!")/2, height/4 + fm.getAscent()/2 - 2);
            g.setFont(new Font("Verdana", Font.BOLD, 15));
            fm = g.getFontMetrics();
            if(score == 1)
                g.drawString("You shot " + score + " zombie!", width/2 - fm.stringWidth("You Shot " + score + " zombie!")/2, height/3 + fm.getAscent()/2 - 2);
            else
                g.drawString("You shot " + score + " zombies!", width/2 - fm.stringWidth("You Shot " + score + " zombies!")/2, height/3 + fm.getAscent()/2 - 2);
            String enterName = "Enter Your Name:";
            if(highlightBox)
                enterName = "Invalid Entry, " + enterName;
            g.drawString(enterName, width/2 - fm.stringWidth(enterName)/2, 2 * height/3 + fm.getAscent()/2 - 2);
            g.drawString("Press Enter to Save Your Score and Return to Menu", width/2 - fm.stringWidth("Press Enter to Save Your Score and Return to Menu")/2, 5 * height/6 + fm.getAscent()/2 - 2);
            String fieldWidth = "000000000000";
            if(keyPressed && remainder == -1)
                remainder = frame % 80;
            else if(keyReleased)
                remainder = -1;
            if(keyReleased && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                time = 0;
            else if(keyPressed && frame >= time + 700 && (frame - remainder) % 80 == 0 && key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            {
                if(time == 0)
                    time = frame;
                name = name.substring(0, Math.max(0, cursorPos - 1)) + name.substring(cursorPos);
                cursorPos--;
            }
            else if(keyReleased && key.getKeyCode() == KeyEvent.VK_DELETE)
                time = 0;
            else if(keyPressed && frame >= time + 700 && (frame - remainder) % 80 == 0 && key.getKeyCode() == KeyEvent.VK_DELETE)
            {
                if(time == 0)
                    time = frame;
                name = name.substring(0, cursorPos) + name.substring(Math.min(cursorPos + 1, name.length()));
            }
            else if(keyTyped && key.getKeyChar() != oldKey.getKeyChar() && (key.getKeyChar() == '\"' || key.getKeyChar() == '\\'))
            {
                oldKey = key;
                name = name.substring(0, cursorPos) + key.getKeyChar() + name.substring(Math.min(cursorPos, name.length()));
                cursorPos++;
            }
            else if(keyTyped && key.getKeyChar() != oldKey.getKeyChar() && name.length() < fieldWidth.length() && key.getKeyChar() != KeyEvent.CHAR_UNDEFINED && key.getKeyChar() != KeyEvent.VK_ESCAPE && key.getKeyChar() != KeyEvent.VK_ENTER && key.getKeyCode() != KeyEvent.VK_BACK_SPACE && key.getKeyCode() != KeyEvent.VK_DELETE && key.getKeyChar() != ' ')
            {
                oldKey = key;
                name = name.substring(0, cursorPos) + key.getKeyChar() + name.substring(Math.min(cursorPos, name.length()));
                cursorPos++;
            }
            else if(keyTyped && key.getKeyChar() == KeyEvent.VK_SHIFT)
            {
                //oldKey = null;
            }
            else if(keyReleased && key.getKeyCode() == KeyEvent.VK_LEFT)
                time = 0;
            else if(keyPressed && frame >= time + 700 && (frame - remainder) % 80 == 0 && key.getKeyCode() == KeyEvent.VK_LEFT)
            {
                if(time == 0)
                    time = frame;
                cursorPos--;
            }
            else if(keyReleased && key.getKeyCode() == KeyEvent.VK_RIGHT)
                time = 0;
            else if(keyPressed && frame >= time + 700 && (frame - remainder) % 80 == 0 && key.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                if(time == 0)
                    time = frame;
                cursorPos++;
            }
            g.setFont(new Font("monospaced", Font.PLAIN, 12));
            fm = g.getFontMetrics();
            if(mousePressed && mouseX >= width/2 - fm.stringWidth(fieldWidth)/2 - fm.stringWidth("0")/4 && mouseX <= width/2 + fm.stringWidth(fieldWidth)/2 + fm.stringWidth("0")/4 && mouseY >= 2 * height/3 + height/16 - 9 * fm.getAscent()/16 && mouseY <= 2 * height/3 + height/16 + 9 * fm.getAscent()/16)
                cursorPos = (int) Math.round(1.0 * (mouseX - (width/2 - fm.stringWidth(fieldWidth)/2) - fm.stringWidth("0")/4)/fm.stringWidth("0"));
            cursorPos = Math.max(0, Math.min(cursorPos, name.length()));
            if(highlightBox)
            {
                g.setColor(new Color(255, 255, 0));
                g.fillRect(width/2 - fm.stringWidth(fieldWidth)/2 - fm.stringWidth("0")/4 - 2, 2 * height/3 + height/16 - 9 * fm.getAscent()/16 - 2, fm.stringWidth(fieldWidth) + 3 * fm.stringWidth("0")/4 + 4, 9 * fm.getAscent()/8 + 4);
            }
            g.setColor(new Color(255, 255, 255));
            g.fillRect(width/2 - fm.stringWidth(fieldWidth)/2 - fm.stringWidth("0")/4, 2 * height/3 + height/16 - 9 * fm.getAscent()/16, fm.stringWidth(fieldWidth) + 3 * fm.stringWidth("0")/4, 9 * fm.getAscent()/8);
            g.setColor(new Color(0, 0, 0));
            g.drawString(name, width/2 - fm.stringWidth(fieldWidth)/2 + 1, 2 * height/3 + height/16 + fm.getAscent()/2 - 2);
            if(frame/1000 % 2 == 0)
                g.drawLine(width/2 - fm.stringWidth(fieldWidth)/2 + fm.stringWidth("0") * cursorPos + 1, 2 * height/3 + height/16 - height/80, width/2 - fm.stringWidth(fieldWidth)/2 + fm.stringWidth("0") * cursorPos + 1, 2 * height/3 + height/16 + height/80 - 1);
            if(keyPressed && key.getKeyCode() == KeyEvent.VK_ENTER)
            {
                if(!name.equals(""))
                {
                    FileWriter fw = new FileWriter("scores.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter scoresFile = new PrintWriter(bw);
                    scoresFile.println(name + " " + score);
                    scoresFile.close();
                    //Add structures made of points
                    points = new ArrayList<ArrayList<Vector3D>>();
                    for(int i = 0; i < files.size(); i++)
                        points.add(new ArrayList<Vector3D>());
                    for(int i = 0; i < 100; i++)
                    {
                        points.get(0).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                        if(points.get(0).get(i).getX() > mapWidth/3 - 0.5 && points.get(0).get(i).getX() < 2 * mapWidth/3 + 0.5 && points.get(0).get(i).getY() > mapHeight/3 - 0.5 && points.get(0).get(i).getY() < 2 * mapHeight/3 + 0.5)
                        {
                            points.get(0).remove(i);
                            i--;
                        }
                        else
                        {
                            for(int j = 0; j < points.get(0).size() - 1; j++)
                            {
                                if(points.get(0).get(j).dist(points.get(0).get(i)) <= 2)
                                {
                                    points.get(0).remove(i);
                                    i--;
                                }
                            }
                        }
                    }
                    for(int i = 0; i < 3; i++)
                    {
                        points.get(2).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                        if(points.get(2).get(i).getX() > mapWidth/3 - 0.5 && points.get(2).get(i).getX() < 2 * mapWidth/3 + 0.5 && points.get(2).get(i).getY() > mapHeight/3 - 0.5 && points.get(2).get(i).getY() < 2 * mapHeight/3 + 0.5)
                        {
                            points.get(2).remove(i);
                            i--;
                        }
                    }
                    //reset position
                    camera = new Camera(mapWidth/2 + 0.5, mapHeight/2 + 0.5, 1, 0, 0, -0.66);
                    addKeyListener(camera);
                    //player
                    player = new Vector(mapWidth/2 + 0.5, mapHeight/2 + 0.5);
                    playerDead = false;
                    //scores
                    score = 0;
                    scoresList = new ArrayList<Integer>();
                    names = new ArrayList<String>();
                    Scanner scores = new Scanner(new File("scores.txt"));
                    while(scores.hasNextLine())
                    {
                        names.add(scores.next());
                        scoresList.add(scores.nextInt());
                    }
                    if(scoresList.size() > 0)
                        sort(scoresList, names, 0, scoresList.size() - 1);
                    //name input
                    name = "";
                    remainder = -1;
                    time = 0;
                    cursorPos = 0;
                    //reset screen
                    screen = new Screen(map, floorMap, ceilingMap, mapWidth, mapHeight, textures, files, width, height);
                    screen.setPoints(points);
                    screen.updateGame(camera, pixels, map, floorMap, ceilingMap, frame);
                    highlightBox = false;
                    gameState = "menu";
                    mousePressed = false;
                }
                else
                    highlightBox = true;
            }
        }
        if(keyReleased)
            keyReleased = false;
        if(keyTyped)
            keyTyped = false;
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
                if(gameState.equals("game"))
                {
                    //updating textures
                    ArrayList<Texture> texturesCopy = new ArrayList<Texture>();
                    for(Texture t : textures)
                        texturesCopy.add(t);
                    textures.clear();
                    for(Texture t : texturesCopy)
                        textures.add(t);
                    //updating camera
                    camera.update(map);
                    //updating screen
                    screen.updateGame(camera, pixels, map, floorMap, ceilingMap, frame);
                    //updating score
                    score = screen.getScore();
                    //checking if player is dead
                    if(screen.isDead())
                        gameState = "lose";
                }
                delta--;
            }
            try
            {
                render();//displays to the screen unrestricted time
            }
            catch(IOException e)
            {

            }
        }
    }

    public void readFile(String loc) throws IOException
    {
        Scanner file = new Scanner(new File(folder.getName() + "/" + loc));
        int ctr = 0;
        while(file.hasNextLine())
        {
            file.nextLine();
            ctr++;
        }
        files.add(new PointsFile(ctr));
        file = new Scanner(new File(folder.getName() + "/" + loc));
        ctr = 0;
        while(file.hasNextDouble())
        {
            files.get(files.size() - 1).x[ctr] = file.nextDouble();
            files.get(files.size() - 1).y[ctr] = file.nextDouble();
            files.get(files.size() - 1).z[ctr] = file.nextDouble();
            files.get(files.size() - 1).color[ctr] = file.nextInt();
            ctr++;
        }
    }

    public void sort(ArrayList<Integer> list, ArrayList<String> names, int first, int last)
    {
        int g = first, h = last;
        int midIndex = (first + last) / 2;
        int dividingValue = list.get(midIndex);
        do
        {
            while(list.get(g) < dividingValue)
            {
                g++;
            }
            while(list.get(h) > dividingValue)
            {
                h--;
            }
            if(g <= h)
            {
                int temp = list.get(g);
                list.set(g, list.get(h));
                list.set(h, temp);
                String tempName = names.get(g);
                names.set(g, names.get(h));
                names.set(h, tempName);
                g++;
                h--;
            }
        }while(g < h);
        if(h > first)
            sort(list, names, first, h);
        if(g < last)
            sort(list, names, g, last);
    }

    public void mouseClicked(MouseEvent me)
    {
        mousePressed = true;
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent me)
    {
        mousePressed = false;
    }

    public void mouseDragged(MouseEvent me)
    {
        mousePressed = true;
        mouseX = me.getX();
        mouseY = me.getY();
    }

    public void mouseMoved(MouseEvent me)
    {
        mousePressed = false;
        mouseX = me.getX();
        mouseY = me.getY();
    }

    public void keyPressed(KeyEvent key)
    {
        keyPressed = !keyTyped;
        this.key = key;
        if(oldKey == null)
            oldKey = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
        keyReleased = true;
        this.key = key;
        oldKey.setKeyChar(' ');
    }

    public void keyTyped(KeyEvent key)
    {
        keyTyped = true;
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

    public static void main(String [] args) throws IOException
    {
        Game g = new Game();
    }
}