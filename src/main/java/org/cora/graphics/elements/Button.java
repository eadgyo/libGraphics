package org.cora.graphics.elements;

import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;
import org.cora.maths.Vector2D;
import org.cora.maths.sRectangle;

/**
 *
 */
public class Button
{
    protected myColor recColor;
    protected myColor backColor;
    protected myColor addColor;
    protected boolean isHighlighted;
    protected boolean isActive;
    
    sRectangle rectangle;

    /**
     * Create a button centered
     * @param x centerX
     * @param y centerY
     * @param width length of the button
     * @param height length of the button
     */
    public Button(int x, int y, int width, int height)
    {
        recColor = new myColor();
        addColor = myColor.WHITE(0.2f);
        backColor = myColor.GREY();
        isHighlighted = false;
        isActive = true;
        rectangle = new sRectangle(x, y, width, height);
    }

    /**
     *
     * @return the color of the lines of rectangle
     */
    public myColor getRecColor()
    {
        return recColor;
    }

    /**
     *
     * @param recColor color of the lines of the rectangle
     */
    public void setRecColor(myColor recColor)
    {
        this.recColor.set(recColor);
    }

    public void setRecColor(float r, float g, float b, float a)
    {
        this.recColor.set(r, g, b, a);
    }

    /**
     *
     * @return background color of the button
     */
    public myColor getBackColor()
    {
        return backColor;
    }

    /**
     *
     * @param backColor background color the button
     */
    public void setBackColor(myColor backColor)
    {
        this.backColor.set(backColor);
    }
    
    public void setBackColor(float r, float g, float b, float a)
    {
        this.backColor.set(r, g, b, a);
    }

    /**
     *
     * @return rectangle of the button
     */
    public sRectangle getRectangle()
    {
        return rectangle;
    }

    /**
     * Change the size and coordinates of the button
     * @param rectangle pattern
     */
    public void setRectangle(sRectangle rectangle)
    {
        this.rectangle.set(rectangle);
    }

    /**
     * Update the button if it has animation
     * Default: no animation
     * @param dt time since last frame
     */
    public void update(float dt)
    {}

    /**
     * Render the button using lengths, coordinates and colors
     * @param g graphicsTools used to render the button
     */
    public void render(Graphics g)
    {
        if (!isActive)
            return;

        if (backColor.isVisible() || isHighlighted)
        {
            if (isHighlighted)
                g.setColor(backColor.add(addColor));
            else
                g.setColor(backColor);
            
            g.fillForm(rectangle);
        }
        
        if (recColor.isVisible())
        {
            g.setColor(recColor);
            g.drawForm(rectangle);
        }
    }

    /**
     * Test if an point element is colliding
     * @param pos position of the element that may be colliding
     * @return result of test
     */
    public boolean isColliding(Vector2D pos)
    {
        return (isActive) ? rectangle.isInsideBorder(pos) : false;
    }
    
    public myColor getAddColor()
    {
        return addColor;
    }

    /**
     * Set highlighted color
     * @param addColor color + addColor
     */
    public void setAddColor(myColor addColor)
    {
        this.addColor.set(addColor);
    }

    public boolean isHighlighted()
    {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted)
    {
        this.isHighlighted = isHighlighted;
    }
    
    public void setX(float x)
    {
        rectangle.setX(x);
    }
    
    public float getCenterX()
    {
        return rectangle.getCenterX();
    }
    
    public float getX()
    {
        return rectangle.getCenterX();
    }
    
    public void setY(float y)
    {
        rectangle.setY(y);
    }
    
    public float getY()
    {
        return rectangle.getCenterY();
    }
    
    public float getCenterY()
    {
        return rectangle.getCenterY();
    }
    
    public void setLeft(float x)
    {
        rectangle.setLeftX(x);
    }
    
    public Vector2D getLeft()
    {
        return rectangle.getLeft();
    }
    
    public float getLeftX()
    {
        return rectangle.getLeft().x;
    }
    
    public float getLeftY()
    {
        return rectangle.getLeft().y;
    }
    
    public void setPos(float x, float y)
    {
        setX(x);
        setY(y);
    }
    
    public void setPos(Vector2D p)
    {
        rectangle.setPos(p);
    }
    
    public void setWidth(float width)
    {
        rectangle.setWidth(width);
    }
    
    public float getWidth()
    {
        return rectangle.getWidth();
    }
    
    public void setHeight(int height)
    {
        rectangle.setHeight(height);
    }
    
    public float getHeight()
    {
        return rectangle.getHeight();
    }

    public void setActive(boolean isActive) { this.isActive = isActive; }

    public boolean getActive() { return this.isActive; }
}
