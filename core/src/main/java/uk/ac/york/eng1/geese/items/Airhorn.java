package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.NPC.Goose;
import uk.ac.york.eng1.geese.entities.Player;

public class Airhorn extends AbstractItems {

    /**
     * How close a goose must be to the player to be scared by the Airhorn
     */
    private static final float SCARE_RADIUS = 300f;

    /**
     * Constructor to get each item to set up their corresponding values
     *
     * Airhorn takes in a sprite parameter to easily allow changing the png later on
     * @param sprite - How the item looks like
     */
    public Airhorn(Texture sprite) {
        super("Airhorn",
            "Blast it to scare nearby geese away ",
            sprite,
            3,
            2f);
    }

    /**
     * Scares every goose within SCARE_RADIUS of the player.
     */
    @Override
    protected void applyEffect(GameMap gameMap, Player player) {
        for (Goose goose : gameMap.getGeese()) {
            if (goose.getPos().dst(player.getPos()) <= SCARE_RADIUS) {
                goose.scare();
            }
        }
    }

    /**
     * Calms every scared goose once the Airhorn's effect runs out.
     */
    @Override
    protected void endEffect(GameMap gameMap, Player player) {
        for (Goose goose : gameMap.getGeese()) {
            goose.calm();
        }
    }

}
