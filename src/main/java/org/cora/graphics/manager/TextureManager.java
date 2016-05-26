package org.cora.graphics.manager;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.Surface;
import org.lwjgl.BufferUtils;

public class TextureManager
{
    private static TextureManager INSTANCE = new TextureManager();
    private Graphics g;
    private Map<String, Surface> textures;
    
    private TextureManager()
    {
        g = null;
        textures = new HashMap<String, Surface>();
    }
    
    public static TextureManager getInstance()
    {
        return INSTANCE;
    }
    
    public void init(Graphics g)
    {
        this.g = g;
    }
    
    public Graphics getDefaultGraphics()
    {
        return g;
    }
    
    public void setDefaultGraphics(Graphics g)
    {
        this.g = g;
    }
    
    public Surface loadTextureFromDef(String file)
    {
        return loadTexture(ConstantManager.textureFolder + "/" + file);
    }
    
    public Surface loadTexture(String file)
    {
        Surface surface = createTexture(file);
        if (surface != null)
        {
            addTexture(surface);
        }
        return surface;
    }
    
    public void addTexture(Surface surface)
    {
        surface.textureName = createName(surface.textureName);
        textures.put(surface.textureName, surface);
    }

    public String createName(String name)
    {
        int i = 0;
        String tmpName = FileManager.removeExtension(name);
        while (isPresent(tmpName))
        {
            tmpName = name + "-" + i;
        }
        return tmpName;
    }
    
    public boolean isPresent(String name)
    {
        return textures.containsKey(name);
    }
    
    public Surface getTexture(String name)
    {
        return textures.get(name);
    }
    
    public static Surface createTextureFromDef(String file)
    {
        return createTexture(ConstantManager.textureFolder + "/" + file);
    }
    
    public static Surface createTexture(String file)
    {
        File f = new File(file);            
        BufferedImage image = FileManager.loadBufferedImage(f);

        if (image == null)
            return null;

        Surface surface = new Surface();
        surface.w = image.getWidth();
        surface.h = image.getHeight();
        surface.BytesPerPixel = 4;// image.getType();
        surface.textureName = f.getName();

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0,
                image.getWidth());

        surface.pixels = BufferUtils.createByteBuffer(image.getWidth()
                * image.getHeight() * surface.BytesPerPixel);

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                surface.pixels.put((byte) ((pixel >> 16) & 0xFF)); // red
                surface.pixels.put((byte) ((pixel >> 8) & 0xFF)); // green
                surface.pixels.put((byte) (pixel & 0xFF)); // blue
                surface.pixels.put((byte) ((pixel >> 24) & 0xFF)); // alpha
            }
        }

        surface.pixels.flip();
        return surface;
    }
    
    public void loadAllTexturesFromDef()
    {
        loadAllTextures(ConstantManager.textureFolder);
    }
    
    public void loadAllTextures(String folder)
    {
        ArrayList<String> files = FileManager.getAllFilesPath(folder, false, true);
        for (int i = 0; i < files.size(); i++)
        {
            loadTexture(files.get(i));
        }
    }
    
    public void freeTexture(String name)
    {
        Surface surface = removeFromMap(name);
        if (surface != null)
        {
            freeTextureGL(surface);
        }
    }
    
    public Surface removeFromMap(String name)
    {
        return textures.remove(name);
    }
    
    public void freeTextureGL(Surface surface)
    {
        g.freeTexture(surface.texture);
        surface.texture = -1;
    }
    
    public void freeAllTextures()
    {
        for (Entry<String, Surface> texture : textures.entrySet())
        {
            freeTextureGL(texture.getValue());
        }
        textures.clear();
    }
}
