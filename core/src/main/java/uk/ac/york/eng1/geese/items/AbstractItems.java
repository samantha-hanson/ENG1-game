package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.Player;

public abstract class AbstractItems extends Player {
    /**
     * The item's name
     */
    protected String name;

    /**
     * The item's description, containing its effect and use
     */
    protected String description;

    /**
     * The image displayed on the inventory to represent the item
     */
    protected Texture sprite;

    /**
     * How many times the item can be used before it's used up
     */
    protected int usesRemaining;

    /**
     * How long the Item's effect will last
     */
    protected float effectDuration;

    /**
     * Time remaining for the current item to stay in use
     */
    protected float effectTimeLeft;

    /**
     * Current state of the Item. (Default state of the Item is set to Inactive)
     */
    protected Status status = Status.INACTIVE;



}
