package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.Player;

public abstract class AbstractItems extends AbstractEntity {
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
    protected Item_Status status = Item_Status.INACTIVE;

    // --------------- Methods ----------------
    /**
     *
     * @param gameMap Gives the item access to the world to interact with the geese
     * @param player Tells the item to check the current player
     */
    public boolean use(GameMap gameMap,Player player){
        if(canUse()) {
            --usesRemaining;
            applyEffect(gameMap,player);
            return true;
        } else return false;
    }

    /**
     * Applies this item's effect when it is used.
     * Will be overwritten by each item subclass
     */
    protected abstract void applyEffect(GameMap gameMap, Player player);

    /**
     * Reverses the item's effect when it ends.
     * Will be overwritten by each item subclass
     */
    protected abstract void endEffect(GameMap gameMap, Player player);

    /**
     * Method that checks whether the item can be used
     * Checks if the item still has uses left and if the item is currently active
     */
    public boolean canUse(){
        return !isActive() && !isUsedUp();
    }

    /**
     * Checks if the item is currently active
     * This is done by checking the item's status
     */
    public boolean isActive(){
        return getStatus() == Item_Status.ACTIVE;
    }

    /**
     * Checks if the item has any uses remaining
     */
    public boolean isUsedUp(){
        return this.usesRemaining <= 0;

    }

    /**
     * Getter to check the current status of the item
     */
    public Item_Status getStatus(){
        return this.status;
    }

    public String getName(){
        return this.name;
    }

    /**
     *Checks the sprite icon to display in the inventory
     */
    public Texture getIcon(){
        return this.sprite;
    }



}
