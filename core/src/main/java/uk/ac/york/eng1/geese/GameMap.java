package uk.ac.york.eng1.geese;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.NPC.Goose;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for managing the map state including LibGDX Map and rendering objects, regions of interest,
 * and entity movement boundaries.
 */
public class GameMap {

    final private float mapBoundLeft;
    final private float mapBoundRight;
    final private float mapBoundRoof;
    final private float mapBoundFloor;

    private TiledMap tileMap;
    private MapProperties mapProps;
    private OrthogonalTiledMapRenderer renderer;

    /**
     * All geese currently on the map. Lets items such as the Airhorn reach them through the GameMap.
     */
    private final List<Goose> geese = new ArrayList<>();

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


        // Set world boundaries using loaded map.
        // Left and Floor are always 0.
        this.mapBoundLeft = 0f;
        this.mapBoundRight = this.mapProps.get("width", Integer.class) * mapProps.get("tilewidth", Integer.class) * unitScale;
        this.mapBoundFloor = 0;
        this.mapBoundRoof = this.mapProps.get("height", Integer.class) * mapProps.get("tileheight", Integer.class) * unitScale;
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
     * Adds a goose to the map so that it can be found through getGeese.
     *
     * @param goose The goose to add to the map.
     */
    public void addGoose(Goose goose){
        this.geese.add(goose);
    }

    /**
     * Fetch all geese currently on the map.
     *
     * @return A read-only copy of the list of geese. The Goose objects inside are the real ones, not copies.
     */
    public List<Goose> getGeese(){
        return List.copyOf(this.geese);
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
