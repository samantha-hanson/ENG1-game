package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;

import uk.ac.york.eng1.geese.entities.NPC.Fox;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.NPC.LongBoi;
import uk.ac.york.eng1.geese.entities.Player;

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
    private LongBoi longBoi;
    private Fox fox;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameMap gameMap;

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

        longBoi = new LongBoi(
            500,
            500,
            3,
            500,
            gameMap
        );

        fox = new Fox(
            800,
            800,
            2,
            500,
            gameMap
        );

        // This should be defined by the goose spawning method
        gooseArray = new Goose[]{goose1, goose2};

        // Ensure the viewport is correctly sized to the window
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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

        if(player.getHitbox().overlaps(longBoi.getHitbox())){
            System.out.println("long boi hit player");
        }

        if(longBoi.getHitbox().overlaps(fox.getHitbox())){
            System.out.println("fox hit longBoi");
        }


        //----------batch begins----------
        batch.begin();
        player.movePlayer();

        for (Goose goose : gooseArray) {
            goose.updateStatus(player);
            goose.moveNPC();
            goose.draw(batch);
        }

        longBoi.updateStatus(player);
        longBoi.moveNPC();
        longBoi.draw(batch);

        fox.updateStatus(longBoi);
        fox.moveNPC();
        fox.draw(batch);

        player.draw(batch);
        batch.end();
        //----------batch ends----------

    }

    // Runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
    }
}
