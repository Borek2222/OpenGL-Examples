package educanet.objects;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Maze {
    public float x;
    public float y;
    public float z;
    public float size;

    float[] vertices;
    int[] indices;
    float[] color;

    public int vaoId;
    final int vboId;
    final int eboId;
    final int colorId;

    FloatBuffer fb;
    IntBuffer ib;
    FloatBuffer cb;

    public Maze() {
        vaoId = GL33.glGenVertexArrays();
        vboId = GL33.glGenBuffers();
        eboId = GL33.glGenBuffers();
        colorId = GL33.glGenBuffers();

    }

    public void draw() {
        GL33.glBindVertexArray(vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public void update() {}

    protected void setup() {
        GL33.glBindVertexArray(vaoId);

        setFloatBuffer(vertices.length, "vert");
        setVertices(this.vertices);
        setFloatBuffer(vertices.length, "col");
        setColor(this.color);

        setIntBuffer(indices.length);
        setIndices(this.indices);

    }

    public void setVertices(float[] vertices) {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);

        fb.clear()
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);
    }

    public void setIndices(int[] indices) {
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, eboId);
        ib.clear()
                .put(indices)
                .flip();

        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);
    }

    public void setColor(float[] color) {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorId);

        FloatBuffer cb = BufferUtils.createFloatBuffer(color.length)
                .put(color)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public void setIntBuffer(int length) {

        if (ib != null) MemoryUtil.memFree(ib);

        ib = BufferUtils.createIntBuffer(length);

    }

    public void setFloatBuffer(int length, String type) {
        if (type.equals("vert")) {
            if (fb != null) MemoryUtil.memFree(fb);

            fb = BufferUtils.createFloatBuffer(length);
        } else if (type.equals("col")) {
            if (cb != null) MemoryUtil.memFree(cb);

            cb = BufferUtils.createFloatBuffer(length);
        }
    }
}
