package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

public class Goose extends AbstractNPC{

    public Goose(float startX, float startY, float vel, String spritePath, float vision){
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture(spritePath);

        this.visualRadius = vision;
        this.targetPos = new Vector2(this.pos);
    }

    @Override
    public void updateStatus(AbstractEntity entity) {
        Vector2 entPos = entity.getPos();
        float distance = this.pos.dst(entPos);

        if(distance <= this.visualRadius){
            this.status = "targeting";
            this.targetPos.set(entPos);
        }
    }
}
