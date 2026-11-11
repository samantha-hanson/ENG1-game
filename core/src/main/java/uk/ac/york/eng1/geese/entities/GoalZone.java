package uk.ac.york.eng1.geese.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * A labelled rectangular region of the map used to mark places the player cares about.
 *
 * <p>
 * The region is stored as a world-space rectangle, so it can be drawn as an overlay on top of the
 * tile map and tested for overlap with an entity's hitbox. Currently used for the player's starting
 * point (A) and the point the player has to reach (B).
 * </p>
 */
public class GoalZone {

    /**
     * Thickness of the outline drawn around the region, in world units.
     */
    private static final float BORDER_THICKNESS = 4f;

    /**
     * Alpha value of the fill colour of the region.
     */
    private static final float FILL_ALPHA = 0.35f;

    /**
     * The area of the map covered by the region, in world coordinates.
     */
    private final Rectangle bounds;

    /**
     * The text drawn in the middle of the region, such as "A" or "B".
     */
    private final String label;

    /**
     * The colour used for the fill, outline and label of the region.
     */
    private final Color colour;

    /**
     * Whether an entity has reached this region yet.
     */
    private boolean reached;

    /**
     * Create a GoalZone object covering the given world-space rectangle.
     *
     * @param bounds the rectangle covered by the region, in world coordinates.
     * @param label  the text drawn in the middle of the region.
     * @param colour the colour of the region's fill, outline and label.
     */
    public GoalZone(Rectangle bounds, String label, Color colour) {
        this.bounds = bounds;
        this.label = label;
        this.colour = colour;
        this.reached = false;
    }

    /**
     * Draw the region as a semi-transparent filled rectangle with a solid outline and a centred label.
     * Should be called inside a SpriteBatch that has already begun drawing.
     *
     * @param batch  the SpriteBatch the region is being drawn to.
     * @param pixel  a 1x1 white texture, tinted to produce the fill and outline.
     * @param font   the font used to draw the label.
     * @param layout a reusable GlyphLayout owned by the caller.
     */
    public void draw(SpriteBatch batch, Texture pixel, BitmapFont font, GlyphLayout layout) {
        batch.setColor(this.colour.r, this.colour.g, this.colour.b, FILL_ALPHA);
        batch.draw(pixel, this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);

        batch.setColor(this.colour.r, this.colour.g, this.colour.b, 1f);
        // Bottom and top edges.
        batch.draw(pixel, this.bounds.x, this.bounds.y, this.bounds.width, BORDER_THICKNESS);
        batch.draw(pixel, this.bounds.x, this.bounds.y + this.bounds.height - BORDER_THICKNESS,
            this.bounds.width, BORDER_THICKNESS);
        // Left and right edges.
        batch.draw(pixel, this.bounds.x, this.bounds.y, BORDER_THICKNESS, this.bounds.height);
        batch.draw(pixel, this.bounds.x + this.bounds.width - BORDER_THICKNESS, this.bounds.y,
            BORDER_THICKNESS, this.bounds.height);
        batch.setColor(Color.WHITE);

        // Centre the label inside the region, restoring the font colour afterwards so the HUD is unaffected.
        Color previousColour = font.getColor().cpy();
        font.setColor(this.colour);
        layout.setText(font, this.label);
        font.draw(batch, layout,
            this.bounds.x + (this.bounds.width - layout.width) / 2f,
            this.bounds.y + (this.bounds.height + layout.height) / 2f);
        font.setColor(previousColour);
    }

    /**
     * Checks whether the passed entity is currently standing inside this region.
     *
     * @param entity the entity to test, such as the player.
     * @return true if the entity's hitbox overlaps the region.
     */
    public boolean isOccupiedBy(AbstractEntity entity) {
        return this.bounds.overlaps(entity.getHitbox());
    }

    /**
     * Fetch the centre of the region.
     * @return the centre of the region as a Vector2.
     */
    public Vector2 getCentre() {
        return new Vector2(this.bounds.x + this.bounds.width / 2f,
            this.bounds.y + this.bounds.height / 2f);
    }

    /**
     * Fetch the area covered by the region.
     * @return the world-space rectangle covered by the region.
     */
    public Rectangle getBounds() {
        return this.bounds;
    }

    /**
     * Fetch the label drawn in the middle of the region.
     * @return the label of the region.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Check whether an entity has already reached this region.
     * @return true if the region has been reached.
     */
    public boolean isReached() {
        return this.reached;
    }

    /**
     * Record that this region has been reached.
     *
     * @param reached true once an entity has reached the region.
     */
    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
