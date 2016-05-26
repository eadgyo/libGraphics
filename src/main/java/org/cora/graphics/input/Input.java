package org.cora.graphics.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;

import org.cora.maths.Vector2D;

public class Input
{
    private GLFWKeyCallback         keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorPosCallback   mouseMotionCallback;
    private GLFWScrollCallback      mouseScrollCallback;

    public static final int         KEY_ENTER             = 0;
    public static final int         KEY_ESC               = 1;
    public static final int         KEY_SPACE             = 2;
    public static final int         MOUSE_BUTTON_1        = 0;
    public static final int         MOUSE_BUTTON_2        = 1;

    private final static int        NUMBER_OF_KEYS        = 4;
    private boolean                 quit;

    private boolean                 mouseMoves;
    private boolean                 mouseScrolls;
    private double                  mousePos[]            = new double[2];
    private boolean                 mouseDown;
    private boolean                 mouseButtonsDown[]    = new boolean[2];
    private boolean                 mousePressed;
    private boolean                 mouseButtonsPressed[] = new boolean[2];
    private int                     mouseWheelX;
    private int                     mouseWheelY;

    private boolean                 keyDown;
    private boolean                 keysDown[]            = new boolean[NUMBER_OF_KEYS];
    private boolean                 keyPressed;
    private boolean                 keysPressed[]         = new boolean[NUMBER_OF_KEYS];

    public Input()
    {
        quit = false;

        mouseScrolls = false;
        mouseMoves = false;
        mouseDown = false;
        mousePressed = false;
        keyDown = false;
        keyPressed = false;

        mouseWheelX = 0;
        mouseWheelY = 0;

        clear();
        keyCallback = new KeyboardListener();
        mouseButtonCallback = new MouseButtonsListener();
        mouseMotionCallback = new MouseMotionListener();
        mouseScrollCallback = new MouseScrollListener();
    }

    public void initGL(long screen)
    {
        glfwSetKeyCallback(screen, keyCallback);
        glfwSetMouseButtonCallback(screen, mouseButtonCallback);
        glfwSetCursorPosCallback(screen, mouseMotionCallback);
        glfwSetScrollCallback(screen, mouseScrollCallback);
    }

    public boolean exit()
    {
        return quit;
    }

    // Mouse
    public boolean isMouseMoving()
    {
        return mouseMoves;
    }

    public double[] getMousePos()
    {
        return mousePos;
    }

    public double getMousePosX()
    {
        return mousePos[0];
    }

    public double getMousePosY()
    {
        return mousePos[1];
    }

    public Vector2D getMousePosV()
    {
        return new Vector2D((float) mousePos[0], (float) mousePos[1]);
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public boolean getMouseDown(int n)
    {
        return mouseButtonsDown[n];
    }

    public boolean getMousePressed(int n)
    {
        return mouseButtonsPressed[n];
    }

    public int getMouseWheelX()
    {
        return mouseWheelX;
    }

    public int getMouseWheelY()
    {
        return mouseWheelY;
    }

    public boolean isMouseScrolling()
    {
        return mouseScrolls;
    }

    // KeyBoard
    public boolean isKeyDown()
    {
        return keyDown;
    }

    public boolean isKeyPressed()
    {
        return keyPressed;
    }

    public boolean getKeyDown(int n)
    {
        return keysDown[n];
    }

    public boolean getKeyPressed(int n)
    {
        return keysPressed[n];
    }

    // Clear
    public void clear()
    {
        clearMouse();
        clearKeys();
    }

    // Mouse
    public void clearMouse()
    {
        clearMousePressed();
        clearMouseDown();
        mouseWheelX = 0;
        mouseWheelY = 0;
        mousePos[0] = 0;
        mousePos[1] = 0;
    }

    public void clearMouse(int n)
    {
        clearMousePressed(n);
        clearMouseDown(n);
    }

    public void clearMouseDown()
    {
        mouseDown = false;
        for (int i = 0; i < 2; i++)
        {
            mouseButtonsDown[i] = false;
        }
    }

    public void clearMouseDown(int n)
    {
        mouseButtonsDown[n] = false;
    }

    public void clearMousePressed()
    {
        mousePressed = false;
        for (int i = 0; i < 2; i++)
        {
            mouseButtonsPressed[i] = false;
        }
    }

    public void clearMousePressed(int n)
    {
        mouseButtonsPressed[n] = false;
    }

    // Keyboard
    public void clearKeys()
    {
        clearKeysPressed();
        clearKeysDown();
    }

    public void clearKey(int n)
    {
        clearKeyPressed(n);
        clearKeyDown(n);
    }

    public void clearKeysDown()
    {
        keyDown = false;
        for (int i = 0; i < NUMBER_OF_KEYS; i++)
        {
            keysDown[i] = false;
        }
    }

    public void clearKeyDown(int n)
    {
        keysDown[n] = false;
    }

    public void clearKeysPressed()
    {
        keyPressed = false;
        for (int i = 0; i < NUMBER_OF_KEYS; i++)
        {
            keysPressed[i] = false;
        }
    }

    public void clearKeyPressed(int n)
    {
        keysPressed[n] = false;
    }

    public void update()
    {
        mouseWheelX = 0;
        mouseWheelY = 0;
        mouseMoves = false;
        mouseScrolls = false;
        clearMousePressed();
        clearKeysPressed();
        glfwPollEvents();
    }

    private class KeyboardListener extends GLFWKeyCallback
    {
        @Override
        public void invoke(long window, int key, int scancode, int action,
                int mods)
        {
            if (action == GLFW_PRESS)
            {
                switch (key)
                {
                    case GLFW_KEY_SPACE:
                        keysPressed[KEY_SPACE] = !keysDown[KEY_SPACE];
                        keysDown[KEY_SPACE] = true;
                        break;
                    case GLFW_KEY_ENTER:
                        keysPressed[KEY_ENTER] = !keysDown[KEY_ENTER];
                        keysDown[KEY_ENTER] = true;
                        break;
                    case GLFW_KEY_ESCAPE:
                        keysPressed[KEY_ESC] = !keysDown[KEY_ESC];
                        keysDown[KEY_ESC] = true;
                        break;
                }
            }
            else if (action == GLFW_RELEASE)
            {
                switch (key)
                {
                    case GLFW_KEY_SPACE:
                        keysDown[KEY_SPACE] = false;
                        break;
                    case GLFW_KEY_ENTER:
                        keysDown[KEY_ENTER] = false;
                        break;
                    case GLFW_KEY_ESCAPE:
                        keysDown[KEY_ESC] = false;
                        break;
                }
            }
        }
    }

    private class MouseButtonsListener extends GLFWMouseButtonCallback
    {
        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            if (action == GLFW_PRESS)
            {
                switch (button)
                {
                    case GLFW_MOUSE_BUTTON_1:
                        mouseButtonsPressed[MOUSE_BUTTON_1] = !mouseButtonsDown[0];
                        mouseButtonsDown[MOUSE_BUTTON_1] = true;
                        break;

                    case GLFW_MOUSE_BUTTON_2:
                        mouseButtonsPressed[MOUSE_BUTTON_2] = !mouseButtonsDown[1];
                        mouseButtonsDown[MOUSE_BUTTON_2] = true;
                        break;
                }
            }
            else
            {
                switch (button)
                {
                    case GLFW_MOUSE_BUTTON_1:
                        mouseButtonsDown[MOUSE_BUTTON_1] = false;
                        break;

                    case GLFW_MOUSE_BUTTON_2:
                        mouseButtonsDown[MOUSE_BUTTON_2] = false;
                        break;
                }
            }
        }
    }

    private class MouseMotionListener extends GLFWCursorPosCallback
    {
        @Override
        public void invoke(long window, double xpos, double ypos)
        {
            mousePos[0] = (int) xpos;
            mousePos[1] = (int) ypos;

            mouseMoves = true;
        }
    }

    private class MouseScrollListener extends GLFWScrollCallback
    {
        @Override
        public void invoke(long window, double xoffset, double yoffset)
        {
            mouseWheelX = (int) xoffset;
            mouseWheelY = (int) yoffset;

            System.out.println("o->" + yoffset);

            mouseScrolls = true;
        }
    }
}
