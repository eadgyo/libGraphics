package org.polytech.polybigbalance.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LUMINANCE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.polytech.polybigbalance.Constants;
import org.polytech.polybigbalance.graphics.base.Image;
import org.polytech.polybigbalance.graphics.base.Rect;
import org.polytech.polybigbalance.graphics.base.SpriteData;

import cora.maths.Form;
import cora.maths.Rectangle;
import cora.maths.Vector2D;

public class Graphics
{
    private long              screen;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;

    public Graphics()
    {
        screen = NULL;
    }

    public long getScreen()
    {
        return screen;
    }

    public void init(String windowName, int width, int height)
    {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback
                .createPrint(System.err));

        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();

        screen = glfwCreateWindow(width, height, windowName, NULL, NULL);

        if (screen == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(screen, keyCallback = new GLFWKeyCallback()
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
                (vidmode.width() - Constants.WINDOW_WIDTH) / 2,
                (vidmode.height() - Constants.WINDOW_HEIGHT) / 2);

        glfwMakeContextCurrent(screen);
        glfwSwapInterval(1);
        glfwShowWindow(screen);

        GL.createCapabilities();
    }

    public void initGL(int width, int height)
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

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void swapGL()
    {
        glfwSwapBuffers(screen);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

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

    public void render(Surface surface, Rect rec)
    {
        render(surface, rec.x, rec.y, rec.w, rec.h);
    }

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

    public void freeTexture(int texture)
    {
        glDeleteTextures(texture);
    }

    // Drawing forms
    public void fillRec(int x, int y, int w, int h)
    {
        glBegin(GL_POLYGON);
        glVertex2f(x, y);
        glVertex2f(x + w, y);
        glVertex2f(x + w, y + h);
        glVertex2f(x, y + h);
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
        if (form.size() == 0)
            return;

        Vector2D a;
        Vector2D b = form.get(0);

        for (int i = 0, j = form.size() - 1; i < form.size(); j = i, i++)
        {
            a = b;
            b = form.get(j);
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
}
