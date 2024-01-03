import java.awt.event.*;
public class Camera implements KeyListener
{
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    public double playerHeight = 0.5;
    private boolean allowJump;
    private boolean left, right, forward, back, jump;
    private boolean up = false;
    private double jumpAcceleration = -0.005;
    private double jumpHeight = 1.0;
    private double jumpSpeed = Math.sqrt(-2 * jumpAcceleration * jumpHeight);
    private boolean jumpHit = false;
    private final double MOVE_SPEED = .08;
    private final double ROTATION_SPEED = .045;
    private double turnLeft = 0;
    private double turnRight = 0;
    private int turns = 0;
    private int nonSolid = 0;
    private int finishX = 0;
    private int finishY = 0;
    public Vector ball;
    public Vector ballDir;
    public Vector startBall;
    public boolean shoot;
    private int[][] map;
    private int[][] map2;
    public Camera(double x, double y, double xd, double yd, double xp, double yp, boolean jump) 
    {   
        //gets starting location and direction
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
        turnLeft = 0;
        turnRight = 0;
        allowJump = jump;
        ball = new Vector(x, y);
        ballDir = new Vector(xd, yd);
        startBall = new Vector(x, y);
        shoot = false;
    }

    //checks if key is pressed
    public void keyPressed(KeyEvent key)
    {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;
        if((key.getKeyCode() == KeyEvent.VK_SPACE) && allowJump && playerHeight == 0.5 && map2[(int) xPos][(int) yPos] == 0)
        {
            jump = true;
            up = true;
            jumpHit = false;
        }
        else if((key.getKeyCode() == KeyEvent.VK_SPACE) && allowJump && playerHeight == 0.5)
        {
            jump = true;
            up = true;
            jumpHit = true;
        }
        else if((key.getKeyCode() == KeyEvent.VK_SPACE))
        {
            ball.setX(xPos);
            ball.setY(yPos);
            ballDir.setX(xDir/3);
            ballDir.setY(yDir/3);
            startBall.setX(xPos);
            startBall.setY(yPos);
            shoot = true;
        }
    }
    //checks if key is released
    public void keyReleased(KeyEvent key)
    {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }
    //checks if key is typed
    public void keyTyped(KeyEvent key)
    {

    }

    public void update(int[][] map)
    {
        this.map = map;
        //moves and turns player based on key input
        if(forward)
        {
            if(!((int)(xPos + xDir * MOVE_SPEED) < 0 || (int)(xPos + xDir * MOVE_SPEED) > map.length - 1 || (int)(yPos + yDir * MOVE_SPEED) < 0 || (int)(yPos + yDir * MOVE_SPEED) > map[0].length - 1))
            {
                if((map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == nonSolid))
                    xPos += xDir * MOVE_SPEED;
                if((map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == nonSolid))
                    yPos += yDir * MOVE_SPEED;
            }
        }
        if(back)
        {
            if(!((int)(xPos - xDir * MOVE_SPEED) < 0 || (int)(xPos - xDir * MOVE_SPEED) > map.length - 1 || (int)(yPos - yDir * MOVE_SPEED) < 0 || (int)(yPos - yDir * MOVE_SPEED) > map[0].length - 1))
            {
                if((map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == nonSolid))
                    xPos -= xDir * MOVE_SPEED;
                if((map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == nonSolid))
                    yPos -= yDir * MOVE_SPEED;
            }
        }
        if(right)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir * Math.sin(-ROTATION_SPEED);
            yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane * Math.sin(-ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane * Math.cos(-ROTATION_SPEED);
        }
        if(left)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
            yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);
        }
        //used to rotate player certain amount at start of
        //a new maze so they are not facing into a wall
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

    public void update(int[][] map, int[][] map2)
    {
        this.map = map;
        this.map2 = map2;
        //moves and turns player based on key input
        //uses physics to allow the player to jump
        if(forward)
        {
            if(playerHeight <= 1)
            {
                if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == nonSolid)
                    xPos+=xDir*MOVE_SPEED;
                if(map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == nonSolid)
                    yPos+=yDir*MOVE_SPEED;
            }
            else
            {
                if((map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == nonSolid) && (map2[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0 || map2[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == nonSolid))
                    xPos+=xDir*MOVE_SPEED;
                if((map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == nonSolid) && (map2[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0 || map2[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == nonSolid))
                    yPos+=yDir*MOVE_SPEED;
            }
        }
        if(back)
        {
            if(playerHeight <= 1)
            {
                if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == nonSolid)
                    xPos-=xDir*MOVE_SPEED;
                if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == nonSolid)
                    yPos-=yDir*MOVE_SPEED;
            }
            else
            {
                if((map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0 || map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == nonSolid) && (map2[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0 || map2[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == nonSolid))
                    xPos-=xDir*MOVE_SPEED;
                if((map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0 || map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == nonSolid) && (map2[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0 || map2[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == nonSolid))
                    yPos-=yDir*MOVE_SPEED;
            }
        }
        if(right)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir * Math.sin(-ROTATION_SPEED);
            yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane * Math.sin(-ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane * Math.cos(-ROTATION_SPEED);
        }
        if(left)
        {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
            yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
            yPlane =oldxPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);
        }
        if(jump)
        {
            if(playerHeight > 0.5 || up)
            {
                playerHeight += jumpSpeed;
                jumpSpeed += jumpAcceleration;
                up = false;
                if(jumpHit && playerHeight >= 0.8)
                {
                    jumpSpeed *= -0.05;
                    jumpHit = false;
                }
            }
            else
            {
                playerHeight = 0.5;
                jumpSpeed = Math.sqrt(-2 * jumpAcceleration * jumpHeight);
                jump = false;
            }
        }
        //playerHeight = 1.5;
        //used to rotate player certain amount at start of
        //a new maze so they are not facing into a wall
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
        turnLeft = amount;
    }

    public void turnRight(double amount)
    {
        //turn to the right
        turnRight = amount;
    }

    public void resetTurn()
    {
        //resets turns so you can use turnLeft or turnRight again
        turns = 0;
    }

    public void setNonSolid(int index)
    {
        nonSolid = index;
    }

    public void setShoot(boolean s)
    {
        shoot = s;
    }
}