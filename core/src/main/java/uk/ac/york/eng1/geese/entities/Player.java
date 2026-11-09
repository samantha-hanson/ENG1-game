package uk.ac.york.eng1.geese.entities;
import uk.ac.york.eng1.geese.items.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;

/**
 * represents the player character, storing necessary information such as the current position of the player, the inventory,
 * the disguise and food being held by the player
 * <p>
 * extends AbstractEntity by adding a way for the player to move the character in the movePlayer method.
 * </p>
 */
public class Player extends AbstractEntity {

    /**
     * Create a player object at given position. It initialises sprite attribute to the player png and hitbox to sprite size.
     *
     * @param startX initial x-coordinate of the player.
     * @param startY initial y-coordinate of the player.
     * @param vel    velocity the player entity can move.
     * @param gameMap represents the gameMap the Player entity can traverse and interact with
     */
    public Player(float startX, float startY, float vel, GameMap gameMap) {
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("playerdummy.png");
        this.hitbox = new Rectangle(this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f), this.sprite.getWidth(), this.sprite.getHeight());
        this.gameMap = gameMap;
    }

    /**
     * Reads input from keyboard and updates player position accordingly.
     * w -> move player up.
     * s -> move player down.
     * a -> move player left.
     * d -> move player right.
     * The new position is then clamped to the world boundaries.
     *
     */
    public void movePlayer() {
        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.pos.x = this.pos.x - this.vel;
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            this.pos.x = this.pos.x + this.vel;
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            this.pos.y = this.pos.y - this.vel;
        }

        if (Gdx.input.isKeyPressed(Keys.W)) {
            this.pos.y = this.pos.y + this.vel;
        }

        this.gameMap.clampPosition(this.pos);
        this.hitbox.setPosition(this.pos);
    }

    /**
     * New inventory object
     */
    private final Inventory inventory = new Inventory();

    /**
     * Getter to display items stored in the inventory
     * @return Items stored in the inventory
     */
    public Inventory getInventory() {

        return this.inventory;
    }


    //Is disguised behavior
    /**
     * Whether the player is currently wearing the goose disguise. Geese ignore a disguised player.
     */
    private boolean isDisguised = false;

    /**
     * Turns the player's disguise on or off.
     *
     * @param disguised true to put the disguise on, false to take it off
     */
    public void setDisguised(boolean disguised) {
        this.isDisguised = disguised;
    }

    /**
     * Checks whether the player is currently disguised.
     *
     * @return true if the player is disguised
     */
    public boolean getDisguised() {
        return this.isDisguised;
    }

    //Changing sprite behavior
    /**
     * Fetch the texture currently used to draw the player.
     *
     * @return The player's current sprite.
     */
    public Texture getSprite(){
        return this.sprite;
    }

    /**
     * Changes the texture used to draw the player.
     *
     * @param sprite The texture to draw the player with.
     */
    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }

    // Food (health)
    /** Maximum amount of Yorkshire pudding the player can hold. */
    private static final int MAX_FOOD = 100;

    /** Seconds the player is safe from further bites after a goose takes some food. */
    private static final float BITE_COOLDOWN = 1f;

    private int food = MAX_FOOD;
    private float biteCooldownLeft = 0f;

    /**
     * Counts down the bite cooldown. Call once per frame.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void updateFoodTimer(float delta) {
        if (this.biteCooldownLeft > 0) {
            this.biteCooldownLeft -= delta;
        }
    }

    /**
     * A goose takes some of the player's food. Ignored during the bite cooldown.
     *
     * @param amount How much food is taken.
     * @return true if food was actually lost.
     */
    public boolean loseFood(int amount) {
        if (this.biteCooldownLeft > 0 || this.food <= 0) {
            return false;
        }
        this.food = Math.max(0, this.food - amount);
        this.biteCooldownLeft = BITE_COOLDOWN;
        return true;
    }

    /** @return food remaining as a fraction between 0 and 1, for drawing the bar. */
    public float getFoodFraction() {
        return (float) this.food / MAX_FOOD;
    }

    /** @return true if all the food has been eaten (the game-over condition). */
    public boolean isOutOfFood() {
        return this.food <= 0;
    }

}
