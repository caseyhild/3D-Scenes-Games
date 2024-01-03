import java.util.*;
import java.awt.*;
public class Screen
{
    private int frame;
    private boolean showMinimap = false;
    private int minimapType = 0;
    private int[][] minimap;
    private int[][] map;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private int mapWidth, mapHeight, width, height;
    private double playerHeight;
    private ArrayList<Texture> textures;
    private double[] ZBuffer;
    private int fogColor = 0;
    private double fogAmount = 0.0;
    private double colorAmount = 1.0;
    private double[] fog;
    private Vector2D player;
    private int shift;

    public Screen(int[][] m, int[][] fm, int[][] cm, int mapW, int mapH, ArrayList<Texture> tex, Vector3D fogCol, double visibility, int w, int h)
    {
        frame = 0;
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
        playerHeight = 0.5;
        ZBuffer = new double[width];
        minimap = new int[mapWidth][mapHeight];
        fog = new double[4 * height/3];
        for(int y = 0; y < fog.length; y++)
        {
            double currentDist = height / (2.0 * y - height);
            fog[y] = 1 / (1 + Math.exp(-currentDist / visibility + 1));
            if(fog[y] > 0.999)
            {
                fog[y] = 1;
            }
        }
        fogColor = rgbNum((int) fogCol.x, (int) fogCol.y, (int) fogCol.z);
        player = new Vector2D();
        shift = 0;
    }

    public int[] updateGame(Camera camera, int[] pixels, int[][] m, int[][] fm, int[][] cm, int f)
    {
        frame = f;
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        player = new Vector2D(camera.xPos, camera.yPos);
        for(int n=0; n<pixels.length; n++)
        {
            if(pixels[n] != fogColor)
            {
                pixels[n] = fogColor;
            }
        }
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
                //set fog/color amount for each pixel
                fogAmount = fog[drawEnd];
                colorAmount = 1.0 - fogAmount;
                for(int y = drawStart; y < drawEnd; y++)
                {
                    texY = (int) (y * a + b);
                    int color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                    if(side == 1)
                        color = (color >> 1) & 8355711;//Make y sides darker
                    color = rgbNum((int) (fogAmount * getR(fogColor) + colorAmount * getR(color)), (int) (fogAmount * getG(fogColor) + colorAmount * getG(color)), (int) (fogAmount * getB(fogColor) + colorAmount * getB(color)));
                    pixels[x + Math.max(0, Math.min(y + shift, height - 1)) * width] = color;
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
                fogAmount = fog[y];
                colorAmount = 1.0 - fogAmount;
                double weight = Math.max(0, Math.min(height / (distWall * (2 * y - height)), 1));
                double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
                int floorTexX = (int) (currentFloorX * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexY = (int) (currentFloorY * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexture = floorMap[(int) currentFloorX][(int) currentFloorY];
                int floorColor = textures.get(floorTexture - 1).pixels[textures.get(floorTexture - 1).SIZE * floorTexY + floorTexX];
                floorColor = rgbNum((int) (fogAmount * getR(fogColor) + colorAmount * getR(floorColor)), (int) (fogAmount * getG(fogColor) + colorAmount * getG(floorColor)), (int) (fogAmount * getB(fogColor) + colorAmount * getB(floorColor)));
                pixels[width * Math.max(0, Math.min(y + shift, height - 1)) + x] = floorColor;
            }
            pixels[width * Math.max(0, Math.min(drawEnd + shift, height - 1)) + x] = pixels[width * Math.max(0, Math.min(drawEnd + 1 + shift, height - 1)) + x];
        }
        shift += (height/2 - camera.mouseY);
        shift = Math.max(-height/3, Math.min(shift, height/3));
        return pixels;
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