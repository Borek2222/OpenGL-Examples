package educanet;

import educanet.objects.Player;
import educanet.objects.Square;
import educanet.utils.FileUtil;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    public static void init(long window) {
        Shaders.initShaders();
        createPlayer();
        setMaze();
        createMaze();
    }

    public static void render(long window) {
        renderPlayer();
        renderMaze();
    }

    public static void update(long window) {
        movePlayer(window, Player.getMatrix());
    }

    // PLAYER
    public static void createPlayer() {
        Player p = new Player();

        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(Player.getSquareVaoId());
        GL33.glDrawElements(GL33.GL_TRIANGLES, Player.getVertices().length, GL33.GL_UNSIGNED_INT, 0);
    }

    public static void renderPlayer() {
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(Player.getSquareVaoId());
        GL33.glDrawElements(GL33.GL_TRIANGLES, Player.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
    }

    public static void movePlayer(long window, Matrix4f matrix) {
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
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) { // Dw
            matrix = matrix.translate(0.0f, -0.01f, 0f);
            System.out.println("-------------------D-------------------\n" +matrix);
        }
    }

    public static ArrayList<Square> mazeObjectArrayList = new ArrayList<>();

    public static String gameField;
    public static int numberOfObjectsMaze;


    public static void setMaze() {

        String path = "Indices/src/levels/lvl1.txt";
        File level = new File(path);

        if (level.exists() && level.canRead())
            gameField = FileUtil.readFile(path);


        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(gameField);
        while (m.find()) {
            numberOfObjectsMaze++;
        }
    }

    public static void createMaze() {
        String[] objs = gameField.split("\n");

        for (int i = 0; i < numberOfObjectsMaze; i++) {
            String[] objAtrribs = objs[i].split(";");
            Square s = new Square(Float.parseFloat(objAtrribs[0]),Float.parseFloat(objAtrribs[1]),0f ,Float.parseFloat(objAtrribs[2]));
            mazeObjectArrayList.add(s);
        }
    }

    public static void renderMaze() {
        new Matrix4f().identity().get(Player.getMatrixBuffer());
        GL33.glUniformMatrix4fv(Player.getUniformMatrixLocation(), false, Player.getMatrixBuffer());
        for (int i = 0; i < numberOfObjectsMaze; i++) {
            Square object = mazeObjectArrayList.get(i);
            if (object != null) object.draw();
        }
    }
}
