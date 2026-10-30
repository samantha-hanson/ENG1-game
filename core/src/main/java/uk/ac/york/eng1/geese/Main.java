package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;

import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.Player;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;
import uk.ac.york.eng1.geese.items.Airhorn;
import uk.ac.york.eng1.geese.items.Goosedisguise;
import com.badlogic.gdx.graphics.Texture;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;
    private Goose goose1;
    private Goose goose2;

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

        player.getInventory().addItem(new Airhorn(new Texture("Airhorn.png")));
        player.getInventory().addItem(new Goosedisguise(new Texture("Goosedisguise.png")));

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
        gameMap.addGoose(goose1);
        gameMap.addGoose(goose2);

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
        for (Goose goose : gameMap.getGeese()){
            if(player.getHitbox().overlaps(goose.getHitbox())){
                System.out.println("goose hit player");
            }
        }

        // Use items
        // gameMap is now passed because Inventory.useItem needs it for the item's effect
        if (Gdx.input.isKeyJustPressed(Input .Keys.E)) {
            player.getInventory().useItem(Airhorn.class, gameMap, player);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            player.getInventory().useItem(Goosedisguise.class, gameMap, player);
        }

        // Tick timed item effects once per frame
        // Fixed: gameMap and player are now passed because items need them to end their effects
        player.getInventory().update(Gdx.graphics.getDeltaTime(), gameMap, player);

        //----------batch begins----------
        batch.begin();
        player.movePlayer();

        for (Goose goose : gameMap.getGeese()) {
            goose.updateStatus(player);
            goose.moveNPC();
            goose.draw(batch);
        }

        player.draw(batch);
        batch.end();
        //----------batch ends----------

    }

    // Runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        player.getInventory().dispose();
    }
}
