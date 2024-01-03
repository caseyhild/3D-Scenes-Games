import java.util.*;
import java.awt.*;
public class Screen
{
    private int frame;
    private int[][] map;
    private int[][] map2;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private int mapWidth, mapHeight, width, height;
    private double playerHeight;
    private ArrayList<Texture> textures;
    private double[] ZBuffer;
    private ArrayList<PointsFile> files;
    private ArrayList<ArrayList<Vector3D>> points;
    private int shotTimer;
    private ArrayList<Shot> shots;
    private Vector player;
    private boolean playerDead;
    private int score;

    public Screen(int[][] m, int[][] fm, int[][] cm, int mapW, int mapH, ArrayList<Texture> tex, ArrayList<PointsFile> files, int w, int h)
    {
        frame = 0;
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        mapWidth = mapW;
        mapHeight = mapH;
        playerHeight = 0.5;
        textures = tex;
        width = w;
        height = h;
        ZBuffer = new double[width];
        this.files = files;
        shotTimer = 0;
        shots = new ArrayList<Shot>();
        player = new Vector();
        playerDead = false;
        score = 0;
    }

    public int[] updateGame(Camera camera, int[] pixels, int[][] m, int[][] fm, int[][] cm, int f)
    {
        frame = f;
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        player = new Vector(camera.xPos, camera.yPos);
        int texX = 0;
        int texY = 0;
        double[] colored = new double[width * height];
        //loops through all x-coordinates of the screen
        for(int x = 0; x < width; x++)
        {
            double cameraX = 2.0 * x / width - 1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;
            //Map position
            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;
            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side = 0;//was the wall vertical or horizontal
            //Figure out the step direction and initial distance to a side
            if(rayDirX < 0)
            {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            }
            else
            {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if(rayDirY < 0)
            {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            }
            else
            {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }
            //Loop to find where the ray hits a wall
            while(!hit)
            {
                //Jump to next square
                if(sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                if(mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight)
                    hit = true;
                else if(map[mapX][mapY] > 0) 
                    hit = true;
            }
            //Calculate distance to the point of impact
            if(side == 0)
                perpWallDist = (mapX - camera.xPos + (1 - stepX) / 2) / rayDirX;
            else
                perpWallDist = (mapY - camera.yPos + (1 - stepY) / 2) / rayDirY;  
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0)
                lineHeight = (int) (height / perpWallDist);
            else
                lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2 + height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;
            //add a texture
            int texNum = 0;
            if(mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight)
                texNum = -1;
            else
                texNum = map[mapX][mapY] - 1;
            double wallX;//Exact position of where wall was hit
            if(side == 1)
                wallX = camera.xPos + perpWallDist * rayDirX;
            else
                wallX = camera.yPos + perpWallDist * rayDirY;
            wallX -= Math.floor(wallX);
            //x coordinate on the texture
            if(texNum >= 0)
            {
                texX = (int) (wallX * textures.get(texNum).SIZE);
                if(side == 0 && rayDirX > 0)
                    texX = textures.get(texNum).SIZE - texX - 1;
                if(side == 1 && rayDirY < 0)
                    texX = textures.get(texNum).SIZE - texX - 1;
            }
            //calculate y coordinate on texture
            if(texNum >= 0)
            {
                double a = 1.0 * textures.get(texNum).SIZE / (2 * lineHeight);
                double b = (-height + lineHeight) * a;
                a *= 2;
                for(int y = drawStart; y < drawEnd; y++) 
                {
                    texY = (int) (y * a + b);
                    int color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                    if(side == 1)
                        color = (color >> 1) & 8355711;//Make y sides darker
                    pixels[x + y * width] = color;
                    if(textures.get(texNum) != Texture.black)
                        colored[x + y * width] = (mapX + wallX - camera.xPos) * (mapX + wallX - camera.xPos) + (mapY - camera.yPos) * (mapY - camera.yPos) - mapWidth * mapHeight;
                }
            }
            ZBuffer[x] = perpWallDist;
            //coordinates of floor at bottom of wall
            double floorXWall;
            double floorYWall;
            if(side == 0 && rayDirX > 0)
            {
                floorXWall = mapX;
                floorYWall = mapY + wallX;
            }
            else if(side == 0 && rayDirX < 0)
            {
                floorXWall = mapX + 1.0;
                floorYWall = mapY + wallX;
            }
            else if(side == 1 && rayDirY > 0)
            {
                floorXWall = mapX + wallX;
                floorYWall = mapY;
            }
            else
            {
                floorXWall = mapX + wallX;
                floorYWall = mapY + 1.0;
            }
            double distWall = perpWallDist;
            if(drawEnd < 0) 
                drawEnd = height;
            //loops through y-coordinates from bottom of wall to bottom of screen
            for(int y = drawEnd + 1; y < height; y++)
            {
                //calculates color on texture for each pixel of the floor
                double weight = height / (distWall * (2 * y - height));
                double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
                int floorTexX = (int) (currentFloorX * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexY = (int) (currentFloorY * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexture = floorMap[(int) currentFloorX][(int) currentFloorY];
                int ceilingTexture = ceilingMap[(int) currentFloorX][(int) currentFloorY];
                int floorColor = textures.get(floorTexture - 1).pixels[textures.get(floorTexture - 1).SIZE * floorTexY + floorTexX];
                int ceilingColor = textures.get(ceilingTexture - 1).pixels[textures.get(ceilingTexture - 1).SIZE * floorTexY + floorTexX];
                pixels[width * y + x] = floorColor;
                pixels[width * (height - y) + x] = ceilingColor;
                if(textures.get(ceilingTexture - 1) != Texture.black)
                    colored[x + (height - y) * width] = (currentFloorX - camera.xPos) * (currentFloorX - camera.xPos) + (currentFloorY - camera.yPos) * (currentFloorY - camera.yPos) - mapWidth * mapHeight;
            }
            pixels[width * drawEnd + x] = pixels[width * (drawEnd - 1) + x];
        }
        //drawing 3D points
        double px = 0.0;
        double py = 0.0;
        double pz = 0.0;
        int pcolor = 0;
        for(int i = 0; i < points.size(); i++)
        {
            PointsFile file = files.get(i);
            Vector[] velocities = new Vector[points.get(i).size()];
            for(int ctr = 0; ctr < file.length; ctr++)
            {
                px = file.x[ctr];
                py = file.y[ctr];
                pz = file.z[ctr];
                pcolor = file.color[ctr];
                for(int j = 0; j < points.get(i).size(); j++)
                {
                    if(i == 2 && ctr == 0)
                    {
                        Vector zombie = new Vector(points.get(i).get(j).getX(), points.get(i).get(j).getY());
                        velocities[j] = Vector.mult(Vector.normalize(Vector.sub(player, zombie)), 0.02);
                        if(Vector.sub(player, zombie).mag() < 0.3)
                            playerDead = true;
                        double minWallDist = 0.2;
                        points.get(i).get(j).setX(points.get(i).get(j).getX() + velocities[j].getX());
                        points.get(i).get(j).setY(points.get(i).get(j).getY() + velocities[j].getY());
                        if(map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0)
                            points.get(i).get(j).setX(points.get(i).get(j).getX() - velocities[j].getX());
                        if(map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0)
                        {
                            points.get(i).get(j).setX(points.get(i).get(j).getX() + velocities[j].getX());
                            points.get(i).get(j).setY(points.get(i).get(j).getY() - velocities[j].getY());
                        }
                        if(map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() + minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() + minWallDist)] > 0 || map[(int) (points.get(i).get(j).getX() - minWallDist)][(int) (points.get(i).get(j).getY() - minWallDist)] > 0)
                            points.get(i).get(j).setX(points.get(i).get(j).getX() - velocities[j].getX());
                    }
                    px += points.get(i).get(j).getX();
                    py += points.get(i).get(j).getY();
                    pz += points.get(i).get(j).getZ();
                    if(i == 2)
                    {
                        Vector zombie = new Vector(points.get(i).get(j).getX(), points.get(i).get(j).getY());
                        Vector v = rotateZ3D(px, py, findAngle(Vector.sub(player, zombie).getX(), Vector.sub(player, zombie).getY()), zombie.getX(), zombie.getY());
                        px = v.getX();
                        py = v.getY();
                    }
                    double pointX = px - camera.xPos;
                    double pointY = py - camera.yPos;
                    double pointZ = pz - playerHeight;
                    double invDet = camera.xPlane * camera.yDir - camera.xDir * camera.yPlane;
                    double transformX = invDet * (camera.yDir * pointX - camera.xDir * pointY);
                    double transformY = invDet * (-camera.yPlane * pointX + camera.xPlane * pointY);
                    int pointScreenX = (int) (width/2 * (1 + transformX/transformY));
                    int pointHeight = (int) (height/transformY);
                    int pointWidth = pointHeight;
                    int drawStartY = -pointHeight/64 + height/2 - (int) (3 * pointHeight/8 * pointZ);
                    if(drawStartY < 0) 
                        drawStartY = 0;
                    int drawEndY = pointHeight/64 + height/2 - (int) (3 * pointHeight/8 * pointZ);
                    if(drawEndY >= height) 
                        drawEndY = height - 1;
                    int drawStartX = -pointWidth/64 + pointScreenX;
                    if(drawStartX < 0)
                        drawStartX = 0;
                    int drawEndX = pointWidth/64 + pointScreenX;
                    if(drawEndX >= width)
                        drawEndX = width - 1;
                    for(int drawX = drawStartX; drawX <= drawEndX; drawX++)
                    {
                        if(transformY > 0 && transformY < ZBuffer[drawX])
                        {
                            for(int drawY = drawStartY; drawY <= drawEndY; drawY++)
                            {
                                if(pointX * pointX + pointY * pointY - mapWidth * mapHeight < colored[drawY * width + drawX])
                                {
                                    pixels[drawY * width + drawX] = pcolor;
                                    colored[drawY * width + drawX] = pointX * pointX + pointY * pointY - mapWidth * mapHeight;
                                }
                            }
                        }
                    }
                    if(i == 2)
                    {
                        Vector zombie = new Vector(points.get(i).get(j).getX(), points.get(i).get(j).getY());
                        Vector v = rotateZ3D(px, py, -findAngle(Vector.sub(player, zombie).getX(), Vector.sub(player, zombie).getY()), zombie.getX(), zombie.getY());
                        px = v.getX();
                        py = v.getY();
                    }
                    px -= points.get(i).get(j).getX();
                    py -= points.get(i).get(j).getY();
                    pz -= points.get(i).get(j).getZ();
                }
            }
        }
        //shots
        if(camera.shoot && shotTimer >= 30)
        {
            shots.add(new Shot(player.getX(), player.getY(), camera.xDir/3, camera.yDir/3));
            shotTimer = 0;
        }
        for(int i = 0; i < shots.size(); i++)
        {
            Shot shot = shots.get(i);
            if(camera.shoot || (!shot.pos.equals(shot.initialPos) && shot.pos.getX() >= 0 && shot.pos.getX() < mapWidth && shot.pos.getY() >= 0 && shot.pos.getY() < mapHeight && map[(int) shot.pos.getX()][(int) shot.pos.getY()] == 0))
                shot.pos.add(shot.dir);
            else
                shots.remove(i);
            //drawing shot
            if(!shot.pos.equals(shot.initialPos))
            {
                px = shot.pos.getX();
                py = shot.pos.getY();
                pz = 0.5;
                pcolor = rgbNum(255, 0, 0);
                double pointX = px - camera.xPos;
                double pointY = py - camera.yPos;
                double pointZ = pz - playerHeight;
                double invDet = 1.0 * (camera.xPlane * camera.yDir - camera.xDir * camera.yPlane);
                double transformX = invDet * (camera.yDir * pointX - camera.xDir * pointY);
                double transformY = invDet * (-camera.yPlane * pointX + camera.xPlane * pointY);
                int pointScreenX = (int) ((width/2) * (1 + transformX/transformY));
                int pointHeight = (int) Math.abs((int) (height/transformY));
                pointHeight /= 4;
                pointHeight += 8;
                pointHeight = Math.max(32, Math.min(pointHeight, 512));
                int drawStartY = -pointHeight/32 + height/2 - (int) (3 * pointHeight/4 * pointZ);
                if(drawStartY <= 0) 
                    drawStartY = 0;
                int drawEndY = pointHeight/32 + height/2 - (int) (3 * pointHeight/4 * pointZ);
                if(drawEndY >= height) 
                    drawEndY = height - 1;
                int pointWidth = Math.abs((int) (height/transformY));
                pointWidth /= 4;
                pointWidth += 8;
                pointWidth = Math.max(32, Math.min(pointWidth, 512));
                int drawStartX = -pointWidth/32 + pointScreenX;
                if(drawStartX <= 0)
                    drawStartX = 0;
                int drawEndX = pointWidth/32 + pointScreenX;
                if(drawEndX >= width)
                    drawEndX = width - 1;
                for(int drawX = drawStartX; drawX <= drawEndX; drawX++)
                {
                    if(transformY > 0 && drawX > 0 && drawX < width && transformY < ZBuffer[drawX])
                    {
                        for(int drawY = drawStartY; drawY <= drawEndY; drawY++)
                        {
                            if(pointX * pointX + pointY * pointY - mapWidth * mapHeight < colored[drawY * width + drawX])
                            {
                                pixels[drawY * width + drawX] = pcolor;
                                colored[drawY * width + drawX] = pointX * pointX + pointY * pointY - mapWidth * mapHeight;
                            }
                        }
                    }
                }
            }
            for(int j = 0; j < points.get(2).size(); j++)
            {
                if(Math.sqrt((shot.pos.getX() - points.get(2).get(j).getX()) * (shot.pos.getX() - points.get(2).get(j).getX()) + (shot.pos.getY() - points.get(2).get(j).getY()) * (shot.pos.getY() - points.get(2).get(j).getY())) < 0.25)
                {
                    points.get(2).remove(j);
                    points.get(2).add(j, new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                    while(points.get(2).get(j).getX() > mapWidth/3 - 0.5 && points.get(2).get(j).getX() < 2 * mapWidth/3 + 0.5 && points.get(2).get(j).getY() > mapHeight/3 - 0.5 && points.get(2).get(j).getY() < 2 * mapHeight/3 + 0.5)
                    {
                        points.get(2).remove(j);
                        points.get(2).add(j, new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                    }
                    points.get(2).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                    while(points.get(2).get(points.get(2).size() - 1).getX() > mapWidth/3 - 0.5 && points.get(2).get(points.get(2).size() - 1).getX() < 2 * mapWidth/3 + 0.5 && points.get(2).get(points.get(2).size() - 1).getY() > mapHeight/3 - 0.5 && points.get(2).get(points.get(2).size() - 1).getY() < 2 * mapHeight/3 + 0.5)
                    {
                        points.get(2).remove(points.get(2).size() - 1);
                        points.get(2).add(new Vector3D(Math.random() * (mapWidth - 3) + 1.5, Math.random() * (mapHeight - 3) + 1.5, 0.5));
                    }
                    shots.remove(i);
                    score++;
                    break;
                }
            }
        }
        camera.shoot = false;
        shotTimer++;
        return pixels;
    }

    public Vector rotateZ3D(double x, double y, double theta, double xPt, double yPt) 
    {
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        double xCopy = x - xPt;
        double yCopy = y - yPt;
        return new Vector(xPt + xCopy * cosTheta - yCopy * sinTheta, yPt + yCopy * cosTheta + xCopy * sinTheta);
    } 

    public Vector rotateY3D(double x, double z, double theta, double xPt, double zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        double xCopy = x - xPt;
        double zCopy = z - zPt;
        return new Vector(xPt + xCopy * cosTheta - zCopy * sinTheta, zPt + zCopy * cosTheta + xCopy * sinTheta);
    } 

    public Vector rotateX3D(double y, double z, double theta, double yPt, double zPt)
    { 
        double sinTheta = Math.sin(Math.toRadians(theta));
        double cosTheta = Math.cos(Math.toRadians(theta));
        double yCopy = y - yPt;
        double zCopy = z - zPt;
        return new Vector(yPt + yCopy * cosTheta - zCopy * sinTheta, zPt + zCopy * cosTheta + yCopy * sinTheta);
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

    public void setPoints(ArrayList<ArrayList<Vector3D>> p)
    {
        points = p;
    }

    public boolean isDead()
    {
        return playerDead;
    }

    public int getScore()
    {
        return score;
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