package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;

import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.NPC.Status;
import uk.ac.york.eng1.geese.entities.Player;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;
import uk.ac.york.eng1.geese.items.Airhorn;
import uk.ac.york.eng1.geese.items.Goosedisguise;
import com.badlogic.gdx.graphics.Texture;

import uk.ac.york.eng1.geese.ui.Hud;

import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameMap gameMap;

    private Hud hud;
    /** Food a goose takes per bite. */
    private static final int BITE_AMOUNT = 10;
    private static final double GOOSE_SPAWN_CHANCE = 0.25;
    private static final double SPAWN_RADIUS = 500;
    private static final Random RANDOM = new Random();

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

        hud = new Hud();

        player.getInventory().addItem(new Airhorn(new Texture("Airhorn.png"),
            Gdx.audio.newSound(Gdx.files.internal("AirhornSound.wav"))));
        player.getInventory().addItem(new Goosedisguise(new Texture("Goosedisguise.png")));

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

        // food timer
        player.updateFoodTimer(Gdx.graphics.getDeltaTime());

        for (Goose goose : gameMap.getGeese()) {
            // Geese can't bite a disguised player and when Scared, matching how they ignore them when targeting
            if (!player.getDisguised() && goose.getStatus() != Status.SCARED && player.getHitbox().overlaps(goose.getHitbox())) {
                player.loseFood(BITE_AMOUNT);
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

        if (RANDOM.nextDouble() < (Gdx.graphics.getDeltaTime() * GOOSE_SPAWN_CHANCE)) {
            double pos_x, pos_y;
            do {
                var angle = RANDOM.nextDouble(2 * Math.PI);
                var offset_x = SPAWN_RADIUS * Math.sin(angle);
                var offset_y = SPAWN_RADIUS * Math.sin(angle);

                pos_x = camera.position.x + offset_x;
                pos_y = camera.position.y + offset_y;

                System.out.println(pos_x);
                System.out.println(pos_y);

            } while (pos_x < gameMap.mapBoundLeft || pos_x > gameMap.mapBoundRight
                || pos_y < gameMap.mapBoundFloor || pos_y > gameMap.mapBoundRoof);
            var goose = new Goose(
                (float) pos_x,
                (float) pos_y,
                2,
                300,
                gameMap
            );
            gameMap.addGoose(goose);
        }

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
        hud.draw(batch, viewport, player);
        //----------batch ends----------

    }

    // Runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        gameMap.dispose();
        player.getInventory().dispose();
        hud.dispose();
    }
}
