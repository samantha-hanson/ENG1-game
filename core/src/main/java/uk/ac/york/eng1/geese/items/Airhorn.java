package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;

public class Airhorn extends AbstractItems {

    /**
     * Constructor to get each item to set up their corresponding values
     *
     * @param sprite         - How the item looks like
     */
    public Airhorn(Texture sprite) {
        super("Airhorn",
            "Blast it to scare nearby geese away ",
            sprite,
            3,
            2f);
    }

    @Override
    protected void applyEffect(GameMap gameMap, Player player) {

    }

    @Override
    protected void endEffect(GameMap gameMap, Player player) {

    }

    public static void main(String[] args) {
    }
}
