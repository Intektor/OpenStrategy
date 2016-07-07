package de.intektor.open_strategy.client.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import de.intektor.open_strategy.utils.FontHelper;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;

import static de.intektor.open_strategy.OpenStrategy.layout;

/**
 * @author Intektor
 */
public class RenderHelper {

    public static void renderSquare(ShapeRenderer renderer, Color renderColor, float x, float y, float width, float height) {
        renderer.identity();
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(renderColor);
        renderer.rect(x, y, width, height);
    }

    public static void renderRectangle(ShapeRenderer renderer, Color color, Rectangle rectangle) {
        renderSquare(renderer, color, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    public static void drawString(float x, float y, String string, BitmapFont font, Batch batch) {
        drawString(x, y, string, font, batch, false, false);
    }

    public static void drawString(float x, float y, String string, BitmapFont font, Batch batch, boolean centerX, boolean centerY) {
        layout.reset();
        layout.setText(font, string);
        float rx = centerX ? x - layout.width / 2 : x;
        float ry = centerY ? y + layout.height / 2 : y;
        font.draw(batch, string, rx, ry);
    }

    public static void drawSplitString(float x, float y, float width, String string, BitmapFont font, Batch batch, int fontHeight) {
        int i = 0;
        for (String s : FontHelper.splitString(string, width, font)) {
            drawString(x, y - (i * fontHeight), s, font, batch);
            i++;
        }
    }


    public static void renderPolygon3D(Camera camera, Polygon polygon, Color color) {
        lineRenderer.begin(camera.combined, GL30.GL_LINES);
        float[] f = polygon.getTransformedVertices();
        for (int i = 0; i < f.length / 2; i++) {
            lineRenderer.color(color);
            lineRenderer.vertex(f[i / 2], 0, f[i / 2 + 1]);
        }
        lineRenderer.end();
    }

    public static void renderVector3D(Camera camera, Point3f origin, Vector3 vector) {
        renderLine3D(camera, origin, new Point3f(origin.x + vector.x * 100, origin.y + vector.y * 100, origin.z + vector.z * 100), Color.RED);
    }

    public static void renderVector2D(ShapeRenderer renderer, Vector2 vector, Point2f startPoint, float length) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.identity();
        renderer.line(startPoint.x - vector.x * (length / 2), startPoint.y - vector.y * (length / 2), startPoint.x + vector.x * (length / 2), startPoint.y + vector.y * (length / 2));
        renderer.end();
    }

    public static void renderVector2D(ShapeRenderer renderer, Vector2f vector, Point2f startPoint, float length, Color color) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.identity();
        renderer.setColor(color);
        renderer.line(startPoint.x - vector.x * length / 2, startPoint.y - vector.y * length / 2, startPoint.x + vector.x * length / 2, startPoint.y + vector.y * length / 2);
        renderer.end();
    }

    public static void renderLine2D(ShapeRenderer renderer, Point2f point1, Point2f point2, Color renderColor) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.identity();
        renderer.setColor(renderColor);
        renderer.line(point1.x, point1.y, point2.x, point2.y);
        renderer.end();
    }

    private static ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);

    public static void renderRay3D(Camera camera, Ray ray, float length, Color color) {
        Point3f point1 = new Point3f(ray.origin.x, ray.origin.y, ray.origin.z);
        Point3f direction = new Point3f(ray.direction.x * length, ray.direction.y * length, ray.direction.z * length);
        direction.add(point1);
        renderLine3D(camera, point1, direction, color);
    }

    public static void renderLine3D(Camera camera, Vector3 point1, Vector3 point2, Color color) {
        renderLine3D(camera, new Point3f(point1.x, point1.y, point2.z), new Point3f(point2.x, point2.y, point2.z), color);
    }

    public static void renderLine3D(Camera camera, Point3f point1, Point3f point2, Color color) {
        renderLine3D(camera, point1, point2, color, color);
    }

    public static void renderLine3D(Camera camera, Point3f point1, Point3f point2, Color color1, Color color2) {
        lineRenderer.begin(camera.combined, GL30.GL_LINES);
        lineRenderer.color(color1);
        lineRenderer.vertex(point1.x, point1.y, point1.z);
        lineRenderer.color(color2);
        lineRenderer.vertex(point2.x, point2.y, point2.z);
        lineRenderer.end();
    }
}
