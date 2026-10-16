package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEntity {
    protected Vector2 pos;
    protected float vel;
    protected Texture sprite;

    //to prevent entities from wandering off map, set the movement bounds and clamp using MathUtil.
    //static since all entities will be bounded to the same loaded map.
    protected static float mapBoundLeft;
    protected static float mapBoundRight;
    protected static float mapBoundRoof;
    protected static float mapBoundFloor;

    public static void setWorldBounds(float left, float right, float roof, float floor){
        mapBoundLeft = left;
        mapBoundRight = right;
        mapBoundRoof = roof;
        mapBoundFloor = floor;
    }
    public void draw(SpriteBatch batch){
        //batch draws sprite with the position passed as the bottom-left corner, so offset to draw center.
        batch.draw(this.sprite, this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f));
    }

    public Vector2 getPos(){
        return this.pos;
    }
}
