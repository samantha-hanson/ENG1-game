package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractEntity {
    public float posX;
    public float posY;

    public float vel;
    public Texture sprite;

    public void draw(SpriteBatch batch){
        batch.draw(this.sprite, this.posX, this.posY);
    }
}
