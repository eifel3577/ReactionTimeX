package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class HoleActor extends Actor {

    public TextureMapObject cell = null;
    public boolean state = false;
    private int oldPosX = 0;
    private int oldPosY = 0;

    public HoleActor(TextureMapObject cell) {
        this.cell = cell;
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
    }

    public TextureMapObject getCell() {
        return this.cell;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {

        return super.hit(x, y, touchable);
    }

    private Action setHoleStateTrue = new Action(){
        @Override
        public boolean act(float delta){
            setHoleState(true);
            return true;
        }
    };

    private Action setHoleStateFalse = new Action(){
        @Override
        public boolean act(float delta){
            setHoleState(false);
            return true;
        }
    };

    public void setHoleState(boolean state) {
        if(cell != null) {
            if(state) {
                cell.getTextureRegion().setRegion(oldPosX, oldPosY, (int) getWidth(), (int) getHeight());
            } else {
                cell.getTextureRegion().setRegion(0, oldPosY, (int) getWidth(), (int) getHeight());
            }
        }
        this.state = state;
    }

    public void setLight(boolean state) {
        float animDuration = 0.8f;
        if(state)
            addAction(sequence(
                    fadeOut(animDuration),
                    setHoleStateTrue,
                    fadeIn(animDuration)
            ));
        else
            addAction(sequence(
                    fadeOut(animDuration),
                    setHoleStateFalse,
                    fadeIn(animDuration)
            ));

    }

    public boolean isActive() {
        return state;
    }

    public void draw(Batch batch, float parentAlpha) {
        if(cell != null) {
            Color temp;
            temp = batch.getColor();

            batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

            batch.draw(cell.getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            batch.setColor(temp);
        }
    }


}