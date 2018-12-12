package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class OtherActor extends Actor {
    public TextureMapObject cell = null;
    public String type;

    private Rectangle bounds;
    private TextureRegion texture;

    public OtherActor(RectangleMapObject cell) {
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "other";
    }

    public OtherActor(TextureMapObject cell) {
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "other";
        this.bounds = new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());

        setName(cell.getName());
        setTouchable(Touchable.disabled);
        setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
        setRotation(cell.getRotation());
        setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                cell.getTextureRegion().getRegionHeight());
        setScale(cell.getScaleX(), cell.getScaleY());
        texture = cell.getTextureRegion();
    }

    public TextureMapObject getCell() {
        return this.cell;
    }
    public Rectangle getBounds() {
        return this.bounds;
    }

    public void startAct(Integer start_steps, Integer timing) {
        float duration = (start_steps - 2) * ((float)timing / 1000f);
        this.addAction(sequence(
                Actions.scaleTo(1f, 1f),
                Actions.scaleTo(1f, 0, duration, Interpolation.linear)));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    public void setState(int state){
        if(cell != null) {
            texture.setRegion((int) getWidth() * state, 0, (int) getWidth(), (int) getHeight());
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if(cell != null) {
            Color temp;
            temp = batch.getColor();

            batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            batch.setColor(temp);
        }
    }


}