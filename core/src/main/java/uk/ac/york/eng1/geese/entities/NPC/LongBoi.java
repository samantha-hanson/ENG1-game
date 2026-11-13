package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.Player;

/**
 * represents the long boi NPC.
 *
 * <p>
 * extends AbstractNPC by implementing UpdateStatus to provide behaviour to long boi.
 * </p>
 */

public class LongBoi extends AbstractNPC{

    /**
     * Create a long boi object at a given position. Initialises the sprite attribute to the long boi png and the hitbox to sprite size.
     *
     * @param startX initial x-coordinate of long boi.
     * @param startY initial y-coordinate of long boi.
     * @param vel    velocity that long boi's entity can move.
     * @param vision represents the distance that the long boi object can detect entities.
     * @param gameMap represents the gameMap that the long boi object can traverse and interact with
     *
     */

    public LongBoi(float startX, float startY, float vel, float vision, GameMap gameMap) {
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("long_boi.png");
        this.hitbox = new Rectangle(this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f), this.sprite.getWidth(), this.sprite.getHeight());
        this.gameMap = gameMap;

        this.visualRadius = vision;
        this.targetPos = new Vector2(this.pos);
    }


    /**
     * {@inheritDoc}
     * implementation note: long boi behaviour currently targets the passed player if it is within the vision of the long boi.
     * Otherwise, it updates the wandering behaviour of long boi.
     *
     */
    @Override
    public void updateStatus(AbstractEntity entity) {
        Vector2 entPos = entity.getPos();

        // A scared goose runs directly away from the entity instead of targeting it
        if (this.status == Status.SCARED) {
            this.targetPos.set(this.pos).sub(entPos).nor().scl(300f).add(this.pos);
            this.gameMap.clampPosition(this.targetPos);
            return;
        }

        float distance = this.pos.dst(entPos);

        // Check if the entity is within sight and approach & if they are not disguised
        if(entity instanceof Player player) {
            //If the player isn't disguised and within the visualRadius -> start Targeting
            if (distance <= this.visualRadius && !player.getDisguised()) {
                this.status = Status.TARGETING;
                this.targetPos.set(entPos);

            } else {
                if (this.status == Status.TARGETING) {
                    this.status = Status.IDLE;
                }

                // Check if the goose has reached some idling wander, and update its target
                // Use epsilon to account for floating point imprecision and math.random to let the goose sit still
                if (this.pos.epsilonEquals(this.targetPos, 0.0001f) && Math.random() > 0.975f) {
                    float wanderDistMax = 200f;
                    float wanderDistMin = 50f;

                    // Calculate a position to wander to as a percentage of some range
                    this.targetPos.setToRandomDirection()
                        .scl((float) (wanderDistMin + Math.random() * (wanderDistMax - wanderDistMin)))
                        .add(this.pos);

                    // Clamp the target position to prevent void wandering
                    this.gameMap.clampPosition(this.targetPos);
                }
            }
        }
    }

    /**
     * Makes long boi flee from the player until calm is called.
     */
    public void scare() {

        this.status = Status.SCARED;
        this.vel = 50f;
    }

    /**
     * Returns a scared long boi to idle. If long boi isn't scared it's left alone.
     */
    public void calm() {
        if (this.status == Status.SCARED) {
            this.status = Status.IDLE;
        }
    }

    /**
     * Displaying the status of long boi object
     * @return Current Status of long boi
     */
    public Status getStatus(){
        return this.status;
    }
}


