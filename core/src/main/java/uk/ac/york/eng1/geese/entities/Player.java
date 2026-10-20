package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player extends AbstractEntity{

    /**
     create a player object at given position.

     @param startX initial x-coordinate of the player.
     @param startY initial y-coordinate of the player.
     @param vel velocity the player entity can move.
     */
    public Player(float startX, float startY, float vel){
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("playerdummy.png");
    }

    /** Reads input from keyboard and updates player position accordingly.
     * w -> move player up.
     * s -> move player down.
     * a -> move player left.
     * d -> move player right.
     * The new position is then clamped to the world boundaries.
     * */
    public void movePlayer(){
        if(Gdx.input.isKeyPressed(Keys.A)){
            this.pos.x = MathUtils.clamp(this.pos.x - this.vel, mapBoundLeft, mapBoundRight);
        }

        if(Gdx.input.isKeyPressed(Keys.D)){
            this.pos.x = MathUtils.clamp(this.pos.x + this.vel, mapBoundLeft, mapBoundRight);
        }

        if(Gdx.input.isKeyPressed(Keys.S)){
            this.pos.y = MathUtils.clamp(this.pos.y - this.vel, mapBoundFloor, mapBoundRoof);
        }

        if(Gdx.input.isKeyPressed(Keys.W)){
            this.pos.y = MathUtils.clamp(this.pos.y + this.vel, mapBoundFloor, mapBoundRoof);
        }
    }
}
