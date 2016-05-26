package org.polytech.polybigbalance.font;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.ByteBuffer;

import org.polytech.polybigbalance.graphics.Graphics;
import org.polytech.polybigbalance.graphics.Surface;
import org.polytech.polybigbalance.graphics.myColor;

import cora.maths.Rectangle;
import cora.maths.Vector2D;

public class Font
{
    public final static int NCHAR = 256;

    private int             width;
    private int             height;
    private int             cols, rows;
    private Surface         surface;
    private FontData        fontData[];
    
    public Font()
    {
        this(NCHAR);
    }

    public Font(int numberChar)
    {
        this.surface = null;
        fontData = new FontData[NCHAR];

        for (int i = 0; i < fontData.length; i++)
        {
            fontData[i] = new FontData();
        }
    }

    public void initialize(Surface surface, int width, int height)
    {
        initialize(surface, width, height, true);
    }
    
    public void initialize(Surface surface, int width)
    {
        initialize(surface, width, width, true);
    }

    public void initialize(Surface surface, int width, boolean computeBounds)
    {
        initialize(surface, width, width, computeBounds);
    }
    
    public void initialize(Surface surface, int width, int height, boolean computeBounds)
    {
        this.surface = surface;
        this.width = width;
        this.height = height;
        cols = surface.w / width;
        rows = surface.h / height;
        if (computeBounds)
        {
            computeBounds();
        }
        else
        {
            initBounds();
        }
    }

    public void initBounds()
    {
        int actual = 0;
        int row = 0;
        int col = 0;
        int width = getWidth();
        
        while (actual < fontData.length && row < rows)
        {
            fontData[actual].left = col*width;
            fontData[actual].width = width;
            
            actual++;
            col++;
            
            if (col >= cols)
            {
                row += col/cols;
                col = col%cols;
            }
        }
    }
    
    public void computeBounds()
    {
        int row = 0;
        int col = 0;
        int actual = 0;
        int width = getWidth();
        int height = getHeight();
        
        while (actual < fontData.length && row < rows)
        {
            computeBound(col*width, row*height, fontData[actual]);
            
            actual++;
            col++;
            
            if (col >= cols)
            {
                row += col/cols;
                col = col%cols;
            }
        }
    }
    
    public void setSpaceSize(int width)
    {
        fontData[' '].width = width;
    }

    public void computeBound(int x0, int y0, FontData data)
    {
        ByteBuffer pixels = surface.pixels;
        data.left = x0;
        data.width = width;
        data.isEmpty = true;
        
        col:
        for (int x = x0; x < width + x0; x++)
        {
            for (int y = y0; y < width + y0; y++)
            {
                byte alpha = pixels.get((x + y * surface.w)*4 + 3);
                if (alpha != 0)
                {
                    data.isEmpty = false;
                    data.width = x - data.left + 1;
                    continue col;
                }
            }

            if (x == data.left)
            {
                data.left = x + 1;
            }
        }
    }

    public int getWidth(char c)
    {
        if (c < fontData.length)
            return fontData[c].width;
        else
            return 0;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return width;
    }

    public int getXRec(char c)
    {
        if (c < fontData.length)
            return fontData[c].left;
        else
            return 0;
    }

    public int getXRecFixed(char c)
    {
        return (c%cols)*getHeight();
    }

    public int getYRec(char c)
    {
        return (c/cols)*getWidth();
    }

    public int[] getWidthHeight(String string)
    {
        int sizes[] = new int[2];
        sizes[0] = 0;
        sizes[1] = width;
        for (int i = 0; i < string.length(); i++)
        {
            sizes[0] += getWidth(string.charAt(i));
        }
        return sizes;
    }

    private void printChar(Graphics g, char c, int w, int xrec, int x, int y, float scale)
    {
        int h = getHeight();
        
        int yrec = getYRec(c);
        
        glPushMatrix();
        
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1.0f);
        glTranslatef(-xrec, -yrec, 0);
        g.render(surface, xrec, yrec, w, h);
        
        glPopMatrix();   
    }
    
    public void print(Graphics g, char c, int x, int y, float scale)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(c), getXRec(c), x, y, scale);
        }
    }
    
    public void printFixedWidth(Graphics g, char c, int x, int y, float scale)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(), getXRecFixed(c), x, y, scale);
        }
    }
    
    private void printChar(Graphics g, char c, int w, int xrec, int x, int y)
    {
        int h = getHeight();
        int yrec = getYRec(c);
        
        glPushMatrix();
        
        glTranslatef(x - xrec, y - yrec, 0);
        g.render(surface, xrec, yrec, w, h);
        
        glPopMatrix();
    }
    
    
    public void print(Graphics g, char c, int x, int y)
    {   
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(c), getXRec(c), x, y);
        }
    }

    public void printFixedWidth(Graphics g, char c, int x, int y)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(), getXRecFixed(c), x, y);
        }
    }
    
    private void printChar(ByteBuffer pixels, char c, int w, int xrec, int x, int y, int width, int bytesPerPixel)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width)*bytesPerPixel;
        int startFont = (xrec + yrec * surface.w)*bytesPerPixel;
        
        for (int j = 0 ; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                for (int t = 0; t < bytesPerPixel; t++)
                {
                    pixels.put(startImage + (i + j * width)*bytesPerPixel + t,
                            surface.pixels.get(startFont + (i + j * surface.w)*bytesPerPixel + t));
                }
            }
        }
    }
    
    private void printCharAlpha(ByteBuffer pixels, char c, int w, int xrec, int x, int y, int width, int bytesPerPixel)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width)*bytesPerPixel;
        int startFont = (xrec + yrec * surface.w)*bytesPerPixel;
        
        for (int j = 0 ; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                byte alpha =  (surface.pixels.get(startFont + (i + j * surface.w)*bytesPerPixel + bytesPerPixel - 1));
                
                if (alpha != 0)
                {
                    for (int t = 0; t < bytesPerPixel - 1; t++)
                    {
                        pixels.put(startImage + (i + j * width)*bytesPerPixel + t,
                                surface.pixels.get(startFont + (i + j * surface.w)*bytesPerPixel + t));
                    }
                    pixels.put(startImage + (i + j * width)*bytesPerPixel + bytesPerPixel - 1,
                            alpha);
                }
            }
        }
    }
    
    public void print(ByteBuffer pixels, char c, int x, int y, int width, int bytesPerPixel)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        { 
            if (bytesPerPixel != 4)
                printChar(pixels, c, getWidth(c), getXRec(c), x, y, width, bytesPerPixel);
            else
                printCharAlpha(pixels, c, getWidth(c), getXRec(c), x, y, width, bytesPerPixel);
        }
    }

    public void printFixedWidth(ByteBuffer pixels, char c, int x, int y, int width, int bytesPerPixel)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        { 
            if (bytesPerPixel != 4)
                printChar(pixels, c, getWidth(), getXRecFixed(c), x, y, width, bytesPerPixel);
            else
                printCharAlpha(pixels, c, getWidth(), getXRecFixed(c), x, y, width, bytesPerPixel);
        }
    }
    
    private void printCharOptimized(ByteBuffer pixels, char c, int w, int xrec, int x, int y, int width, int bytesPerPixel, myColor fontColor, myColor backColor)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width)*bytesPerPixel;
        int startFont = (xrec + yrec * surface.w)*bytesPerPixel;
        
        int offset;
        int offset2;
        
        byte res;
        int v;
        float alpha;
        
        for (int j = 0 ; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                offset = startImage + (i + j * width)*bytesPerPixel;
                offset2 = startFont + (i + j * surface.w)*bytesPerPixel;
                
                v = unsignedToBytes(surface.pixels.get(offset2 + bytesPerPixel - 1));
                alpha = (v / 255) * fontColor.a;
                
                if (v != 0)
                {
                    v = 1;
                }
                
                if (alpha == 0)
                {
                    for (int t = 0; t < bytesPerPixel; t++)
                    {
                        pixels.put(offset + t, backColor.getByte(t));
                    }
                }
                else
                {
                    for (int t = 0; t < bytesPerPixel; t++)
                    {
                        v = unsignedToBytes(surface.pixels.get(offset2 + t));
                        res = (byte) (v * fontColor.get(t) * alpha + backColor.getInt(t) * backColor.a * (1 - alpha));
                        pixels.put(offset + t, res);
                    }
                }
            }
        }
    }
    
    public static int unsignedToBytes(byte b)
    {
        return b & 0xFF;
    }
    
    public void printOptimized(ByteBuffer pixels, char c, int x, int y, int width, int bytesPerPixel, myColor fontColor, myColor backColor)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        { 
            printCharOptimized(pixels, c, getWidth(c), getXRec(c), x, y, width, bytesPerPixel, fontColor, backColor);
        }
    }
    
    public void printFixedWidthOptimized(ByteBuffer pixels, char c, int x, int y, int width, int bytesPerPixel, myColor fontColor, myColor backColor)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        { 
            printCharOptimized(pixels, c, getWidth(), getXRecFixed(c), x, y, width, bytesPerPixel, fontColor, backColor);
        }
    }
}
