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
    private Vector3D player;
    private int shift;

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
        player = new Vector3D();
        shift = 0;
    }

    public int[] updateGame(Camera camera, int[] pixels, int[][] m, int[][] fm, int[][] cm, int f)
    {
        frame = f;
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        player = new Vector3D(camera.xPos, camera.yPos, playerHeight);
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
            double sideDistX, sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            //perpendicular distance to the wall
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
            double wallX;//Exact position of where wall was hit
            //Calculate distance to the point of impact
            if(side == 0)
            {
                perpWallDist = (mapX - camera.xPos + (1 - stepX) / 2) / rayDirX;
                wallX = camera.yPos + perpWallDist * rayDirY;
            }
            else
            {
                perpWallDist = (mapY - camera.yPos + (1 - stepY) / 2) / rayDirY;
                wallX = camera.xPos + perpWallDist * rayDirX;
            }
            //Get fractional part of wallX
            wallX -= (int) wallX;
            //Calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0)
                lineHeight = (int) (height / perpWallDist);
            else
                lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2 + height/2;
            if(drawStart < -shift)
                drawStart = -shift;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height - shift) 
                drawEnd = height - 1 - shift;
            //add a texture
            int texNum = 0;
            if(mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight)
                texNum = -1;
            else
                texNum = map[mapX][mapY] - 1;
            //x coordinate on the texture
            if(texNum >= 0)
            {
                texX = (int) (wallX * textures.get(texNum).SIZE);
                if(side == 0 && rayDirX > 0)
                    texX = textures.get(texNum).SIZE - texX - 1;
                if(side == 1 && rayDirY < 0)
                    texX = textures.get(texNum).SIZE - texX - 1;
                //calculate y coordinate on texture
                double a = 1.0 * textures.get(texNum).SIZE / lineHeight;
                double b = (-height/2 + lineHeight/2) * a;
                for(int y = drawStart; y < drawEnd; y++)
                {
                    texY = (int) (y * a + b);
                    int color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                    if(side == 1)
                        color = (color >> 1) & 8355711;//Make y sides darker
                    pixels[x + Math.max(0, Math.min(y + shift, height - 1)) * width] = color;
                    if(textures.get(texNum) != Texture.black)
                        colored[x + Math.max(0, Math.min(y + shift, height - 1)) * width] = (mapX + wallX - camera.xPos) * (mapX + wallX - camera.xPos) + (mapY - camera.yPos) * (mapY - camera.yPos) - mapWidth * mapHeight;
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
            floorXWall = Math.max(0, Math.min(floorXWall, mapWidth - 1));
            floorYWall = Math.max(0, Math.min(floorYWall, mapHeight - 1));
            double distWall = perpWallDist;
            if(drawEnd < 0) 
                drawEnd = height - shift;
            //loops through y-coordinates from bottom of wall to bottom of screen
            for(int y = drawEnd + 1; y < height + Math.abs(shift); y++)
            {
                //calculates color on texture for each pixel of the floor
                double weight = Math.max(0, Math.min(height / (distWall * (2 * y - height)), 1));
                double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
                int floorTexX = (int) (currentFloorX * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexY = (int) (currentFloorY * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexture = floorMap[(int) currentFloorX][(int) currentFloorY];
                int ceilingTexture = ceilingMap[(int) currentFloorX][(int) currentFloorY];
                int floorColor = textures.get(floorTexture - 1).pixels[textures.get(floorTexture - 1).SIZE * floorTexY + floorTexX];
                int ceilingColor = textures.get(ceilingTexture - 1).pixels[textures.get(ceilingTexture - 1).SIZE * floorTexY + floorTexX];
                pixels[width * Math.max(0, Math.min(y + shift, height - 1)) + x] = floorColor;
                pixels[width * Math.max(0, Math.min(height - y + shift, drawStart + shift - 1)) + x] = ceilingColor;
                if(textures.get(ceilingTexture - 1) != Texture.black)
                    colored[x + Math.max(0, Math.min(height - y + shift, drawStart + shift - 1)) * width] = (currentFloorX - camera.xPos) * (currentFloorX - camera.xPos) + (currentFloorY - camera.yPos) * (currentFloorY - camera.yPos) - mapWidth * mapHeight;
            }
            pixels[width * Math.max(0, Math.min(drawEnd + shift, height - 1)) + x] = pixels[width * Math.max(0, Math.min(drawEnd - 1 + shift, height - 1)) + x];
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
                    px += points.get(i).get(j).getX();
                    py += points.get(i).get(j).getY();
                    pz += points.get(i).get(j).getZ();
                    if(i == 1)
                    {
                        Vector3D v = new Vector3D(px, py, pz);
                        Vector3D center = new Vector3D(points.get(i).get(j).getX(), points.get(i).get(j).getY(), points.get(i).get(j).getZ());
                        v.rotateZ3D(frame/5.0, center);
                        px = v.x;
                        py = v.y;
                        pz = v.z;
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
                    if(drawStartY < -shift) 
                        drawStartY = -shift;
                    int drawEndY = pointHeight/64 + height/2 - (int) (3 * pointHeight/8 * pointZ);
                    if(drawEndY >= height - shift) 
                        drawEndY = height - 1 - shift;
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
                                if(pointX * pointX + pointY * pointY - mapWidth * mapHeight < colored[Math.max(0, Math.min(drawY + shift, height - 1)) * width + drawX])
                                {
                                    pixels[Math.max(0, Math.min(drawY + shift, height - 1)) * width + drawX] = pcolor;
                                    colored[Math.max(0, Math.min(drawY + shift, height - 1)) * width + drawX] = pointX * pointX + pointY * pointY - mapWidth * mapHeight;
                                }
                            }
                        }
                    }
                    if(i == 1)
                    {
                        Vector3D v = new Vector3D(px, py, pz);
                        Vector3D center = new Vector3D(points.get(i).get(j).getX(), points.get(i).get(j).getY(), points.get(i).get(j).getZ());
                        v.rotateZ3D(-frame/5.0, center);
                        px = v.x;
                        py = v.y;
                        pz = v.z;
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
            shots.add(new Shot(player.getX(), player.getY(), player.getZ(), camera.xDir/30, camera.yDir/30, (double) shift/6400));
            shotTimer = 0;
        }
        for(int i = 0; i < shots.size(); i++)
        {
            Shot shot = shots.get(i);
            if(camera.shoot || (!shot.pos.equals(shot.initialPos) && shot.pos.getX() >= 0 && shot.pos.getX() < mapWidth && shot.pos.getY() >= 0 && shot.pos.getY() < mapHeight && shot.pos.getZ() >= -0.1 && map[(int) shot.pos.getX()][(int) shot.pos.getY()] == 0))
                shot.pos.add(shot.dir);
            else
                shots.remove(i);
            //drawing shot
            if(!shot.pos.equals(shot.initialPos))
            {
                px = shot.pos.getX();
                py = shot.pos.getY();
                pz = shot.pos.getZ();
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
                                pixels[Math.max(0, Math.min(drawY + shift, height - 1)) * width + drawX] = pcolor;
                                colored[Math.max(0, Math.min(drawY + shift, height - 1)) * width + drawX] = pointX * pointX + pointY * pointY - mapWidth * mapHeight;
                            }
                        }
                    }
                }
            }
        }
        camera.shoot = false;
        shotTimer++;
        shift += (height/2 - camera.mouseY);
        shift = Math.max(-height/3, Math.min(shift, height/3));
        return pixels;
    }

    public void setPoints(ArrayList<ArrayList<Vector3D>> p)
    {
        points = p;
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