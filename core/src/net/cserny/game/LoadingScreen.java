package net.cserny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private final FlapeeBeeGame game;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress = 0;

    public LoadingScreen(FlapeeBeeGame game) {
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "flapee_bee_assets.atlas";
        game.getAssetManager().load("font.fnt", BitmapFont.class, bitmapFontParameter);

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        game.getAssetManager().load("flapee_bee_assets.atlas", TextureAtlas.class);
//        game.getAssetManager().load("bg.png", Texture.class);
//        game.getAssetManager().load("flowerBottom.png", Texture.class);
//        game.getAssetManager().load("flowerTop.png", Texture.class);
//        game.getAssetManager().load("bee.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void update() {
        if (game.getAssetManager().update()) {
            game.setScreen(new GameScreen(game));
        } else {
            progress = game.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.rect(WORLD_WIDTH / 2 - PROGRESS_BAR_WIDTH / 2,
                WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
}
