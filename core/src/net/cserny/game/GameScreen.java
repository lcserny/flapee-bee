package net.cserny.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float GAP_BETWEEN_FLOWERS = 200f;

    private final FlapeeBeeGame game;

    private int score = 0;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout layout;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private Flapee flapee;
    private Array<Flower> flowers = new Array<Flower>();

    private Texture background;
    private Texture flowerBottom;
    private Texture flowerTop;
    private Texture flapeeTexture;

    public GameScreen(FlapeeBeeGame game) {
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        layout = new GlyphLayout();
        background = game.getAssetManager().get("bg.png");
        flowerBottom = game.getAssetManager().get("flowerBottom.png");
        flowerTop = game.getAssetManager().get("flowerTop.png");
        flapeeTexture = game.getAssetManager().get("bee.png");
        flapee = new Flapee(flapeeTexture);
        flapee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);

        draw();

//        shapeRenderer.setProjectionMatrix(camera.projection);
//        shapeRenderer.setTransformMatrix(camera.view);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        flapee.drawDebug(shapeRenderer);
//        drawDebug();
//        shapeRenderer.end();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        batch.draw(background, 0, 0);
        drawFlowers();
        flapee.draw(batch);
        drawScore();

        batch.end();
    }

    private void createNewFlower() {
        Flower flower = new Flower(flowerBottom, flowerTop);
        flower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    private boolean checkForCollision() {
        for (Flower flower : flowers) {
            if (flower.isFlapeeColliding(flapee)) {
                return true;
            }
        }
        return false;
    }

    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size == 0) {
            createNewFlower();
        } else {
            Flower flower = flowers.peek();
            if (flower.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }

    private void removeFlowersIfPassed() {
        if (flowers.size > 0) {
            Flower firstFlower = flowers.first();
            if (firstFlower.getX() < -Flower.WIDTH) {
                flowers.removeValue(firstFlower, true);
            }
        }
    }

    private void update(float delta) {
        updateFlapee(delta);
        updateFlowers(delta);
        updateScore();
        if (checkForCollision()) {
            restart();
        }
    }

    private void drawScore() {
        String scoreAsString = Integer.toString(score);
        layout.setText(font, scoreAsString);
        font.draw(batch, scoreAsString,
                (viewport.getWorldWidth() - layout.width) / 2,
                (4 * viewport.getWorldHeight() / 5) - layout.height / 2);
    }

    private void updateScore() {
        Flower flower = flowers.first();
        if (flower.getX() < flapee.getX() && !flower.isPointClaimed()) {
            flower.markPointClaimed();
            score++;
        }
    }

    private void restart() {
        flapee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        flowers.clear();
        score = 0;
    }

    private void updateFlapee(float delta) {
        flapee.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flapee.flyUp();
        }
        blockFlapeeLeavingTheWorld();
    }

    private void updateFlowers(float delta) {
        for (Flower flower : flowers) {
            flower.update(delta);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }

    private void drawDebug() {
        for (Flower flower : flowers) {
            flower.drawDebug(shapeRenderer);
        }
    }

    private void drawFlowers() {
        for (Flower flower : flowers) {
            flower.draw(batch);
        }
    }

    private void blockFlapeeLeavingTheWorld() {
        flapee.setPosition(flapee.getX(), MathUtils.clamp(flapee.getY(), 0, WORLD_HEIGHT));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
