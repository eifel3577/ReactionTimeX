package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BackObjActor;
import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class ElementGroup extends Group {


    public ElementGroup(MapLayer layer, TextureRegion textureRegion){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    String[] name = cell.getName().split("_");

                    if(name[0].equals("element")) {
                        ElementActor actor = new ElementActor(cell, textureRegion);

                        actor.setName(cell.getName());

                        actor.setTouchable(Touchable.disabled);
                        actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                        actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                cell.getTextureRegion().getRegionHeight());

                        //actor.setScale(cell.getScaleX(), cell.getScaleY());
                        /*actor.setWidth(210);
                        actor.setHeight(210);*/
                        /*
                        actor.setX(cell.getX());
                        actor.setY(cell.getY());*/

                        addActor(actor);
                    } else {
                        ElementActor actor = new ElementActor(cell);

                        actor.setName(cell.getName());

                        actor.setTouchable(Touchable.disabled);
                        actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                        actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                cell.getTextureRegion().getRegionHeight());

                        actor.setScale(cell.getScaleX(), cell.getScaleY());
                        actor.setWidth(cell.getTextureRegion().getRegionWidth());
                        actor.setHeight(cell.getTextureRegion().getRegionHeight());
                        actor.setX(cell.getX());
                        actor.setY(cell.getY());


                        addActor(actor);
                    }
                }
            }
        }
    }

    public ElementGroup(MapLayer layer, TextureRegion textureRegion, boolean big){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    String[] name = cell.getName().split("_");

                    if(name[0].equals("element")) {
                        ElementActor actor = new ElementActor(cell, textureRegion);

                        actor.setName(cell.getName());

                        actor.setTouchable(Touchable.disabled);
                        actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                        actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                cell.getTextureRegion().getRegionHeight());

                        //actor.setScale(cell.getScaleX(), cell.getScaleY());
                        actor.setWidth(220);
                        actor.setHeight(220);
                        /*
                        actor.setX(cell.getX());
                        actor.setY(cell.getY());*/

                        addActor(actor);
                    } else {
                        ElementActor actor = new ElementActor(cell);

                        actor.setName(cell.getName());

                        actor.setTouchable(Touchable.disabled);
                        actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                        actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                cell.getTextureRegion().getRegionHeight());

                        actor.setScale(cell.getScaleX(), cell.getScaleY());
                        actor.setWidth(cell.getTextureRegion().getRegionWidth());
                        actor.setHeight(cell.getTextureRegion().getRegionHeight());
                        actor.setX(cell.getX());
                        actor.setY(cell.getY());


                        addActor(actor);
                    }
                }
            }
        }
    }

    public void setPlanetView(int planetNum) {

    }

}
