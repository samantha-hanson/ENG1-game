package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.Player;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapProperties;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;
    private Goose goose1;
    private Goose goose2;

    private OrthographicCamera camera;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapProperties mapProps;

    //runs once on start-up
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        //load the map, and constrict entity movement
        map = new TmxMapLoader().load("mapdummy.tmx");
        mapProps = map.getProperties();

        float unitScale = 5f;
        float right = mapProps.get("width", Integer.class) * mapProps.get("tilewidth", Integer.class) * unitScale;
        float roof = mapProps.get("height", Integer.class) * mapProps.get("tileheight", Integer.class) * unitScale;

        AbstractEntity.setWorldBounds(0, right, roof, 0);//left and floor are always 0
        mapRenderer = new OrthogonalTiledMapRenderer(this.map, unitScale);

        player = new Player(
            100,
            100,
            10
        );

        goose1 = new Goose(
            300,
            300,
            2,
            300
        );

        goose2 = new Goose(
            400,
            400,
            2,
            300
        );
    }

    //per frame behaviour
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //load map and set up camera
        camera.position.set(player.getPos(), 0);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        //----------batch begins----------
        batch.begin();
        player.movePlayer();

        goose1.updateStatus(player);
        goose1.moveNPC();

        goose2.updateStatus(player);
        goose2.moveNPC();

        player.draw(batch);
        goose1.draw(batch);
        goose2.draw(batch);

        batch.end();
        //----------batch ends----------
    }
    //runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
