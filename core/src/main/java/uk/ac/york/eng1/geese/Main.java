package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;

import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.Player;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private static final float TOTAL_TIME = 300F;
    private SpriteBatch batch;

    private Player player;
    private Goose goose1;
    private Goose goose2;
    private Goose[] gooseArray;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameMap gameMap;
    private BitmapFont timerFont;
    private GlyphLayout timerLayout;
    private float timer;
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // Runs once on start-up
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 600, camera);

        // load the map, and constrict entity movement
        float unitScale = 5f;
        gameMap = new GameMap("mapdummy.tmx", unitScale);

        player = new Player(
            100,
            100,
            10,
            gameMap
        );

        goose1 = new Goose(
            300,
            300,
            2,
            300,
            gameMap
        );

        goose2 = new Goose(
            400,
            400,
            2,
            300,
            gameMap
        );

        // This should be defined by the goose spawning method
        gooseArray = new Goose[]{goose1, goose2};

        // Ensure the viewport is correctly sized to the window
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Start the 5-minute countdown timer from the moment the game begins.
        timer = TOTAL_TIME;
        timerFont = new BitmapFont();
        timerFont.getData().setScale(2f);
        timerLayout = new GlyphLayout();
    }

    // Per frame behaviour
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Load map and set up camera
        camera.position.set(player.getPos(), 0);
        camera.update();
        gameMap.setViewAndRender(camera);
        batch.setProjectionMatrix(camera.combined);

        // Check for collision between entities -> this is very inefficient.
        for (Goose goose : gooseArray){
            if(player.getHitbox().overlaps(goose.getHitbox())){
                System.out.println("goose hit player");
            }
        }

        //----------batch begins----------
        batch.begin();
        player.movePlayer();

        for (Goose goose : gooseArray) {
            goose.updateStatus(player);
            goose.moveNPC();
            goose.draw(batch);
        }

        player.draw(batch);
        batch.end();
        //----------batch ends----------

        //----------HUD: 5-minute countdown timer in the top-right corner----------
        timer -= Gdx.graphics.getDeltaTime();
        if (timer < 0f) {
            timer = 0f;
        }

        int minutes = (int) (timer / 60);
        int seconds = (int) (timer % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerLayout.setText(timerFont, timeText);

        // Switch to screen coordinates so the timer stays fixed in the corner
        // of the window regardless of where the camera is.
        batch.getProjectionMatrix().setToOrtho2D(0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        float padding = 20f;
        timerFont.draw(batch, timeText,
            Gdx.graphics.getWidth() - timerLayout.width - padding,
            Gdx.graphics.getHeight() - padding);
        batch.end();
        //----------HUD ends----------
    }

    // Runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        timerFont.dispose();
        gameMap.dispose();
    }
}
