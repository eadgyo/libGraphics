package org.cora.graphics.elements;

import org.cora.graphics.base.Image;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;

public class ImagesButton extends Button
{
    protected Image images[];

    public ImagesButton(int x, int y, int width, int height, Image images[])
    {
        super(x, y, width, height);
        this.images = images;
    }

    public void render(Graphics g)
    {
        if (isActive)
        {
            for (int i = 0; i < images.length; i++)
            {
                myColor savedColor = images[i].getColor();
                images[i].setColor(savedColor.add(addColor));
                images[i].draw(g);
                images[i].setColor(savedColor);
            }
        }
        else
        {
            for (int i = 0; i < images.length; i++)
            {
                images[i].draw(g);
            }
        }

        super.render(g);
    }
}
