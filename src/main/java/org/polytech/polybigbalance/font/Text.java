package org.polytech.polybigbalance.font;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.polytech.polybigbalance.graphics.Graphics;
import org.polytech.polybigbalance.graphics.Surface;
import org.polytech.polybigbalance.graphics.myColor;
import org.polytech.polybigbalance.graphics.base.Image;

import cora.maths.Vector2D;

public class Text
{
    private Font            font;

    public static final int TAB_SIZE         = 4;
    public static final int UNDERLINE        = 95;
    public static final int DIST_SPACING     = 0;
    public static final int VERTICAL_SPACING = 5;

    private int             maxWidth;
    private myColor         fontColor;
    private myColor         backColor;
    private int             distSpacing;
    private int             verticalSpacing;
    private boolean         isUnderlined;
    private int             tabSize;
    private Alignement      align;
    private float           delta;
    private float           size;
    private boolean         isProportional;

    private int             x, y;

    public Text(Font font)
    {
        this.font = font;
        fontColor = myColor.WHITE();
        backColor = myColor.BLACK(0);
        isProportional = true;
        distSpacing = DIST_SPACING;
        verticalSpacing = VERTICAL_SPACING;
        isUnderlined = false;
        tabSize = TAB_SIZE;
        delta = 0;
        size = 1.0f;
        maxWidth = 0;
        x = 0;
        y = 0;
        align = Alignement.LEFT;
    }

    public void print(Graphics g, String string)
    {
        print(g, string, x, y);
    }

    public Image transformToImage(String string)
    {
        return transformToImage(string, x, y);
    }

    public Image transformToImage(String string, int x, int y)
    {
        ArrayList<String> strs = new ArrayList<String>();
        ArrayList<Integer> widths = new ArrayList<Integer>();
        ArrayList<Alignement> aligns = new ArrayList<Alignement>();
        transformToStrs(string, strs, widths, aligns);

        Image text = new Image();
        Surface surface = new Surface();

        surface.h = strs.size() * getHeight();
        surface.w = Integer.MIN_VALUE;
        surface.BytesPerPixel = 4;

        for (int i = 0; i < widths.size(); i++)
        {
            if (widths.get(i) > surface.w)
                surface.w = widths.get(i);
        }

        surface.pixels = BufferUtils.createByteBuffer(surface.w * surface.h
                * surface.BytesPerPixel);

        int x0 = 0;
        int y0 = 0;
        int height = getHeight();

        for (int i = 0; i < strs.size(); i++)
        {
            printLine(surface.pixels, strs.get(i), x0, y0, surface.w,
                    aligns.get(i), surface.BytesPerPixel);
            y0 += height;
        }

        text.initialize(surface);
        text.setScale(size);
        text.setPos(new Vector2D(x, y));

        return text;
    }

    public void transformToStrs(String string, ArrayList<String> strs,
            ArrayList<Integer> widths, ArrayList<Alignement> aligns)
    {
        if (string.length() == 0)
            return;

        float height = getHeight();

        float maxWidth = this.maxWidth / size;

        if (maxWidth == 0)
        {
            String strsNL[] = string.split("\n");
            String str;

            for (int i = 0; i < strsNL.length; i++)
            {
                str = strsNL[i];
                widths.add(getWidth(strsNL[i]));
                strs.add(str);
                aligns.add(align);
            }
        }
        else
        {
            char c = ' ';
            char lastC;
            float spacing = (isProportional) ? getProportionalSpacing() : 0;

            StringBuilder word = new StringBuilder();
            StringBuilder sentence = new StringBuilder();

            IntA widthWord = new IntA();
            IntA width = new IntA();
            widthWord.v = 0;
            width.v = 0;

            Alignement artificialAlign = (align == Alignement.FULL) ? Alignement.LEFT
                    : align;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);

                switch (c)
                {
                    case ' ':
                        flush(widthWord, width, word, sentence);

                        word.append(' ');
                        widthWord.v += getWidth(c);

                        if (width.v + widthWord.v >= maxWidth)
                        {
                            strs.add(removeStartEnd(sentence, width));
                            widths.add(width.v);
                            aligns.add(align);

                            reset(width, sentence);
                            reset(widthWord, word);
                        }

                        flush(widthWord, width, word, sentence);
                        break;

                    case '\n':
                        flush(widthWord, width, word, sentence);

                        strs.add(removeStartEnd(sentence, width));
                        widths.add(width.v);
                        aligns.add(artificialAlign);

                        reset(width, sentence);
                        y += height;
                        break;

                    case '\t':
                        flush(widthWord, width, word, sentence);

                        int size = sentence.length();
                        int rest = tabSize - size % tabSize;

                        widthWord.v += getWidth(' ') * rest;
                        for (int j = 0; j < rest; j++)
                        {
                            word.append('\t');
                        }
                        if (width.v + widthWord.v >= maxWidth)
                        {
                            strs.add(removeStartEnd(sentence, width));
                            widths.add(width.v);
                            aligns.add(align);

                            reset(width, sentence);
                            y += height;
                        }

                        flush(widthWord, width, word, sentence);
                        break;

                    default:
                        widthWord.v += getWidth(c);
                        word.append(c);

                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            widthWord.v += spacing;

                        if (widthWord.v + width.v >= maxWidth)
                        {
                            if (width.v == 0) // Le mot dépasse la taille max
                            {
                                flush(widthWord, width, word, sentence);

                                strs.add(removeStartEnd(sentence, width));
                                widths.add(width.v);
                                aligns.add(align);

                                reset(width, sentence);
                                y += height;
                                c = ' ';
                            }
                            else
                            {
                                strs.add(removeStartEnd(sentence, width));
                                widths.add(width.v);
                                aligns.add(align);

                                reset(width, sentence);
                                y += height;
                                c = ' ';
                            }
                        }
                        break;
                }
            }

            flush(widthWord, width, word, sentence);
            strs.add(removeStartEnd(sentence, width));
            widths.add(width.v);
            aligns.add(artificialAlign);
        }
    }

    public void print(Graphics g, String string, int x, int y)
    {
        if (string.length() == 0)
            return;

        float height = getHeight();

        float maxWidth = this.maxWidth / size;

        if (maxWidth == 0)
        {
            String strsNL[] = string.split("\n");
            float width;
            String str;

            for (int i = 0; i < strsNL.length; i++)
            {
                str = strsNL[i];
                width = getWidth(strsNL[i]);

                printLine(g, str, x, y, width, align);
                y += height;
            }
        }
        else
        {
            char c = ' ';
            char lastC;
            float spacing = (isProportional) ? getProportionalSpacing() : 0;

            StringBuilder word = new StringBuilder();
            StringBuilder sentence = new StringBuilder();

            IntA widthWord = new IntA();
            IntA width = new IntA();
            widthWord.v = 0;
            width.v = 0;

            Alignement artificialAlign = (align == Alignement.FULL) ? Alignement.LEFT
                    : align;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);

                switch (c)
                {
                    case ' ':
                        flush(widthWord, width, word, sentence);

                        word.append(' ');
                        widthWord.v += getWidth(c);

                        if (width.v + widthWord.v >= maxWidth)
                        {
                            printLine(g, removeStartEnd(sentence, width), x, y,
                                    width.v, align);
                            reset(width, sentence);
                            reset(widthWord, word);
                            y += height;
                        }

                        flush(widthWord, width, word, sentence);
                        break;

                    case '\n':
                        flush(widthWord, width, word, sentence);
                        printLine(g, removeStartEnd(sentence, width), x, y,
                                width.v, artificialAlign);
                        reset(width, sentence);
                        y += height;
                        break;

                    case '\t':
                        flush(widthWord, width, word, sentence);

                        int size = sentence.length();
                        int rest = tabSize - size % tabSize;

                        widthWord.v += getWidth(' ') * rest;
                        for (int j = 0; j < rest; j++)
                        {
                            word.append('\t');
                        }
                        if (width.v + widthWord.v >= maxWidth)
                        {
                            printLine(g, removeStartEnd(sentence, width), x, y,
                                    width.v, align);
                            reset(width, sentence);
                            y += height;
                        }

                        flush(widthWord, width, word, sentence);
                        break;

                    default:
                        widthWord.v += getWidth(c);
                        word.append(c);

                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            widthWord.v += spacing;

                        if (widthWord.v + width.v >= maxWidth)
                        {
                            if (width.v == 0 && widthWord.v != 0) // Le mot dépasse la taille max
                            {
                                int tmp = getWidth(c);
                                widthWord.v -= tmp;
                                word.setLength(word.length() - 1);
                                
                                flush(widthWord, width, word, sentence);
                                printLine(g, removeStartEnd(sentence, width),
                                        x, y, width.v, align);
                                reset(width, sentence);
                                
                                widthWord.v += tmp;
                                word.append(c);
                                
                                y += height;
                                c = ' ';
                            }
                            else
                            {
                                printLine(g, removeStartEnd(sentence, width),
                                        x, y, width.v, align);
                                reset(width, sentence);
                                y += height;
                                c = ' ';
                            }
                        }
                        break;
                }
            }

            flush(widthWord, width, word, sentence);
            printLine(g, removeStartEnd(sentence, width), x, y, width.v,
                    artificialAlign);
        }
    }

    private void flush(IntA widthWord, IntA width, StringBuilder word,
            StringBuilder sentence)
    {
        if (widthWord.v == 0)
            return;

        sentence.append(word);
        word.setLength(0);
        width.v += widthWord.v;
        widthWord.v = 0;
    }

    private void reset(IntA width, StringBuilder sentence)
    {
        sentence.setLength(0);
        width.v = 0;
    }

    public String removeStartEnd(StringBuilder sb, IntA width)
    {
        if (sb.length() == 0)
            return "";

        int start = 0;
        int end = sb.length();

        // Start
        while (start < sb.length() && sb.charAt(start) == ' ')
        {
            start++;
        }

        // End
        if (start == sb.length())
        {
            sb.setLength(0);
            width.v = 0;
            return "";
        }

        while (sb.charAt(end - 1) == ' ')
        {
            end--;
        }

        width.v -= (sb.length() - end + start) * getWidth(' ');
        return sb.substring(start, end);
    }

    private void printLine(ByteBuffer pixels, String string, int x, int y,
            int width, Alignement alignement, int bytePerPixel)
    {
        float x0, y0;
        float distSpace = getWidth(' ');
        float distWord = (isProportional) ? getProportionalSpacing() : 0;
        y0 = y;

        switch (alignement)
        {
            case LEFT:
                x0 = x;
                break;
            case FULL:
                x0 = x;
                int nSpace = 0;
                for (int i = 0; i < string.length(); i++)
                {
                    if (string.charAt(i) == ' ')
                    {
                        nSpace++;
                    }
                    else if (string.charAt(i) == '\t')
                    {
                        nSpace++;
                    }
                }
                float rest = maxWidth / size - width;
                if (rest > 0)
                {
                    if (nSpace != 0)
                    {
                        distSpace = (float) (rest) / nSpace + distSpace;
                    }
                    else if ((nSpace = string.length() - 1) != 0)
                    {
                        distWord = (float) (rest) / nSpace + distWord;
                    }
                }
                break;
            case TOP_CENTER:
                x0 = x + (maxWidth / size - width) * 0.5f;
                break;
            case RIGHT:
                x0 = x + maxWidth / size - width;
                break;
            default:
                x0 = x;
                break;
        }
        if (isProportional)
        {
            char c = ' ';
            char lastC;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);
                switch (c)
                {
                    case ' ':
                        x0 += distSpace;
                        font.printOptimized(pixels, c, (int) (x0), (int) y0,
                                width, bytePerPixel, fontColor, backColor);
                        break;
                    case '\t':
                        x0 += distSpace;
                        font.printOptimized(pixels, c, (int) (x0), (int) y0,
                                width, bytePerPixel, fontColor, backColor);
                        break;
                    default:
                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            x0 += distWord;
                        font.printOptimized(pixels, c, (int) (x0), (int) y0,
                                width, bytePerPixel, fontColor, backColor);
                        x0 += getWidth(c);
                        break;
                }
            }
        }
        else
        {
            char c = ' ';
            char lastC;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);
                switch (c)
                {
                    case ' ':
                        font.printFixedWidthOptimized(pixels, c, (int) x0,
                                (int) y0, width, bytePerPixel, fontColor,
                                backColor);
                        x0 += distSpace;
                        break;
                    case '\t':
                        font.printFixedWidthOptimized(pixels, c, (int) x0,
                                (int) y0, width, bytePerPixel, fontColor,
                                backColor);
                        x0 += distSpace;
                        break;
                    default:
                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            x0 += distWord;
                        font.printFixedWidthOptimized(pixels, c, (int) x0,
                                (int) y0, width, bytePerPixel, fontColor,
                                backColor);
                        x0 += getWidth(c);
                        break;
                }
            }
        }
    }

    private void printLine(Graphics g, String string, int x, int y,
            float width, Alignement alignement)
    {
        float x0, y0;
        float distSpace = getWidth(' ');
        float distWord = (isProportional) ? getProportionalSpacing() : 0;
        y0 = y;
        
        switch (alignement)
        {
            case LEFT:
                x0 = x;
                break;
            case FULL:
                x0 = x;
                int nSpace = 0;
                for (int i = 0; i < string.length(); i++)
                {
                    if (string.charAt(i) == ' ')
                    {
                        nSpace++;
                    }
                    else if (string.charAt(i) == '\t')
                    {
                        nSpace++;
                    }
                }
                float rest = maxWidth / size - width;
                if (rest > 0)
                {
                    if (nSpace != 0)
                    {
                        distSpace = (float) (rest) / nSpace + distSpace;
                    }
                    else if ((nSpace = string.length() - 1) != 0)
                    {
                        distWord = (float) (rest) / nSpace + distWord;
                    }
                }
                width = maxWidth;
                break;
            case TOP_CENTER:
                x0 = x + (maxWidth / size - width) * 0.5f;
                break;
            case RIGHT:
                x0 = x + maxWidth / size - width;
                break;
            default:
                x0 = x;
                break;
        }
        
        glPushMatrix();
        glTranslatef(x0, y0, 0);
        
        x0 = 0;
        y0 = 0;
        glScalef(size, size, 1.0f);
        
        if (isBackVisible())
        {
            g.setColor(backColor);
            g.fillRec(0, 0, (int) width, getHeight());
        }
        
        g.setColor(fontColor);
        if (isProportional)
        {     
            char c = ' ';
            char lastC;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);
                switch (c)
                {
                    case ' ':
                        x0 += distSpace;
                        break;
                    case '\t':
                        x0 += distSpace;
                        break;
                    default:
                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            x0 += distWord;
                        font.print(g, c, (int) (x0), (int) y0);
                        x0 += getWidth(c);
                        break;
                }
            }
        }
        else
        {            
            char c = ' ';
            char lastC;

            for (int i = 0; i < string.length(); i++)
            {
                lastC = c;
                c = string.charAt(i);
                switch (c)
                {
                    case ' ':
                        x0 += distSpace;
                        break;
                    case '\t':
                        x0 += distSpace;
                        break;
                    default:
                        if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                            x0 += distWord;
                        font.printFixedWidth(g, c, (int) x0, (int) y0, size);
                        x0 += getWidth(c);
                        break;
                }
            }
        }
        glPopMatrix();
    }

    // Getter - Setter
    public void setSize(float size)
    {
        this.size = size;
    }

    public float getSize()
    {
        return size;
    }

    public int getVerticalSpacing()
    {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing)
    {
        this.verticalSpacing = verticalSpacing;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth)
    {
        if (maxWidth > tabSize)
            this.maxWidth = maxWidth;
    }

    public int getTabSize()
    {
        return tabSize;
    }

    public void setTabSize(int tabSize)
    {
        if (tabSize < maxWidth)
            this.tabSize = tabSize;
    }

    public int getHeight()
    {
        return font.getHeight();
    }

    public int getWidth(String str)
    {
        if (isProportional)
            return getProportionalWidth(str);
        else
            return getFixedWidth(str);
    }

    public int getWidth(StringBuilder sb)
    {
        if (isProportional)
            return getProportionalWidth(sb);
        else
            return getFixedWidth(sb);
    }

    public int getProportionalWidth(String str)
    {
        int width = 0;
        char c = ' ';
        char lastC;
        int spacing = (isProportional) ? getProportionalSpacing() : 0;
        for (int i = 0; i < str.length(); i++)
        {
            lastC = c;
            c = str.charAt(i);
            if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                width += spacing;

            width += getProportionalWidth(c);
        }
        return width;
    }

    public int getProportionalWidth(StringBuilder sb)
    {
        int width = 0;
        char c = ' ';
        char lastC;
        int spacing = (isProportional) ? getProportionalSpacing() : 0;
        for (int i = 0; i < sb.length(); i++)
        {
            lastC = c;
            c = sb.charAt(i);
            if (lastC != ' ' && lastC != '\n' && lastC != '\t')
                width += spacing;

            width += getProportionalWidth(c);
        }
        return width;
    }

    public void setAlignement(Alignement align)
    {
        this.align = align;
    }
    
    public Alignement getAlignement()
    {
        return align;
    }
    
    public int getFixedWidth(String str)
    {
        return str.length() * getWidth();
    }

    public int getFixedWidth(StringBuilder sb)
    {
        return sb.length() * getWidth();
    }

    public int getWidth(char c)
    {
        if (isProportional)
            return getProportionalWidth(c);
        else
            return getWidth();
    }

    public int getProportionalWidth(char c)
    {
        return font.getWidth(c);
    }

    public int getWidth()
    {
        return font.getWidth();
    }

    public void setFont(Font font)
    {
        this.font = font;
    }

    public Font getFont()
    {
        return font;
    }

    public void setFontColor(myColor color)
    {
        fontColor.set(color);
    }

    public myColor getFontColor()
    {
        return fontColor;
    }

    public void setBackColor(myColor color)
    {
        backColor.set(color);
    }

    public myColor getBackColor(myColor color)
    {
        return backColor;
    }
    
    public boolean isFontVisible()
    {
        return fontColor.a != 0;
    }
    
    public boolean isBackVisible()
    {
        return backColor.a != 0;
    }

    public void setProportional(boolean p)
    {
        isProportional = p;
    }

    public boolean getProportional()
    {
        return isProportional;
    }

    public void setProportionalSpacing(int d)
    {
        distSpacing = d;
    }

    public int getProportionalSpacing()
    {
        return distSpacing;
    }

    public void setUnderline(boolean u)
    {
        isUnderlined = u;
    }

    public boolean getUnderlined()
    {
        return isUnderlined;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
