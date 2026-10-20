package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEntity {
    /** Current position of the entity*/
    protected Vector2 pos;

    /** the speed the entity can be moved */
    protected float vel;

    /** The image drawn to the screen to represent the entity */
    protected Texture sprite;

    /**
    to prevent entities from wandering off map, set the world bounds and clamp movement.
    static since all entities will be bounded to the same loaded map.
     */
    protected static float mapBoundLeft;
    protected static float mapBoundRight;
    protected static float mapBoundRoof;
    protected static float mapBoundFloor;

    /** Set the world bounds used to constrain entity movement*/
    public static void setWorldBounds(float left, float right, float roof, float floor){
        mapBoundLeft = left;
        mapBoundRight = right;
        mapBoundRoof = roof;
        mapBoundFloor = floor;
    }

    /**
     * Standardises SpriteBatch draw so that entity is drawn from center, not bottom-left corner.
     * Use when adding any entity to the SpriteBatch.
     * */
    public void draw(SpriteBatch batch){
        //batch draws sprite with the position passed as the bottom-left corner, so offset to draw center.
        batch.draw(this.sprite, this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f));
    }

    /** Fetch current position of the entity
     *
     * @return Entity's current position as a Vector2
     */
    public Vector2 getPos(){
        return this.pos;
    }
}
