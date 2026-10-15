package uk.ac.york.eng1.geese;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import uk.ac.york.eng1.geese.entities.Player;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private Player player;
    //runs once on start-up
    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(
            100,
            100,
            10,
            "playerdummy.png"
        );
    }

    //per frame behaviour
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        player.movePlayer();
        player.draw(batch);

        batch.end();
    }

    //runs once at the end of the program
    @Override
    public void dispose() {
        batch.dispose();
    }
}
