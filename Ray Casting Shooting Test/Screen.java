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
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private ArrayList<Double> spriteDistance = new ArrayList<Double>();
    private ArrayList<Integer> spriteOrder = new ArrayList<Integer>();
    private double[] ZBuffer;
    private Vector ball;
    private Vector ballDir;
    private int ballColor;
    private boolean shoot = false;
    private double enemyPosX;
    private double enemyPosY;
    private boolean isDead = false;
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
        ZBuffer = new double[width];
        minimap = new int[mapWidth][mapHeight];
        ball = new Vector(1.5, 1.5);
        ballDir = new Vector(0, 0);
        ballColor = rgbNum(0, 255, 0);
    }
    
    public int[] updateGame(Camera camera, int[] pixels, int[][] m, int[][] fm, int[][] cm)
    {
        map = m;
        floorMap = fm;
        ceilingMap = cm;
        int texX = 0;
        int texY = 0;
        //loops through all x-coordinates of the screen
        for(int x = 0; x < width; x++)
        {
            double cameraX = 2 * x / (double)(width) - 1;
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
                if(mapX < 0 || mapX > mapWidth - 1 || mapY < 0 || mapY > mapHeight - 1)
                    hit = true;
                else if(map[mapX][mapY] > 0) 
                    hit = true;
            }
            //Calculate distance to the point of impact
            if(side == 0)
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
            int drawStart = -lineHeight/2 + height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;
            //add a texture
            int texNum = 0;
            if(mapX < 0 || mapX > mapWidth - 1 || mapY < 0 || mapY > mapHeight - 1)
                texNum = -1;
            else
                texNum = map[mapX][mapY] - 1;
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
            if(texNum >= 0)
            {
                texX = (int) (wallX * (textures.get(texNum).SIZE));
                if(side == 0 && rayDirX > 0) 
                    texX = textures.get(texNum).SIZE - texX - 1;
                if(side == 1 && rayDirY < 0) 
                    texX = textures.get(texNum).SIZE - texX - 1;
            }
            //calculate y coordinate on texture
            for(int y = drawStart; y < drawEnd; y++) 
            {
                texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color = 0;
                if(texNum >= 0)
                {
                    if(side == 0 && texX + (texY * textures.get(texNum).SIZE) >= 0)
                        color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                    else if(texX + (texY * textures.get(texNum).SIZE) >= 0)
                        color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711;//Make y sides darker
                    pixels[x + y * (width)] = color;
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
                double weight = (currentDist - distPlayer) / (distWall - distPlayer);
                double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
                double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
                int floorTexX = (int) (currentFloorX * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexY = (int) (currentFloorY * textures.get(0).SIZE) % textures.get(0).SIZE;
                int floorTexture = floorMap[(int) currentFloorX][(int) currentFloorY];
                int ceilingTexture = ceilingMap[(int) currentFloorX][(int) currentFloorY];
                int floorColor = textures.get(floorTexture - 1).pixels[textures.get(floorTexture - 1).SIZE * floorTexY + floorTexX];
                int ceilingColor = textures.get(ceilingTexture - 1).pixels[textures.get(ceilingTexture - 1).SIZE * floorTexY + floorTexX];
                pixels[width * y + x] = rgbNum((int) (0.75 * getR(floorColor)), (int) (0.75 * getG(floorColor)), (int) (0.75 * getB(floorColor)));
                pixels[width * (height - y) + x] = rgbNum((int) (0.75 * getR(ceilingColor)), (int) (0.75 * getG(ceilingColor)), (int) (0.75 * getB(ceilingColor)));;
                if(y == drawEnd + 1)
                    lineColor = floorColor;
            }
            pixels[width * drawEnd + x] = lineColor;
        }
        //sprite casting
        if(sprites.size() >= 1)
        {
            //finding order of the sprites
            //farthest to closest
            spriteOrder.clear();
            spriteDistance.clear();
            for(int i = 0; i < sprites.size(); i++)
            {
                spriteOrder.add(i);
                spriteDistance.add((camera.xPos - sprites.get(i).xPos) * (camera.xPos - sprites.get(i).xPos) + (camera.yPos - sprites.get(i).yPos) * (camera.yPos - sprites.get(i).yPos));
            }
            combSort(spriteOrder, spriteDistance, sprites.size());
            //create all of the sprites
            for(int i = 0; i < sprites.size(); i++)
            {
                //set sprite location and size
                double spriteX = sprites.get(spriteOrder.get(i)).xPos - camera.xPos;
                double spriteY = sprites.get(spriteOrder.get(i)).yPos - camera.yPos;
                double invDet = 1.0 * (camera.xPlane * camera.yDir - camera.xDir * camera.yPlane);
                double transformX = invDet * (camera.yDir * spriteX - camera.xDir * spriteY);
                double transformY = invDet * (-camera.yPlane * spriteX + camera.xPlane * spriteY);
                int spriteScreenX = (int) ((width/2) * (1 + transformX/transformY));
                int spriteHeight = (int) Math.abs((int) (height/transformY));
                //change sprite height
                spriteHeight /= 2;
                int drawStartY = -spriteHeight/2 + height/2;
                if(drawStartY <= 0) 
                    drawStartY = 0;
                int drawEndY = spriteHeight/2 + height/2;
                if(drawEndY >= height) 
                    drawEndY = height - 1;
                int spriteWidth = Math.abs((int) (height/transformY));
                //change sprite width
                spriteWidth /= 2;
                int drawStartX = -spriteWidth/2 + spriteScreenX;
                if(drawStartX <= 0) 
                    drawStartX = 0;
                int drawEndX = spriteWidth/2 + spriteScreenX;
                if(drawEndX >= width) 
                    drawEndX = width - 1;
                //set color of each pixel on sprites
                for(int stripe = drawStartX; stripe < drawEndX; stripe++)
                {
                    int textureX = (int) (256 * (stripe + spriteWidth/2 - spriteScreenX) * sprites.get(spriteOrder.get(i)).texture.SIZE/spriteWidth)/256;
                    if(transformY > 0 && stripe > 0 && stripe < width && transformY < ZBuffer[stripe])
                    {
                        for(int y = drawStartY; y < drawEndY; y++)
                        {
                            int d = y * 256 - height * 128 + spriteHeight * 128;
                            int textureY = ((d * sprites.get(spriteOrder.get(i)).texture.SIZE)/spriteHeight)/256;
                            int color = sprites.get(spriteOrder.get(i)).texture.pixels[sprites.get(spriteOrder.get(i)).texture.SIZE * textureY + textureX];
                            if(color != 0 && Math.sqrt(spriteDistance.get(i)) < ZBuffer[stripe])
                                pixels[y * width + stripe] = color;
                            //for transparent sprites
                            //pixels[y * width + stripe] = rgbNum((getR(pixels[y * width + stripe]) + getR(color))/2, (getG(pixels[y * width + stripe]) + getG(color))/2, (getB(pixels[y * width + stripe]) + getB(color))/2);
                        }
                    }
                }
            }
        }
        //ball
        if(shoot)
            camera.setShoot(false);
        if(camera.shoot)
        {
            ball.setX(camera.ball.getX());
            ball.setY(camera.ball.getY());
            ballDir.setX(camera.ballDir.getX());
            ballDir.setY(camera.ballDir.getY());
            shoot = true;
        }
        if(camera.shoot || ((ball.getX() != camera.startBall.getX() || ball.getY() != camera.startBall.getY()) && ball.getX() >= 0 && ball.getX() < mapWidth && ball.getY() >= 0 && ball.getY() < mapHeight && map[(int) ball.getX()][(int) ball.getY()] == 0))
        {
            ball.setX(ball.getX() + ballDir.getX());
            ball.setY(ball.getY() + ballDir.getY());
        }
        else
        {
            ball.setX(camera.startBall.getX());
            ball.setY(camera.startBall.getY());
            shoot = false;
        }
        double dist = Math.sqrt((ball.getX() - camera.startBall.getX()) * (ball.getX() - camera.startBall.getX()) + (ball.getY() - camera.startBall.getY()) * (ball.getY() - camera.startBall.getY()));
        //drawing moving 3D Points
        if(frame % 60 == 0 && (ball.getX() != camera.startBall.getX() || ball.getY() != camera.startBall.getY()))
        {
            double[] colored = new double[width * height];
            for(int i = 0; i < width * height; i++)
            {
                colored[i] = 1.0 * mapWidth * mapHeight;
            }
            double px = ball.getX();
            double py = ball.getY();
            double pz = 0.5;
            double pointX = px - camera.xPos;
            double pointY = py - camera.yPos;
            double pointZ = pz - 0.5;
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
                        if((pointX * pointX + pointY * pointY + pointZ * pointZ) < colored[drawY * width + drawX])
                        {
                            pixels[drawY * width + drawX] = ballColor;
                            colored[drawY * width + drawX] = (pointX * pointX + pointY * pointY + pointZ * pointZ);
                        }
                    }
                }
            }
        }
        if(!isDead && shoot && Math.sqrt((ball.getX() - enemyPosX) * (ball.getX() - enemyPosX) + (ball.getY() - enemyPosY) * (ball.getY() - enemyPosY)) < 0.35)
        {
            isDead = true;
            ball.setX(camera.startBall.getX());
            ball.setY(camera.startBall.getY());
        }
        return pixels;
    }

    private void combSort(ArrayList<Integer> order, ArrayList<Double> dist, int amount)
    {
        int gap = amount;
        boolean swapped = false;
        while(gap > 1 || swapped)
        {
            gap = (gap * 10)/13;
            if(gap == 9 || gap == 10)
                gap = 11;
            if(gap < 1)
                gap = 1;
            swapped = false;
            for(int i = 0; i < amount - gap; i++)
            {
                int j = i + gap;
                if(dist.get(i) < dist.get(j))
                {
                    double d = dist.get(i);
                    int o = order.get(i);
                    dist.set(i, dist.get(j));
                    dist.set(j, d);
                    order.set(i, order.get(j));
                    order.set(j, o);
                    swapped = true;
                }
            }
        }
    }

    public void setSprites(ArrayList<Sprite> s)
    {
        //set all sprites
        sprites = s;
    }

    public void setEnemyPos(double x, double y)
    {
        enemyPosX = x;
        enemyPosY = y;
    }

    public void setDead(boolean dead)
    {
        isDead = dead;
    }

    public boolean isDead()
    {
        return isDead;
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
