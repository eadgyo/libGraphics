package org.polytech.polybigbalance.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.BufferUtils;
import org.polytech.polybigbalance.graphics.Surface;

public class FileManager
{
    private DocumentBuilderFactory factory;

    // Parseur
    public final String binFolder = "res";
    public final String objectsFolder = binFolder + "/" + "Objects";
    public final String textureFolder = binFolder + "/" + "pic";

    private static FileManager INSTANCE = new FileManager();

    private FileManager()
    {
	createFolder(binFolder);
	createFolder(objectsFolder);
	createFolder(textureFolder);
    }

    // Load
    public Surface loadSurfaceFromDef(String file)
    {
	return loadSurface(textureFolder + "/" + file);
    }
    
    public Surface loadSurface(String file)
    {
	BufferedImage image = loadTexture(file);
	
	if (image == null)
	    return null;
	
	Surface surface = new Surface();
	surface.w = image.getWidth();
	surface.h = image.getHeight();
	surface.BytesPerPixel = 4;// image.getType();
	
	int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        surface.pixels = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * surface.BytesPerPixel);

        for(int y = 0 ; y < image.getHeight() ; y++)
        {
            for(int x = 0 ; x < image.getWidth() ; x++)
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
    
    public BufferedImage loadTextureFromDef(String file)
    {
	return loadTexture(textureFolder + "/" + file);
    }
    
    public BufferedImage loadTexture(String file)
    {
	BufferedImage texture;
	try
	{
	    texture = ImageIO.read(new File(file));
	} 
	catch (IOException e)
	{
	    texture = null;
	}
	return texture;
    }
    
    public BufferedImage loadTexture(URL url)
    {
	BufferedImage texture;
	try
	{
	    texture = ImageIO.read(url);
	} 
	catch (IOException e)
	{
	    texture = null;
	}
	return texture;
    }

    public ArrayList<String> getAllFilesName(String Directory, boolean canCreate)
    {
	ArrayList<String> files = new ArrayList<String>();
	File folder = new File(Directory);
	if (folder.exists())
	{
	    File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++)
	    {
		if (listOfFiles[i].isFile())
		{
		    files.add(listOfFiles[i].getName());
		}
	    }
	} 
	else if (canCreate)
	{
	    try
	    {
		folder.mkdir();
	    } 
	    catch (SecurityException se)
	    {
		
	    }
	}
	return files;
    }

    public boolean isFolderExisting(String Directory)
    {
	File folder = new File(Directory);
	return folder.exists();
    }

    public boolean createFolder(String directory)
    {
	File folder = new File(directory);
	if (!folder.exists())
	{
	    try
	    {
		folder.mkdir();
		return true;
	    } catch (SecurityException se)
	    {

	    }
	}
	return true;
    }

    public boolean isFileExisting(String Directory)
    {
	File file = new File(Directory);
	return file.exists() && file.isFile();
    }

    public static FileManager getInstance()
    {
	return INSTANCE;
    }
}
