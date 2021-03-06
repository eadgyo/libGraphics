package org.cora.graphics.manager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class FileManager
{
    // Load

    private static Class main = null;

    public static void init(Class mainClass)
    {
        main = mainClass;
    }

    /**
     * Create object, texture and font folder
     */
    public static void createDefaultFolder()
    {
        createFolder(ConstantManager.binFolder);
        createFolder(ConstantManager.objectsFolder);
    }

    /**
     * Create a bufferImage from default location path
     * @param file location path
     * @return created bufferedImage
     */
    public static BufferedImage loadBufferedImageFromDef(String file)
    {
        return loadBufferedImage(ConstantManager.textureFolder + "/" + file);
    }

    /**
     * Create a bufferImage from a file
     * @param file image file
     * @return created bufferedImage
     */
    public static BufferedImage loadBufferedImage(File file)
    {
        BufferedImage texture;
        try
        {
            texture = ImageIO.read(file);
        }
        catch (IOException e)
        {
            texture = null;
        }
        return texture;
    }

    /**
     * Create a bufferImage from a internal path
     * @param file location path
     * @return created bufferedImage
     */
    public static BufferedImage loadBufferedImage(String file)
    {
        return loadBufferedImage(file, true);
    }

    /**
     * Get url from a internal path
     * @param file location path
     * @return created bufferedImage
     */
    public static URL getInternalURL(String file)
    {
        return main.getResource(file);
    }

    /**
     * Create a bufferImage from a location path
     * @param file location path
     * @param isInternal in jar
     * @return created bufferedImage
     */
    public static BufferedImage loadBufferedImage(String file, boolean isInternal)
    {
        BufferedImage texture;
        try
        {
            if(isInternal)
            {
                URL url = main.getResource(file);
                texture = ImageIO.read(url);
            }
            else
                texture = ImageIO.read(new File(file));
        }
        catch (IOException e)
        {
            texture = null;
        }
        return texture;
    }

    /**
     * Create a bufferImage from an url
     * @param url image destination
     * @return created bufferedImage
     */
    public static BufferedImage loadBufferedImage(URL url)
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

    private static void getAllFilesPath(String Directory, ArrayList<String> files)
    {
        File folder = new File(Directory);
        String dir = Directory + "/";
        
        if (folder.isDirectory())
        {
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++)
            {
                if (listOfFiles[i].isFile())
                {
                    files.add(dir + listOfFiles[i].getName());
                }
                else
                {
                    getAllFilesPath(dir + listOfFiles[i].getName(), files);
                }
            }
        }
    }

    /**
     * Get all files destination in one directory
     * @param Directory directory location
     * @param canCreate if the path does not exist, create the directory
     * @param recursive recursive file seeking
     * @return list of relative path of files
     */
    public static ArrayList<String> getAllFilesPath(String Directory, boolean canCreate, boolean recursive)
    {
        ArrayList<String> files = new ArrayList<String>();
        File folder = new File(Directory);
        String dir = Directory + ((Directory.charAt(Directory.length() - 1) == '/') ? "" : "/");
        
        if (folder.exists())
        {
            File[] listOfFiles = folder.listFiles();
            if (recursive)
            {
                for (int i = 0; i < listOfFiles.length; i++)
                {
                    if (listOfFiles[i].isFile())
                    {
                        files.add(dir + listOfFiles[i].getName());
                    }
                    else
                    {
                        getAllFilesPath(dir + listOfFiles[i].getName(), files);
                    }
                }
            }
            else
            {
                for (int i = 0; i < listOfFiles.length; i++)
                {
                    if (listOfFiles[i].isFile())
                    {
                        files.add(dir + listOfFiles[i].getName());
                    }
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

    public static boolean isFolderExisting(String Directory)
    {
        File folder = new File(Directory);
        return folder.exists();
    }

    public static boolean createFolder(String directory)
    {
        File folder = new File(directory);
        if (!folder.exists())
        {
            try
            {
                folder.mkdir();
                return true;
            }
            catch (SecurityException se)
            {

            }
        }
        return true;
    }

    public static boolean isFileExisting(String Directory)
    {
        File file = new File(Directory);
        return file.exists() && file.isFile();
    }

    /**
     * Remove extension like .png from a filename or do nothing if no extension
     * @param name filename with extension
     * @return name without extension
     */
    public static String removeExtension(String name)
    {
        int i = name.length() - 1;
        
        while (i > -1 && name.charAt(i) != '.')
        {
            i--;
        }
        
        if (i != -1)
            return name.substring(0, i - 1);
        
        return name;
    }

    /**
     * Save an object in a binary file
     * @param directory destination folder
     * @param object saved object
     */
    public static void saveObject(String directory, Object object)
    {
        try
        {
            FileOutputStream f_out = new FileOutputStream(directory);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject(object);
            obj_out.close();
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    /**
     * Load an object from a binary file
     * @param path object path
     * @return loaded object
     */
    public static Object loadObject(String path)
    {
        Object obj = null;

        if(isFileExisting(path))
        {
            try
            {
                FileInputStream f_in = new FileInputStream(path);
                ObjectInputStream obj_in = new ObjectInputStream(f_in);
                obj = obj_in.readObject();
                obj_in.close();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        return obj;
    }
}
