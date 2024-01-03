import java.io.*;
public class Spiral
{
    public Spiral(PrintWriter outFile)
    {
        int numVertical = 10;
        int numAround = 6;
        double radius = 0.2;
        double height = 0.6;
        double twistAngle = 120;
        double x, y, z;
        for(double deg = 0; deg < 360; deg += 360.0/numAround)
        {
            for(int i = 0; i < numVertical; i++)
            {
                x = radius * Math.cos(Math.toRadians(deg + i * twistAngle/numVertical));
                y = radius * Math.sin(Math.toRadians(deg + i * twistAngle/numVertical));
                z = -height/2 + height * i/(numVertical - 1);
                outFile.printf("%.4f", x);
                outFile.print("  ");
                outFile.printf("%.4f", y);
                outFile.print("  ");
                outFile.printf("%.4f", z);
                outFile.print("  ");
                outFile.println(rgbNum(128 + (int) (128 * z/height), 0, 0));
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