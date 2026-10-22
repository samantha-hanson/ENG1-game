package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

/**
 * provides the standard for defining non-player entities.
 *
 * <p>
z * This extends AbstractEntity by providing behaviour that all NPC's will need, such as a target position, visual radius and status.
 * also implements the behaviour for a NPC to move, and update its status.
 * </p>
 *
 */
public abstract class AbstractNPC extends AbstractEntity {
    /**
     * The target position for the entity when it is idling
     */
    protected Vector2 targetPos;

    /**
     * Current state of the entity defaulted to Status.IDLE
     */
    protected Status status = Status.IDLE;

    /**
     * The radius of vision in which the Entity can detect other entities
     */
    protected float visualRadius;

    // Create temporary locations to reduce memory initialisation in render loop and prevent in-place alteration.
    private Vector2 tempMoveDir = new Vector2(0, 0);
    private float moveScale;

    /**
     * Calculates and moves the NPC according to its velocity.
     *
     * <p>
     * Once distance between entity and target becomes short enough the speed is capped.
     * This is done to prevent overshoot and jittering.
     * After the new position is calculated, it is clamped to the world boundaries before being applied to
     * the entity.
     * </p>
     *
     */
    public void moveNPC() {
        float distance = this.pos.dst(this.targetPos);
        // Use moveScale to prevent overshoot and consequent jittering -> very ugly
        moveScale = Math.min(distance, this.vel);
        this.tempMoveDir.set(this.targetPos).sub(this.pos);
        this.tempMoveDir.nor().scl(moveScale);
        this.pos.add(this.tempMoveDir);

        // Clamp position and move hitbox with entity
        this.gameMap.clampPosition(this.pos);
        this.hitbox.setPosition(this.pos);
    }

    /**
     * get the target position of the entity
     *
     * @return The entity's current target position as a Vector2.
     */
    public Vector2 getTargetPos() {
        return this.targetPos;
    }

    /**
     * Determines the entity's behaviour and how it's status changes depending on a nearby entity. Should then
     * update the entity's Status. Can use the entity type for more dynamic behaviour.
     *
     * @param entity The entity affecting the behaviour of the calling object.
     */
    public abstract void updateStatus(AbstractEntity entity);
}
