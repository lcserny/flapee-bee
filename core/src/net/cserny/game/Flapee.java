package net.cserny.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Flapee {

    private static final int TILE_WIDTH = 118;
    private static final int TILE_HEIGHT = 118;
    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCEL = 0.30f;
    private static final float FLY_ACCEL = 5f;
    private static final float FRAME_DURATION = 0.25f;
    private final Circle collisionCircle;
    private final Animation<TextureRegion> animation;

    private float x = 0, y = 0;
    private float ySpeed = 0;
    private float animationTimer = 0;

    public Flapee(TextureRegion flapeeTexture) {
        TextureRegion[][] textureRegions = flapeeTexture.split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation<TextureRegion>(FRAME_DURATION, textureRegions[0][0], textureRegions[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
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

    public void update(float delta) {
        animationTimer += delta;
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_ACCEL;
        setPosition(x , y + ySpeed);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = animation.getKeyFrame(animationTimer);
        float textureX = collisionCircle.x - frame.getRegionWidth() / 2;
        float textureY = collisionCircle.y - frame.getRegionHeight() / 2;
        batch.draw(frame, textureX, textureY);
    }
}
