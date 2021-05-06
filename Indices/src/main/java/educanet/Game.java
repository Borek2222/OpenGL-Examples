package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public class Game {
    private static final float[] vertices = {
            0.25f, 0.25f, 0.0f, // 0 -> Top right
            0.25f, -0.25f, 0.0f, // 1 -> Bottom right
            -0.25f, -0.25f, 0.0f, // 2 -> Bottom left
            -0.25f, 0.25f, 0.0f, // 3 -> Top left
    };

    private static final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    private static int squareVaoId;
    private static int squareVboId;
    private static int squareEboId;
    private static int colorsId;
    private static int uniformMatrixLocation;

    private static Matrix4f matrix = new Matrix4f()
            .identity()
            .translate(0.25f, 0.25f, 0.25f);
    private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

    public static void init(long window) {
        Shaders.initShaders();
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        colorsId = GL33.glGenBuffers();

        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        MemoryUtil.memFree(fb);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);

        FloatBuffer cb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        GL33.glUseProgram(Shaders.shaderProgramId);

        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        MemoryUtil.memFree(cb);
        MemoryUtil.memFree(fb);
    }

    public static void render(long window) {
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public static void update(long window) {
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) { // W
            matrix = matrix.translate(0.01f, 0f, 0f);
            System.out.println("-------------------W-------------------\n" + matrix);
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) { // S
            matrix = matrix.translate(-0.01f, 0f, 0f);
            System.out.println("-------------------S-------------------\n" +matrix);
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) { // A
            matrix = matrix.translate(0.0f, 0.01f, 0f);
            System.out.println("-------------------A-------------------\n" +matrix);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) { // D
            matrix = matrix.translate(0.0f, -0.01f, 0f);
            System.out.println("-------------------D-------------------\n" +matrix);
        }
    }
}