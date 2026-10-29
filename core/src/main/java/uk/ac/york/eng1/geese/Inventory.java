package uk.ac.york.eng1.geese.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public <T extends AbstractItems> boolean useItem(Class<T> type, Player player) {
        Optional<T> found = getItem(type);
        found.ifPresent(item -> item.use(player));
        return found.isPresent();
    }

    /** Ticks all held items so timed effects can expire. Call once per frame from Main. */
    public void update(float delta) {
        for (AbstractItems item : this.items) {
            item.update(delta);
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
