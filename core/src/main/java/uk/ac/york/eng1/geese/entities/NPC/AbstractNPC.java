package uk.ac.york.eng1.geese.entities.NPC;

import uk.ac.york.eng1.geese.entities.AbstractEntity;

public abstract class AbstractNPC extends AbstractEntity {
    //target position for when NPC is idling
    protected float targetX;
    protected float targetY;

    //String 'idle' - 'targeting' - 'scared' lowercase
    protected String status;
    protected float visualRadius;


    public abstract void updateStatus(AbstractEntity entity);
}
