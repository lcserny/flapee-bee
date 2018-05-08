package net.cserny.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Flower {

    private static final float COLLISION_RECTANGLE_WIDTH = 13f;
    private static final float COLLISION_RECTANGLE_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float MAX_SPEED_PER_SECOND = 100f;
    private static final float HEIGHT_OFFSET = -400f;
    private static final float DISTANCE_BETWEEN_FLOOR_AND_CEILING = 225f;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    private final Circle floorCollisionCircle;
    private final Rectangle floorCollisionRectangle;
    private final Circle ceilingCollisionCircle;
    private final Rectangle ceilingCollisionRectangle;
    private final Texture floorTexture;
    private final Texture ceilingTexture;

    private float x = 0, y = 0;
    private boolean pointClaimed = false;

    public Flower(Texture floorTexture, Texture ceilingTexture) {
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
        this.y = MathUtils.random(HEIGHT_OFFSET);

        floorCollisionRectangle = new Rectangle(x, y, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        floorCollisionCircle = new Circle(x + floorCollisionRectangle.width / 2, y + floorCollisionRectangle.height, COLLISION_CIRCLE_RADIUS);
        ceilingCollisionRectangle = new Rectangle(x, floorCollisionCircle.y + DISTANCE_BETWEEN_FLOOR_AND_CEILING,
                COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT );
        ceilingCollisionCircle = new Circle(x + ceilingCollisionRectangle.width / 2, ceilingCollisionRectangle.y, COLLISION_CIRCLE_RADIUS);
    }

    public boolean isPointClaimed() {
        return pointClaimed;
    }

    public void markPointClaimed() {
        this.pointClaimed = true;
    }

    public float getX() {
        return x;
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
        updateCollisionRectangle();
    }

    private void updateCollisionCircle() {
        floorCollisionCircle.setX(x + floorCollisionRectangle.width / 2);
        ceilingCollisionCircle.setX(x + ceilingCollisionRectangle.width / 2);
    }

    private void updateCollisionRectangle() {
        floorCollisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
    }

    public void draw(SpriteBatch batch) {
        drawFloorFlower(batch);
        drawCeilingFlower(batch);
    }

    private void drawFloorFlower(SpriteBatch batch) {
        float textureX = floorCollisionCircle.x - floorTexture.getWidth() / 2;
        float textureY = floorCollisionRectangle.getY() + COLLISION_CIRCLE_RADIUS;
        batch.draw(floorTexture, textureX, textureY);
    }

    private void drawCeilingFlower(SpriteBatch batch) {
        float textureX = ceilingCollisionCircle.x - ceilingTexture.getWidth() / 2;
        float textureY = ceilingCollisionRectangle.getY() - COLLISION_CIRCLE_RADIUS;
        batch.draw(ceilingTexture, textureX, textureY);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(floorCollisionCircle.x, floorCollisionCircle.y, floorCollisionCircle.radius);
        shapeRenderer.rect(floorCollisionRectangle.x, floorCollisionRectangle.y, floorCollisionRectangle.width, floorCollisionRectangle.height);
        shapeRenderer.circle(ceilingCollisionCircle.x, ceilingCollisionCircle.y, ceilingCollisionCircle.radius);
        shapeRenderer.rect(ceilingCollisionRectangle.x, ceilingCollisionRectangle.y, ceilingCollisionRectangle.width, ceilingCollisionRectangle.height);
    }

    public boolean isFlapeeColliding(Flapee flapee) {
        Circle flapeeCollisionCircle = flapee.getCollisionCircle();
        return Intersector.overlaps(flapeeCollisionCircle, ceilingCollisionCircle)
                || Intersector.overlaps(flapeeCollisionCircle, floorCollisionCircle)
                || Intersector.overlaps(flapeeCollisionCircle, ceilingCollisionRectangle)
                || Intersector.overlaps(flapeeCollisionCircle, floorCollisionRectangle);
    }
}
