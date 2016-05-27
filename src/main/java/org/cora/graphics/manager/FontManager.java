package org.cora.graphics.manager;

import java.util.HashMap;
import java.util.Map;

import org.cora.graphics.font.Font;
import org.cora.graphics.graphics.Surface;

/**
 * Holds fonts
 */
public class FontManager
{
    private static FontManager INSTANCE = new FontManager();
    private Map<String, Font> fonts;
    
    private FontManager()
    {
        fonts = new HashMap<String, Font>();
    }
    
    public static FontManager getInstance()
    {
        return INSTANCE;
    }

    /**
     * Create font from surface
     * @param surface font texture
     * @param width element length
     * @param height element length
     * @return created Font
     */
    public Font loadFont(Surface surface, int width, int height)
    {
        Font font = new Font(surface, width, height);
        addFont(font);
        return font;
    }

    /**
     * Create font from surface
     * @param surface font texture
     * @param width element length
     * @return created Font
     */
    public Font loadFont(Surface surface, int width)
    {
        return loadFont(surface, width, width);
    }

    public Font loadFont(String file, int width)
    {
        return loadFont(file, width, width);
    }

    /**
     * Create font from file location
     * @param file font surface name
     * @param width element length
     * @param height element length
     * @return created Font
     */
    public Font loadFont(String file, int width, int height)
    {
        Font font = createFont(file, width, height);
        if (font != null)
        {
            addFont(font);
        }
        return font;
    }

    /**
     * Add font in FontManager's map
     * @param font created font
     */
    public void addFont(Font font)
    {
        font.setName(createName(font.getName()));
        fonts.put(font.getName(), font);
    }

    /**
     * Generate font avalaible name
     * @param name font name
     * @return generated font name
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
        return fonts.containsKey(name);
    }

    public Font createFont(String file, int width)
    {
        return createFont(file, width, width);
    }
    
    public Font createFont(String file, int width, int height)
    {
        Surface surface = TextureManager.getInstance().loadTexture(file);
        
        if (surface == null)
            return null;
        
        Font font = new Font(surface, width, height);
        return font;
    }

    /**
     * Remove font but not texture
     * @param name font name
     * @return removed font
     */
    public Font removeFont(String name)
    {
        return fonts.remove(name);
    }

    /**
     * Remove all fonts but not textures
     */
    public void removeAllFonts()
    {
        fonts.clear();
    }
}
