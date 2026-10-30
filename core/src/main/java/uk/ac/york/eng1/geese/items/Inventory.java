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

    public boolean addItem(AbstractItems item) {
        if (item == null) {
            return false;
        }
        this.items.add(item);
        return true;
    }

    public boolean removeItem(AbstractItems item) {
        return this.items.remove(item);
    }

    public <T extends AbstractItems> boolean hasItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    public <T extends AbstractItems> Optional<T> getItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return Optional.of(type.cast(item));
            }
        }
        return Optional.empty();
    }

    /**
     * Uses the first held item of the given type.
     *
     * <p>
     * Fixed: this used to call item.use(player), but AbstractItems.use needs the GameMap as well.
     * gameMap was added as a parameter because Inventory does not store it, and items need it to
     * reach the geese.
     * </p>
     *
     * @param type The class of the item to use, such as Airhorn.class.
     * @param gameMap Passed to the item so its effect can reach the geese.
     * @param player The player using the item.
     * @return true if an item of that type was found, even if it could not be used.
     */
    public <T extends AbstractItems> boolean useItem(Class<T> type, GameMap gameMap, Player player) {
        Optional<T> found = getItem(type);
        found.ifPresent(item -> item.use(gameMap, player));
        return found.isPresent();
    }

    /**
     * Ticks all held items so timed effects can expire. Call once per frame from Main.
     *
     * <p>
     * Fixed: this used to call item.update(delta), but AbstractItems.update also needs the GameMap
     * and Player. An item passes them to endEffect when its time runs out, so that it can undo its effect.
     * </p>
     *
     * @param delta Time in seconds since the last frame.
     * @param gameMap Passed to each item so endEffect can reach the geese.
     * @param player Passed to each item so endEffect can undo changes made to the player.
     */
    public void update(float delta, GameMap gameMap, Player player) {
        for (AbstractItems item : this.items) {
            item.update(delta, gameMap, player);
        }
    }

    public List<AbstractItems> getItems() {
        return List.copyOf(this.items);
    }

    public void clear() {
        this.items.clear();
    }

    public int size() {
        return this.items.size();
    }

    /** Free all item textures. Call from Main.dispose(). */
    public void dispose() {
        for (AbstractItems item : this.items) {
            item.dispose();
        }
        this.items.clear();
    }
}
