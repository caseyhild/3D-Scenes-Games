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
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
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
    private Vector2D player;
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
        readFile("spiral.txt");
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
        points.get(1).add(new Vector3D(mapWidth/2.0 + 2, mapHeight/2.0, 0.5));
        //initial map and location
        camera = new Camera(mapWidth/2 + 0.5, mapHeight/2 + 0.5, 1, 0, 0, -0.66);//coordinates from topleft of map, facing down
        floorMap = new int[mapWidth][mapHeight];
        ceilingMap = new int[mapWidth][mapHeight];
        //player
        player = new Vector2D(mapWidth/2 + 0.5, mapHeight/2 + 0.5);
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
                }
                else
                {
                    floorMap[mapX][mapY] = 3;
                    ceilingMap[mapX][mapY] = 4;
                    if(mapX == 0 || mapX == map.length - 1 || mapY == 0 || mapY == map.length - 1) {
                        map[mapX][mapY] = 4;
                    }
                }
            }
        }
        //keyboard input
        addKeyListener(camera);
        //mouse input
        addMouseListener(camera);
        addMouseMotionListener(camera);
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
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(width/2 - 2, height/2 - 11, 4, 8);
        g.fillRect(width/2 - 2, height/2 + 3, 4, 8);
        g.fillRect(width/2 - 11, height/2 - 2, 8, 4);
        g.fillRect(width/2 + 3, height/2 - 2, 8, 4);
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
                //updating textures
                ArrayList<Texture> texturesCopy = new ArrayList<Texture>();
                for(Texture t : textures)
                    texturesCopy.add(t);
                textures.clear();
                for(Texture t : texturesCopy)
                    textures.add(t);
                //updating camera
                camera.update(map, frame);
                //updating screen
                screen.updateGame(camera, pixels, map, floorMap, ceilingMap, frame);
                delta--;
            }
            try
            {
                // Hide the cursor
                BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                        cursorImg, new Point(0, 0), "blank cursor");
                this.getContentPane().setCursor(blankCursor);

                render();//displays to the screen unrestricted time
            }
            catch(IOException e)
            {

            }
        }
    }

    public void readFile(String loc) throws IOException
    {
        Scanner file = new Scanner(new File("src/" + folder.getName() + "/" + loc));
        int ctr = 0;
        while(file.hasNextLine())
        {
            file.nextLine();
            ctr++;
        }
        files.add(new PointsFile(ctr));
        file = new Scanner(new File("src/" + folder.getName() + "/" + loc));
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

    public static void main(String[] args) throws IOException
    {
        Game g = new Game();
    }
}