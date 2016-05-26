package org.cora.graphics.test;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;

import org.cora.graphics.base.Image;
import org.cora.graphics.font.Alignement;
import org.cora.graphics.font.Font;
import org.cora.graphics.font.TextRenderer;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.Surface;
import org.cora.graphics.graphics.myColor;
import org.cora.graphics.input.Input;
import org.cora.graphics.manager.TextureManager;

public class Main
{
    public static void main(String[] args)
    {
        Graphics g = new Graphics();
        g.init("B2OBA", 800, 600);
        g.initGL(800, 600);
        
        Input input = new Input();
        input.initGL(g.getScreen());
        
        Surface textFontSurface = TextureManager.createTextureFromDef("font.bmp");
        
        g.loadTextureGL(textFontSurface);
        
        Font font = new Font();
        font.initialize(textFontSurface, 32);
        font.setSpaceSize(20);
        
        TextRenderer text = new TextRenderer(font);
        text.setProportional(false);
        //text.setProportionalSpacing(10);
        text.setMaxWidth(200);
        
        text.setFontColor(myColor.WHITE(1.0f));
        text.setBackColor(myColor.BLUE(0.5f));
        text.setAlignement(Alignement.FULL);

        Image textImage = text.transformToImage("Je suis assez fort pour soulever une voiture! \n Bonjour", 250, 250);
        g.loadTextureGL(textImage.getSpriteData().surface);

        while (glfwWindowShouldClose(g.getScreen()) == GL_FALSE)
        {
            //g.setColor(myColor.WHITE());
            
            g.clear();
            g.setColor(myColor.WHITE());

            //text.print(g, "Bonjour je m'appelle Rodrigo DeSanchez, je suis le plus beau des princes.\n\t Hier j'ai copulé avec Madry.", 0, 0);
            g.render(textImage);
            
            input.update();
            

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
            }

            g.swapGL();
        }
    }
}
