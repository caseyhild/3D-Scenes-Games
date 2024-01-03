import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
public class RayMarching extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    Timer tm = new Timer(1, this);
    private static int width = 600;
    private static int height = 600;
    private int mapSize;
    private int[][] pixels;
    private Screen screen;
    private Camera camera;
    private Shape[] shapes;
    private boolean keyPressed;
    private KeyEvent key;
    private boolean mouseClicked;
    private boolean mousePressed;
    private int mouseX;
    private int mouseY;

    public RayMarching()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        pixels = new int[height][width];
        mapSize = 6;
        screen = new Screen(width, height, mapSize);
        camera = new Camera(3, 4, 0, 0, -1, 0, 360);
        shapes = new Shape[2];
        shapes[0] = new Sphere(2, 2, 0, 0.5);
        shapes[1] = new Cube(4, 3, 0, 0.5, 0.5 , 0.5);
        screen.update(camera, shapes, pixels);
        setBackground(new Color(0, 0, 0));
    }

    public void update()
    {
        if(keyPressed && key.getKeyCode() == KeyEvent. VK_UP)
            camera.pos.y -= 0.1;
        if(keyPressed && key.getKeyCode() == KeyEvent. VK_DOWN)
            camera.pos.y += 0.1;
        if(keyPressed && key.getKeyCode() == KeyEvent. VK_LEFT)
            camera.pos.x -= 0.1;
        if(keyPressed && key.getKeyCode() == KeyEvent. VK_RIGHT)
            camera.pos.x += 0.1;
        if(camera.pos.x <= 0.505)
            camera.pos.x = 0.505;
        if(camera.pos.x >= mapSize - 0.505)
            camera.pos.x = mapSize - 0.505;
        if(camera.pos.y <= 0.505)
            camera.pos.y = 0.505;
        if(camera.pos.y >= mapSize - 0.505)
            camera.pos.y = mapSize - 0.505;
        screen.update(camera, shapes, pixels);
    }

    public void draw(Graphics g)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                g.setColor(new Color(pixels[y][x]));
                g.drawLine(x, y, x, y);
            }
        }
    }

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
        if(mouseClicked)
            mouseClicked = false;
        tm.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    public void mouseClicked(MouseEvent me)
    {
        mouseClicked = true;
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {
        mouseClicked = false;
        mousePressed = false;
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
        keyPressed = true;
        this.key = key;
    }

    public void keyReleased(KeyEvent key)
    {
        keyPressed = false;
        key = null;
    }

    public void keyTyped(KeyEvent key)
    {

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

    public static void main(String[] args)
    {
        RayMarching r  = new RayMarching();
        JFrame jf = new JFrame();
        jf.setTitle("Ray Marching");
        jf.setSize(width + 7, height + 30);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(r);
        jf.setVisible(true);
    }
}