package org.polytech.polybigbalance.graphics.base;

import org.polytech.polybigbalance.graphics.Graphics;
import org.polytech.polybigbalance.graphics.Surface;
import org.polytech.polybigbalance.graphics.myColor;

import cora.maths.Rectangle;
import cora.maths.Vector2D;

public class Image
{
    protected SpriteData spriteData;
    protected myColor colorFilter;
    protected myColor color;
    protected int cols;
    protected int currentFrame;
    protected Rectangle rec;
    protected int startFrame;
    protected int endFrame;
    protected float frameDuration;
    protected float time;
    
    public Image()
    {
	cols = 0;
	currentFrame = 0;
	startFrame = 0;
	endFrame = 0;
	spriteData = new SpriteData();
	color = new myColor(1.0f, 1.0f, 1.0f, 1.0f);
	colorFilter = new myColor();
	frameDuration = 0;
	time = 0;
	
	rec = new Rectangle();
    }
    
    public Image(Image image)
    {
	rec = new Rectangle();
	
	set(image);
    }
    
    public Image(Surface surface)
    {
	this(surface, surface.w, surface.h, 0);
    }
    
    public Image(Surface surface, int x, int y, int width, int height)
    {
	spriteData = new SpriteData();
	color = new myColor(1.0f, 1.0f, 1.0f, 1.0f);
	colorFilter = new myColor();
	rec = new Rectangle();
	
	initialize(surface, x, y, width, height);
    }
    
    public Image(Surface surface, int width, int height, int currentFrame)
    {
	spriteData = new SpriteData();
	color = new myColor(1.0f, 1.0f, 1.0f, 1.0f);
	colorFilter = new myColor();
	
	initialize(surface, width, height, currentFrame);
    }
    
    public void reset()
    {
	setFlipV(false);
	setFlipH(false);
	color.set(1.0f, 1.0f, 1.0f, 1.0f);
	rec.set(new Vector2D(), new Vector2D(spriteData.rect.w, spriteData.rect.h), 0); //RECTANGLE_CREATION
    }
    
///////////////////
//	Get      //
///////////////////
    
    public Rectangle getRectangle()
    {
	return rec;
    }
    
    public int getCols()
    {
	return cols;
    }
    
    public float getVisible()
    {
	return color.a;
    }
    
    public float getWidth()
    {
	return rec.getWidth();
    }
    
    public float getHeight()
    {
	return rec.getHeight();
    }
    
    public Vector2D getPos()
    {
	return rec.getCenter();
    }
    
    public float getX()
    {
	return rec.getCenterX();
    }
    
    public float getY()
    {
	return rec.getCenterY();
    }
    
    public Vector2D getLeftPos()
    {
	return rec.getLeft();
    }
    
    public float getRadians()
    {
	return rec.getAngle();
    }
    
    float getRadians(Vector2D vec)
    {
    	return rec.getAngle(vec);
    }
    
    public float getDegrees()
    {
	return (float) ((rec.getAngle()*180)/Math.PI);
    }
    
    public float getDegress(Vector2D vec)
    {
	return (float) ((rec.getAngle(vec)*180)/Math.PI);
    }
    
    public int getCurrentFrame()
    {
	return currentFrame;
    }
    
    public Rect getSpriteDataRect()
    {
	return spriteData.rect;
    }
    
    public myColor getColorFilter()
    {
	return colorFilter;
    }
    
    public myColor getColor()
    {
	return color;
    }
    
    public boolean getFlipV()
    {
	return spriteData.flipV;
    }
    
    public boolean getFlipH()
    {
	return spriteData.flipH;
    }
    
    public float getScale()
    {
	return rec.getScale();
    }
    
    public float getRecWidth()
    {
	return rec.getWidth();
    }
    
    public float getRecHeight()
    {
	return rec.getHeight();
    }

    public SpriteData getSpriteData()
    {
	return spriteData;
    }

    public float getTime()
    {
	return time;
    }
    
    public float getFrameDuration()
    {
	return frameDuration;
    }
    
    public int getStartFrame()
    {
	return startFrame;
    }
    
    public int getEndFrame()
    {
	return endFrame;
    }
    
///////////////////
//	Set      //
///////////////////
    
    public void set(Image image)
    {
	SpriteData l_sd = image.getSpriteData();
	initialize(l_sd.surface, l_sd.rect.w, l_sd.rect.h,
		image.getCurrentFrame());
        rec.set(image.getRectangle());
        setColorFilter(image.getColorFilter());
        setColor(image.getColor());
        setFrames(image.getStartFrame(), image.getEndFrame());
        setTime(image.getTime());
    }
    
    public void setFrameDuration(float dt)
    {
	frameDuration = dt;
    }
    
    public void setTime(float time)
    {
	this.time = time;
    }
    
    public void setFrames(int start, int end)
    {
	startFrame = start;
	endFrame = end;
    }
    
    public void setX(float x)
    {
	translateX(x-getX());
    }
    
    public void setY(float y)
    {
	translateY(y-getY());
    }
    
    public void setPos(Vector2D center)
    {
	Vector2D translateV = new Vector2D(getPos(), center);
	translate(translateV);
    }
    
    public void setLeftPos(Vector2D left)
    {
	Vector2D translateV = new Vector2D(getLeftPos(), left);
	translate(translateV);
    }
    
    public void setDegrees(float degrees)
    {
    	setRadians((float) ((degrees*Math.PI)/180));
    }
    
    public void setDegrees(float degrees, Vector2D vec)
    {
    	setRadians((float) ((degrees*Math.PI)/180), vec);
    }
    
    public void setRadians(float radians)
    {
	float angle = radians - rec.getAngle();
    	rotateRadians(angle);
    }
    
    public void setRadians(float radians, Vector2D vec)
    {
    	float angle = radians - rec.getAngle(vec);
    	rotateRadians(angle, getPos());
    }
    
    public void setScale(float scale, Vector2D center)
    {
	float factor = scale*(1.0f/getScale());
	this.scale(factor, getPos());
    }
    
    public void setFlipH(boolean b, Vector2D center)
    {
	if(spriteData.flipH != b)
	{
	    flipH(center);
	}
    }

    public void setFlipV(boolean b, Vector2D center)
    {
	if(spriteData.flipV != b)
	{
	    flipV(center);
	}
    }
    
    public void setFlipH(boolean flipHorizontal)
    {
	if(spriteData.flipH != flipHorizontal)
	{
	    flipH();
	}
    }
    
    public void setFlipV(boolean flipVertical)
    {
	if(spriteData.flipV != flipVertical)
	{
	    flipV();
	}
    }
    
    public void setColorFilter(myColor colorFilter)
    {
	this.colorFilter.set(colorFilter);
    }
    
    public void setColor(myColor color)
    {
	this.color.set(color);
    }
    
    public void setAlpha(float a)
    {
	color.a = a;
    }
    
    public void setScale(float scale)
    {
	float factor = scale*(1.0f/getScale());
	scale(factor);
    }
    
    public void setSizeW(int sizeW)
    {
	setScale(sizeW/spriteData.rect.w);
    }
    
    public void setSizeH(int sizeH)
    {
	setScale(sizeH/spriteData.rect.h);
    }
    
    public void setSpriteData(SpriteData sd)
    {
	spriteData.flipH = sd.flipH;
	spriteData.flipV = sd.flipV;
	spriteData.surface = sd.surface;
	
	setSpriteDataRect(sd.rect);
    }
    
    public void setSpriteDataRect(Rect rect)
    {
	spriteData.rect.w = rect.w;
	spriteData.rect.h = rect.h;
	spriteData.rect.x = rect.x;
	spriteData.rect.y = rect.y;
    }
    
    public void setRec(Rectangle rec)
    {
	this.rec.set(rec);
    }
    
    public void setCurrentFrame(int current)
    {
	if (current > 0)
	{
	    currentFrame = current;
	    setRect();
	}
    }
    
    public void clearSurface()
    {
	spriteData.surface = null;
    }
    
    public void nextFrame()
    {
	currentFrame = ((currentFrame + 1) > endFrame)? startFrame + (currentFrame + 1)%endFrame : currentFrame + 1;
	setRect();
    }
    
    public void update(float dt)
    {
	time += dt;
	while (time > frameDuration)
	{
	    time -= frameDuration;
	    currentFrame++;
	}
	currentFrame = ((currentFrame) > endFrame)? startFrame + (currentFrame)%endFrame : currentFrame;
	setRect();
    }
    
    public void setRect()
    {
	spriteData.rect.set((currentFrame % cols)*spriteData.rect.w,
		(currentFrame / cols)*spriteData.rect.h);
    }
    
    public void setRect(int width, int height)
    {
	spriteData.rect.set((currentFrame % cols)*spriteData.rect.w,
		(currentFrame / cols)*spriteData.rect.h,
		width,
		height);
    }
    
    public void setRect(int x, int y, int width, int height)
    {
	spriteData.rect.set(x,
		y,
		width,
		height);
    }
    
    public void initialize(Surface surface)
    {
	initialize(surface, surface.w, surface.h, 0);
    }
    
    public void initialize(Surface surface, int width, int height, int currentFrame)
    {
	this.cols = surface.w / width;
	this.currentFrame = currentFrame;
	this.spriteData.surface = surface;
	this.rec.set(new Vector2D(width*0.5f, height*0.5f), new Vector2D(width, height), 0);

	setRect(width, height);
	
	startFrame = 0;
	endFrame = cols * surface.h / height;
    }
    
    public void initialize(Surface surface, int x, int y, int width, int height)
    {
	this.cols = surface.w / width;
	this.currentFrame = (x/width) + (y/height)*(cols);
	this.spriteData.surface = surface;
	this.rec.set(new Vector2D(width*0.5f, height*0.5f), new Vector2D(width, height), 0);
	setRect(x, y, width, height);
	
	startFrame = 0;
	endFrame = cols * surface.h / height;
    }
    
    public void draw(Graphics g)
    {
	if (spriteData.surface == null || color.a < 0.01f)
	    return;
	
	g.render(this);
    }
    
    ///////////////////////////////
    //Transformations      	//
    ///////////////////////////////
    public void translate(Vector2D vec)
    {
	rec.translate(vec);
    }
    
    public void translateX(float vecX)
    {
	rec.translateX(vecX);
    }
    
    public void translateY(float vecY)
    {
	rec.translateY(vecY);
    }
    
    public void flipH(Vector2D center)
    {
	rec.flipH(center);
	spriteData.flipH = !spriteData.flipH; 
    }
    
    public void flipV(Vector2D center)
    {
	rec.flipV(center);
	spriteData.flipV = !spriteData.flipV;
    }
    
    public void flipH()
    {
	rec.flipH();
	spriteData.flipH = !spriteData.flipH; 
    }
    
    public void flipV()
    {
	rec.flipV();
	spriteData.flipV = !spriteData.flipV;
    }
    
    public void scale(float factor, Vector2D center)
    {
	if (factor > 0)
	    rec.scale(factor, center);
    }
    
    public void scale(float factor)
    {
	rec.scale(factor);
    }
    
    public void rotateRadians(float radians, Vector2D center)
    {
	rec.rotateRadians(radians, center);
    }
    
    public void rotateRadians(float radians)
    {
	rec.rotateRadians(radians);
    }
    
    public void visible(float f)
    {
	color.a *= f;
    }
}
