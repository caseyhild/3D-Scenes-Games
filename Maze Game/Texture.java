import java.util.*;

public class Texture 
{
    //current frame
    private static int frame = 0;
    //all of the different textures
    public static Texture ball = new Texture(64, "ball");
    public static Texture blackwhite = new Texture(64, "blackwhite");
    public static Texture black = new Texture(64, "black");
    public static Texture bricks = new Texture(64, "bricks");
    public static Texture candycane = new Texture(64, "candycane");
    public static Texture circle = new Texture(64, "circle");
    public static Texture circles = new Texture(64, "circles");
    public static Texture collisions = new Texture(64, "collisions");
    public static Texture coolpattern = new Texture(64, "coolpattern");
    public static Texture cross = new Texture(64, "cross");
    public static Texture defaultTexture = new Texture(64, "default");
    public static Texture fakebricks = new Texture(64, "fakebricks");
    public static Texture flag = new Texture(64, "flag");
    public static Texture grass = new Texture(64, "grass");
    public static Texture gravel = new Texture(64, "gravel");
    public static Texture gray = new Texture(64, "gray");
    public static Texture pillar = new Texture(64, "pillar");
    public static Texture random = new Texture(64, "random");    
    public static Texture stars = new Texture(64, "stars");
    public static Texture saved = new Texture(64, "saved");
    public static Texture tester = new Texture(64, "tester");
    public static Texture wave = new Texture(64, "wave");
    public static Texture white = new Texture(64, "white");
    //variables for each texture
    private String name = "";
    public int SIZE = 64;
    public int[] pixels = new int[SIZE * SIZE];
    
    public Texture()
    {

    }

    public Texture(int size, String texName)
    {
        //name of texture
        name = texName;
        //size of texture (64)
        SIZE = size;
        //all pixels in texture
        pixels = new int[SIZE * SIZE];
        //what design there is for each texture
        if(texName.equals("ball"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int shade = 255 - (int) (255 * Math.sqrt(Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1) % SIZE - i % SIZE, 2) + Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1)/SIZE - i/SIZE, 2))/20.0);
                if(shade <= 1)
                    shade = 1;
                if(Math.sqrt(Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1) % SIZE - i % SIZE, 2) + Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1)/SIZE - i/SIZE, 2)) < 20)
                    pixels[i] = rgbNum(0, 0, shade);
                else if(Math.sqrt(Math.pow((SIZE * SIZE/2 + SIZE/2) % SIZE - i % SIZE, 2) + Math.pow((SIZE * SIZE/2 + SIZE/2)/SIZE - i/SIZE, 2)) < 20)
                    pixels[i] = rgbNum(0, 0, shade);
                else
                    pixels[i] = rgbNum(0, 0, 0);
            }
        }
        else if(texName.equals("black"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                pixels[i] = rgbNum(0, 0, 0);
            }
        }
        else if(texName.equals("blackwhite"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(i % 20 < 10)
                    pixels[i] = rgbNum(255, 255, 255);
                else
                    pixels[i] = rgbNum(0, 0, 0);
            }
        }
        else if(texName.equals("bricks"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int backgroundColor = rgbNum(128, 128, 128);
                int brickColor = rgbNum(128, 32, 0);
                if(i < 3 * SIZE || i >= (SIZE - 3) * SIZE)
                    pixels[i] = backgroundColor;
                else if(i/SIZE % 10 == 1 || i/SIZE % 10 == 2)
                    pixels[i] = backgroundColor;
                else if(i/SIZE % 20 >= 3 && i/SIZE % 20 <= 10 && (((i % SIZE % 20 == 1 || i % SIZE % 20 == 2) && !(i % SIZE < 3 || i % SIZE >= SIZE - 3)) || (i % SIZE < 1 || i % SIZE >= SIZE - 1)))
                    pixels[i] = backgroundColor;
                else if((i/SIZE % 20 >= 13 && i/SIZE % 20 <= 19 || i/SIZE % 20 == 0) && (i % SIZE % 20 == 11 || i % SIZE % 20 == 12))
                    pixels[i] = backgroundColor;
                else if(i % SIZE % 10 < 5)
                    pixels[i] = brickColor;
                else
                    pixels[i] = brickColor;
            }
        }
        else if(texName.equals("candycane"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(Math.abs(i % SIZE + i/SIZE) <= 5 || Math.abs(i % SIZE - SIZE/2 + i/SIZE) <= 5 || Math.abs(i % SIZE - SIZE + i/SIZE) <= 5 || Math.abs(i % SIZE - 3 * SIZE/2 + i/SIZE) <= 5 || Math.abs(i % SIZE - 2 * SIZE + i/SIZE) <= 5)
                    pixels[i] = rgbNum(255, 0, 0);
                else
                    pixels[i] = rgbNum(255, 255, 255);
            }
        }
        else if(texName.equals("circle"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(Math.sqrt(Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1) % SIZE - i % SIZE, 2) + Math.pow((SIZE * (SIZE/2 - 1) + SIZE/2 - 1)/SIZE - i/SIZE, 2)) <= SIZE/2)
                    pixels[i] = rgbNum(0, 255, 255);
                else if(Math.sqrt(Math.pow((SIZE * SIZE/2 + SIZE/2) % SIZE - i % SIZE, 2) + Math.pow((SIZE * SIZE/2 + SIZE/2)/SIZE - i/SIZE, 2)) <= SIZE/2)
                    pixels[i] = rgbNum(0, 255, 255);
                else
                    pixels[i] = rgbNum(255, 128, 0);
            }
        }
        else if(texName.equals("circles"))
        {
            int radius = 3;
            int num = 6;
            int[] pointsx = new int[num];
            int[] pointsy = new int[num];
            int[] startDeg = new int[num];
            int[] color = new int[num];
            color[0] = rgbNum(255, 0, 0);
            color[1] = rgbNum(255, 128, 0);
            color[2] = rgbNum(255, 255, 0);
            color[3] = rgbNum(0, 255, 0);
            color[4] = rgbNum(0, 0, 255);
            color[5] = rgbNum(192, 0, 192);
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(i % SIZE <= 2 || i % SIZE >= SIZE - 3 || i/SIZE <= 2 || i/SIZE >= SIZE - 3)
                    pixels[i] = rgbNum(64, 64, 64);
            }
            for(int i = 0; i < num; i++)
            {
                startDeg[i] = (int) (360.0/num * i);
                pointsx[i] = (int) (SIZE/2 - SIZE/4 * Math.cos(Math.toRadians(3 * frame + startDeg[i])));
                pointsy[i] = (int) (SIZE/2 - SIZE/4 * Math.sin(Math.toRadians(3 * frame + startDeg[i])));
                for(int y = -radius + 1; y < radius; y++)
                {
                    for(int x = -radius + 1; x < radius; x++)
                    {
                        if(pointsx[i] + x < SIZE && pointsx[i] - x >= 0 && Math.hypot(x, y) <= Math.hypot(radius - 1, radius - 2))
                            pixels[(pointsy[i] + y) * SIZE + (pointsx[i] + x)] = color[i];
                    }
                }
            }
        }
        else if(texName.equals("coolpattern"))
        {
            for(int i = 0; i < SIZE; i++)
            {
                for(int j = 0; j < SIZE; j++)
                {
                    int color = 0;
                    if(i < SIZE/2 && j < SIZE/2)
                        color = i * j;
                    else if(i < SIZE/2)
                        color = i * (SIZE - j);
                    else if(j < SIZE/2)
                        color = (SIZE - i) * j;
                    else
                        color = (SIZE - i) * (SIZE - j);
                    color *= 276;
                    pixels[i * SIZE + j] = color;
                }
            }
        }
        else if(texName.equals("cross"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(Math.abs(i % SIZE - i/SIZE) <= 2.5 || Math.abs(i % SIZE - SIZE + i/SIZE) <= 2.5)
                    pixels[i] = rgbNum(128, 0, 255);
                else
                    pixels[i] = rgbNum(0, 255, 0);
            }
        }
        else if(texName.equals("fakebricks"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int backgroundColor = rgbNum(128, 128, 128);
                int brickColor = rgbNum(128, 32, 0);
                if(i < 3 * SIZE || i >= (SIZE - 3) * SIZE)
                    pixels[i] = backgroundColor;
                else if(i/SIZE % 10 == 1 || i/SIZE % 10 == 2)
                    pixels[i] = backgroundColor;
                else if(i/SIZE % 20 >= 3 && i/SIZE % 20 <= 10 && (((i % SIZE % 20 == 1 || i % SIZE % 20 == 2) && !(i % SIZE < 3 || i % SIZE >= SIZE - 3)) || (i % SIZE < 1 || i % SIZE >= SIZE - 1)))
                    pixels[i] = backgroundColor;
                else if((i/SIZE % 20 >= 13 && i/SIZE % 20 <= 19 || i/SIZE % 20 == 0) && (i % SIZE % 20 == 11 || i % SIZE % 20 == 12))
                    pixels[i] = backgroundColor;
                else if(i % SIZE % 10 < 5)
                    pixels[i] = brickColor;
                else
                    pixels[i] = brickColor;
            }
        }
        else if(texName.equals("flag"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(i/SIZE % 10 < 4)
                    pixels[i] = rgbNum(255, 0, 0);
                else
                    pixels[i] = rgbNum(255, 255, 255);
            }
            for(int i = 0; i < SIZE * (SIZE/2 + 2); i++)
            {
                if(i % SIZE < SIZE/2 - 5)
                {
                    if(i % SIZE % 4 == 3 && i/SIZE % 6 == 4)
                        pixels[i] = rgbNum(255, 255, 255);
                    else if(i % SIZE % 4 == 1 && i/SIZE % 6 == 1 && (i > SIZE * 2 && i < SIZE * (SIZE/2 - 2) && i % SIZE > 2 && i % SIZE < SIZE/2 - 7))
                        pixels[i] = rgbNum(255, 255, 255);
                    else
                        pixels[i] = rgbNum(0, 0, 255);
                }
            }
        }
        else if(texName.equals("grass"))
        {
            int color = rgbNum(0, 128, 0);
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                double random = Math.random();
                int newColor = rgbNum((int) (random * getR(color)), (int) (random * getG(color)), (int) (random * getB(color)));
                pixels[i] = newColor;
            }
        }
        else if(texName.equals("gravel"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int shade = (int) (Math.random() * 128 + 64);
                pixels[i] = rgbNum(shade, shade, shade);
            }
        }
        else if(texName.equals("gray"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(i % SIZE <= 1 || i % SIZE >= SIZE - 2 || i/SIZE <= 1 || i/SIZE >= SIZE - 2)
                    pixels[i] = rgbNum(96, 96, 96);
                else 
                    pixels[i] = rgbNum(64, 64, 64);
            }
        }
        else if(texName.equals("pillar"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int shade = 255 - 12 * ((int) Math.abs(i % SIZE - SIZE/2));
                shade = Math.min(shade, 192);
                shade = Math.max(shade, 64);
                if(i % SIZE >= SIZE/2 - 10 && i % SIZE <= SIZE/2 + 10)
                    pixels[i] = rgbNum(shade, shade, shade);
                else if(i % SIZE >= SIZE/2 - 2 - (i % SIZE - i/SIZE) && i % SIZE < SIZE/2)
                    pixels[i] = rgbNum(shade, shade, shade);
                else if(i % SIZE <= 3 * SIZE/2 + (-i % SIZE - i/SIZE) && i % SIZE >= SIZE/2)
                    pixels[i] = rgbNum(shade, shade, shade);
                else if(i % SIZE >= 3 * SIZE/2 - 2 + (-i % SIZE - i/SIZE) && i % SIZE < SIZE/2)
                    pixels[i] = rgbNum(shade, shade, shade);
                else if(i % SIZE <= SIZE/2 - (i % SIZE - i/SIZE) && i % SIZE >= SIZE/2)
                    pixels[i] = rgbNum(shade, shade, shade);
                else
                    pixels[i] = rgbNum(0, 0, 0);
            }
        }
        else if(texName.equals("random"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                pixels[i] = rgbNum((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
            }
        }
        else if(texName.equals("stars"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                int star = (int) (Math.random() * 400);
                int up = 1;
                int down = 1;
                int left = 1;
                int right = 1;
                if(i >= SIZE && pixels[i - SIZE] != rgbNum(255, 255, 255))
                    up = 0;
                if(i < (SIZE - 1) * SIZE && pixels[i + SIZE] != rgbNum(255, 255, 255))
                    down = 0;
                if(i % SIZE >= 1 && pixels[i - 1] != rgbNum(255, 255, 255))
                    left = 0;
                if(i % SIZE < SIZE - 1 && pixels[i + 1] != rgbNum(255, 255, 255))
                    right = 0;
                int color;
                if(star == 0 && up == 0 && down == 0 && left == 0 && right == 0)
                    color = rgbNum(255, 255, 255);
                else
                    color = rgbNum(0, 0, 0);
                pixels[i] = color;
            }
        }
        else if(texName.equals("tester"))
        {
            for(int i = 0; i < SIZE; i++)
            {
                for(int j = 0; j < SIZE; j++)
                {
                    int color = 0;
                    int x = 0;
                    int y = 0;
                    if(i < SIZE/2 && j < SIZE/2)
                    {
                        x = SIZE/2 - j;
                        y = SIZE/2 - i;
                    }
                    else if(j < SIZE/2)
                    {
                        x = SIZE/2 - j;
                        y = i - SIZE/2 + 1;
                    }
                    else if(i < SIZE/2)
                    {
                        x = j - SIZE/2 + 1;
                        y = SIZE/2 - i;
                    }
                    else
                    {
                        x = j - SIZE/2 + 1;
                        y = i - SIZE/2 + 1;
                    }
                    color = (int) (256 * 5.55 * Math.sqrt(x * x + y * y));
                    pixels[i * SIZE + j] = color;
                }
            }
        }
        else if(texName.equals("wave"))
        {
            int radius = 3;
            int space = 1;
            int border = 0;
            int num = (SIZE - 2 * border)/space;
            int[] pointsx = new int[num];
            int[] pointsy = new int[num];
            int[] startDeg = new int[num];
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                pixels[i] = rgbNum(255, 255, 255);
            }
            for(int index = 0; index < num; index++){
                startDeg[index] = (int) (360.0 * (index * space + border)/SIZE);
                pointsx[index] = (int) (index * space + border);
                pointsy[index] = (int) (SIZE/2 - SIZE/4 * Math.sin(Math.toRadians(frame + startDeg[index])));
                pointsy[index] = (int) Math.min(pointsy[index], 3 * SIZE/4 - 1);
                for(int i = -2; i <= 3 * SIZE/4 - 1; i++)
                {
                    if(i <= 2)
                        pixels[(pointsy[index] + i) * SIZE + pointsx[index]] = rgbNum(0, 0, 255);
                    else
                        pixels[(int) Math.min(pointsy[index] + i, SIZE - 1) * SIZE + pointsx[index]] = rgbNum(0, 0, 0);
                }
            }
        }
        else if(texName.equals("white"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                pixels[i] = rgbNum(255, 255, 255);
            }
        }
    }

    public Texture(int size, String texName, int num, Vector[] position, Vector[] velocity, Vector[] acceleration, int[] ballSize, int[] color)
    {
        //name of texture
        name = texName;
        //size of texture (64)
        SIZE = size;
        //all pixels in texture
        pixels = new int[SIZE * SIZE];
        //what design there is for each texture
        if(texName.equals("collisions"))
        {
            updateCollisions(position, velocity, acceleration, ballSize, num);
            for(int i = 0; i < num; i++)
            {
                for(int j = i + 1; j < num; j++)
                    checkCollision(position, velocity, ballSize, i, j);
            }
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                if(i % SIZE <= 2 || i % SIZE >= SIZE - 3 || i/SIZE <= 2 || i/SIZE >= SIZE - 3)
                    pixels[i] = rgbNum(64, 64, 64);
            }
            for(int i = 0; i < num; i++)
            {
                for(int y = -ballSize[i]/3; y <= ballSize[i]/3; y++)
                {
                    for(int x = -ballSize[i]/3; x <= ballSize[i]/3; x++)
                    {
                        if(position[i].getX() + x < SIZE && position[i].getX() + x >= 0 && position[i].getY() + y < SIZE && position[i].getY() + y >= 0 && Math.hypot(x, y) + 4.5 <= Math.hypot(ballSize[i] - 1, ballSize[i] - 1))
                            pixels[(int) (position[i].getY() + y) * SIZE + (int) (position[i].getX() + x)] = color[i];
                    }
                }
            }
        }
    }

    public void update(String thisName, int frame)
    {
        //set the frame from the game class
        setFrame(frame);
        //update any individual texture
        if(thisName.equals("ball"))
            ball = new Texture(64, "ball");
        if(thisName.equals("black"))
            black = new Texture(64, "black");
        if(thisName.equals("blackwhite"))
            blackwhite = new Texture(64, "blackwhite");
        if(thisName.equals("bricks"))
            bricks = new Texture(64, "bricks");
        if(thisName.equals("candycane"))
            candycane = new Texture(64, "candycane");
        if(thisName.equals("circle"))
            circle = new Texture(64, "circle");
        if(thisName.equals("circles"))
            circles = new Texture(64, "circles");
        if(thisName.equals("coolpattern"))
            coolpattern = new Texture(64, "coolpattern");
        if(thisName.equals("cross"))
            cross = new Texture(64, "cross");
        if(thisName.equals("default"))
            defaultTexture = new Texture(64, "default");
        if(thisName.equals("fakebricks"))
            fakebricks = new Texture(64, "fakebricks");
        if(thisName.equals("flag"))
            flag = new Texture(64, "flag");
        if(thisName.equals("grass"))
            grass = new Texture(64, "grass");
        if(thisName.equals("gravel"))
            gravel = new Texture(64, "gravel");
        if(thisName.equals("gray"))
            gray = new Texture(64, "gray");
        if(thisName.equals("random"))
            random = new Texture(64, "random");
        if(thisName.equals("pillar"))
            pillar = new Texture(64, "pillar");
        if(thisName.equals("stars"))
            stars = new Texture(64, "stars");
        if(thisName.equals("saved"))
            saved = new Texture(64, "saved");
        if(thisName.equals("tester"))
            tester = new Texture(64, "tester");
        if(thisName.equals("wave"))
            wave = new Texture(64, "wave");
        if(thisName.equals("white"))
            white = new Texture(64, "white");
    }

    public void update(String thisName, int frame, int num, Vector[] position, Vector[] velocity, Vector[] acceleration, int[] ballSize, int[] color)
    {
        //set the frame from the game class
        setFrame(frame);
        //update any individual texture
        if(thisName.equals("collisions"))
            collisions = new Texture(64, "collisions", num, position, velocity, acceleration, ballSize, color);
    }

    public void updateCollisions(Vector[] position, Vector[] velocity, Vector[] acceleration, int[] ballSize, int num)
    {
        for(int i = 0; i < num; i++)
        {
            position[i].add(velocity[i]);
            velocity[i].add(acceleration[i]);
            checkBoundaries(position, velocity, ballSize, num);
        }
    }

    public void checkBoundaries(Vector[] position, Vector[] velocity, int[] ballSize, int num)
    {
        for(int i = 0; i < num; i++)
        {
            if(position[i].getX() < ballSize[i])
            {
                position[i].setX(ballSize[i]);
                velocity[i].setX(-velocity[i].getX());
            }
            if(position[i].getX() > SIZE - ballSize[i])
            {
                position[i].setX(SIZE - ballSize[i]);
                velocity[i].setX(-velocity[i].getX());
            }
            if(position[i].getY() < ballSize[i])
            {
                position[i].setY(ballSize[i]);
                velocity[i].setY(-velocity[i].getY());
            }
            if(position[i].getY() > SIZE - ballSize[i])
            {
                position[i].setY(SIZE - ballSize[i]);
                velocity[i].setY(-velocity[i].getY());
            }
        }
    }

    public void checkCollision(Vector[] position, Vector[] velocity, int[] ballSize, int a, int b)
    {
        double dist = Math.sqrt((position[a].getX() - position[b].getX()) * (position[a].getX() - position[b].getX()) + (position[a].getY() - position[b].getY()) * (position[a].getY() - position[b].getY()));
        if(dist <= (ballSize[a]/2 + ballSize[b]/2) * 0.8)
        {
            doCollision(position, velocity, ballSize, a, b);
            separate(position, velocity, ballSize, a, b , dist);
        }
    }

    public void separate(Vector[] position, Vector[] velocity, int[] ballSize, int a, int b, double dist)
    {
        double amt = (ballSize[a]/2 + ballSize[b]/2 - dist)/2;
        double angle = Math.asin((position[a].getY() - position[b].getY())/dist);
        if(position[a].getX() - position[b].getX() < 0)
            angle = Math.PI - angle;
        else if(position[a].getX() - position[b].getX() > 0 && position[a].getY() - position[b].getY() < 0)
            angle += 2 * Math.PI;
        Vector dir = new Vector(Math.cos(angle), Math.sin(angle));
        dir.normalize();
        dir.mult(amt);
        position[a].add(dir);
        position[b].sub(dir);
    }

    public void doCollision(Vector[] position, Vector[] velocity, int[] ballSize, int a, int b)
    {
        double v1 = velocity[a].mag();
        double v2 = velocity[b].mag();
        double v1x = velocity[a].getX();
        double v2x = velocity[b].getX();
        double v1y = velocity[a].getY();
        double v2y = velocity[b].getY();
        double dx = position[b].getX() - position[a].getX();
        double dy = position[b].getY() - position[a].getY();
        double angle;
        if(Math.abs(dx) <= 0.000001)
            angle = Math.PI/2;
        else
            angle = Math.atan(dy/dx);
        double angle1 = findAngle(v1x, v1y) * Math.PI/180;
        double angle2 = findAngle(v2x, v2y) * Math.PI/180;
        double v1xr = v1 * Math.cos(angle1 - angle);
        double v1yr = v1 * Math.sin(angle1 - angle);
        double v2xr = v2 * Math.cos(angle2 - angle);
        double v2yr = v2 * Math.sin(angle2 - angle);
        double v1xrf = ((ballSize[a] - ballSize[b]) * v1xr + 2 * ballSize[b] * v2xr)/(ballSize[a] + ballSize[b]);
        double v2xrf = (2 * ballSize[a] * v1xr + (ballSize[b] - ballSize[a]) * v2xr)/(ballSize[a] + ballSize[b]);
        double v1yrf = v1yr;
        double v2yrf = v2yr;
        double v1xf = Math.cos(angle) * v1xrf + Math.cos(angle + Math.PI/2) * v1yrf;
        double v1yf = Math.sin(angle) * v1xrf + Math.sin(angle + Math.PI/2) * v1yrf;
        double v2xf = Math.cos(angle) * v2xrf + Math.cos(angle + Math.PI/2) * v2yrf;
        double v2yf = Math.sin(angle) * v2xrf + Math.sin(angle + Math.PI/2) * v2yrf;
        velocity[a].setX(v1xf);
        velocity[a].setY(v1yf);
        velocity[b].setX(v2xf);
        velocity[b].setY(v2yf);
    }

    public double findAngle(double x, double y)
    {
        double angle;
        if(x < -0.000001)
            angle = 180 + Math.atan(y/x) * 180/Math.PI;
        else if(x > 0.000001 && y >= -0.000001)
            angle = Math.atan(y/x) * 180/Math.PI;
        else if(x > 0.000001 && y < -0.000001)
            angle = 360 + Math.atan(y/x) * 180/Math.PI;
        else if(Math.abs(x) <= 0.000001 && Math.abs(y) <= 0.000001)
            angle = 0;
        else if(Math.abs(x) <= 0.000001 && y >= -0.000001)
            angle = 90;
        else
            angle = 270;
        return angle;
    }

    public void setFrame(int f)
    {
        //set the frame
        frame = f;
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
}