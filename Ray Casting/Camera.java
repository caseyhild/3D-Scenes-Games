import java.awt.*;
import java.awt.event.*;
public class Camera implements MouseListener, MouseMotionListener, KeyListener
{
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    private double playerHeight;
    private boolean left, right, forward, back, jump;
    private final double MOVE_SPEED = .08;
    private final double ROTATION_SPEED = .045;
    private double turnLeft;
    private double turnRight;
    private int turns;
    public boolean shoot;
    public int timer;
    private int[][] map;
    private boolean mousePressed;
    public int mouseX;
    public int mouseY;

    public Camera(double x, double y, double xd, double yd, double xp, double yp) 
    {   
        //gets starting location and direction
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
        playerHeight = 0.5;
        left = false;
        right = false;
        forward = false;
        back = false;
        turnLeft = 0;
        turnRight = 0;
        shoot = false;
        timer = 0;
        mouseX = 320;
        mouseY = 240;
        //setting mouse position to center of screen
        try
        {
            Robot r = new Robot();
            r.mouseMove(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - 23);
        }
        catch(AWTException e)
        {

        }
    }
    //checks if key is pressed
    public void keyPressed(KeyEvent key)
    {
        //closes program with escape key
        if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
            System.exit(0);
        //W to move forward
        if((key.getKeyCode() == KeyEvent.VK_W))
            forward = true;
        //A to move left
        if((key.getKeyCode() == KeyEvent.VK_A))
            left = true;
        //S to move back
        if((key.getKeyCode() == KeyEvent.VK_S))
            back = true;
        //D to move right
        if((key.getKeyCode() == KeyEvent.VK_D))
            right = true;
    }
    //checks if key is released
    public void keyReleased(KeyEvent key)
    {
        //stops moving forward
        if((key.getKeyCode() == KeyEvent.VK_W))
            forward = false;
        //stops moving left
        if((key.getKeyCode() == KeyEvent.VK_A))
            left = false;
        //stops moving back
        if((key.getKeyCode() == KeyEvent.VK_S))
            back = false;
        //stops moving right
        if((key.getKeyCode() == KeyEvent.VK_D))
            right = false;
    }
    //checks if key is typed
    public void keyTyped(KeyEvent key)
    {

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
        shoot = true;
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

    public void update(int[][] map, int frame)
    {
        this.map = map;

        //moves and turns player based on key input
        if(forward)
        {
            if(!((int) (xPos + xDir * MOVE_SPEED) < 0 || (int) (xPos + xDir * MOVE_SPEED) > map.length - 1 || (int) (yPos + yDir * MOVE_SPEED) < 0 || (int) (yPos + yDir * MOVE_SPEED) > map[0].length - 1))
            {
                xPos += xDir * MOVE_SPEED;
                if(xDir > 0 && map[(int) (xPos + 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) - 0.01;
                else if(xDir < 0 && map[(int) (xPos - 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) + 0.01;
                yPos += yDir * MOVE_SPEED;
                if(yDir > 0 && map[(int) xPos][(int) (yPos + 0.01)] != 0)
                    yPos = Math.round(yPos) - 0.01;
                else if(yDir < 0 && map[(int) xPos][(int) (yPos - 0.01)] != 0)
                    yPos = Math.round(yPos) + 0.01;
            }
        }
        
        if(back)
        {
            if(!((int) (xPos - xDir * MOVE_SPEED) < 0 || (int) (xPos - xDir * MOVE_SPEED) > map.length - 1 || (int) (yPos - yDir * MOVE_SPEED) < 0 || (int) (yPos - yDir * MOVE_SPEED) > map[0].length - 1))
            {
                xPos -= xDir * MOVE_SPEED;
                if(xDir > 0 && map[(int) (xPos - 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) + 0.01;
                else if(xDir < 0 && map[(int) (xPos + 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) - 0.01;
                yPos -= yDir * MOVE_SPEED;
                if(yDir > 0 && map[(int) xPos][(int) (yPos - 0.01)] != 0)
                    yPos = Math.round(yPos) + 0.01;
                else if(yDir < 0 && map[(int) xPos][(int) (yPos + 0.01)] != 0)
                    yPos = Math.round(yPos) - 0.01;
            }
        }

        if(left)
        {
            if(!((int) (xPos - yDir * MOVE_SPEED) < 0 || (int) (xPos - yDir * MOVE_SPEED) > map.length - 1 || (int) (yPos + xDir * MOVE_SPEED) < 0 || (int) (yPos + xDir * MOVE_SPEED) > map[0].length - 1))
            {
                xPos -= yDir * MOVE_SPEED;
                if(yDir > 0 && map[(int) (xPos - 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) + 0.01;
                else if(yDir < 0 && map[(int) (xPos + 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) - 0.01;
                yPos += xDir * MOVE_SPEED;
                if(xDir > 0 && map[(int) xPos][(int) (yPos + 0.01)] != 0)
                    yPos = Math.round(yPos) - 0.01;
                else if(xDir < 0 && map[(int) xPos][(int) (yPos - 0.01)] != 0)
                    yPos = Math.round(yPos) + 0.01;
            }
        }

        if(right)
        {
            if(!((int) (xPos + yDir * MOVE_SPEED) < 0 || (int) (xPos + yDir * MOVE_SPEED) > map.length - 1 || (int) (yPos - xDir * MOVE_SPEED) < 0 || (int) (yPos - xDir * MOVE_SPEED) > map[0].length - 1))
            {
                xPos += yDir * MOVE_SPEED;
                if(yDir > 0 && map[(int) (xPos + 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) - 0.01;
                else if(yDir < 0 && map[(int) (xPos - 0.01)][(int) yPos] != 0)
                    xPos = Math.round(xPos) + 0.01;
                yPos -= xDir * MOVE_SPEED;
                if(xDir > 0 && map[(int) xPos][(int) (yPos - 0.01)] != 0)
                    yPos = Math.round(yPos) + 0.01;
                else if(xDir < 0 && map[(int) xPos][(int) (yPos + 0.01)] != 0)
                    yPos = Math.round(yPos) - 0.01;
            }
        }

        //setting mouse position to center of screen
        if (timer++ % 5 == 0) {
            try {
                Robot r = new Robot();
                r.mouseMove(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 23);
            } catch (AWTException e) {

            }
        }

        //turning using the mouse
        {
            double rotationAmount = 0.002 * (320 - mouseX);
            double oldxDir = xDir;
            xDir = xDir * Math.cos(rotationAmount) - yDir * Math.sin(rotationAmount);
            yDir = oldxDir * Math.sin(rotationAmount) + yDir * Math.cos(rotationAmount);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(rotationAmount) - yPlane * Math.sin(rotationAmount);
            yPlane = oldxPlane * Math.sin(rotationAmount) + yPlane * Math.cos(rotationAmount);
        }

        //rotates player certain amount left
        if(turnLeft > 0 && turns == 0)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(turnLeft) - yDir * Math.sin(turnLeft);
            yDir = oldxDir * Math.sin(turnLeft) + yDir * Math.cos(turnLeft);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(turnLeft) - yPlane * Math.sin(turnLeft);
            yPlane = oldxPlane * Math.sin(turnLeft) + yPlane * Math.cos(turnLeft);
            turns = 1;
        }

        //rotates player certain amount right
        if(turnRight > 0 && turns == 0)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(-turnRight) - yDir * Math.sin(-turnRight);
            yDir = oldxDir * Math.sin(-turnRight) + yDir * Math.cos(-turnRight);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(-turnRight) - yPlane * Math.sin(-turnRight);
            yPlane = oldxPlane * Math.sin(-turnRight) + yPlane * Math.cos(-turnRight);
            turns = 1;
        }
    }

    public void setPos(double x, double y)
    {
        //set new position
        xPos = x;
        yPos = y;
    }

    public void setDir(double x, double y)
    {
        //set new direction
        xDir = x;
        yDir = y;
    }

    public void setPlane(double x, double y)
    {
        //set new camera plane
        xPlane = x;
        yPlane = y;
    }

    public void turnLeft(double amount)
    {
        //turn to the left
        turnLeft = amount + 0.000001;
    }

    public void turnRight(double amount)
    {
        //turn to the right
        turnRight = amount + 0.000001;
    }

    public void resetTurn()
    {
        //resets turns so you can use turnLeft or turnRight again
        turns = 0;
    }
}