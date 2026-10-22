package uk.ac.york.eng1.geese;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Map {

    final private float mapBoundLeft;
    final private float mapBoundRight;
    final private float mapBoundRoof;
    final private float mapBoundFloor;

    private TiledMap tileMap;
    private MapProperties mapProps;
    private MapRenderer renderer;

    public Map(String txm_path, float unitScale){
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

    public void SetViewAndRender(OrthographicCamera camera){
        this.renderer.setView(camera);
        this.renderer.render();
    }

    public void clampPosition(Vector2 pos){
        pos.x = MathUtils.clamp(pos.x, this.mapBoundLeft, this.mapBoundRight);
        pos.y = MathUtils.clamp(pos.y, this.mapBoundFloor, this.mapBoundRoof);
    }
}
