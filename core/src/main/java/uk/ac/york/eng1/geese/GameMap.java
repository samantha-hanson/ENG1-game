package uk.ac.york.eng1.geese;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
     * Used to free the occupied memory of the stored tileMap and renderer once they are no longer needed.
     * Should be called in the dispose method in Main.
     */
    public void dispose(){
        this.tileMap.dispose();
        this.renderer.dispose();
    }
}
