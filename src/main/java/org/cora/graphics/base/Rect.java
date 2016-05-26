package org.cora.graphics.base;

public class Rect implements Cloneable
{
    public int x;
    public int y;
    public int w;
    public int h;
    
    public Rect()
    {
	this(0, 0, 0, 0);
    }
    
    public Rect(int x, int y, int width, int height)
    {
	set(x, y, width, height);
    }
 
    public void set(int x, int y, int width, int height)
    {
	this.x = x;
	this.y = y;
	this.w = width;
	this.h = height;
    }
    
    public void set(int x, int y)
    {
	this.x = x;
	this.y = y;
    }
    
    public Object clone()
    {
	return new Rect(x, y, w, h);
    }
}
