package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

public abstract class AbstractNPC extends AbstractEntity {
    //target position for when NPC is idling
    protected Vector2 targetPos;

    protected Status status = Status.IDLE;
    protected float visualRadius;

    //create temporary locations to reduce memory initialisation in render loop and prevent in-place alteration.
    private Vector2 tempMoveDir = new Vector2(0, 0);

    private float moveScale;

    public Vector2 getTargetPos(){
        return this.targetPos;
    }

    public void moveNPC(){
        float distance = this.pos.dst(this.targetPos);
        moveScale = Math.min(distance, this.vel);//use moveScale to prevent overshoot and consequent jittering -> very ugly
        this.tempMoveDir.set(this.targetPos).sub(this.pos);
        this.tempMoveDir.nor().scl(moveScale);
        this.pos.add(this.tempMoveDir);

        this.pos.x = MathUtils.clamp(this.pos.x, mapBoundLeft, mapBoundRight);
        this.pos.y = MathUtils.clamp(this.pos.y, mapBoundFloor, mapBoundRoof);
    }
    public abstract void updateStatus(AbstractEntity entity);
}
