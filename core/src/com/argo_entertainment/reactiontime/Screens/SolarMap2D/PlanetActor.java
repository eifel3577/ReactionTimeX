package com.argo_entertainment.reactiontime.Screens.SolarMap2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetActor extends Actor {
    public TextureMapObject cell;
    public String type;
    public boolean state = false;

    private TextureRegion textureRegion;
    private Rectangle bounds;

    public PlanetActor(TextureMapObject cell) {
        this.cell = cell;
        this.type = cell.getProperties().get("type") != null ? cell.getProperties().get("type").toString() : "element";
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        textureRegion = cell.getTextureRegion();
    }

    public TextureMapObject getCell() {
        return this.cell;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private boolean closed = false;
    public void setClose(Boolean state) {
        closed = state;
    }

    public void draw(Batch batch, float parentAlpha) {

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if(cell != null) {
            Color c = new Color(batch.getColor());

            if(closed)
                batch.setColor(Color.GRAY);

            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            batch.setColor(c);
        }
    }


}