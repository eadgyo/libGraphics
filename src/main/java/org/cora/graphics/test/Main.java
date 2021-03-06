package org.cora.graphics.test;

import org.cora.graphics.base.Image;
import org.cora.graphics.elements.TextButton;
import org.cora.graphics.font.Alignement;
import org.cora.graphics.font.Font;
import org.cora.graphics.font.TextPosition;
import org.cora.graphics.font.TextRenderer;
import org.cora.graphics.graphics.Graphics;
import org.cora.graphics.graphics.Surface;
import org.cora.graphics.graphics.myColor;
import org.cora.graphics.input.Input;
import org.cora.graphics.manager.TextureManager;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class Main
{
    public static void main(String[] args)
    {
        Graphics g = new Graphics("B2OBA", 800, 600, true, true, Main.class);

        Input input = new Input();
        input.initGL(g.getScreen());

        Surface textFontSurface = TextureManager.getInstance().loadTexture("/font.bmp");

        g.loadTextureGL(textFontSurface);




        Font font = new Font();
        font.initialize(textFontSurface, 32);

        TextRenderer text = new TextRenderer(font);
        text.setTextPosition(TextPosition.TOP_CENTER);
        text.setAlignement(Alignement.TOP_CENTER);
        //text.setWidth(512);
        TextButton button0 = new TextButton(0, 0, 200, 200, text);

        //ou
        TextButton button = new TextButton(0, 0, 200, 200, font);
        button.setTextBotCenter();
        //button.getTextRenderer().setWidth(32);
        //button.getTextRenderer().setTextPosition(TextPosition.RIGHT);

        text.setFontColor(myColor.RED());
        //text.setProportionalSpacing(10);
        //text.setMaxWidth(200);



        button.setTxt("S.E.V.R.A.N");

        //text.setFontColor(myColor.WHITE(1.0f));
        //text.setBackColor(myColor.BLUE(0.5f));
        //text.setAlignement(Alignement.FULL);

        Image textImage = text.transformToImage("Je suis assez fort pour! \n Bonjour", 0, 0);
        g.loadTextureGL(textImage.getSpriteData().surface);


        float a = 0;
        float b = (float) (Math.PI*2);
        float add = 0.1f;
        while (glfwWindowShouldClose(g.getScreen()) == GL_FALSE)
        {
            //g.setColor(myColor.WHITE());

            g.clear();
            g.setColor(myColor.WHITE());
            g.drawCircle(150, 150, 60, 10);
            g.drawCircle(100, 100, 60, 15, a, b);
            a += add;
            if ( a > 2*(Math.PI))
            {
                add = -add;
                a = (float)(2*(Math.PI));
            }
            if ( a < 0)
            {
                add = -add;
                a = 0;
            }
            //g.fillCircle(100, 100, 60, 10);
            //text.print(g, "Bonjour je m'appelle Rodrigo DeSanchez, je suis le plus beau des princes.\n\t Hier j'ai copulé avec Madry.", 0, 0);
            
            //button.render(g);


            //g.render(textImage);

            input.update(0.016f);
            System.out.println(input.getTemp());

            /*
            if (input.isKeyDown(Input.KEY_ENTER))
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
