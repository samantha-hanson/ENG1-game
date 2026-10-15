package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class Player extends AbstractEntity{

    public Player(float startX, float startY, float vel, String sprite_path){
        this.posX = startX;
        this.posY = startY;
        this.vel = vel;
        this.sprite = new Texture(sprite_path);
    }

    public void movePlayer(){
        if(Gdx.input.isKeyPressed(Keys.A)){
            this.posX -= this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.D)){
            this.posX += this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.S)){
            this.posY -= this.vel;
        }

        if(Gdx.input.isKeyPressed(Keys.W)){
            this.posY += this.vel;
        }
    }
}
