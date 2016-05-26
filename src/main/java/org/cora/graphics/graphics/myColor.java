package org.cora.graphics.graphics;

public class myColor implements Cloneable
{
    public float r;
    public float g;
    public float b;
    public float a;

    public myColor()
    {
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public myColor(float r, float g, float b, float a)
    {
        set(r, g, b, a);
    }

    public myColor(float r, float g, float b)
    {
        set(r, g, b, 1.0f);
    }
    
    @Override
    public Object clone()
    {
        Object o = null;
        
        try
        {
            o = super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        
        return o;
    }

    public void set(float r, float g, float b, float a)
    {
        set(r, g, b);
        this.a = a;
    }

    public void set(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public myColor add(myColor color)
    {
        return new myColor(color.r + r, color.g + g, color.b + b, color.a + a);
    }

    public myColor add(float color)
    {
        return new myColor(color + r, color + g, color + b, color + a);
    }

    public void selfAdd(myColor color)
    {
        r += color.r;
        a += color.a;
        g += color.g;
        b += color.b;
    }

    public void selfAdd(float color)
    {
        r += color;
        a += color;
        g += color;
        b += color;
    }

    public myColor multiply(float factor)
    {
        return new myColor(factor * r, factor * g, factor * b, factor * a);
    }

    public void selfMultiply(float factor)
    {
        r *= factor;
        a *= factor;
        g *= factor;
        b *= factor;
    }

    public void set(myColor color)
    {
        r = color.r;
        a = color.a;
        g = color.g;
        b = color.b;
    }

    public void set(float color)
    {
        r = color;
        a = color;
        g = color;
        b = color;
    }

    public int getInt(int t)
    {
        return (int) (get(t) * 255);
    }
    
    public byte getByte(int t)
    {
        return (byte) (get(t) * 255);
    }

    public float get(int t)
    {
        switch (t)
        {
            case 0:
                return r;
            case 1:
                return g;
            case 2:
                return b;
            case 3:
                return a;
            default:
                return 1.0f;
        }
    }
    
    public boolean isVisible()
    {
        return a != 0;
    }

    public static myColor RED(float a)
    {
        return new myColor(1.0f, 0.0f, 0.0f, a);
    }

    public static myColor RED()
    {
        return RED(1.0f);
    }

    public static myColor WHITE(float a)
    {
        return new myColor(1.0f, 1.0f, 1.0f, a);
    }

    public static myColor WHITE()
    {
        return WHITE(1.0f);
    }
    
    public static myColor GREY(float a)
    {
        return new myColor(0.5f, 0.5f, 0.5f, a);
    }

    public static myColor GREY()
    {
        return WHITE(1.0f);
    }


    public static myColor BLUE(float a)
    {
        return new myColor(0.0f, 0.0f, 1.0f, a);
    }

    public static myColor BLUE()
    {
        return BLUE(1.0f);
    }

    public static myColor GREEN(float a)
    {
        return new myColor(0.0f, 1.0f, 0.0f, a);
    }

    public static myColor GREEN()
    {
        return GREEN(1.0f);
    }

    public static myColor BLACK(float a)
    {
        return new myColor(0.0f, 0.0f, 0.0f, a);
    }

    public static myColor BLACK()
    {
        return BLACK(1.0f);
    }
}
