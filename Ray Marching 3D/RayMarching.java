import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
public class RayMarching extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    javax.swing.Timer tm = new javax.swing.Timer(1, this);
    private static int width = 600;
    private static int height = 600;
    private int frame;
    private BufferedImage bufferedImage;
    private Camera camera;
    private Screen screen;
    private ArrayList<Shape> shapes;
    private Light light;
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
        frame = 0;
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        shapes = new ArrayList<Shape>();

        shapes.add(new Cube(-1, 5, 8, 0.5, 0.5, 0.5, new Vector3D(0, 255, 0)));
                ((Cube) shapes.get(shapes.size() - 1)).pos.rotateY3D(45, new Vector3D(3, 1, 8));
        shapes.add(new Sphere(2, 6, 10, 2, new Vector3D(255, 0, 0)));
                ((Sphere) shapes.get(shapes.size() - 1)).pos.rotateY3D(45, new Vector3D(3, 1, 8));
        shapes.add(new Torus(0, 4, 6, 1, 0.25, new Vector3D(0, 0, 255)));
                ((Torus) shapes.get(shapes.size() - 1)).pos.rotateY3D(45, new Vector3D(3, 1, 8));
        shapes.add(new Plane(0, "xz", new Vector3D(255, 255, 255)));

        for(int r = -5; r <= 5; r++)
        {
            for(int c = -5; c <= 5; c++)
            {
                shapes.add(new Sphere(3 * r - 6, 1, 14 + 3 * c, 1, new Vector3D(64, 64, 64)));
            }
        }
        light = new Light(3, 10, 4);
        camera = new Camera(0, 10, -4);
        camera.ray.pos.rotateY3D(45, new Vector3D(3, 1, 8));
        screen = new Screen(camera, width, height);
        screen.update(frame, shapes, light, bufferedImage);
        setBackground(new Color(0, 0, 0));
    }

    public void update()
    {
        frame++;
        screen.update(frame, shapes, light, bufferedImage);
    }

    public void draw(Graphics g)
    {
        g.drawImage(bufferedImage, 0, 0, this);
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
        jf.setSize(width + 6, height + 29);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(r);
        jf.setVisible(true);
    }
}