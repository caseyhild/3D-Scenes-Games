import java.io.*;
public class Tree
{
    public Tree(PrintWriter outFile)
    {
        int num = 10;
        double x = 0;
        double y = 0;
        double z = 1 - 0.75/num;
        double dx = 0.0;
        double dy = 0.0;
        for(int i = 0; i < 2 * num; i++)
        {
            z -= 0.75/num;
            outFile.printf("%.4f", x);
            outFile.print("  ");
            outFile.printf("%.4f", y);
            outFile.print("  ");
            outFile.printf("%.4f", z);
            outFile.print("  ");
            outFile.println(rgbNum(64, 32, 0));
        }
        for(int deg = 0; deg < 360; deg += 10)
        {
            x = 0;
            y = 0;
            z = 1;
            dx = 0.5 * Math.cos(Math.toRadians(deg));
            dy = 0.5 * Math.sin(Math.toRadians(deg));
            for(int i = 0; i < num; i++)
            {
                x += dx/num;
                y += dy/num;
                z -= 1.2/num;
                outFile.printf("%.4f", x);
                outFile.print("  ");
                outFile.printf("%.4f", y);
                outFile.print("  ");
                outFile.printf("%.4f", z);
                outFile.print("  ");
                outFile.println(rgbNum(0, 64 + (int) (Math.random() * 64), 0));
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