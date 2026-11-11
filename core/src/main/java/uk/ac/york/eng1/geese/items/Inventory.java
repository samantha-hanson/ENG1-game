package uk.ac.york.eng1.geese.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;

/**
 * Holds the items the player is carrying.
 *
 * <p>
 * Owned by Player via composition. Sits outside the AbstractEntity hierarchy — it has no
 * position, sprite or hitbox of its own. No capacity limit is applied: with only two items
 * in the game, a cap would be dead logic.
 * </p>
 */
public class Inventory {

    private final List<AbstractItems> items = new ArrayList<>();

    /**
     * Adds an item to the inventory and rejects a null value
     * @param item Specific item to be added to the inventory
     * @return true if the item is successfully added
     */
    public boolean addItem(AbstractItems item) {
        if (item == null) {
            return false;
        }
        this.items.add(item);
        return true;
    }

    /**
     * Removes this exact item instance
     * @param item What is to be removed from the inventory
     * @return true if the item was present in the inventory
     */
    public boolean removeItem(AbstractItems item) {
        return this.items.remove(item);
    }

    /**
     *
     * @param type
     * @return
     * @param <T>
     */
    public <T extends AbstractItems> boolean hasItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param type
     * @return
     * @param <T>
     */
    public <T extends AbstractItems> Optional<T> getItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return Optional.of(type.cast(item));
            }
        }
        return Optional.empty();
    }

    /**
     * Counter for how many items have been used
     */
    private int itemsUsed = 0;

    /**
     * Uses the first held item of the given type.
     *
     * @param type The class of the item to use, such as Airhorn.class.
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     * @return true if an item of that type was found, even if it could not be used.
     */
    public <T extends AbstractItems> boolean useItem(Class<T> type, GameMap gameMap, Player player) {
        Optional<T> found = getItem(type);
        if (found.isPresent() && found.get().use(gameMap, player)) {
            this.itemsUsed++;
        }
        return found.isPresent();
    }

    /**
     * Returns how many item activations have succeeded.
     */
    public int getItemsUsed() {
        return this.itemsUsed;
    }

    /**
     * Ticks every held item's timed effect; call once per frame.
     * @param delta Time in seconds since the last frame.
     * @param gameMap Passed to each item so endEffect can reach the geese.
     * @param player Passed to each item so endEffect can undo changes made to the player
     */
    public void update(float delta, GameMap gameMap, Player player) {
        for (AbstractItems item : this.items) {
            item.update(delta, gameMap, player);
        }
    }

    /**
     * Returns a read-only copy of the item list (item instances are the live ones).
     */
    public List<AbstractItems> getItems() {
        return List.copyOf(this.items);
    }

    /**
     *Empties the inventory without disposing textures; use dispose() at shutdown.
     */
    public void clear() {
        this.items.clear();
    }

    /**
     * Returns the number of items currently held.
     */
    public int size() {
        return this.items.size();
    }

    /**
     Disposes every item's resources and empties the inventory; call once at shutdown.
     */
    public void dispose() {
        for (AbstractItems item : this.items) {
            item.dispose();
        }
        this.items.clear();
    }
}
