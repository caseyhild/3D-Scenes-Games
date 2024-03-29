import java.util.*;
import java.awt.*;
public class Screen
{
    private int frame = 0;
    private int level = 1;
    private boolean showMinimap = false;
    private int minimapType = 0;
    private int[][] minimap;
    private int[][] map;
    private int[][] floorMap;
    private int[][] ceilingMap;
    private int mapWidth, mapHeight, width, height;
    private ArrayList<Texture> textures;
    //not used currently
    private int fogColor = rgbNum(0, 0, 0);
    private double fogAmount = 0.0;
    private double colorAmount = 1.0;
    private double[] fog;
    //maximum seeing distance
    private double visibility = 1.5;
    //0 = no fog
    //1 = complete fog
    private int fogSetting = 1;
    public Screen(int[][] m, int[][] fm, int[][] cm, int mapW, int mapH, ArrayList<Texture> tex, int w, int h)
    {
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
        minimap = new int[mapWidth][mapHeight];
        fog = new double[height/2];
        for(int y = height/2; y < height; y++)
        {
            double currentDist = height / (2.0 * y - height);
            fog[y - height/2] = 1 / (1 + Math.exp(-currentDist));
        }
    }
    
    public int[] updateMazeGame(Camera camera, int[] pixels, int[][] m, int[][] fm, int[][] cm)
    {
        if(minimapType == 1)
        {
            mapWidth = 2 * level + 3;
            mapHeight = 2 * level + 3;
        }
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        //sets all pixels to the fog color
        //if no fog all will be reset to other colors later
        for(int n=0; n<pixels.length; n++)
        {
            if(pixels[n] != fogColor)
            {
                pixels[n] = fogColor;
            }
        }
        int texX = 0;
        int texY = 0;
        //loops through all x-coordinates of the screen
        for(int x = 0; x < width; x++)
        {
            double cameraX = 2 * x / (double) width - 1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;
            //Map position
            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
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
                if(map[mapX][mapY] > 0) 
                    hit = true;
            }
            //Calculate distance to the point of impact
            if(side==0)
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);  
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) 
                lineHeight = Math.abs((int)(height / perpWallDist));
            else 
                lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2+ height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;
            //add a texture
            int texNum = map[mapX][mapY] - 1;
            double wallX;//Exact position of where wall was hit
            if(side == 1)
            {//If its a y-axis wall
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
            }
            else
            {//X-axis wall
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }
            wallX -= Math.floor(wallX);
            //x coordinate on the texture
            texX = (int) (wallX * (textures.get(texNum).SIZE));
            if(side == 0 && rayDirX > 0) 
                texX = textures.get(texNum).SIZE - texX - 1;
            if(side == 1 && rayDirY < 0) 
                texX = textures.get(texNum).SIZE - texX - 1;
            //set fog/color amount for each pixel
            //(fog amount is 0 if there is no fog)
            fogAmount = fog[drawEnd - height/2];
            colorAmount = 1.0 - fogAmount;
            //calculate y coordinate on texture
            for(int y = drawStart; y < drawEnd; y++) 
            {
                texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color = 0;
                if(side == 0 && texX + (texY * textures.get(texNum).SIZE) >= 0)
                    color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                else if(texX + (texY * textures.get(texNum).SIZE) >= 0)
                    color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711;//Make y sides darker
                color = rgbNum((int) (fogAmount * getR(fogColor) + colorAmount * getR(color)), (int) (fogAmount * getG(fogColor) + colorAmount * getG(color)), (int) (fogAmount * getB(fogColor) + colorAmount * getB(color)));
                pixels[x + y * (width)] = color;
            }
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
            double currentDist;
            double distWall = perpWallDist;
            double distPlayer = 0.0;
            if(drawEnd < 0) 
                drawEnd = height;
            int lineColor = 0;
            //loops through y-coordinates from bottom of wall to
            //bottom of screen
            for(int y = drawEnd + 1; y < height; y++)
            {
                //calculates color on texture for each pixel of the floor
                currentDist = height / (2.0 * y - height);
                //fog/color amount same as for walls
                fogAmount = fog[y - height/2];
                colorAmount = 1.0 - fogAmount;
                double weight = (currentDist - distPlayer) / (distWall - distPlayer);
                double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
                int floorTexX = (int) (currentFloorX * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexY = (int) (currentFloorY * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexture = floorMap[(int) currentFloorX][(int) currentFloorY];
                int ceilingTexture = ceilingMap[(int) currentFloorX][(int) currentFloorY];
                int floorColor = textures.get(floorTexture - 1).pixels[textures.get(floorTexture - 1).SIZE * floorTexY + floorTexX];
                int ceilingColor = textures.get(ceilingTexture - 1).pixels[textures.get(ceilingTexture - 1).SIZE * floorTexY + floorTexX];
                floorColor = rgbNum((int) (fogAmount * getR(fogColor) + colorAmount * getR(floorColor)), (int) (fogAmount * getG(fogColor) + colorAmount * getG(floorColor)), (int) (fogAmount * getB(fogColor) + colorAmount * getB(floorColor)));
                ceilingColor = rgbNum((int) (fogAmount * getR(fogColor) + colorAmount * getR(ceilingColor)), (int) (fogAmount * getG(fogColor) + colorAmount * getG(ceilingColor)), (int) (fogAmount * getB(fogColor) + colorAmount * getB(ceilingColor)));
                pixels[width * y + x] = floorColor;
                pixels[width * (height - y) + x] = ceilingColor;
                if(y == drawEnd + 1)
                    lineColor = floorColor;
            }
            pixels[width * drawEnd + x] = lineColor;
        }
        //minimap
        if(showMinimap == true)
        {
            int minimapSize = 150;
            int squareSize = minimapSize/minimap.length;
            if(squareSize >= 18)
                squareSize = 18;
            if(squareSize <= 1)
                squareSize = 1;
            boolean transparent = false;
            for(int mapX = 0; mapX < minimap[0].length; mapX++)
            {
                for(int mapY = 0; mapY < minimap.length; mapY++)
                {
                    int color = 0;
                    if(minimap[mapX][mapY] == 0)
                        color = rgbNum(0, 0, 0);
                    else if(minimap[mapX][mapY] == 1)
                        color = rgbNum(255, 255, 255);
                    else if(minimap[mapX][mapY] == 2)
                        color = rgbNum(0, 255, 0);
                    else if(minimap[mapX][mapY] == 3)
                        color = rgbNum(0, 0, 255);
                    for(int x = 0; x < squareSize; x++)
                    {
                        for(int y = 0; y < squareSize; y++)
                        {
                            if(!transparent)
                                pixels[(squareSize * mapX + 25 + x) * width + width - (squareSize * (minimap.length - 1 - mapY) + 3 + y)] = color;
                            else
                                pixels[(squareSize * mapX + 25 + x) * width + width - (squareSize * (minimap.length - 1 - mapY) + 3 + y)] = rgbNum((getR(pixels[(squareSize * mapY + 25 + y) * width + width - (squareSize * mapX + 3 + x)]) + getR(color))/2, (getG(pixels[(squareSize * mapY + 25 + y) * width + width - (squareSize * mapX + 3 + x)]) + getG(color))/2, (getB(pixels[(squareSize * mapY + 25 + y) * width + width - (squareSize * mapX + 3 + x)]) + getB(color))/2);
                        }
                    }
                }
            }
            for(int i = 1; i <= mapWidth * squareSize; i++)
            {
                int color = rgbNum(255, 255, 255);
                if(!transparent)
                {
                    pixels[(i + 25) * width - mapWidth * squareSize - 3] = color;
                    pixels[(mapHeight * squareSize + 25) * width - i - 3] = color;
                }
                else
                {
                    pixels[(i + 25) * width - mapWidth * squareSize - 3] = rgbNum((getR(pixels[(i + 25) * width - mapWidth * squareSize - 3]) + getR(color))/2, (getG(pixels[(i + 25) * width - mapWidth * squareSize - 3]) + getG(color))/2, (getB(pixels[(i + 25) * width - mapWidth * squareSize - 3]) + getB(color))/2);
                    pixels[(mapHeight * squareSize + 25) * width - i - 3] = rgbNum((getR(pixels[(mapHeight * squareSize + 25) * width - i - 3]) + getR(color))/2, (getG(pixels[(mapHeight * squareSize + 25) * width - i - 3]) + getG(color))/2, (getB(pixels[(mapHeight * squareSize + 25) * width - i - 3]) + getB(color))/2);
                }
            }
        }
        return pixels;
    }
    
    public void setLevel(int lvl)
    {
        //sets new level
        level = lvl;
    }

    public void setMinimap(int type)
    {
        //turns on minimap
        showMinimap = true;
        minimapType = type;
    }

    public void setMinimap(int type, int[][] m)
    {
        //turns on minimap
        showMinimap = true;
        minimapType = type;
        for(int mapX = 0; mapX < minimap[0].length; mapX++)
        {
            for(int mapY = 0; mapY < minimap.length; mapY++)
            {
                minimap[mapX][mapY] = m[mapX][mapY];
            }
        }
    }

    public void resetMinimap(int w, int h)
    {
        minimap = new int[w][h];
    }

    private int rgbNum(int r, int g, int b)
    {
        //gets rgb decimal value from rgb input
        int rgb = r * 65536 + g * 256 + b;
        return rgb;
    }

    private int getR(int color)
    {
        //gets r value from rgb decimal input
        int r = color/65536;
        return r;
    }

    private int getG(int color)
    {
        //gets g value from rgb decimal input
        color -= color/65536 * 65536;
        int g = color/256;
        return g;
    }

    private int getB(int color)
    {
        //gets b value from rgb decimal input
        color -= color/65536 * 65536;
        color -= color/256 * 256;
        int b = color;
        return b;
    }
}