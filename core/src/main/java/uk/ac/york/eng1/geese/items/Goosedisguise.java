package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;

/**
 * Represents the goose disguise item
 *
 * <p>
 *     Extends abstract items by changing the way the player appears as well as how the geese target the player.
 *     Once used it will change the player sprite to a disguised one, and once the effect ends the sprite reverts back to normal
 * </p>
 */

public class Goosedisguise extends AbstractItems {

    /**
     * Original Sprite of the player will be stored in this field
     */
    private Texture originalSprite;

    private float originalVel;

    /**
     * Constructor to set up the initial values of the goose disguise
     * @param sprite How the disguise looks like
     */
    public Goosedisguise(Texture sprite) {
        super("Disguise",
            "Use it to sneak past geese",
            sprite,
            2,
            10f);
    }

    /**
     *
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     */
    @Override
    protected void applyEffect(GameMap gameMap, Player player) {
        player.setDisguised(true);
        //Changing the player appearance
        originalSprite = player.getSprite();    //Storing original sprite
        player.setSprite(this.sprite); //Changing current sprite
        originalVel = player.getVel();  //Storing original Velocity
        player.setVel(5);   //Changing current velocity
    }

    @Override
    protected void endEffect(GameMap gameMap, Player player) {
        player.setDisguised(false);
        //Changing the sprite design back to the original one
        player.setSprite(originalSprite);
        //Changing Velocity back to the original value
        player.setVel(originalVel);
    }
}
