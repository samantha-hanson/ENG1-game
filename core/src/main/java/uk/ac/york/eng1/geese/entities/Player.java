package uk.ac.york.eng1.geese.entities;
import uk.ac.york.eng1.geese.items.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;

/**
 * represents the player character, storing necessary information such as the current position of the player.
 *
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

    private final Inventory inventory = new Inventory();

    public Inventory getInventory() {
        return this.inventory;
    }

}
