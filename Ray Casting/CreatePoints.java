import java.io.*;
public class CreatePoints
{
    private String folder;
    
    public CreatePoints(String folder) throws IOException
    {
        this.folder = folder;
        Tree tree = new Tree(newFile("tree.txt"));
        Spiral spiral = new Spiral(newFile("spiral.txt"));
    }

    private PrintWriter newFile(String file) throws IOException
    {
        return new PrintWriter(new File(folder + "/" + file));
    }
}