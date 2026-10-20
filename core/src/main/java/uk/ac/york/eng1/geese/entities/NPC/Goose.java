package uk.ac.york.eng1.geese.entities.NPC;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.eng1.geese.entities.AbstractEntity;

/**
 * represents the Goose NPC.
 *
 * <p>
 *     extends AbstractNPC by implementing UpdateStatus to provide behaviour to the goose.
 * </p>
 */
public class Goose extends AbstractNPC{

    /**
     * Create a Goose object at a given position. Initialises the sprite attribute to the goose png.
     *
     * @param startX initial x-coordinate of the Goose.
     * @param startY initial y-coordinate of the Goose.
     * @param vel velocity the Goose entity can move.
     * @param vision represents the distance the Goose object can detect entities.
     * */
    public Goose(float startX, float startY, float vel, float vision){
        this.pos = new Vector2(startX, startY);
        this.vel = vel;
        this.sprite = new Texture("goosedummy.png");

        this.visualRadius = vision;
        this.targetPos = new Vector2(this.pos);
    }

    /**
     * {@inheritDoc}
     * implementation note: Goose behaviour currently targets the passed entity if it is within the vision of the goose.
     * Otherwise, it updates the wandering behaviour of the goose.
     * */
    @Override
    public void updateStatus(AbstractEntity entity) {
        Vector2 entPos = entity.getPos();
        float distance = this.pos.dst(entPos);

        //check if the entity is within sight and approach
        if (distance <= this.visualRadius) {
            this.status = Status.TARGETING;
            this.targetPos.set(entPos);

        } else {
            if (this.status == Status.TARGETING) {
                this.status = Status.IDLE;
            }

            //check if the goose has reached some idling wander, and update its target
            //use epsilon to account for floating point nonsense and math.random to let the goose sit still
            if (this.pos.epsilonEquals(this.targetPos, 0.0001f) && Math.random() > 0.975f) {
                float wanderDistMax = 200f;
                float wanderDistMin = 50f;

                //calculate a position to wander to as a percentage of some range
                this.targetPos.setToRandomDirection()
                    .scl((float) (wanderDistMin + Math.random() * (wanderDistMax - wanderDistMin)))
                    .add(this.pos);

                //clamp the target position to prevent void wandering
                this.targetPos.x = MathUtils.clamp(this.targetPos.x, mapBoundLeft, mapBoundRight);
                this.targetPos.y = MathUtils.clamp(this.targetPos.y, mapBoundFloor, mapBoundRoof);
            }
        }
    }
}
