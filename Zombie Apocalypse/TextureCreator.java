import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class TextureCreator extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    Timer tm = new Timer(1, this);
    private static int width = 900;
    private static int height = 512;
    private int size = 64;
    private int[][] pixels = new int[size][size];
    private Texture t;
    private int colorR = -1;
    private int colorG = -1;
    private int colorB = -1;
    private int colorPickerX = 522;
    private int colorPickerY = 10;
    private int colorX = colorPickerX;
    private int colorY = colorPickerY;
    private int drawingSize = 1;
    private int rectWidth = 0;
    private int rectHeight = 0;
    private String drawingMode = "Draw";
    private int rectWidth2 = 0;
    private int rectHeight2 = 0;
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;
    private boolean drawCopyText = false;
    private int copyTextWidth = 0;
    private int copyTextHeight = 0;
    private int[][] copy;
    private int pasteWidth = 0;
    private int pasteHeight = 0;
    private int pasteX = 0;
    private int pasteY = 0;
    private int adjustx = 0;
    private int adjusty = 0;
    private int numRecentColors = 5;
    private int[] recentColors = new int[numRecentColors];
    private boolean clickedRecent = false;
    private int mouseX;
    private int mouseY;

    public TextureCreator()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
        t = Texture.saved;
        for(int i = 0; i < size * size; i++)
        {
            pixels[i % size][i/size] = t.pixels[i];
        }
        for(int i = 0; i < recentColors.length; i++)
        {
            recentColors[i] = -1;
        }
        copy = new int[0][0];
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(192, 192, 192));
        g.fillRect(0, 0, width, height);
        for(int x = 0; x < size; x++)
        {
            for(int y = 0; y < size; y++)
            {
                g.setColor(new Color(getR(pixels[x][y]), getG(pixels[x][y]), getB(pixels[x][y])));
                g.fillRect(x * height/size, y * height/size, height/size, height/size);
                g.setColor(new Color(0, 0, 0));
                g.drawRect(x * height/size, y * height/size, height/size, height/size);
            }
        }
        for(int x = 0; x < 223; x++)
        {
            int lineR = 0;
            int lineG = 0;
            int lineB = 0;
            for(int y = 0; y < 192; y++)
            {
                if(x == 0)
                {
                    lineR = 255;
                    lineG = 0;
                    lineB = 0;
                }
                else if(x < 32)
                {
                    lineR = 255;
                    lineG = x % 32 * 8;
                    lineB = 0;
                }
                else if(x < 64)
                {
                    lineR = 255 - x % 32 * 8;
                    lineG = 255;
                    lineB = 0;
                }
                else if(x < 96)
                {
                    lineR = 0;
                    lineG = 255;
                    lineB = x % 32 * 8;
                }
                else if(x < 128)
                {
                    lineR = 0;
                    lineG = 255 - x % 32 * 8;
                    lineB = 255;
                }
                else if(x < 160)
                {
                    lineR = x % 32 * 8;
                    lineG = 0;
                    lineB = 255;
                }
                else if(x < 192)
                {
                    lineR = 255;
                    lineG = 0;
                    lineB = 255 - x % 32 * 8;
                }
                else
                {
                    lineR = 128;
                    lineG = 128;
                    lineB = 128;
                }
                if(y < 96)
                {
                    lineR = (int) (lineR * y/96.0);
                    lineG = (int) (lineG * y/96.0);
                    lineB = (int) (lineB * y/96.0);
                }
                else
                {
                    lineR = 255 - (int) ((255 - lineR) * (192 - y)/96.0);
                    lineG = 255 - (int) ((255 - lineG) * (192 - y)/96.0);
                    lineB = 255 - (int) ((255 - lineB) * (192 - y)/96.0);
                }
                lineR = Math.max(lineR, 0);
                lineG = Math.max(lineG, 0);
                lineB = Math.max(lineB, 0);
                lineR = Math.min(lineR, 255);
                lineG = Math.min(lineG, 255);
                lineB = Math.min(lineB, 255);
                g.setColor(new Color(lineR, lineG, lineB));
                g.drawLine(x + colorPickerX, y + colorPickerY, x + colorPickerX, y + colorPickerY);
            }
        }
        g.setColor(new Color(0, 0, 0));
        g.fillRect(colorPickerX - 5, colorPickerY - 5, 233, 5);
        g.fillRect(colorPickerX - 5, colorPickerY + 192, 233, 5);
        g.fillRect(colorPickerX - 5, colorPickerY - 5, 5, 202);
        g.fillRect(colorPickerX + 192, colorPickerY - 5, 5, 202);
        g.fillRect(colorPickerX + 223, colorPickerY - 5, 5, 202);
        Font font = new Font("Verdana", Font.PLAIN, 15);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        if(colorR != -1 && colorG != -1 && colorB != -1)
        {
            g.drawString("R: " + colorR, 770, 33 + fm.getAscent()/2);
            g.drawString("G: " + colorG, 770, 53 + fm.getAscent()/2);
            g.drawString("B: " + colorB, 770, 73 + fm.getAscent()/2);
        }
        else
        {
            g.drawString("R: ", 770, 33 + fm.getAscent()/2);
            g.drawString("G: ", 770, 53 + fm.getAscent()/2);
            g.drawString("B: ", 770, 73 + fm.getAscent()/2);
        }
        g.drawString("Amount To Fill In", 825 - fm.stringWidth("Amount To Fill In")/2, 99 + fm.getAscent()/2);
        g.drawLine(760, 101 + fm.getAscent()/2, 764 + fm.stringWidth("Amount To Fill In"), 101 + fm.getAscent()/2);
        g.setColor(new Color(255, 255, 255));
        rectWidth = 10 + fm.stringWidth("1x1");
        rectHeight = fm.getAscent() + 4;
        if(drawingSize == 1)
        {
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(765, 120 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(765, 120 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
        }
        if(drawingSize == 3)
        {
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(765, 140 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(765, 140 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
        }
        if(drawingSize == 5)
        {
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(765, 160 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(765, 160 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
        }
        if(drawingSize == 7)
        {
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(765, 180 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(765, 180 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
        }
        if(drawingSize == 9)
        {
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(765, 200 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(765, 200 - fm.getAscent()/2, rectWidth, rectHeight, 10, 10);
        }
        g.setColor(new Color(0, 0, 0));
        g.drawString("1x1", 770, 120 + fm.getAscent()/2);
        g.drawString("3x3", 770, 140 + fm.getAscent()/2);
        g.drawString("5x5", 770, 160 + fm.getAscent()/2);
        g.drawString("7x7", 770, 180 + fm.getAscent()/2);
        g.drawString("9x9", 770, 200 + fm.getAscent()/2);
        g.drawString("Recently Used Colors", 522, 222);
        g.drawLine(522, 224, 522 + fm.stringWidth("Recently Used Colors"), 224);
        for(int i = 0; i < recentColors.length; i++)
        {
            if(recentColors[i] != -1)
            {
                g.setColor(new Color(recentColors[i]));
                g.fillRect(522 + 40 * i, 232, 30, 30);
                g.setColor(new Color(0, 0, 0));
                g.fillRect(522 + 40 * i, 232, 30, 2);
                g.fillRect(522 + 40 * i, 260, 30, 2);
                g.fillRect(522 + 40 * i, 232, 2, 30);
                g.fillRect(550 + 40 * i, 232, 2, 30);
            }
        }
        g.setColor(new Color(0, 0, 0));
        g.drawString("Drawing Mode", 522, 294);
        g.drawLine(522, 296, 522 + fm.stringWidth("Drawing Mode"), 296);
        rectHeight2 = fm.getAscent() + 4;
        if(drawingMode.equals("Draw"))
        {
            rectWidth2 = 10 + fm.stringWidth("Draw");
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(527, 312 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(527, 312 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
        }
        if(drawingMode.equals("Copy"))
        {
            rectWidth2 = 10 + fm.stringWidth("Copy");
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(527, 332 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(527, 332 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
        }
        if(drawingMode.equals("Paste"))
        {
            rectWidth2 = 10 + fm.stringWidth("Paste");
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(527, 352 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
            g.setColor(new Color(0, 0, 0));
            g.drawRoundRect(527, 352 - fm.getAscent()/2, rectWidth2, rectHeight2, 10, 10);
        }
        g.drawString("Draw", 532, 312+ fm.getAscent()/2);
        g.drawString("Copy", 532, 332 + fm.getAscent()/2);
        g.drawString("Paste", 532, 352 + fm.getAscent()/2);
        if(drawingMode.equals("Copy"))
        {
            g.setColor(new Color(128, 128, 128, 128));
            g.fillRect(x1, y1, x2 - x1, y2 - y1);
            if(drawCopyText)
            {
                g.setColor(new Color(255, 255, 255));
                g.fillRoundRect((x2 - x1)/2 + x1 - (10 + fm.stringWidth("Copy"))/2, (y2 - y1)/2 + y1 - (fm.getAscent() + 4)/2, 10 + fm.stringWidth("Copy"), fm.getAscent() + 4, 10, 10);
                g.setColor(new Color(0, 0, 0));
                g.drawRoundRect((x2 - x1)/2 + x1 - (10 + fm.stringWidth("Copy"))/2, (y2 - y1)/2 + y1 - (fm.getAscent() + 4)/2, 10 + fm.stringWidth("Copy"), fm.getAscent() + 4, 10, 10);
                g.setColor(new Color(0, 0, 0));
                g.drawString("Copy", (x2 - x1)/2 + x1 - fm.stringWidth("Copy")/2, (y2 - y1)/2 + y1 + fm.getAscent()/2 - 2);
                copyTextWidth = 10 + fm.stringWidth("Copy");
                copyTextHeight = fm.getAscent() + 4;
            }
        }
        if(drawingMode.equals("Paste") && mouseX < height)
        {
            g.setColor(new Color(128, 128, 128, 128));
            if(pasteWidth % 2 == 0)
                adjustx = 4;
            else
                adjustx = 0;
            if(pasteHeight % 2 == 0)
                adjusty = 4;
            else
                adjusty = 0;
            if(copy.length > 0)
            {
                pasteWidth = copy.length;
                pasteHeight = copy[0].length;
                pasteX = Math.max(0, Math.min((mouseX + adjustx)/8 - pasteWidth/2, size - pasteWidth));
                pasteY = Math.max(0, Math.min((mouseY + adjusty)/8 - pasteHeight/2, size - pasteHeight));
            }
            g.fillRect(8 * pasteX, 8 * pasteY, 8 * pasteWidth, 8 * pasteHeight);
        }
        g.setColor(new Color(255, 255, 255));
        g.fillRoundRect(width - 100, height - 100, 90, 90, 16, 16);
        g.setColor(new Color(0, 0, 0));
        g.drawRoundRect(width - 100, height - 100, 90, 90, 16, 16);
        g.drawString("SAVE", width - 55 - fm.stringWidth("SAVE")/2, height - 55 + fm.getAscent()/2);
        if(colorR != -1 && colorG != -1 && colorB != -1)
        {
            g.setColor(new Color(0, 0, 0));
            g.drawOval(colorX - 4, colorY - 4, 8, 8);
            g.drawOval(colorX - 5, colorY - 5, 10, 10);
        }
        tm.start();
    }

    public void actionPerformed(ActionEvent ae)
    {
        repaint();
    }

    public void mouseClicked(MouseEvent me)
    {
        if(mouseX >= colorPickerX + 191 && mouseX <= colorPickerX + 194)
            mouseX = colorPickerX + 191;
        if(mouseX >= colorPickerX + 195 && mouseX <= colorPickerX + 197)
            mouseX = colorPickerX + 197;
        if(mouseX >= colorPickerX && mouseX < colorPickerX + 223 && mouseY >= colorPickerY && mouseY < colorPickerY + 192)
        {
            if(mouseX == colorPickerX)
            {
                colorR = 255;
                colorG = 0;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 32)
            {
                colorR = 255;
                colorG = (mouseX - colorPickerX) % 32 * 8;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 64)
            {
                colorR = 255 - (mouseX - colorPickerX) % 32 * 8;
                colorG = 255;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 96)
            {
                colorR = 0;
                colorG = 255;
                colorB = (mouseX - colorPickerX) % 32 * 8;
            }
            else if(mouseX < colorPickerX + 128)
            {
                colorR = 0;
                colorG = 255 - (mouseX - colorPickerX) % 32 * 8;
                colorB = 255;
            }
            else if(mouseX < colorPickerX + 160)
            {
                colorR = (mouseX - colorPickerX) % 32 * 8;
                colorG = 0;
                colorB = 255;
            }
            else if(mouseX < colorPickerX + 192)
            {
                colorR = 255;
                colorG = 0;
                colorB = 255 - (mouseX - colorPickerX) % 32 * 8;
            }
            else
            {
                colorR = 128;
                colorG = 128;
                colorB = 128;
            }
            if(mouseY >= colorPickerY && mouseY < colorPickerY + 96)
            {
                colorR = (int) (colorR * (mouseY - colorPickerY)/96.0);
                colorG = (int) (colorG * (mouseY - colorPickerY)/96.0);
                colorB = (int) (colorB * (mouseY - colorPickerY)/96.0);
            }
            else if(mouseY >= colorPickerY + 96 && mouseY < colorPickerY + 192)
            {
                colorR = 255 - (int) ((255 - colorR) * (colorPickerY + 191 - mouseY)/96.0);
                colorG = 255 - (int) ((255 - colorG) * (colorPickerY + 191 - mouseY)/96.0);
                colorB = 255 - (int) ((255 - colorB) * (colorPickerY + 191 - mouseY)/96.0);
            }
            colorR = (int) Math.round(colorR/8.0) * 8;
            colorG = (int) Math.round(colorG/8.0) * 8;
            colorB = (int) Math.round(colorB/8.0) * 8;
            colorR = Math.max(colorR, 0);
            colorG = Math.max(colorG, 0);
            colorB = Math.max(colorB, 0);
            colorR = Math.min(colorR, 255);
            colorG = Math.min(colorG, 255);
            colorB = Math.min(colorB, 255);
            colorX = mouseX;
            colorY = mouseY;
            if(colorX >= colorPickerX + 191 && colorX <= colorPickerX + 194)
                colorX = colorPickerX + 191;
            if(colorX >= colorPickerX + 195 && colorX <= colorPickerX + 197)
                colorX = colorPickerX + 197;
            clickedRecent = false;
        }
        else if(mouseX >= 0 && mouseX < height && mouseY >= 0 && mouseY < height)
        {
            if(drawingMode.equals("Draw"))
            {
                for(int y = size * mouseY/height - drawingSize/2; y <= size * mouseY/height + drawingSize/2; y++)
                {
                    for(int x = size * mouseX/height - drawingSize/2; x <= size * mouseX/height + drawingSize/2; x++)
                    {
                        boolean stillRecent = false;
                        for(int i = recentColors.length - 1; i >= 0; i--)
                        {
                            if(recentColors[i] == rgbNum(colorR, colorG, colorB))
                                stillRecent = true;
                        }
                        if(stillRecent && (colorR != -1 && colorG != -1 && colorB != -1))
                        {
                            int color = 0;
                            int index = 0;
                            for(int i = recentColors.length - 1; i >= 0; i--)
                            {
                                if(recentColors[i] == rgbNum(colorR, colorG, colorB))
                                {
                                    color = rgbNum(colorR, colorG, colorB);
                                    index = i;
                                    if(clickedRecent)
                                    {
                                        colorX = 537;
                                        colorY = 247;
                                    }
                                }
                            }
                            for(int i = index; i >= 1; i--)
                            {
                                recentColors[i] = recentColors[i - 1];
                            }
                            recentColors[0] = color;
                        }
                        else if(colorR != -1 && colorG != -1 && colorB != -1)
                        {
                            for(int i = recentColors.length - 1; i >= 0; i--)
                            {
                                if(i == 0)
                                    recentColors[0] = rgbNum(colorR, colorG, colorB);
                                else
                                    recentColors[i] = recentColors[i - 1];
                            }
                        }
                        if(colorR != -1 && colorG != -1 && colorB != -1)
                            pixels[Math.min(Math.max(x, 0), 63)][Math.min(Math.max(y, 0), 63)] = rgbNum(colorR, colorG, colorB);
                    }
                }
            }
            else if(drawingMode.equals("Copy"))
            {
                if(drawCopyText && mouseX > (x2 - x1)/2 + x1 - copyTextWidth/2 && mouseX < (x2 - x1)/2 + x1 + copyTextWidth/2 && mouseY > (y2 - y1)/2 + y1 - copyTextHeight/2 && mouseY < (y2 - y1)/2 + y1 + copyTextHeight/2)
                    copy();
                else
                {
                    x1 = -1;
                    y1 = -1;
                    x2 = -1;
                    y2 = -1;
                }
            }
            else if(drawingMode.equals("Paste"))
            {
                if(copy.length > 0 && pasteWidth > 0 && pasteHeight > 0)
                    paste();
            }
        }
        if(mouseX > width - 100 && mouseX < width - 10 && mouseY > height - 100 && mouseY < height - 10)
        {
            try
            {
                saveTexture();
            }
            catch(IOException e)
            {
                
            }
        }
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 122 - rectHeight/2 && mouseY < 122 + rectHeight)
            drawingSize = 1;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 142 - rectHeight/2 && mouseY < 142 + rectHeight)
            drawingSize = 3;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 162 - rectHeight/2 && mouseY < 162 + rectHeight)
            drawingSize = 5;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 182 - rectHeight/2 && mouseY < 182 + rectHeight)
            drawingSize = 7;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 202 - rectHeight/2 && mouseY < 202 + rectHeight)
            drawingSize = 9;
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 312 - rectHeight/2 && mouseY < 312 + rectWidth)
            drawingMode = "Draw";
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 332 - rectHeight/2 && mouseY < 332 + rectWidth)
            drawingMode = "Copy";
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 352 - rectHeight/2 && mouseY < 352 + rectWidth)
            drawingMode = "Paste";
        for(int i = 0; i < recentColors.length; i++)
        {
            if(mouseX > 522 + 40 * i && mouseX < 552 + 40 * i && mouseY > 232 && mouseY < 262)
            {
                colorR = getR(recentColors[i]);
                colorG = getG(recentColors[i]);
                colorB = getB(recentColors[i]);
                colorX = 537 + 40 * i;
                colorY = 247;
                clickedRecent = true;
            }
        }
        repaint();
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {
        if(drawingMode.equals("Copy"))
        {
            if(drawCopyText && mouseX > (x2 - x1)/2 + x1 - copyTextWidth/2 && mouseX < (x2 - x1)/2 + x1 + copyTextWidth/2 && mouseY > (y2 - y1)/2 + y1 - copyTextHeight/2 && mouseY < (y2 - y1)/2 + y1 + copyTextHeight/2)
                copy();
            else
            {
                x1 = -1;
                y1 = -1;
                x2 = -1;
                y2 = -1;
            }
        }
        drawCopyText = false;
    }

    public void mouseReleased(MouseEvent me)
    {
        if(drawingMode.equals("Copy") && (x2 != x1 || y2 != y1))
        {
            drawCopyText = true;
        }
    }

    public void mouseDragged(MouseEvent me)
    {
        mouseX = me.getX();
        mouseY = me.getY();
        if(mouseX >= colorPickerX + 191 && mouseX <= colorPickerX + 194)
            mouseX = colorPickerX + 191;
        if(mouseX >= colorPickerX + 195 && mouseX <= colorPickerX + 197)
            mouseX = colorPickerX + 197;
        if(mouseX >= colorPickerX && mouseX < colorPickerX + 223 && mouseY >= colorPickerY && mouseY < colorPickerY + 192)
        {
            if(mouseX == colorPickerX)
            {
                colorR = 255;
                colorG = 0;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 32)
            {
                colorR = 255;
                colorG = (mouseX - colorPickerX) % 32 * 8;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 64)
            {
                colorR = 255 - (mouseX - colorPickerX) % 32 * 8;
                colorG = 255;
                colorB = 0;
            }
            else if(mouseX < colorPickerX + 96)
            {
                colorR = 0;
                colorG = 255;
                colorB = (mouseX - colorPickerX) % 32 * 8;
            }
            else if(mouseX < colorPickerX + 128)
            {
                colorR = 0;
                colorG = 255 - (mouseX - colorPickerX) % 32 * 8;
                colorB = 255;
            }
            else if(mouseX < colorPickerX + 160)
            {
                colorR = (mouseX - colorPickerX) % 32 * 8;
                colorG = 0;
                colorB = 255;
            }
            else if(mouseX < colorPickerX + 192)
            {
                colorR = 255;
                colorG = 0;
                colorB = 255 - (mouseX - colorPickerX) % 32 * 8;
            }
            else
            {
                colorR = 128;
                colorG = 128;
                colorB = 128;
            }
            if(mouseY >= colorPickerY && mouseY < colorPickerY + 96)
            {
                colorR = (int) (colorR * (mouseY - colorPickerY)/96.0);
                colorG = (int) (colorG * (mouseY - colorPickerY)/96.0);
                colorB = (int) (colorB * (mouseY - colorPickerY)/96.0);
            }
            else if(mouseY >= colorPickerY + 96 && mouseY < colorPickerY + 192)
            {
                colorR = 255 - (int) ((255 - colorR) * (colorPickerY + 191 - mouseY)/96.0);
                colorG = 255 - (int) ((255 - colorG) * (colorPickerY + 191 - mouseY)/96.0);
                colorB = 255 - (int) ((255 - colorB) * (colorPickerY + 191 - mouseY)/96.0);
            }
            colorR = (int) Math.round(colorR/8.0) * 8;
            colorG = (int) Math.round(colorG/8.0) * 8;
            colorB = (int) Math.round(colorB/8.0) * 8;
            colorR = Math.max(colorR, 0);
            colorG = Math.max(colorG, 0);
            colorB = Math.max(colorB, 0);
            colorR = Math.min(colorR, 255);
            colorG = Math.min(colorG, 255);
            colorB = Math.min(colorB, 255);
            colorX = mouseX;
            colorY = mouseY;
            if(colorX >= colorPickerX + 191 && colorX <= colorPickerX + 194)
                colorX = colorPickerX + 191;
            if(colorX >= colorPickerX + 195 && colorX <= colorPickerX + 197)
                colorX = colorPickerX + 197;
            clickedRecent = false;
        }
        else if(mouseX >= 0 && mouseX < height && mouseY >= 0 && mouseY < height)
        {
            if(drawingMode.equals("Draw"))
            {
                for(int y = size * mouseY/height - drawingSize/2; y <= size * mouseY/height + drawingSize/2; y++)
                {
                    for(int x = size * mouseX/height - drawingSize/2; x <= size * mouseX/height + drawingSize/2; x++)
                    {
                        boolean stillRecent = false;
                        for(int i = recentColors.length - 1; i >= 0; i--)
                        {
                            if(recentColors[i] == rgbNum(colorR, colorG, colorB))
                                stillRecent = true;
                        }
                        if(stillRecent && (colorR != -1 && colorG != -1 && colorB != -1))
                        {
                            int color = 0;
                            int index = 0;
                            for(int i = recentColors.length - 1; i >= 0; i--)
                            {
                                if(recentColors[i] == rgbNum(colorR, colorG, colorB))
                                {
                                    color = rgbNum(colorR, colorG, colorB);
                                    index = i;
                                    if(clickedRecent)
                                    {
                                        colorX = 537;
                                        colorY = 247;
                                    }
                                }
                            }
                            for(int i = index; i >= 1; i--)
                            {
                                recentColors[i] = recentColors[i - 1];
                            }
                            recentColors[0] = color;
                        }
                        else if(colorR != -1 && colorG != -1 && colorB != -1)
                        {
                            for(int i = recentColors.length - 1; i >= 0; i--)
                            {
                                if(i == 0)
                                    recentColors[0] = rgbNum(colorR, colorG, colorB);
                                else
                                    recentColors[i] = recentColors[i - 1];
                            }
                        }
                        if(colorR != -1 && colorG != -1 && colorB != -1)
                            pixels[Math.min(Math.max(x, 0), 63)][Math.min(Math.max(y, 0), 63)] = rgbNum(colorR, colorG, colorB);
                    }
                }
            }
            else if(drawingMode.equals("Copy"))
            {
                if(x1 == -1)
                    x1 = 8 * (int) (mouseX/8.0);
                if(y1 == -1)
                    y1 = 8 * (int) (mouseY/8.0);
                x2 = 8 * (int) (mouseX/8.0 + 1);
                y2 = 8 * (int) (mouseY/8.0 + 1);
                if(drawCopyText && mouseX > (x2 - x1)/2 + x1 - copyTextWidth/2 && mouseX < (x2 - x1)/2 + x1 + copyTextWidth/2 && mouseY > (y2 - y1)/2 + y1 - copyTextHeight/2 && mouseY < (y2 - y1)/2 + y1 + copyTextHeight/2)
                    copy();
            }
            else if(drawingMode.equals("Paste"))
            {
                if(copy.length > 0 && pasteWidth > 0 && pasteHeight > 0)
                    paste();
            }
        }
        if(mouseX > width - 100 && mouseX < width - 10 && mouseY > height - 100 && mouseY < height - 10)
        {
            try
            {
                saveTexture();
            }
            catch(IOException e)
            {
                
            }
        }
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 122 - rectHeight/2 && mouseY < 122 + rectHeight)
            drawingSize = 1;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 142 - rectHeight/2 && mouseY < 142 + rectHeight)
            drawingSize = 3;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 162 - rectHeight/2 && mouseY < 162 + rectHeight)
            drawingSize = 5;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 182 - rectHeight/2 && mouseY < 182 + rectHeight)
            drawingSize = 7;
        if(mouseX > 765 && mouseX < 775 + rectWidth && mouseY > 202 - rectHeight/2 && mouseY < 202 + rectHeight)
            drawingSize = 9;
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 312 - rectHeight/2 && mouseY < 312 + rectWidth)
            drawingMode = "Draw";
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 332 - rectHeight/2 && mouseY < 332 + rectWidth)
            drawingMode = "Copy";
        if(mouseX > 532 && mouseX < 542 + rectWidth2 && mouseY > 352 - rectHeight/2 && mouseY < 352 + rectWidth)
            drawingMode = "Paste";
        for(int i = 0; i < recentColors.length; i++)
        {
            if(mouseX > 522 + 40 * i && mouseX < 552 + 40 * i && mouseY > 232 && mouseY < 262)
            {
                colorR = getR(recentColors[i]);
                colorG = getG(recentColors[i]);
                colorB = getB(recentColors[i]);
                colorX = 537 + 40 * i;
                colorY = 247;
                clickedRecent = true;
            }
        }
        repaint();
    }

    public void mouseMoved(MouseEvent me)
    {
        mouseX = me.getX();
        mouseY = me.getY();
    }

    private void saveTexture() throws IOException
    {
        String location = JOptionPane.showInputDialog("Name of file to save to:");
        if(location != null && !location.equals(""))
        {
            PrintWriter outFile = new PrintWriter(new File("SavedTextures/" + location + ".txt"));
            for(int x = 0; x < size; x++)
            {
                for(int y = 0; y < size; y++)
                {
                    outFile.print(pixels[y][x] + " ");
                }
                outFile.println();
            }
            outFile.close();
            JOptionPane.showMessageDialog(null, "Saved to '" + location + ".txt'\nin SavedTextures folder.");
        }
        else
        {
            location = JOptionPane.showInputDialog("Name of file to save to:");
            if(location != null && !location.equals(""))
            {
                PrintWriter outFile = new PrintWriter(new File("E:/Adventure/SavedTextures/" + location + ".txt"));
                for(int x = 0; x < 15; x++)
                {
                    for(int y = 0; y < 15; y++)
                    {
                        if(pixels[y][x] != rgbNum(255, 255, 255))
                            outFile.print(pixels[y][x] + " ");
                        else
                            outFile.print(-1 + " ");
                    }
                    outFile.println();
                }
                outFile.close();
                JOptionPane.showMessageDialog(null, "Saved to '" + location + ".txt'\nin SavedTextures folder.");
            }
            else
            {
                location = JOptionPane.showInputDialog("Name of file to save to:");
                if(location != null && !location.equals(""))
                {
                    PrintWriter outFile = new PrintWriter(new File("E:/AI/SavedTextures/" + location + ".txt"));
                    for(int x = 0; x < 64; x++)
                    {
                        for(int y = 0; y < 64; y++)
                        {
                            if(pixels[y][x] != rgbNum(0, 0, 0))
                                outFile.print(1 + " ");
                            else
                                outFile.print(0 + " ");
                        }
                        outFile.println();
                    }
                    outFile.close();
                    JOptionPane.showMessageDialog(null, "Saved to '" + location + ".txt'\nin SavedTextures folder.");
                }
            }
        }
    }

    private void copy()
    {
        pasteWidth = x2/8 - x1/8;
        pasteHeight = y2/8 - y1/8;
        copy = new int[pasteWidth][pasteHeight];
        for(int x = 0; x < pasteWidth; x++)
        {
            for(int y = 0; y < pasteHeight; y++)
            {
                copy[x][y] = pixels[x + x1/8][y + y1/8];
            }
        }
    }

    private void paste()
    {
        if(pasteWidth > 0 && pasteHeight > 0)
        {
            for(int x = 0; x < copy.length; x++)
            {
                for(int y = 0; y < copy[0].length; y++)
                {
                    if(pasteX < 0)
                        pasteX = 0;
                    if(pasteX > size - copy.length)
                        pasteX = size - copy.length;
                    if(pasteY < 0)
                        pasteY = 0;
                    if(pasteY > size - copy[0].length)
                        pasteY = size - copy[0].length;
                    pixels[x + pasteX][y + pasteY] = copy[x][y];
                }
            }
            pasteWidth = 0;
            pasteHeight = 0;
            pasteX = 0;
            pasteY = 0;
        }
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

    public static void main(String[] args)
    {
        TextureCreator tc = new TextureCreator();
        JFrame jf = new JFrame();
        jf.setTitle("Texture Creator");
        jf.setSize(width + 6, height + 29);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(tc);
        jf.setVisible(true);
    }
}