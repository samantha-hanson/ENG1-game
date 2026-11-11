package uk.ac.york.eng1.geese.items;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;
import uk.ac.york.eng1.geese.stats.GameStats;

/**
 * Represents the standard for defining future items
 *
 * <p>
 *     Holds the common attributes and methods shared between all items in the game
 * </p>
 */

public abstract class AbstractItems {
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
     * How long the Item's effect will last once used (seconds)
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

    /**
     * Constructor to get each item to set up their corresponding values
     * @param name - Item name
     * @param description - What the item does
     * @param sprite - What the item looks like
     * @param uses - How many times the item can be used
     * @param effectDuration - How long the effect lasts once used (seconds)
     */
    protected AbstractItems(String name, String description, Texture sprite, int uses, float effectDuration){
        this.name = name;
        this.description = description;
        this.sprite = sprite;
        this.usesRemaining = uses;
        this.effectDuration = effectDuration;
    }

    // --------------- Methods ----------------
    /**
     *Method that actually activates the item to be used
     * @param gameMap Gives the item access to the world to interact with the geese
     * @param player The player using the item
     * @return true if the item was used, false if it couldn't be
     */
    public boolean use(GameMap gameMap,Player player){
        if(canUse()) {
            --usesRemaining;
            status = Item_Status.ACTIVE;
            effectTimeLeft = effectDuration;
            applyEffect(gameMap,player);
            return true;
        } else return false;
    }

    /**
     * Counts down the active effect and ends it when time runs out.
     * Call once per frame.
     * @param delta Time in seconds since the last frame, used to count down effectTimeLeft
     * @param gameMap Gives the item access to the world so endEffect can undo changes made to it
     * @param player The player who used the item, passed to endEffect so it can undo changes made to them
     */
    public void update(float delta, GameMap gameMap, Player player){
        if(!isActive()) return;
        effectTimeLeft -= delta;
        if(effectTimeLeft <= 0) {
            status = Item_Status.INACTIVE;
            endEffect(gameMap, player);
        }
    }

    /**
     * Applies this item's effect when it is used.
     * Will be overridden by each item subclass
     *
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     */
    protected abstract void applyEffect(GameMap gameMap, Player player);

    /**
     * Reverses the item's effect when it ends.
     * Will be overridden by each item subclass
     *
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     */
    protected abstract void endEffect(GameMap gameMap, Player player);

    /**
     * Method that checks whether the item can be used
     * Checks if the item still has uses left and if the item is currently not active
     * @return true if the item can still be used and is currently INACTIVE
     */
    public boolean canUse(){
        return !isActive() && !isUsedUp();
    }

    /**
     * Checks if the item is currently active
     * This is done by checking the item's status
     * @return true if the status is ACTIVE
     */
    public boolean isActive(){
        //True if the getStatus() is ACTIVE
        return getStatus() == Item_Status.ACTIVE;
    }

    /**
     * Checks if the item has no uses remaining
     * @return true if the usesRemaining are 0 or less
     */
    public boolean isUsedUp(){
        return this.usesRemaining <= 0;

    }

    /**
     * Getter to check the current status of the item
     * @return The item's current status
     */
    public Item_Status getStatus(){
        return this.status;
    }

    /**
     * Getter to display the item's name
     * @return The name as a String
     */
    public String getName(){
        return this.name;
    }

    /**
     * Getter to display the item's description
     * @return The description
     */
    public String getDescription(){
        return this.description;
    }

    /**
     *Gets the sprite icon to display in the inventory
     * @return The sprite into the inventory
     */
    public Texture getIcon(){
        return this.sprite;
    }

    /**
     * Frees the memory used by the item's texture. Called by Inventory.dispose().
     */
    public void dispose(){
        if(this.sprite != null) {
            this.sprite.dispose();
        }
    }

    /**
     * Getter to show how many uses left in item
     *
     * @return Number of uses remaining for the item
     */
    public int getUsesRemaining() {
        return this.usesRemaining;
    }


}
