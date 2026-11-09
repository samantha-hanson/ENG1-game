package uk.ac.york.eng1.geese.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import uk.ac.york.eng1.geese.entities.Player;
import uk.ac.york.eng1.geese.items.AbstractItems;

/**
 * Heads-up display drawn in screen space (it does not move with the world camera).
 * Shows the player's remaining Yorkshire pudding as a bar, and the inventory as a row of icons.
 */
public class Hud {

    private static final float MARGIN = 20f;
    private static final float BAR_WIDTH = 200f;
    private static final float BAR_HEIGHT = 24f;
    private static final float ICON_SIZE = 48f;
    private static final float SLOT_GAP = 12f;

    /** Fixed camera so the HUD stays in the same place on screen. */
    private final OrthographicCamera hudCamera = new OrthographicCamera();

    /** A 1x1 white texture that is stretched and tinted to draw the bar rectangles. */
    private final Texture pixel;
    private final Texture puddingIcon;
    private final BitmapFont font = new BitmapFont();

    public Hud() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.pixel = new Texture(pixmap);
        pixmap.dispose();

        this.puddingIcon = new Texture("yorkshirepudding.png");
    }

    /**
     * Draws the whole HUD. Call at the end of Main.render(), after the world has been drawn.
     *
     * @param batch The shared SpriteBatch (must NOT be between begin() and end()).
     * @param viewport The game viewport, used to size the HUD to the window.
     * @param player The player whose food and inventory are displayed.
     */
    public void draw(SpriteBatch batch, Viewport viewport, Player player) {
        this.hudCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setProjectionMatrix(this.hudCamera.combined);

        batch.begin();
        drawFoodBar(batch, player, viewport.getWorldHeight());
        drawInventory(batch, player);
        batch.setColor(Color.WHITE); // always reset the tint so other drawing isn't affected
        batch.end();
    }

    private void drawFoodBar(SpriteBatch batch, Player player, float worldHeight) {
        float iconY = worldHeight - MARGIN - ICON_SIZE;
        float barX = MARGIN + ICON_SIZE + 8f;
        float barY = iconY + (ICON_SIZE - BAR_HEIGHT) / 2f;

        batch.setColor(Color.WHITE);
        batch.draw(this.puddingIcon, MARGIN, iconY, ICON_SIZE, ICON_SIZE);

        // Dark background, then the remaining food on top
        batch.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        batch.draw(this.pixel, barX, barY, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(0.9f, 0.7f, 0.2f, 1f);
        batch.draw(this.pixel, barX, barY, BAR_WIDTH * player.getFoodFraction(), BAR_HEIGHT);
    }

    private void drawInventory(SpriteBatch batch, Player player) {
        float x = MARGIN;
        for (AbstractItems item : player.getInventory().getItems()) {
            // Grey out used-up items, tint active ones, leave the rest normal
            if (item.isUsedUp()) {
                batch.setColor(0.4f, 0.4f, 0.4f, 0.6f);
            } else if (item.isActive()) {
                batch.setColor(1f, 1f, 0.5f, 1f);
            } else {
                batch.setColor(Color.WHITE);
            }
            batch.draw(item.getIcon(), x, MARGIN, ICON_SIZE, ICON_SIZE);

            batch.setColor(Color.WHITE);
            this.font.draw(batch, "x" + item.getUsesRemaining(), x, MARGIN + ICON_SIZE + 16f);

            x += ICON_SIZE + SLOT_GAP;
        }
    }

    /** Frees the textures and font. Call from Main.dispose(). */
    public void dispose() {
        this.pixel.dispose();
        this.puddingIcon.dispose();
        this.font.dispose();
    }
}
