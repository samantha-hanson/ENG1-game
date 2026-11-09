package uk.ac.york.eng1.geese.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.Player;

/** Holds the player's items. Owned by Player; not an AbstractEntity (no position/sprite/hitbox). */
public class Inventory {

    private final List<AbstractItems> items = new ArrayList<>();

    /** Adds an item; rejects null. Returns true if added. */
    public boolean addItem(AbstractItems item) {
        if (item == null) {
            return false;
        }
        this.items.add(item);
        return true;
    }

    /** Removes this exact item instance. Returns true if it was present. */
    public boolean removeItem(AbstractItems item) {
        return this.items.remove(item);
    }

    /** Returns true if any held item is an instance of {@code type}. */
    public <T extends AbstractItems> boolean hasItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the first held item of {@code type}, or empty if none. */
    public <T extends AbstractItems> Optional<T> getItem(Class<T> type) {
        for (AbstractItems item : this.items) {
            if (type.isInstance(item)) {
                return Optional.of(type.cast(item));
            }
        }
        return Optional.empty();
    }

    /** Count of successful item activations, for future scoring. */
    private int itemsUsed = 0;

    /** Uses the first held item of {@code type}; returns true if one was found (not necessarily used). */
    public <T extends AbstractItems> boolean useItem(Class<T> type, GameMap gameMap, Player player) {
        Optional<T> found = getItem(type);
        if (found.isPresent() && found.get().use(gameMap, player)) {
            this.itemsUsed++;
        }
        return found.isPresent();
    }

    /** Returns how many item activations have succeeded. */
    public int getItemsUsed() {
        return this.itemsUsed;
    }

    /** Ticks every held item's timed effect; call once per frame. */
    public void update(float delta, GameMap gameMap, Player player) {
        for (AbstractItems item : this.items) {
            item.update(delta, gameMap, player);
        }
    }

    /** Returns a read-only copy of the item list (item instances are the live ones). */
    public List<AbstractItems> getItems() {

        return List.copyOf(this.items);
    }

    /** Empties the inventory without disposing textures; use dispose() at shutdown. */
    public void clear() {
        this.items.clear();
    }

    /** Returns the number of items currently held. */
    public int size() {
        return this.items.size();
    }

    /** Disposes every item's texture and empties the inventory; call once at shutdown. */
    public void dispose() {
        for (AbstractItems item : this.items) {
            item.dispose();
        }
        this.items.clear();
    }
}
