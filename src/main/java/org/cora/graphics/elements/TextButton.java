package org.cora.graphics.elements;

import org.cora.graphics.base.Image;
import org.cora.graphics.font.Alignement;
import org.cora.graphics.font.Font;
import org.cora.graphics.font.TextPosition;
import org.cora.graphics.font.TextRenderer;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;
import org.cora.graphics.manager.TextureManager;

/**
 * Button with text display
 */
public class TextButton extends Button
{
    private String txt;
    private TextRenderer text;
    private Image textImage = null;
    private boolean preRendering = true;
    
    public TextButton(int x, int y, int width, int height, TextRenderer text)
    {
        super(x, y, width, height);
        this.text = text;

        setTextMiddleCenter();
    }
    
    public TextButton(int x, int y, int width, int height, Font font, myColor textColor, myColor backColorText, int size)
    {
        this(x, y, width, height, font, textColor, size);
        text.setBackColor(backColorText);
    }
    
    public TextButton(int x, int y, int width, int height, Font font, myColor textColor, int size)
    {
        this(x, y, width, height, font, textColor);
        text.setSize(size);
    }
    
    public TextButton(int x, int y, int width, int height, Font font, myColor textColor)
    {
        super(x, y, width, height);
    
        text = new TextRenderer(font);
        text.setFontColor(textColor);

        setTextMiddleCenter();
    }


    public void setTextMiddleCenter()
    {
        text.setPos((int) (getWidth()*0.5f), (int) (getHeight()*0.5f - text.getHeight()*0.5f));
        text.setTextPosition(TextPosition.TOP_CENTER);
        text.setAlignement(Alignement.TOP_CENTER);
    }

    public void setTextMiddleLeft()
    {
        text.setPos(0, (int) (getHeight()*0.5f - text.getHeight()*0.5f));
        text.setTextPosition(TextPosition.LEFT);
        text.setAlignement(Alignement.LEFT);
    }

    public void setTextMiddleRight()
    {
        text.setPos((int) getWidth(), (int) (getHeight()*0.5f - text.getHeight()*0.5f));
        text.setTextPosition(TextPosition.RIGHT);
        text.setAlignement(Alignement.RIGHT);
    }

    public void setTextTopCenter()
    {
        text.setPos((int) (getWidth()*0.5f), 0);
        text.setTextPosition(TextPosition.TOP_CENTER);
        text.setAlignement(Alignement.TOP_CENTER);
    }

    public void setTextTopLeft()
    {
        text.setPos(0, 0);
        text.setTextPosition(TextPosition.LEFT);
        text.setAlignement(Alignement.LEFT);
    }

    public void setTextTopRight()
    {
        text.setPos((int) getWidth(), 0);
        text.setTextPosition(TextPosition.RIGHT);
        text.setAlignement(Alignement.RIGHT);
    }

    public void setTextBotCenter()
    {
        text.setPos((int) (getWidth()*0.5f), (int) (getHeight() - text.getHeight()));
        text.setTextPosition(TextPosition.TOP_CENTER);
        text.setAlignement(Alignement.TOP_CENTER);
    }

    public void setTextBotLeft()
    {
        text.setPos(0, (int)  (int) (getHeight() - text.getHeight()));
        text.setTextPosition(TextPosition.LEFT);
        text.setAlignement(Alignement.LEFT);
    }

    public void setTextBotRight()
    {
        text.setPos((int) getWidth(), (int) (int) (getHeight() - text.getHeight()));
        text.setTextPosition(TextPosition.RIGHT);
        text.setAlignement(Alignement.RIGHT);
    }


    /**
     * Set relative pos of the text
     * Relative means left corner is origin
     * @param x coordinate
     * @param y coordinate
     */
    public void setTextPos(int x, int y)
    {
        text.setPos(x, y);
    }
    
    public TextButton(int x, int y, int width, int height, Font font)
    {
        this(x, y, width, height, font, myColor.BLACK());
    }
    
    public void setTextBackColor(myColor backColorText)
    {
        text.setBackColor(backColorText);
    }
    
    public void setTextColor(myColor fontColorText)
    {
        text.setFontColor(fontColorText);
    }

    private void freeTextImage()
    {
        if (textImage != null)
        {
            TextureManager.getInstance().freeTextureGL(textImage.getSpriteData().surface);
            textImage = null;
        }
    }

    private void  loadTextImage()
    {
        if (textImage != null)
        {
            TextureManager.getInstance().loadTextureGL(textImage.getSpriteData().surface);
        }
    }

    /**
     * Update the text image used with preRendering
     */
    public void updateImage()
    {
        freeTextImage();

        textImage = text.transformToImage(txt);

        loadTextImage();
    }
    
    public void render(Graphics g)
    {
        if (!isActive)
            return;

        super.render(g);

        g.translate(getLeft());

        if (preRendering)
        {
            if (textImage == null)
                updateImage();
            textImage.draw(g);
        }
        else
        {
            text.print(g, txt);
        }

        g.translate(getLeft().multiply(-1));
    }
    
    public String getTxt()
    {
        return txt;
    }
    
    public void setTxt(String txt)
    {
        this.txt = txt;
    }
    
    public TextRenderer getTextRenderer()
    {
        return text;
    }
    
    public void setTextRenderer(TextRenderer text)
    {
        this.text = text;
    }

    /**
     *
     * @param preRendering activate stored image of the text
     */
    public void setPreRendering(boolean preRendering)
    {
        this.preRendering = preRendering;
    }

    /**
     *
     * @return image is recalculated at each rendering
     */
    public boolean getPreRendering()
    {
        return preRendering;
    }

    public void setTextSize(int size)
    {
        text.setSize(size);
        textImage = null;
    }
}
