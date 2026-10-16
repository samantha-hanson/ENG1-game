package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

public abstract class AbstractNPC extends AbstractEntity {
    //target position for when NPC is idling
    protected Vector2 targetPos;

    //String 'idle' - 'targeting' - 'scared' lowercase
    protected String status = "idle";
    protected float visualRadius;

    private Vector2 tempMoveDir = new Vector2(0, 0);
    private float moveScale;
    public void moveNPC(){
        float distance = this.pos.dst(this.targetPos);
        moveScale = Math.min(distance, this.vel);
        this.tempMoveDir.set(this.targetPos).sub(this.pos);//use temp variable to prevent in place altering of target pos
        this.tempMoveDir.nor().scl(moveScale);
        this.pos.add(this.tempMoveDir);

        this.pos.x = MathUtils.clamp(this.pos.x, mapBoundLeft, mapBoundRight);
        this.pos.y = MathUtils.clamp(this.pos.y, mapBoundFloor, mapBoundRoof);
    }
    public abstract void updateStatus(AbstractEntity entity);
}
