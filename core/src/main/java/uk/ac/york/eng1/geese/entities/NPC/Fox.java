package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.Player;

import java.util.List;

/**
 * represents the fox NPC.
 *
 * <p>
 * extends AbstractNPC by implementing UpdateStatus to provide behaviour to the fox.
 * </p>
 */

public class Fox extends AbstractNPC{

    /**
     * How close a goose must be to the fox to be scared away
     */
    private static final float SCARE_RADIUS = 200f;

    /**
     * Create a fox object at a given position. Initialises the sprite attribute to the fox png and the hitbox to sprite size.
     *
     * @param startX initial x-coordinate of the fox.
     * @param startY initial y-coordinate of the fox.
     * @param vel    velocity that the fox entity can move.
     * @param vision represents the distance that the fox object can detect entities.
     * @param gameMap represents the gameMap that the fox object can traverse and interact with
     *
     */

    public Fox(float startX, float startY, float vel, float vision, GameMap gameMap) {
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("fox.png");
        this.hitbox = new Rectangle(this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f), this.sprite.getWidth(), this.sprite.getHeight());
        this.gameMap = gameMap;

        this.visualRadius = vision;
        this.targetPos = new Vector2(this.pos);
    }

    /**
     * {@inheritDoc}
     * implementation note: fox behaviour targets the closest goose, player or long boi.
     * Otherwise, it enters its idle state.
     *
     */

    @Override
    public void updateStatus(AbstractEntity entity) {
        Vector2 entPos = entity.getPos();
        float distance = this.pos.dst(entPos);

        // Check if the entity is within sight and approach
        if (distance <= this.visualRadius) {
            this.status = Status.TARGETING;
            this.targetPos.set(entPos);

        } else {
            if (this.status == Status.TARGETING) {
                this.status = Status.IDLE;
            }

            // Check if long boi has reached some idling wander, and update its target
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

    /**
     * Calculates which entity is closest to the fox within its vision.
     *
     * @param geese List of all goose entities.
     * @param mainTarget either long boi entity if it is currently spawned, otherwise the player entity.
     * @return closest entity in the fox's vision.
     */
    public AbstractEntity findClosestTarget(List<Goose> geese, AbstractEntity mainTarget){
        Goose closestGoose = null;
        float minDist = Float.MAX_VALUE;
        float maxDist = 300f;
        for(Goose goose: geese){
            float dist = this.getPos().dst2(goose.getPos());
            if (dist < minDist){
                minDist = dist;
                closestGoose = goose;
            }
        }

        float targetDist = this.pos.dst2(mainTarget.getPos());
        return minDist < targetDist ? closestGoose: mainTarget;
    }

    protected void applyEffect(GameMap gameMap, Player player) {
        for (Goose goose : gameMap.getGeese()) {
            if (goose.getPos().dst(player.getPos()) <= SCARE_RADIUS) {
                goose.scare();
            }
        }
    }
}
