package org.cora.graphics.font;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.ByteBuffer;

import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.Surface;
import org.cora.graphics.graphics.myColor;

/**
 * Font used to render text
 */
public class Font
{
    public final static int NCHAR = 256;

    private int             width;
    private int             height;
    private int             cols, rows;
    private Surface         surface;
    private FontData        fontData[];
    private String          name;

    public Font()
    {
        this(NCHAR);
    }

    /**
     *
     * @param numberChar number Of characters in font
     */
    public Font(int numberChar)
    {
        this.surface = null;
        fontData = new FontData[NCHAR];

        for (int i = 0; i < fontData.length; i++)
        {
            fontData[i] = new FontData();
        }
    }

    /**
     * Create font and load the image
     * @param surface texture
     * @param width length of one element
     * @param height length of one element
     */
    public Font(Surface surface, int width, int height)
    {
        this.surface = surface;
        this.width = width;
        this.height = height;

        cols = surface.w / width;
        rows = surface.h / height;

        fontData = new FontData[cols * rows];

        for (int i = 0; i < fontData.length; i++)
        {
            fontData[i] = new FontData();
        }

        name = surface.textureName;
        
        computeBounds();
    }

    /**
     * Initialize font texture
     * @param surface texture
     * @param width length of one element
     * @param height length of one element
     */
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

    /**
     *
     * @param surface texture
     * @param width length of one element
     * @param height length of one element
     * @param computeBounds compute the real length of each letter
     */
    public void initialize(Surface surface, int width, int height,
            boolean computeBounds)
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
        
        name = surface.textureName;
    }

    public void initBounds()
    {
        int actual = 0;
        int row = 0;
        int col = 0;
        int width = getWidth();

        while (actual < fontData.length && row < rows)
        {
            fontData[actual].left = col * width;
            fontData[actual].width = width;

            actual++;
            col++;

            if (col >= cols)
            {
                row += col / cols;
                col = col % cols;
            }
        }
    }

    /**
     * Compute real length of each letter
     */
    public void computeBounds()
    {
        int row = 0;
        int col = 0;
        int actual = 0;
        int width = getWidth();
        int height = getHeight();

        while (actual < fontData.length && row < rows)
        {
            computeBound(col * width, row * height, fontData[actual]);

            actual++;
            col++;

            if (col >= cols)
            {
                row += col / cols;
                col = col % cols;
            }
        }
    }

    /**
     *
     * @param width size of space
     */
    public void setSpaceSize(int width)
    {
        fontData[' '].width = width;
    }

    /**
     * Compute real length of one letter
     */
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
                byte alpha = pixels.get((x + y * surface.w) * 4 + 3);
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

    /**
     *
     * @param c letter
     * @return computed width of the letter
     */
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
        return height;
    }

    /**
     *
     * @param c letter
     * @return position of the letter in the texture
     */
    public int getXRec(char c)
    {
        if (c < fontData.length)
            return fontData[c].left;
        else
            return 0;
    }

    /**
     *
     * @param c letter
     * @return position of the letter in the texture with fixed size
     */
    public int getXRecFixed(char c)
    {
        return (c % cols) * getWidth();
    }

    /**
     *
     * @param c letter
     * @return position of the letter in the texture
     */
    public int getYRec(char c)
    {
        return (c / cols) * getHeight();
    }

    public int[] getWidthHeight(String string)
    {
        int sizes[] = new int[2];
        sizes[0] = 0;
        sizes[1] = height;
        for (int i = 0; i < string.length(); i++)
        {
            sizes[0] += getWidth(string.charAt(i));
        }
        return sizes;
    }

    /**
     * Render one letter
     * @param g tool to render
     * @param c letter
     * @param w width
     * @param xrec position of the letter
     * @param x rendering position
     * @param y rendering position
     * @param scale scaling factor
     */
    private void printChar(Graphics g, char c, int w, int xrec, int x, int y,
            float scale)
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

    /**
     * Render one letter
     * @param g tool to render
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param scale scaling factor
     */
    public void print(Graphics g, char c, int x, int y, float scale)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(c), getXRec(c), x, y, scale);
        }
    }

    /**
     * Render one letter with fixed size
     * @param g tool to render
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param scale scaling factor
     */
    public void printFixedWidth(Graphics g, char c, int x, int y, float scale)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(), getXRecFixed(c), x, y, scale);
        }
    }

    /**
     * Render one letter
     * @param g tool to render
     * @param c letter
     * @param w width
     * @param xrec position of the letter
     * @param x rendering position
     * @param y rendering position
     */
    private void printChar(Graphics g, char c, int w, int xrec, int x, int y)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        glPushMatrix();

        glTranslatef(x - xrec, y - yrec, 0);
        g.render(surface, xrec, yrec, w, h);

        glPopMatrix();
    }

    /**
     * Render one letter
     * @param g tool to render
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     */
    public void print(Graphics g, char c, int x, int y)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(c), getXRec(c), x, y);
        }
    }

    /**
     * Render one letter with fixed size
     * @param g tool to render
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     */
    public void printFixedWidth(Graphics g, char c, int x, int y)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            printChar(g, c, getWidth(), getXRecFixed(c), x, y);
        }
    }

    /**
     * Render one letter on buffer wihtout alpha
     * @param pixels render output
     * @param c letter
     * @param w width
     * @param xrec position of the letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     */
    private void printChar(ByteBuffer pixels, char c, int w, int xrec, int x,
            int y, int width, int bytesPerPixel)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width) * bytesPerPixel;
        int startFont = (xrec + yrec * surface.w) * bytesPerPixel;

        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                for (int t = 0; t < bytesPerPixel; t++)
                {
                    pixels.put(
                            startImage + (i + j * width) * bytesPerPixel + t,
                            surface.pixels.get(startFont + (i + j * surface.w)
                                    * bytesPerPixel + t));
                }
            }
        }
    }

    /**
     * Render one letter on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param w width
     * @param xrec position of the letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     */
    private void printCharAlpha(ByteBuffer pixels, char c, int w, int xrec,
            int x, int y, int width, int bytesPerPixel)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width) * bytesPerPixel;
        int startFont = (xrec + yrec * surface.w) * bytesPerPixel;

        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                byte alpha = (surface.pixels.get(startFont
                        + (i + j * surface.w) * bytesPerPixel + bytesPerPixel
                        - 1));

                if (alpha != 0)
                {
                    for (int t = 0; t < bytesPerPixel - 1; t++)
                    {
                        pixels.put(
                                startImage + (i + j * width) * bytesPerPixel
                                        + t,
                                surface.pixels.get(startFont
                                        + (i + j * surface.w) * bytesPerPixel
                                        + t));
                    }
                    pixels.put(startImage + (i + j * width) * bytesPerPixel
                            + bytesPerPixel - 1, alpha);
                }
            }
        }
    }

    /**
     * Render rectangle on render
     * @param pixels render output
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     * @param color back ground color
     */
    public void printSquare(ByteBuffer pixels, int x, int y, int width,
            int height, int surfaceWidth, int bytesPerPixel, myColor color)
    {
        int startImage = (x + y * surfaceWidth) * bytesPerPixel;
        int offset;

        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                offset = startImage + (i + j * surfaceWidth) * bytesPerPixel;

                for (int t = 0; t < bytesPerPixel; t++)
                {
                    pixels.put(offset + t, color.getByte(t));
                }
            }
        }
    }

    /**
     * Render one letter on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     */
    public void print(ByteBuffer pixels, char c, int x, int y, int width,
            int bytesPerPixel)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            if (bytesPerPixel != 4)
                printChar(pixels, c, getWidth(c), getXRec(c), x, y, width,
                        bytesPerPixel);
            else
                printCharAlpha(pixels, c, getWidth(c), getXRec(c), x, y, width,
                        bytesPerPixel);
        }
    }

    /**
     * Render one letter with fixed width on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     */
    public void printFixedWidth(ByteBuffer pixels, char c, int x, int y,
            int width, int bytesPerPixel)
    {
        if (c < fontData.length && !fontData[c].isEmpty)
        {
            if (bytesPerPixel != 4)
                printChar(pixels, c, getWidth(), getXRecFixed(c), x, y, width,
                        bytesPerPixel);
            else
                printCharAlpha(pixels, c, getWidth(), getXRecFixed(c), x, y,
                        width, bytesPerPixel);
        }
    }

    /**
     * Render one letter on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param w width
     * @param xrec position of the letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     * @param fontColor text color
     * @param backColor background color
     */
    private void printCharOptimized(ByteBuffer pixels, char c, int w, int xrec,
            int x, int y, int width, int bytesPerPixel, myColor fontColor,
            myColor backColor)
    {
        int h = getHeight();
        int yrec = getYRec(c);

        int startImage = (x + y * width) * bytesPerPixel;
        int startFont = (xrec + yrec * surface.w) * bytesPerPixel;

        int offset;
        int offset2;

        byte res;
        int v;
        float alpha;

        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                offset = startImage + (i + j * width) * bytesPerPixel;
                offset2 = startFont + (i + j * surface.w) * bytesPerPixel;

                v = unsignedToBytes(surface.pixels.get(offset2 + bytesPerPixel
                        - 1));
                alpha = (v / 255) * fontColor.a;

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
                        res = (byte) (v * fontColor.get(t) * alpha + backColor
                                .getInt(t) * backColor.a * (1 - alpha));
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

    /**
     * Render one letter on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     * @param fontColor text color
     * @param backColor background color
     */
    public void printOptimized(ByteBuffer pixels, char c, int x, int y,
            int width, int bytesPerPixel, myColor fontColor, myColor backColor)
    {
        if (c < fontData.length && (!fontData[c].isEmpty || backColor.a != 0))
        {
            printCharOptimized(pixels, c, getWidth(c), getXRec(c), x, y, width,
                    bytesPerPixel, fontColor, backColor);
        }
    }

    /**
     * Render one letter on buffer with alpha support
     * @param pixels render output
     * @param c letter
     * @param x rendering position
     * @param y rendering position
     * @param bytesPerPixel number of bytes per pixel
     * @param fontColor text color
     * @param backColor background color
     */
    public void printFixedWidthOptimized(ByteBuffer pixels, char c, int x,
            int y, int width, int bytesPerPixel, myColor fontColor,
            myColor backColor)
    {
        if (c < fontData.length && (!fontData[c].isEmpty || backColor.a != 0))
        {
            printCharOptimized(pixels, c, getWidth(), getXRecFixed(c), x, y,
                    width, bytesPerPixel, fontColor, backColor);
        }
    }

    /**
     *
     * @return font name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Change font name
     * @param name font name
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
