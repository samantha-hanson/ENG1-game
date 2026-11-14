package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.GameMap;
import uk.ac.york.eng1.geese.entities.AbstractEntity;
import uk.ac.york.eng1.geese.entities.Player;

/**
 * represents the Goose NPC.
 *
 * <p>
 * extends AbstractNPC by implementing UpdateStatus to provide behaviour to the goose.
 * </p>
 * <p>Requirements: UR_GEESE</p>
 */
public class Goose extends AbstractNPC {

    /**
     * Create a Goose object at a given position. Initialises the sprite attribute to the goose png and the hitbox to sprite size.
     *
     * @param startX initial x-coordinate of the Goose.
     * @param startY initial y-coordinate of the Goose.
     * @param vel    velocity the Goose entity can move.
     * @param vision represents the distance the Goose object can detect entities.
     * @param gameMap represents the gameMap the Goose object can traverse and interact with
     *
     */
    public Goose(float startX, float startY, float vel, float vision, GameMap gameMap) {
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("goosedummy.png");
        this.hitbox = new Rectangle(this.pos.x - (this.sprite.getWidth() / 2f), this.pos.y - (this.sprite.getHeight() / 2f), this.sprite.getWidth(), this.sprite.getHeight());
        this.gameMap = gameMap;

        this.visualRadius = vision;
        this.targetPos = new Vector2(this.pos);
    }

    /**
     * {@inheritDoc}
     * implementation note: A scared goose runs directly away from the passed entity. Otherwise, the goose targets the
     * passed entity if it is within the vision of the goose, and updates its wandering behaviour if not. This only occurs
     * if the passed entity is a player.
     * Additionally, disguised players are ignored by the geese and not targeted
     * <p>Requirements: UR_GEESE (tracks the player), FR_GEESE_AVOIDENCE (ignores a disguised player),
     * FR_GEESE_REMOVAL (a scared goose runs away)</p>
     *
     */
    @Override
    public void updateStatus(AbstractEntity entity) {
        Vector2 entPos = entity.getPos();

        // A scared goose runs directly away from the entity instead of targeting it
        if (this.status == Status.SCARED) {
            this.targetPos.set(this.pos).sub(entPos).nor().scl(300f).add(this.pos);
            this.gameMap.clampPosition(this.targetPos);
            return;
        }

        float distance = this.pos.dst(entPos);

        // Check if the entity is within sight and approach & if they are not disguised
        if(entity instanceof Player player) {
            //If the player isn't disguised and within the visualRadius -> start Targeting
            if (distance <= this.visualRadius && !player.getDisguised()) {
                this.status = Status.TARGETING;
                this.targetPos.set(entPos);

            } else {
                if (this.status == Status.TARGETING) {
                    this.status = Status.IDLE;
                }

                // Check if the goose has reached some idling wander, and update its target
                // Use epsilon to account for floating point imprecision and math.random to let the goose sit still
                if (this.pos.epsilonEquals(this.targetPos, 0.0001f) && Math.random() > 0.975f) {
                    float wanderDistMax = 200f;
                    float wanderDistMin = 50f;

                    // Calculate a position to wander to as a percentage of some range
                    this.targetPos.setToRandomDirection()
                        .scl((float) (wanderDistMin + Math.random() * (wanderDistMax - wanderDistMin)))
                        .add(this.pos);

                    // Clamp the target position to prevent void wandering
                    this.gameMap.clampPosition(this.targetPos);
                }
            }
        }
    }

    /**
     * Makes the goose flee from the player until calm is called.
     * <p>Requirements: UR_GEESE_DISPERSAL, UR_MECHANICS, FR_GEESE_REMOVAL, FR_VIOLENCE_PROHIBITED, QR_NON_VIOLENCE
     * (geese are dispersed by being scared, never harmed)</p>
     */
    public void scare() {
        this.status = Status.SCARED;
        this.vel = 50f;
    }

    /**
     * Returns a scared goose to idle. Geese that aren't scared are left alone.
     */
    public void calm() {
        if (this.status == Status.SCARED) {
            this.status = Status.IDLE;
        }
    }

    /**
     * Displaying the status of the goose object
     * @return Current Status of the goose
     */
    public Status getStatus(){
        return this.status;
    }
}
