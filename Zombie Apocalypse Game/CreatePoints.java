import java.io.*;
public class CreatePoints
{
    private String folder;
    
    public CreatePoints(String folder) throws IOException
    {
        this.folder = folder;
        Tree tree = new Tree(newFile("tree.txt"));
        Zombie zombie = new Zombie(newFile("zombie.txt"));
        Sphere sphere = new Sphere(newFile("sphere.txt"));
    }

    private PrintWriter newFile(String file) throws IOException
    {
        return new PrintWriter(new File(folder + "/" + file));
    }
}