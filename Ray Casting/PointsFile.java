public class PointsFile
{
    public int length;
    public double[] x;
    public double[] y;
    public double[] z;
    public int[] color;
    
    public PointsFile(int length)
    {
        this.length = length;
        x = new double[length];
        y = new double[length];
        z = new double[length];
        color = new int[length];
    }
}