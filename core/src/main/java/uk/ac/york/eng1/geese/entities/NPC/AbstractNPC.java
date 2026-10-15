package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

public abstract class AbstractNPC extends AbstractEntity {
    //target position for when NPC is idling
    protected Vector2 targetPos;

    //String 'idle' - 'targeting' - 'scared' lowercase
    protected String status = "idle";
    protected float visualRadius;

    public void moveNPC(){
        float dirX = Math.signum(this.targetPos.x - this.pos.x);
        float dirY = Math.signum(this.targetPos.y - this.pos.y);

        this.pos.x += (this.vel * dirX);
        this.pos.y += (this.vel * dirY);
    }
    public abstract void updateStatus(AbstractEntity entity);
}
