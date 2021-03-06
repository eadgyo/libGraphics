package org.cora.graphics.elements;

import org.cora.graphics.base.Image;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;

/**
 * Button with an image centered
 */
public class ImageButton extends Button
{
    protected Image image;
    public ImageButton(int x, int y, int width, int height, Image image)
    {
        super(x, y, width, height);
        this.image = image;
        image.setPos((float) x, (float) y);
    }
    
    public void render(Graphics g)
    {
        if (!isActive)
            return;

        super.render(g);

        if (isHighlighted)
        {
            myColor savedColor = image.getColor();
            image.setColor(highLightColor);
            image.draw(g);
            image.setColor(savedColor);
        }
        else
            image.draw(g);
    }
}
