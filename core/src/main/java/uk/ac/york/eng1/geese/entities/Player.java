package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends AbstractEntity{

    public Player(float startX, float startY, float vel, String sprite_path){
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture(sprite_path);
    }

    public void movePlayer(){
        if(Gdx.input.isKeyPressed(Keys.A)){
            this.pos.x -= this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.D)){
            this.pos.x += this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.S)){
            this.pos.y -= this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.W)){
            this.pos.y += this.vel;
        }
    }
}
