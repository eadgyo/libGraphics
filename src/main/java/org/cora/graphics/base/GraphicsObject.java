package org.cora.graphics.base;

import org.cora.graphics.graphics.Graphics;

public abstract class GraphicsObject
{
    public GraphicsObject(int width, int height) {}
    public abstract void render(Graphics g);
}
