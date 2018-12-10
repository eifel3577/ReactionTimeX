package com.argo_entertainment.reactiontime.GameField.Skill;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Random;


public class ScreenShake {
    private float elapsed, duration, intensity;
    private Random random = new Random();
    /**
     * Start the screen shaking with a given power and duration
     * @param intensity How much intensity should the shaking use.
     * @param duration Time in milliseconds the screen should shake.
     */
    public void shake(float intensity, float duration) {
        this.elapsed = 0;
        this.duration = duration / 1000f;
        this.intensity = intensity;
    }

    /**
     * Updates the shake and the camera.
     * This must be called prior to camera.update()
     */
    public void update(float delta, OrthographicCamera camera) {

        // Only shake when required.
        if(elapsed < duration) {

            // Calculate the amount of shake based on how long it has been shaking already
            float currentPower = intensity * camera.zoom * ((duration - elapsed) / duration);
            float x = (random.nextFloat() - 0.5f) * currentPower;
            float y = (random.nextFloat() - 0.5f) * currentPower;
            camera.translate(-x, -y);

            // Increase the elapsed time by the delta provided.
            elapsed += delta;
        }
    }
}
