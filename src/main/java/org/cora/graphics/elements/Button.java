package org.cora.graphics.elements;

import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.myColor;
import org.cora.maths.Vector2D;
import org.cora.maths.sRectangle;

public class Button
{
    protected myColor recColor;
    protected myColor backColor;
    protected myColor addColor;
    protected boolean isActive;
    
    sRectangle rectangle;
    
    public Button(int x, int y, int width, int height)
    {
        recColor = new myColor();
        addColor = myColor.WHITE(0.2f);
        backColor = myColor.GREY();
        isActive = false;
        rectangle = new sRectangle(x, y, width, height);
    }

    public myColor getRecColor()
    {
        return recColor;
    }

    public void setRecColor(myColor recColor)
    {
        this.recColor.set(recColor);
    }
    
    public void setRecColor(float r, float g, float b, float a)
    {
        this.recColor.set(r, g, b, a);
    }

    public myColor getBackColor()
    {
        return backColor;
    }

    public void setBackColor(myColor backColor)
    {
        this.backColor.set(backColor);
    }
    
    public void setBackColor(float r, float g, float b, float a)
    {
        this.backColor.set(r, g, b, a);
    }

    public sRectangle getRectangle()
    {
        return rectangle;
    }

    public void setRectangle(sRectangle rectangle)
    {
        this.rectangle.set(rectangle);
    }
    
    public void update(float dt)
    {}
    
    public void render(Graphics g)
    {
        if (backColor.isVisible() || isActive)
        {
            if (isActive)
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
    
    public boolean isColliding(Vector2D pos)
    {
        return rectangle.isInsideBorder(pos);
    }
    
    public myColor getAddColor()
    {
        return addColor;
    }

    public void setAddColor(myColor addColor)
    {
        this.addColor.set(addColor);
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
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
}
