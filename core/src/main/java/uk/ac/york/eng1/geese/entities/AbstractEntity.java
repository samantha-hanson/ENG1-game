package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEntity {
    protected Vector2 pos;

    protected float vel;
    protected Texture sprite;

    public void draw(SpriteBatch batch){
        batch.draw(this.sprite, this.pos.x, this.pos.y);
    }
}
