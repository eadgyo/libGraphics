package org.cora.graphics.manager;


import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.Surface;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Handle textures
 */
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

    /**
     * Set default graphics renderer
     *
     * @param g tool renderer
     */
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

    /**
     * Load texture from default folder
     *
     * @param file file relative location
     *
     * @return generated surface
     */
    public Surface loadTextureFromDef(String file)
    {
        return loadTexture(ConstantManager.textureFolder + "/" + file);
    }


    /**
     * Load texture from file
     *
     * @param file image location
     * @param isInternal in jar
     * @return generated surface
     */
    public Surface loadTexture(String file, boolean isInternal)
    {
        Surface surface = createTexture(file);

        if (g != null)
        {
            loadTextureGL(surface);
        }

        if (surface != null)
        {
            addTexture(surface);
        }
        return surface;
    }

    /**
     * Load texture from internal file
     *
     * @param file image location
     * @return generated surface
     */
    public Surface loadTexture(String file)
    {
        return loadTexture(file, true);
    }

    /**
     * Add texture to TextureManager's map
     *
     * @param surface texture
     */
    public void addTexture(Surface surface)
    {
        surface.textureName = createName(surface.textureName);
        textures.put(surface.textureName, surface);
    }

    /**
     * Generate available name for texture
     *
     * @param name textureName
     *
     * @return generated name
     */
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

    /**
     * Load and create texture from file
     *
     * @param file location image
     *
     * @return texture
     */
    private static Surface createTexture(String file, boolean isInternal)
    {
        if (isInternal)
        {
            URL url = FileManager.getInternalURL(file);
            BufferedImage image = FileManager.loadBufferedImage(file, true);

            if (image == null)
                return null;

            Surface surface = transformToImage(image);
            surface.textureName = url.getFile();
            return surface;
        }
        else
        {
            File f = new File(file);
            BufferedImage image = FileManager.loadBufferedImage(f);

            if (image == null)
                return null;

            Surface surface = transformToImage(image);
            surface.textureName = f.getName();
            return surface;
        }
    }

    private static Surface createTexture(String file)
    {
        return createTexture(file, true);
    }
    

    public static Surface transformToImage(BufferedImage image)
    {
        Surface surface = new Surface();
        surface.w = image.getWidth();
        surface.h = image.getHeight();
        surface.BytesPerPixel = 4;// image.getType();


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

    /**
     * Free texture from textureManager and video memory
     *
     * @param name texture name
     */
    public void freeTexture(String name)
    {
        Surface surface = removeFromMap(name);
        if (surface != null)
        {
            freeTextureGL(surface);
        }
    }

    /**
     * Remove texture from map of textureManager withtout video memory release
     *
     * @param name texture name
     *
     * @return removed texture
     */
    public Surface removeFromMap(String name)
    {
        return textures.remove(name);
    }


    /**
     * Load texture in video memory
     *
     * @param surface texture
     */
    public void loadTextureGL(Surface surface)
    {
        g.loadTextureGL(surface);
    }

    /**
     * Free texture from video memory
     *
     * @param surface texture
     */
    public void freeTextureGL(Surface surface)
    {
        g.freeTexture(surface.texture);
        surface.texture = -1;
    }

    /**
     * Free all texture from TextureManager and video memory
     */
    public void freeAllTextures()
    {
        for (Entry<String, Surface> texture : textures.entrySet())
        {
            freeTextureGL(texture.getValue());
        }
        textures.clear();
    }
}
