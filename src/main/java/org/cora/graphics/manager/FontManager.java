package org.cora.graphics.manager;

import java.util.HashMap;
import java.util.Map;

import org.cora.graphics.font.Font;
import org.cora.graphics.graphics.Surface;

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
    
    public void loadFont(String file, int width)
    {
        loadFont(file, width, width);
    }
    
    public Font loadFont(String file, int width, int height)
    {
        Font font = createFont(file, width, height);
        if (font != null)
        {
            addFont(font);
        }
        return font;
    }
    
    public void addFont(Font font)
    {
        font.setName(createName(font.getName()));
        fonts.put(font.getName(), font);
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
    
    public Font removeFont(String name)
    {
        return fonts.remove(name);
    }
    
    public void removeAllFonts()
    {
        fonts.clear();
    }
}
