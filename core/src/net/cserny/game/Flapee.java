package net.cserny.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Flapee {

    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCEL = 0.30f;
    private static final float FLY_ACCEL = 5f;
    private final Circle collisionCircle;

    private float x = 0, y = 0;
    private float ySpeed = 0;

    public Flapee() {
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update() {
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_ACCEL;
        setPosition(x , y + ySpeed);
    }
}
