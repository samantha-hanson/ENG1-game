package uk.ac.york.eng1.geese.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Game over screen drawn in screen space on top of the frozen game world.
 * Shown when the player runs out of Yorkshire pudding.
 */
public class EndScreen {
    /** How dark the overlay over the frozen world is (0 = invisible, 1 = solid black). */
    private static final float OVERLAY_ALPHA = 0.7f;

    private final OrthographicCamera screenCamera = new OrthographicCamera();
    private final Texture pixel;
    private final BitmapFont titleFont = new BitmapFont();
    private final BitmapFont textFont = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();

    /**
     * Creates the end screen and loads its resources. Call once the graphics context is ready.
     */
    public EndScreen() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.pixel = new Texture(pixmap);
        pixmap.dispose();

        this.titleFont.getData().setScale(4f);
        this.titleFont.setColor(Color.RED);
        this.textFont.getData().setScale(1.5f);
    }

    /**
     * Draws the game over screen. Call at the end of render(), after the world and HUD.
     *
     * @param batch The shared SpriteBatch (must NOT be between begin() and end()).
     * @param viewport The game viewport, used to size the screen to the window.
     * @param secondsSurvived How long the player lasted, in seconds.
     */
    public void draw(SpriteBatch batch, Viewport viewport, float secondsSurvived) {
        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();
        this.screenCamera.setToOrtho(false, w, h);
        batch.setProjectionMatrix(this.screenCamera.combined);

        batch.begin();

        // Dim the frozen world
        batch.setColor(0f, 0f, 0f, OVERLAY_ALPHA);
        batch.draw(this.pixel, 0, 0, w, h);
        batch.setColor(Color.WHITE);

        drawCentred(batch, this.titleFont, "GAME OVER", w, h * 0.65f);
        drawCentred(batch, this.textFont, "The geese ate all your Yorkshire pudding!", w, h * 0.50f);

        int minutes = (int) (secondsSurvived / 60);
        int seconds = (int) (secondsSurvived % 60);
        drawCentred(batch, this.textFont,
            String.format("You survived %02d:%02d", minutes, seconds), w, h * 0.42f);

        drawCentred(batch, this.textFont, "Press R to restart   |   Press ESC to quit", w, h * 0.30f);

        batch.end();
    }

    /**
     * Draws a line of text horizontally centred in the screen at the given height.
     */
    private void drawCentred(SpriteBatch batch, BitmapFont font, String text, float screenWidth, float y) {
        this.layout.setText(font, text);
        font.draw(batch, this.layout, (screenWidth - this.layout.width) / 2f, y);
    }

    /**
     * Frees the texture and fonts. Call from Main.dispose().
     */
    public void dispose() {
        this.pixel.dispose();
        this.titleFont.dispose();
        this.textFont.dispose();
    }
}
