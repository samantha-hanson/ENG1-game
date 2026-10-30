package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;

public class Goosedisguise extends AbstractItems {

    private Texture originalsprite;

    public Goosedisguise(Texture sprite) {
        super("Disguise",
            "Use it to sneak past geese",
            sprite,
            2,
            10f);
    }

    @Override
    protected void applyEffect(GameMap gameMap, Player player) {
        player.setDisguised(true);
        originalsprite = player.getSprite();    //Storing original sprite
        player.setSprite(new Texture("GooseDisguise.png")); //Changing current sprite

    }

    @Override
    protected void endEffect(GameMap gameMap, Player player) {
        player.setDisguised(false);
        player.setSprite(originalsprite);
    }
}
