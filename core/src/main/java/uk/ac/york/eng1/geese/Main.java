package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import uk.ac.york.eng1.geese.entities.GoalZone;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.NPC.Fox;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.NPC.LongBoi;
import uk.ac.york.eng1.geese.entities.NPC.Status;
import uk.ac.york.eng1.geese.entities.Player;
import uk.ac.york.eng1.geese.items.Airhorn;
import uk.ac.york.eng1.geese.items.Goosedisguise;
import uk.ac.york.eng1.geese.ui.Hud;
import uk.ac.york.eng1.geese.ui.EndScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import uk.ac.york.eng1.geese.entities.GoalZone;

import java.util.Iterator;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private static final float TOTAL_TIME = 300F;

    // Start point A: the bottom-left tile of the map, (0, 19) in Tiled coordinates.
    private static final int START_TILE_X = 0;
    private static final int START_TILE_Y = 19;

    private static final int FOX_TILE_X = 8;
    private static final int FOX_TILE_Y = 8;


    // Goal point B: the top-most yellow rectangle in the top-right corner of the map,
    // which covers tiles (21, 1) and (21, 2) in Tiled coordinates.
    private static final int GOAL_TILE_X = 21;
    private static final int GOAL_TILE_Y = 1;
    private static final int GOAL_TILES_WIDE = 1;
    private static final int GOAL_TILES_TALL = 2;

    private SpriteBatch batch;

    private Player player;
    private LongBoi longBoi;
    // initially set to false, set to true when 2 minutes have passed
    private boolean long_boi_spawned = false;
    private Fox fox;
    // initially set to false, set to true when the timer reaches 2 minutes 45 seconds have passed
    private boolean fox_spawned = false;

//    private GoalZone startPoint;
//    private GoalZone goalPoint;
    private GoalZone foxPoint;
    private Texture pixel;
    private GlyphLayout markerLayout;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameMap gameMap;

    private BitmapFont timerFont;
    private GlyphLayout timerLayout;
    private float timer;

    private EndScreen endScreen;
    private boolean gameOver = false;

    private Hud hud;
    /** Food a goose takes per bite. */
    private static final int BITE_AMOUNT = 10;
    private static final double GOOSE_SPAWN_CHANCE = 0.5f;

    private static final Random RANDOM = new Random();

    /** How the game ended; null while the game is still running. */
    private EndScreen.Result result;

    /** The zone the player has to reach to win, plus what's needed to draw it. */
    private Texture zonePixel;
    private BitmapFont zoneFont;
    private GlyphLayout zoneLayout;

    private int gooseDisposeCounter = 0;

    // Variables for ending the game
    private float minDisposeCount = 0f;
    private  boolean endConditionsMet = false;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // Runs once on start-up
    @Override
    public void create() {
        longBoi = null;
        fox = null;
        long_boi_spawned = false;
        fox_spawned = false;
        gameOver = false;
        result = null;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 600, camera);

        // A 1x1 white texture used to draw the coloured rectangles of the start and goal zones.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        // load the map, and constrict entity movement
        float unitScale = 5f;
        gameMap = new GameMap("park.tmx", unitScale);

        // Goal zone B: a 3x3 tile block in the top-right corner of the map
        Pixmap pixmap1 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap1.setColor(Color.WHITE);
        pixmap1.fill();
        zonePixel = new Texture(pixmap1);
        pixmap1.dispose();

        zoneFont = new BitmapFont();
        zoneFont.getData().setScale(2f);
        zoneLayout = new GlyphLayout();

        int[] mapSize = gameMap.getSizeInTiles();
        gameMap.endZone = new GoalZone(gameMap.getTileBounds(0, 3, 3, 3), "END ZONE", Color.GREEN);

        // The player starts at A, in the centre of the bottom-left tile, and has to reach B.
        gameMap.startZone = new GoalZone(gameMap.getTileBounds(START_TILE_X, START_TILE_Y, 1, 1),
            "START", Color.SKY);

        player = new Player(
            gameMap.startZone.getCentre().x,
            gameMap.startZone.getCentre().y,
            10,
            gameMap
        );

        hud = new Hud();
        endScreen = new EndScreen();

        player.getInventory().addItem(new Airhorn(new Texture("Airhorn.png"),
            Gdx.audio.newSound(Gdx.files.internal("AirhornSound.wav"))));
        player.getInventory().addItem(new Goosedisguise(new Texture("Goosedisguise.png")));

        // Ensure the viewport is correctly sized to the window
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Start the 5-minute countdown timer from the moment the game begins.
        timer = TOTAL_TIME;
        timerFont = new BitmapFont();
        timerFont.getData().setScale(2f);
        timerLayout = new GlyphLayout();
        markerLayout = new GlyphLayout();
    }

    // Per frame behaviour
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if (gameOver) {
            renderGameOver();
            return;
        }

        // ---spawn logic begins---
        if (!long_boi_spawned && timer <= 290f){
            // spawns longboi related to the players position
            Vector2 spawnPos = player.getPos().cpy().add(300,300);
            longBoi = new LongBoi(
                spawnPos.x,
                spawnPos.y,
                3,
                500,
                gameMap
            );
            long_boi_spawned = true;
            System.out.println("Long Boi has spawned!");
        }


        if (timer <= 280f){
            foxPoint = new GoalZone(gameMap.getTileBounds(FOX_TILE_X, FOX_TILE_Y, 1, 1),
                "Den", Color.GREEN);
        }

        if (foxPoint != null && !foxPoint.isReached() && foxPoint.isOccupiedBy(player)) {
            foxPoint.setReached(true);
            if  (!fox_spawned) {
                // spawns fox related to the players position
                Vector2 spawnPos = player.getPos();
                fox = new Fox(
                    spawnPos.x,
                    spawnPos.y,
                    3,
                    500,
                    gameMap
                );
                fox_spawned = true;
                System.out.println("fox has spawned!");
            }
        }
        // ---spawn logic ends---

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
        if (longBoi != null) {
            if (longBoi.getStatus() != Status.SCARED && !player.getDisguised() && player.getHitbox().overlaps(longBoi.getHitbox())) {
                player.loseFood(BITE_AMOUNT);

            }

            if (fox != null && longBoi.getHitbox().overlaps(fox.getHitbox())) {
                System.out.println("fox hit longBoi");
            }
        }

        // Use items
        player.useItem();

        // Tick timed item effects once per frame
        // Fixed: gameMap and player are now passed because items need them to end their effects
        player.getInventory().update(Gdx.graphics.getDeltaTime(), gameMap, player);

        // Uses a percentage chance to spawn a goose within the map
        if (RANDOM.nextDouble() < (Gdx.graphics.getDeltaTime() * GOOSE_SPAWN_CHANCE)) {
            gameMap.spawnGoose(camera.position);
        }

        //----------batch begins----------
        batch.begin();
        player.movePlayer();
        // Draw the start and goal markers beneath the entities so the player is always visible.
        gameMap.startZone.draw(batch, pixel, timerFont, markerLayout);

        if(foxPoint != null){
            foxPoint.draw(batch, pixel, timerFont, markerLayout);
        }

        Iterator<Goose> it = gameMap.getGeese().iterator();
        while(it.hasNext()){
            Goose goose = it.next();
            if(goose.getStatus() == Status.SCARED && player.getPos().dst2(goose.getPos()) > 250000f){
                goose.dispose();
                it.remove();
                gooseDisposeCounter ++ ;
            } else{
                goose.updateStatus(player);
                goose.moveNPC();
                goose.draw(batch);
            }
        }

        if (longBoi != null) {
            longBoi.updateStatus(player);
            longBoi.moveNPC();
            longBoi.draw(batch);
        }

        if (fox != null) {
            AbstractEntity target = (longBoi != null ? longBoi : player);
            if (target instanceof LongBoi){
                longBoi.scare();
                longBoi.dispose();
                longBoi = null;
            }
            fox.updateStatus(target);
            fox.moveNPC();
            fox.draw(batch);
        }

        if(endConditionsMet){
            gameMap.endZone.draw(batch, zonePixel, zoneFont, markerLayout);
        }

        player.draw(batch);
        batch.end();
        //----------batch end----------

        //----------HUD: 5-minute countdown timer in the top-right corner----------
        hud.draw(batch, viewport, player);
        timer -= Gdx.graphics.getDeltaTime();
        if (gameMap.endZone != null && gameMap.endZone.isReached()) {
            endScreen.draw(batch, viewport, EndScreen.Result.REACHED_GOAL, TOTAL_TIME - timer);
        }
        if (gameMap.endZone != null && !gameMap.endZone.isReached()) {
            timer -= Gdx.graphics.getDeltaTime();
            if (timer < 0f) {
                timer = 0f;
            }
        }

        int minutes = (int) (timer / 60);
        int seconds = (int) (timer % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerLayout.setText(timerFont, timeText);

        // Switch to screen coordinates so the timer stays fixed in the corner
        // of the window regardless of where the camera is.
        batch.getProjectionMatrix().setToOrtho2D(0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //----------hud batch begins - has no draw call----------
        batch.begin();

        float padding = 20f;
        timerFont.draw(batch, timeText,
            Gdx.graphics.getWidth() - timerLayout.width - padding,
            Gdx.graphics.getHeight() - padding);

        if (gameMap.endZone != null && gameMap.endZone.isReached()) {
            String goalText = "Goal B reached!";
            markerLayout.setText(timerFont, goalText);
            timerFont.draw(batch, markerLayout,
                (Gdx.graphics.getWidth() - markerLayout.width) / 2f,
                Gdx.graphics.getHeight() / 2f);
        }
        batch.end();
        //----------hud batch ends----------

        if (player.isOutOfFood()) {
            gameOver = true;
            result = EndScreen.Result.LOST;

        } else if (gameMap.endZone.isOccupiedBy(player) && endConditionsMet) {
            gameMap.endZone.setReached(true);
            gameOver = true;
            result = EndScreen.Result.REACHED_GOAL;

        } else if (timer <= 0f) {
            gameOver = true;
            result = EndScreen.Result.SURVIVED;
        }

        //-------------------------Check if conditions for end of game have been met-------------------------
        if(gooseDisposeCounter >= minDisposeCount){
            endConditionsMet = true;
        }
    }

    /** Draws the frozen world with the end screen on top, and listens for restart/quit. */
    private void renderGameOver() {
        camera.position.set(player.getPos(), 0);
        camera.update();
        gameMap.setViewAndRender(camera);
        batch.setProjectionMatrix(camera.combined);

        // Draw entities without updating them, so everything stays frozen
        batch.begin();
        for (Goose goose : gameMap.getGeese()) {
            goose.draw(batch);
        }
        if (longBoi != null) longBoi.draw(batch);
        if (fox != null) fox.draw(batch);
        player.draw(batch);
        batch.end();

        hud.draw(batch, viewport, player);
        endScreen.draw(batch, viewport, result, TOTAL_TIME - timer);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restart();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    /** Frees everything and starts a fresh game. */
    private void restart() {
        dispose();
        create();
    }

    // Runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        timerFont.dispose();
        pixel.dispose();
        gameMap.dispose();
        player.getInventory().dispose();
        hud.dispose();
        endScreen.dispose();
        zonePixel.dispose();
        zoneFont.dispose();

        for (Goose goose: gameMap.getGeese()){
            goose.dispose();
        }
    }
}
