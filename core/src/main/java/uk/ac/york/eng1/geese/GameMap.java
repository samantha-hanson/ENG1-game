package uk.ac.york.eng1.geese;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.w3c.dom.Text;
import uk.ac.york.eng1.geese.entities.GoalZone;
import uk.ac.york.eng1.geese.entities.NPC.Goose;

import java.util.*;

/**
 * Class used for managing the map state including LibGDX Map and rendering objects, regions of interest,
 * and entity movement boundaries.
 */
public class GameMap {
    private static final Random RANDOM = new Random();

    final private float mapBoundLeft;
    final private float mapBoundRight;
    final private float mapBoundRoof;
    final private float mapBoundFloor;

    final private int mapWidthInTiles;
    final private int mapHeightInTiles;
    final private float tileWidthInWorld;
    final private float tileHeightInWorld;

    private TiledMap tileMap;
    private MapProperties mapProps;
    private OrthogonalTiledMapRenderer renderer;

    // variables for spawning geese and managing geese
    private static final double SPAWN_RADIUS = 500;
    /** All geese currently on the map. Lets items such as the Airhorn reach them through the GameMap. */
    private final List<Goose> geese = new ArrayList<>();

    // variables for managing zones of interest
    private final Map<String, GoalZone> zones = new HashMap<String, GoalZone>(); // Store based on label
    public GoalZone startZone;
    public GoalZone endZone;

    /**
     * Creates a GameMap object using the txm object and LIBGDX's TiledMap class. Initialises the world
     * boundaries according to the loaded TiledMap and passed unitScale.
     *
     * @param txm_path Path to the txm asset representing the map.
     * @param unitScale The scale factor used to control map and render size.
     */
    public GameMap(String txm_path, float unitScale){
        this.tileMap = new TmxMapLoader().load(txm_path);
        this.mapProps = this.tileMap.getProperties();
        this.renderer = new OrthogonalTiledMapRenderer(this.tileMap, unitScale);

        // Store the map dimensions in tiles and in world units so callers can convert tile coordinates.
        this.mapWidthInTiles = this.mapProps.get("width", Integer.class);
        this.mapHeightInTiles = this.mapProps.get("height", Integer.class);
        this.tileWidthInWorld = this.mapProps.get("tilewidth", Integer.class) * unitScale;
        this.tileHeightInWorld = this.mapProps.get("tileheight", Integer.class) * unitScale;

        // Set world boundaries using loaded map.
        // Left and Floor are always 0.
        this.mapBoundLeft = 0f;
        this.mapBoundRight = this.mapWidthInTiles * this.tileWidthInWorld;
        this.mapBoundFloor = 0;
        this.mapBoundRoof = this.mapHeightInTiles * this.tileHeightInWorld;
    }

    /**
     * Update the renderer's view to the passed camera and then render the map.
     * Should be called once per frame in the Main render() loop.
     *
     * @param camera The camera representing the window viewport. Renders map to this camera view.
     */
    public void setViewAndRender(OrthographicCamera camera){
        this.renderer.setView(camera);
        this.renderer.render();
    }

    /**
     * Clamps the passed vector according to the world bounds. Changes are made in-place and modifies the passed vector.
     *
     * @param pos The position vector to be bound by the world.
     */
    public void clampPosition(Vector2 pos){
        pos.x = MathUtils.clamp(pos.x, this.mapBoundLeft, this.mapBoundRight);
        pos.y = MathUtils.clamp(pos.y, this.mapBoundFloor, this.mapBoundRoof);
    }

    /**
     * Converts a block of tiles into the world-space rectangle it covers.
     *
     * <p>
     * Tile rows are passed top-down, matching the Tiled editor, while world y coordinates grow upwards.
     * </p>
     *
     * @param tileX     The left-most tile column of the block.
     * @param tileY     The top-most tile row of the block, counted from the top of the map.
     * @param tilesWide The width of the block in tiles.
     * @param tilesTall The height of the block in tiles.
     * @return The world-space rectangle covered by the block, with its origin at the bottom-left corner.
     */
    public Rectangle getTileBounds(int tileX, int tileY, int tilesWide, int tilesTall){
        float x = tileX * this.tileWidthInWorld;
        float y = (this.mapHeightInTiles - tileY - tilesTall) * this.tileHeightInWorld;
        return new Rectangle(x, y, tilesWide * this.tileWidthInWorld, tilesTall * this.tileHeightInWorld);
    }

    /**
     * Fetch the map dimensions in tiles.
     * @return the map width as the first element and the map height as the second element.
     */
    public int[] getSizeInTiles(){
        return new int[]{this.mapWidthInTiles, this.mapHeightInTiles};
    }

    /**
     * Fetch the size of a single tile in world units.
     * @return the tile width as the first element and the tile height as the second element.
     */
    public float[] getTileSizeInWorld() {
        return new float[]{this.tileWidthInWorld, this.tileHeightInWorld};
    }

    /**
     * Adds a goose to the map so that it can be found through getGeese.
     *
     * @param goose The goose to add to the map.
     */
    public void addGoose(Goose goose){
        this.geese.add(goose);
    }

    /**
     * uses random and angles to produce a random distance from the player at which the goose is spawned
     * and added to stored entities.
     *
     * @param origin the center point around which a goose is spawned
     */
    public void spawnGoose(Vector2 origin){
        double angle = RANDOM.nextDouble(2 * Math.PI);
        double offsetX = SPAWN_RADIUS * Math.sin(angle);
        double offsetY = SPAWN_RADIUS * Math.cos(angle);

        float posX = (float) (origin.x + offsetX);
        float posY = (float) (origin.y + offsetY);

        this.addGoose(
            new Goose(posX, posY, 5, 300, this)
        );
    }

    /**
     * uses random and angles to produce a random distance from the player at which the goose is spawned
     * and added to stored entities.
     *
     * @param origin the center point around which a goose is spawned
     */
    public void spawnGoose(Vector3 origin){
        double angle = RANDOM.nextDouble(2 * Math.PI);
        double offsetX = SPAWN_RADIUS * Math.sin(angle);
        double offsetY = SPAWN_RADIUS * Math.cos(angle);

        this.addGoose(new Goose(
            (float) (origin.x + offsetX),
            (float) (origin.y + offsetY),
            2,
            300,
            this)
        );
    }

    /**
     * Fetch all geese currently on the map.
     *
     * @return A read-only copy of the list of geese. The Goose objects inside are the real ones, not copies.
     */
    public List<Goose> getGeese(){
        return this.geese;
    }

    // ====================zone methods ====================

    public void drawZones(SpriteBatch batch, Texture pixel, BitmapFont font, GlyphLayout layout){
        if(this.startZone.isDrawable){
            this.startZone.draw(batch, pixel, font, layout);
        }
        if(this.endZone.isDrawable){
            this.endZone.draw(batch, pixel, font, layout);
        }

        for(Map.Entry<String, GoalZone> entry : this.zones.entrySet()){
            GoalZone zone = entry.getValue();
            if(zone.isDrawable){
                zone.draw(batch, pixel, font, layout);
            }
        }
    }

    /**
     * Adds GoalZone zone to the hashmap, using label as the key.
     * @param zone GoalZone to be added to stored regions.
     */
    public void addZone(GoalZone zone){
        this.zones.put(zone.getLabel(), zone);
    }

    /**
     * fetches zone from the zones hashmap.
     * @param label String key used to fetch the zone
     * @return GoalZone fetched by map, null if key not found.
     */
    public GoalZone getZone(String label){
        return this.zones.get(label);
    }

    /**
     * removes GoalZone stored by label from zones hashmap.
     * @param label String key used to remove zone from hashmap.
     */
    public void removeZone(String label){
        this.zones.remove(label);
    }

    /**
     * Used to free the occupied memory of the stored tileMap and renderer once they are no longer needed.
     * Should be called in the dispose method in Main.
     */
    public void dispose(){
        this.tileMap.dispose();
        this.renderer.dispose();
    }
}
