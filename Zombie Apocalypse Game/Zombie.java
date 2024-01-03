import java.io.*;
public class Zombie
{
    public Zombie(PrintWriter outFile)
    {
        this(outFile, 0, 0, 0);
    }

    public Zombie(PrintWriter outFile, double xCenter, double yCenter, double zCenter)
    {
        //zombie
        for(double x = -0.2; x <= 0.2; x += 0.05)
        {
            for(double y = -0.2; y <= 0.2; y += 0.05)
            {
                for(double z = -0.2; z <= 0.2; z += 0.05)
                {
                    if(x * x + y * y + z * z <= 0.04)
                    {
                        int shade = (int) (255 * (z + 0.8)/1.1);
                        outFile.printf("%.4f", (x + xCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (y + yCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (z + zCenter + 0.4));
                        outFile.print("  ");
                        outFile.println(rgbNum(shade, shade, shade));
                    }
                }
            }
        }
        for(double x = -0.05; x <= 0.05; x += 0.05)
        {
            for(double y = -0.05; y <= 0.05; y += 0.05)
            {
                for(double z = 0.3; z <= 0.85; z += 0.05)
                {
                    if(x * x + y * y <= 0.0025)
                    {
                        int shade = (int) (255 * (z - 0.1)/1.1);
                        outFile.printf("%.4f", (x + xCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (y + yCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (z + zCenter - 0.5));
                        outFile.print("  ");
                        outFile.println(rgbNum(shade, shade, shade));
                    }
                }
            }
        }
        for(double x = -0.05; x <= 0.05; x += 0.05)
        {
            for(double y = -0.2; y <= 0.2; y += 0.05)
            {
                for(double z = -0.05; z <= 0.05; z += 0.05)
                {
                    if(x * x + z * z <= 0.0025)
                    {
                        int shade = (int) (255 * (z + 0.4)/1.1);
                        outFile.printf("%.4f", (x + xCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (y + yCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (z + zCenter));
                        outFile.print("  ");
                        outFile.println(rgbNum(shade, shade, shade));
                    }
                }
            }
        }
        for(double x = -0.05; x <= 0.05; x += 0.05)
        {
            for(double y = -0.15; y <= 0.15; y += 0.025)
            {
                for(double z = -0.05; z <= 0.05; z += 0.05)
                {
                    if(x * x + z * z <= 0.0025)
                    {
                        int shade = (int) (255 * (z + 0.2 - 2 * Math.abs(y))/1.1);
                        if(shade < 0)
                            shade = 0;
                        outFile.printf("%.4f", (x + xCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (y + yCenter));
                        outFile.print("  ");
                        outFile.printf("%.4f", (-2 * Math.abs(y) + z + zCenter - 0.2));
                        outFile.print("  ");
                        outFile.println(rgbNum(shade, shade, shade));
                    }
                }
            }
        }
        outFile.close();
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