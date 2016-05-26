package org.polytech.polybigbalance.test;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.polytech.polybigbalance.font.Alignement;
import org.polytech.polybigbalance.font.Font;
import org.polytech.polybigbalance.font.Text;
import org.polytech.polybigbalance.graphics.Graphics;
import org.polytech.polybigbalance.graphics.Surface;
import org.polytech.polybigbalance.graphics.myColor;
import org.polytech.polybigbalance.graphics.base.Image;
import org.polytech.polybigbalance.input.Input;
import org.polytech.polybigbalance.manager.FileManager;

import cora.maths.Rectangle;
import cora.maths.Vector2D;

public class Main
{
    public static void main(String[] args)
    {
        Graphics g = new Graphics();
        g.init("B2OBA", 800, 600);
        g.initGL(800, 600);
        
        Input input = new Input();
        input.initGL(g.getScreen());
        
        FileManager fm = FileManager.getInstance();
        Surface textFontSurface = fm.loadSurfaceFromDef("font.bmp");
        
        g.loadTextureGL(textFontSurface);
        
        Font font = new Font();
        font.initialize(textFontSurface, 32);
        font.setSpaceSize(10);
        
        Text text = new Text(font);
        text.setProportional(true);
        text.setMaxWidth(200);
        text.setFontColor(myColor.WHITE(1.0f));
        text.setBackColor(myColor.BLUE(0.5f));
        text.setAlignement(Alignement.FULL);

        Image textImage = text.transformToImage("Je suis assez fort pour", 100, 100);
        g.loadTextureGL(textImage.getSpriteData().surface);

        while (glfwWindowShouldClose(g.getScreen()) == GL_FALSE)
        {
            //g.setColor(myColor.WHITE());
            
            g.clear();
            g.setColor(myColor.WHITE());

            text.print(g, "Bonjour je m'appelle Rodrigo DeSanchez, je suis le plus beau des princes.\n\t Hier j'ai copulé avec Madry.", 0, 0);
            //g.render(textImage);
            
            input.update();
            /*

            if (input.getKeyDown(Input.KEY_ENTER))
            {
                System.out.println("Enter");
            }

            if (input.isMouseMoving())
            {
                System.out.println("mouse moving:" + input.getMousePosX()
                        + ", " + input.getMousePosY());
            }

            if (input.isMouseScrolling())
            {
                System.out.println("mouse scolling:" + input.getMouseWheelX()
                        + ", " + input.getMouseWheelY());
            }*/

            g.swapGL();
        }
    }
}
