package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;

/**
 * Represents the standard for defining future entities.
 * <p>
 * provides implementations of attributes and methods that would be standard across all entities, such as sprite and position.
 * Also stores the boundaries for valid entity movement as floats, which inheriting entities will use to prevent out of bound
 * behaviour.
 * </p>
 */
public abstract class AbstractEntity {
    /**
     * Current position of the entity.
     */
    protected Vector2 pos;

    /**
     * the speed the entity can be moved.
     */
    protected float vel;

    /**
     * The image drawn to the screen to represent the entity.
     */
    protected Texture sprite;

    /**
     * The underlying object used to determine collision.
     */
    protected Rectangle hitbox;

    /**
     * the map state that all entities can wander, interact with and traverse.
     */
    protected GameMap gameMap;

    /**
     * Standardises SpriteBatch draw so that entity is drawn from center, not bottom-left corner.
     * Use when adding any entity to the SpriteBatch.
     *
     * @param batch the SpriteBatch the entity is being drawn to.
     *
     */
    public void draw(SpriteBatch batch) {
        // Batch draws sprite with the position passed as the bottom-left corner, so offset to draw center.
        batch.draw(this.sprite, this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f));
    }

    /**
     * Fetch current position of the entity
     *
     * @return Entity's current position as a Vector2
     */
    public Vector2 getPos() {
        return this.pos;
    }

    /**
     * Fetch the current hitbox of the entity
     * @return Rectangle representing the hitbox.
     */
    public Rectangle getHitbox(){ return this.hitbox; }

    public void dispose(){
        this.sprite.dispose();
    }

    public void setVel(float vel){
        this.vel = vel;
    }
}
