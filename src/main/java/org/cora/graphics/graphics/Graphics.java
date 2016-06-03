package org.cora.graphics.graphics;

import org.cora.graphics.base.Image;
import org.cora.graphics.base.Rect;
import org.cora.graphics.base.SpriteData;
import org.cora.graphics.manager.FileManager;
import org.cora.graphics.manager.TextureManager;
import org.cora.maths.Circle;
import org.cora.maths.Form;
import org.cora.maths.Rectangle;
import org.cora.maths.Vector2D;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Graphics
{
    private long              screen;
    int width;
    int height;

    public Graphics()
    {
        screen = NULL;
    }

    /**
     * Create Tool renderer
     * @param windowName name of the created window
     * @param width width of the window
     * @param height height of the window
     * @param initGL initGL 2D
     * @param initManager init others
     */
    public Graphics(String windowName, int width, int height, boolean initGL, boolean initManager, Class main)
    {
        init(windowName, width, height);

        if (initGL)
            initGL();

        if (initManager)
        {
            TextureManager.getInstance().init(this);
            FileManager.init(main);
        }
    }

    public long getScreen()
    {
        return screen;
    }

    /**
     *
     * @return state of window
     */
    public boolean isTerminated()
    {
        return (glfwWindowShouldClose(screen) == GL_TRUE);
    }

    /**
     *
     * @return state of window
     */
    public boolean isNotTerminated()
    {
        return (glfwWindowShouldClose(screen) == GL_FALSE);
    }

    /**
     * Close the window
     */
    public void terminate()
    {
        glfwSetWindowShouldClose(screen, GL_TRUE);
    }

    /**
     *
     * @param windowName window name
     * @param width width of window
     * @param height height of window
     */
    public void init(String windowName, int width, int height)
    {
        this.width = width;
        this.height = height;

        glfwSetErrorCallback(GLFWErrorCallback
                .createPrint(System.err));

        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();

        screen = glfwCreateWindow(width, height, windowName, NULL, NULL);

        if (screen == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        
        glfwSetKeyCallback(screen, new GLFWKeyCallback()
        {
            @Override
            public void invoke(long window, int key, int scancode, int action,
                    int mods)
            {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will
                // detect this
                // in our
                // rendering
                // loop
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(screen,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(screen);
        glfwSwapInterval(1);
        glfwShowWindow(screen);

        GL.createCapabilities();
    }

    /**
     * Init GL 2D features
     */
    public void initGL()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set the cleared screen colour
        // to black
        glViewport(0, 0, width, height); // This sets up the viewport so that
        // the coordinates (0, 0) are at the
        // top left of the window

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -10, 10);

        // Back to the modelview so we can draw stuff
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the screen
        // and depth buffer
        glEnable(GL_TEXTURE_2D);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Clear screen
     */
    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Swap screen buffer
     */
    public void swapGL()
    {
        glfwSwapBuffers(screen);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Render image on screen
     * @param image rendered image
     */
    public void render(Image image)
    {
        glPushMatrix();

        Rectangle rec = image.getRectangle();
        SpriteData sd = image.getSpriteData();

        glTranslatef(rec.getCenterX() + ((int) rec.getWidth())
                * ((sd.flipH) ? 1 : 0),
                rec.getCenterY() + ((int) rec.getHeight())
                        * ((sd.flipV) ? 1 : 0), 0.0f);

        glScalef(rec.getWidth() / sd.rect.w * ((sd.flipH) ? -1.0f : 1.0f),
                rec.getHeight() / sd.rect.h * ((sd.flipV) ? -1.0f : 1.0f), 0.0f);

        if (Math.abs(rec.getAngle()) > 0.001f
                && Math.abs(rec.getAngle() - Math.PI * 2) > 0.001f)
            glRotatef((float) ((rec.getAngle() * 180) / Math.PI), 0, 0, 1.0f);

        glTranslatef(-sd.rect.x - sd.rect.w
                * (0.5f - ((sd.flipH) ? 1.0f : 0.0f)), -sd.rect.y - sd.rect.h
                * (0.5f - ((sd.flipV) ? 1.0f : 0.0f)), 0.0f);

        setColor(image.getColor());

        render(sd.surface, sd.rect);

        glPopMatrix();
    }

    public void setOutpout(int x, int y, int width, int height)
    {
        glViewport(x, y, width, height);
    }

    /**
     * Reset output to window output
     */
    public void resetOuput()
    {
        glViewport(0, 0, width, height);
    }


    /**
     * Render part of texture to screen
     * @param surface texture
     * @param rec selection part
     */
    public void render(Surface surface, Rect rec)
    {
        render(surface, rec.x, rec.y, rec.w, rec.h);
    }

    /**
     * Render part of texture to screen
     * @param surface texture
     * @param x coordinate selection part
     * @param y coordinate selection part
     * @param width length selection part
     * @param height length selection part
     */
    public void render(Surface surface, int x, int y, int width, int height)
    {
        glBindTexture(GL_TEXTURE_2D, surface.texture);

        // Render texture quad
        float x1 = ((float) x) / surface.w;
        float x2 = ((float) x + width) / surface.w;
        float y1 = ((float) y) / surface.h;
        float y2 = ((float) y + height) / surface.h;

        glBegin(GL_QUADS);
        glTexCoord2f(x1, y1);
        glVertex2f((float) x, (float) y); // Bottom left
        glTexCoord2f(x2, y1);
        glVertex2f((float) (x + width), (float) y); // Bottom right
        glTexCoord2f(x2, y2);
        glVertex2f((float) (x + width), (float) (y + height)); // Top
        // right
        glTexCoord2f(x1, y2);
        glVertex2f((float) x, (float) (y + height)); // Top left
        glEnd();

        // On deselectionne la Texture
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    /**
     * Load texture in video memory
     * @param surface texture
     */
    public void loadTextureGL(Surface surface)
    {
        int texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0); // Always
        // set the base and max mipmap levels of a texture.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);

        // Map the surface to the texture in video memory
        switch (surface.BytesPerPixel)
        {
            case 4:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, surface.w, surface.h,
                        0, GL_RGBA, GL_UNSIGNED_BYTE, surface.pixels); // GL_PNG
                break;
            case 3:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, surface.w, surface.h, 0,
                        GL_RGB, GL_UNSIGNED_BYTE, surface.pixels); // GL_BITMAP
                break;
            case 1:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, surface.w,
                        surface.h, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE,
                        surface.pixels); // GL_BITMAP
                break;
            default:
                System.out.println("not supported bytes per pixel "
                        + surface.BytesPerPixel);
                break;
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        surface.texture = texture;
    }

    /**
     * Free texture from video memory
     * @param texture textureId
     */
    public void freeTexture(int texture)
    {
        glDeleteTextures(texture);
    }

    /**
     * Free texture from video memory
     * @param surface textureId
     */
    public void freeTexture(Surface surface) { freeTexture(surface.texture); };

    // Drawing forms
    public void fillRec(int x, int y, int w, int h)
    {
        glBegin(GL_POLYGON);
        glVertex2f(x    , y     );
        glVertex2f(x + w, y     );
        glVertex2f(x + w, y + h );
        glVertex2f(x    , y + h );
        glEnd();
    }

    public void drawRec(float x, float y, float w, float h)
    {
        drawLine(x      , y     , x + w , y     );
        drawLine(x + w  , y     , x + w , y + h );
        drawLine(x + w  , y + h , x     , y + h );
        drawLine(x      , y + h , x     , y     );
    }

    /**
     * Fill circle
     * @param circle source
     * @param n number of side of circle
     */
    public void fillCircle(Circle circle, int n)
    {
        fillCircle(circle.getCenter(), circle.getRadius(), n);
    }

    /**
     * Fill circle
     * @param center circle center
     * @param radius circle radius
     * @param n number of sides of circle
     */
    public void fillCircle(Vector2D center, float radius, int n)
    {
        fillCircle(center.x, center.y, radius, n);
    }

    /**
     * Fill circle
     * @param x0 circle center x
     * @param y0 circle center y
     * @param radius circle radius
     * @param n number of sides of circle
     */
    public void fillCircle(float x0, float y0, float radius, int n)
    {
        if (n < 0)
            return;

        glBegin(GL_POLYGON);

        float radiusPart = (float) (Math.PI / (n * 2));
        for (int i = 0; i < n; i++)
        {
            float x = (float) Math.cos(radiusPart*i);
            float y = (float) Math.sin(radiusPart*i);
            glVertex2f(x0 + x*radius, y0 + y*radius);
            glVertex2f(x0 + x*radius, y0 - y*radius);
            glVertex2f(x0 - x*radius, y0 - y*radius);
            glVertex2f(x0 - x*radius, y0 + y*radius);
        }
        glEnd();
    }

    /**
     * Draw circle
     * @param circle source
     * @param n number of side of circle
     */
    public void drawCircle(Circle circle, int n)
    {
        drawCircle(circle.getCenter(), circle.getRadius(), n);
    }

    /**
     * Draw circle
     * @param center circle center
     * @param radius circle radius
     * @param n number of sides of circle
     */
    public void drawCircle(Vector2D center, float radius, int n)
    {
        drawCircle(center.x, center.y, radius, n);
    }

    /**
     * Draw circle
     * @param x circle center x
     * @param y circle center y
     * @param radius circle radius
     * @param n number of sides of circle
     */
    public void drawCircle(float x, float y, float radius, int n)
    {
        if (n < 1)
            return;

        float radiusPart = (float) (Math.PI / ((n - 1) * 2));

        float x1 = (float) Math.cos(0);
        float y1 = (float) Math.sin(0);
        float x0;
        float y0;
        for (int i = 1; i < n; i++)
        {
            x0 = x1;
            y0 = y1;
            x1 = (float) Math.cos(radiusPart*i);
            y1 = (float) Math.sin(radiusPart*i);
            drawLine(x + x0*radius, y + y0*radius, x + x1*radius, y + y1*radius);
            drawLine(x + x0*radius, y - y0*radius, x + x1*radius, y - y1*radius);
            drawLine(x - x0*radius, y + y0*radius, x - x1*radius, y + y1*radius);
            drawLine(x - x0*radius, y - y0*radius, x - x1*radius, y - y1*radius);
        }
    }

    public void drawLine(float x0, float y0, float x1, float y1)
    {
        glBegin(GL_LINES);
        glVertex2f(x0, y0);
        glVertex2f(x1, y1);
        glEnd();
    }

    public void drawLine(Vector2D p1, Vector2D p2)
    {
        glBegin(GL_LINES);
        glVertex2f(p1.x, p1.y);
        glVertex2f(p2.x, p2.y);
        glEnd();
    }

    public void fillTriangle(Vector2D p1, Vector2D p2, Vector2D p3)
    {
        glBegin(GL_TRIANGLES);
        glVertex2f(p1.x, p1.y);
        glVertex2f(p2.x, p2.y);
        glVertex2f(p3.x, p3.y);
        glEnd();
    }

    public void fillForm(Form form)
    {
        if (form.size() < 2)
            return;

        glBegin(GL_POLYGON);
        for (int i = 0; i < form.size(); i++)
        {
            Vector2D a = form.get(i);
            glVertex2f(a.x, a.y);
        }
        glEnd();
    }

    public void drawForm(Form form)
    {
        if (form.size() < 2)
            return;

        Vector2D a;
        Vector2D b = form.get(form.size() - 1);

        for (int i = 0, j = form.size() - 1; i < form.size(); j = i, i++)
        {
            a = b;
            b = form.get(i);
            drawLine(a, b);
        }
    }

    public void setColor(float r, float g, float b)
    {
        glColor3f(r, g, b);
    }

    public void setColor(float r, float g, float b, float a)
    {
        glColor4f(r, g, b, a);
    }

    public void setColor(myColor color)
    {
        glColor4f(color.r, color.g, color.b, color.a);
    }

    public void setLineSize(float s)
    {
        glLineWidth(s);
    }

    public void translate(Vector2D vec)
    {
        glTranslatef(vec.x, vec.y, 0);
    }

    public void translate(float x, float y)
    {
        glTranslatef(x, y, 0);
    }

    public void translateX(float x)
    {
        glTranslatef(x, 0, 0);
    }

    public void translateY(float y)
    {
        glTranslatef(0, y, 0);
    }

    public void scale(float factor)
    {
        glScalef(factor, factor, 1);
    }

    public void rotate(float rad)
    {
        glRotatef(rad, 0, 0, 0);
    }

    public void rotate(float rad, float x, float y)
    {
        glRotatef(rad, x, y, 0);
    }

    public void rotate(float rad, Vector2D vec)
    {
        glRotatef(rad, vec.x, vec.y, 0);
    }

    public void pushMatrix()
    {
        glPushMatrix();
    }

    public void popMatrix()
    {
        glPopMatrix();
    }
}
