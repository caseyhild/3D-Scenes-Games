import java.util.*;
public class Texture
{
    //current frame
    private static int frame = 0;
    //all of the different textures
    public static Texture bricks = new Texture(64, "bricks");
    public static Texture grass = new Texture(64, "grass");
    public static Texture gravel = new Texture(64, "gravel");
    public static Texture gray = new Texture(64, "gray");
    public static Texture noise = new Texture(64, "noise");
    //variables for each texture
    private String name = "";
    private Vector3D color;
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
        if(texName.equals("bricks"))
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
    }

    public Texture(int size, String texName, Vector3D col)
    {
        //name of texture
        name = texName;
        //size of texture (64)
        SIZE = size;
        //all pixels in texture
        pixels = new int[SIZE * SIZE];
        //color for texture
        color = new Vector3D(col);
        //what design there is for each texture
        if(texName.equals("solidColor"))
        {
            for(int i = 0; i < SIZE * SIZE; i++)
            {
                pixels[i] = rgbNum((int) color.x, (int) color.y, (int) color.z);
            }
        }
    }

    public void update(String thisName, int frame)
    {
        //set the frame from the game class
        setFrame(frame);
        //update any individual texture
        if(thisName.equals("bricks"))
            bricks = new Texture(64, "bricks");
        if(thisName.equals("grass"))
            grass = new Texture(64, "grass");
        if(thisName.equals("gravel"))
            gravel = new Texture(64, "gravel");
        if(thisName.equals("gray"))
            gray = new Texture(64, "gray");
        if(thisName.equals("noise"))
            noise = new Texture(64, "noise");
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