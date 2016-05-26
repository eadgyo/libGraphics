package org.polytech.polybigbalance.graphics.base;

import org.polytech.polybigbalance.graphics.Surface;

public class SpriteData
{
    public Surface surface;
    public Rect    rect;
    public boolean flipH;
    public boolean flipV;

    public SpriteData()
    {
        this(null, new Rect());
    }

    public SpriteData(Rect rect)
    {
        this(null, rect);
    }

    public SpriteData(Surface surface)
    {
        this(surface, new Rect());
    }

    public SpriteData(Surface surface, Rect rect)
    {
        flipH = false;
        flipV = false;
        this.rect = rect;
        this.surface = surface;
    }
}
