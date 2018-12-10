package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ElementActor extends Actor {
    public TextureMapObject cell;
    public String type;
    public boolean state = false;
    private int oldPosX = 0;
    private int oldPosY = 0;

    private TextureRegion textureRegion;
    private TextureRegion drawTexture;
    private Rectangle bounds;

    public ElementActor(TextureMapObject cell) {
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "element";
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
        textureRegion = cell.getTextureRegion();
    }

    public ElementActor(TextureMapObject cell, TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        textureRegion.setRegion(0,0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "element";
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
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

        bounds.setX((int)getX());
        bounds.setY((int)getY());
        super.positionChanged();
    }

    private boolean ElField = false;
    public void setElemField(boolean state) {
        ElField = state;
    }

    private boolean activeElField = false;
    public void setElField(boolean state) {
        activeElField = state;
    }

    int planetNum = 1, elementNum = 1;
    public void setState(int planet, int element) {
        planetNum = planet;
        elementNum = element;
    }

    public boolean getState() {
        return state;
    }

    public void draw(Batch batch, float parentAlpha) {

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if(cell != null) {

            if(elementNum >= 0) {
                textureRegion.setRegion(textureRegion.getRegionWidth() * (planetNum - 1), textureRegion.getRegionHeight() * (elementNum - 1), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

                batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            } else {
                int row = 0, planetNu = 0;
                if(planetNum <= 3) {
                    row = 0;
                    planetNu = planetNum;
                } else if(planetNum <= 6) {
                    row = 1;
                    planetNu = planetNum - 3;
                } else {
                    row = 2;
                    planetNu = planetNum - 6;
                }
                textureRegion.setRegion(textureRegion.getRegionWidth() * (planetNu - 1), textureRegion.getRegionHeight() * row, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

                batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }
    }


}