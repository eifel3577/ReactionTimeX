package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameElementActor extends Actor {
    public TextureMapObject cell;
    public String type;
    public boolean state = false;
    private int oldPosX = 0;
    private int oldPosY = 0;

    private TextureRegion textureRegion;
    private TextureRegion drawTexture;
    private Rectangle bounds;

    public GameElementActor(TextureMapObject cell) {
        setName(cell.getName());
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "element";
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
        drawTexture = cell.getTextureRegion();
    }

    public GameElementActor(TextureMapObject cell, TextureRegion textureRegion) {
        setName(cell.getName());
        this.textureRegion = textureRegion;
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "element";
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
        drawTexture = cell.getTextureRegion();
    }

    public TextureMapObject getCell() {
        return this.cell;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {

        return super.hit(x, y, touchable);
    }

    @Override
    protected void positionChanged() {
        bounds.setHeight(getHeight());
        bounds.setWidth(getWidth());

        bounds.setX((int)getX());
        bounds.setY((int)getY());
        super.positionChanged();
    }

    public void setTextureRegion(TextureRegion textureRegion){
        this.textureRegion = textureRegion;
    }

    private boolean ElField = false;
    public void setElemField(boolean state) {
        ElField = state;
    }

    private boolean activeElField = false;
    public void setElField(boolean state) {
        activeElField = state;
    }

    public void setState(boolean state) {
        this.state = state;
        if(!state) {
            cell.getTextureRegion().setRegion(0, 0, (int) getWidth(), (int) getHeight());
            drawTexture = cell.getTextureRegion();
        }
        else {
            drawTexture = textureRegion;
        }
    }

    public boolean getState() {
        return state;
    }

    public void draw(Batch batch, float parentAlpha) {
        if(cell != null) {
            Color temp;
            temp = batch.getColor();

            batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

            batch.draw(drawTexture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            if(ElField) {
                Color c = new Color(batch.getColor());

                if (!activeElField) {
                    batch.setColor(Color.GRAY);
                }

                batch.draw(textureRegion, getX() + (getWidth()/2) - (textureRegion.getRegionWidth()/2), getY() + (getHeight()/2) - (textureRegion.getRegionHeight()/2), 0, 0,
                        textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                batch.setColor(c);

            }

            batch.setColor(temp);
        }
    }


}