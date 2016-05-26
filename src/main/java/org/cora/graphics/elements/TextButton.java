package org.cora.graphics.elements;

import org.cora.graphics.base.Image;
import org.cora.graphics.font.Font;
import org.cora.graphics.font.TextRenderer;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;
import org.cora.graphics.manager.TextureManager;

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
    
    public void updateImage()
    {
        if (textImage != null)
        {
            TextureManager.getInstance().freeTexture(textImage.getSpriteData().surface.textureName);
        }
        
        textImage = text.transformToImage(txt);
        
        if (textImage != null)
        {
            TextureManager.getInstance().addTexture(textImage.getSpriteData().surface);
        }
    }
    
    public void render(Graphics g)
    {    
        super.render(g);
        
        if (preRendering)
        {
            if (textImage == null)
                updateImage();
            textImage.draw(g);
        }
        else
        {
            text.print(g, txt, (int) getCenterX(), (int) getCenterY());
        }
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
}
