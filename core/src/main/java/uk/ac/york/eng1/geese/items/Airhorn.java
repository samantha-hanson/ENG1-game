package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.Player;
import com.badlogic.gdx.audio.Sound;

/**
 * Represents the airhorn item, storing information such as the Scare radius for the item and its
 * interactions with the geese
 *
 * <p>
 *     Extends abstract items by adding a way for the item to interact with the world and the geese
 * </p>
 */

public class Airhorn extends AbstractItems {

    /**
     * How close a goose must be to the player to be scared by the Airhorn
     */
    private static final float SCARE_RADIUS = 300f;

    /**
     * Sound to be played when the airhorn is used
     */
    private final Sound sound;

    /**
     * <p>
     * Creating an Airhorn object
     * Airhorn takes in a sprite parameter to easily allow changing the png later on
     * </p>
     * @param sprite - How the item looks like
     * @param sound - Sound played when the airhorn goes off
     */
    public Airhorn(Texture sprite, Sound sound) {
        super("Airhorn",
            "Blast it to scare nearby geese away ",
            sprite,
            20,
            2f);
        this.sound = sound;
    }

    /**
     * Scares every goose within SCARE_RADIUS of the player and plays an airhorn sound
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     */
    @Override
    protected void applyEffect(GameMap gameMap, Player player) {
        sound.play(0.1f);
        for (Goose goose : gameMap.getGeese()) {
            if (goose.getPos().dst(player.getPos()) <= SCARE_RADIUS) {
                goose.scare();
            }
        }
    }

    /**
     * Calms every scared goose once the Airhorn's effect runs out.
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     */
    @Override
    protected void endEffect(GameMap gameMap, Player player) {
        for (Goose goose : gameMap.getGeese()) {
            goose.calm();
        }
    }
    /**
     * Frees the Airhorn's texture and sound.
     */
    @Override
    public void dispose() {
        super.dispose();
        sound.dispose();
    }
}
