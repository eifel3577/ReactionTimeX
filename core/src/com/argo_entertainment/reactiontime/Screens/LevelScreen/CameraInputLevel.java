package com.argo_entertainment.reactiontime.Screens.LevelScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class CameraInputLevel extends CameraInputController {
    public CameraInputLevel(Camera camera) {
        super(camera);
    }

    public static final float TIME_TO_SCROLL = 2.0f;

    private float mTimer;
    private float mVelocityY;
    private float velocitySum = 0;


    private float startX, startY;
    float currentZoom=0;
    private final float maxZoom = .2f;
    private final float minZoom = -0.6f;

    public void update(float delta) {

        if (mTimer > 0) {// if timer is not 0
            float acceleration_y = mVelocityY * delta;// calculate acceleration (the rate of change of velocity)
            mTimer -= delta;// decreasing timer
            mVelocityY -= acceleration_y;// decreasing velocity
            velocitySum += mVelocityY;

            process(mVelocityY, 0, rotateButton);
        }
        super.update();
    }

    public void fling(float velocity){
        mTimer = TIME_TO_SCROLL;
        mVelocityY = velocity * Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mTimer = 0;
        //return super.touchDown(screenX, 960, pointer, button);
        startX = screenX;

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log("button LEVEL", button + "");
        if(button == -1)
            return super.touchDragged(screenX, screenY, pointer);

        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        startX = screenX;

        return process(deltaX, 0, rotateButton);
    }

    @Override
    protected boolean pinchZoom(float amount) {
        Gdx.app.log("ZOOM LEVEL", amount + "");
        currentZoom += amount;
        if(currentZoom>=maxZoom) currentZoom=maxZoom;
        if(currentZoom<=minZoom) currentZoom=minZoom;

        if(currentZoom>minZoom && currentZoom<maxZoom){
            return zoom(pinchZoomFactor * amount);
        }
        return false;
    }
}
