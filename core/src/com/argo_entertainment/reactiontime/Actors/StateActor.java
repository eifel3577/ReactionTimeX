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

public class StateActor extends Actor {
    public TextureMapObject cell = null;
    public String type;

    private Rectangle bounds;
    private TextureRegion texture;

    public StateActor(TextureMapObject cell) {
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "other";
        this.bounds = new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());

        setName(cell.getName());
        setOrigin(cell.getOriginX(), cell.getOriginY());
        setRotation(cell.getRotation());
        setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                cell.getTextureRegion().getRegionHeight());
        texture = cell.getTextureRegion();
    }

    public TextureMapObject getCell() {
        return this.cell;
    }
    public Rectangle getBounds() {
        return this.bounds;
    }

    public void setState(Integer state) {
        texture.setRegion(texture.getRegionWidth() * state, 0, texture.getRegionWidth(), texture.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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