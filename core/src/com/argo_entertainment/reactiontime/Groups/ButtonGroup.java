package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ButtonGroup extends Group {

    public ButtonGroup(){

    }

    public ButtonGroup(MapLayer layer){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    BtnActor actor = new BtnActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.enabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());

                    addActor(actor);
                } else if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;

                    BtnActor actor = new BtnActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.enabled);
                    actor.setOrigin(cell.getRectangle().width / 2, cell.getRectangle().height / 2);
                    actor.setBounds(cell.getRectangle().x, cell.getRectangle().y, cell.getRectangle().width,
                            cell.getRectangle().height);

                    addActor(actor);
                }
            }
        }
    }

    public ButtonGroup(MapLayer layer, Boolean forceCheck){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if(forceCheck) {
                    if(!object.getProperties().get("type").equals("btn")){
                        break;
                    }
                }

                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    BtnActor actor = new BtnActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.enabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());

                    addActor(actor);
                } else if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;

                    BtnActor actor = new BtnActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.enabled);
                    actor.setOrigin(cell.getRectangle().width / 2, cell.getRectangle().height / 2);
                    actor.setBounds(cell.getRectangle().x, cell.getRectangle().y, cell.getRectangle().width,
                            cell.getRectangle().height);

                    addActor(actor);
                }
            }
        }
    }

    public ButtonGroup(MapLayer layer, ReactionTimeClass parent){
        int nowPlanet = parent.nowPlayPlanet();
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    String[] name = cell.getName().split("_");
                    int planetNum = Integer.parseInt(name[2]);

                    if(name[1].equals("btn")) {
                        if (planetNum != nowPlanet) {
                            BtnActor actor = new BtnActor(cell);

                            actor.setName(cell.getName());

                            actor.setTouchable(Touchable.enabled);
                            actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                            actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                    cell.getTextureRegion().getRegionHeight());

                            if (!parent.isClosedPlanet(planetNum)) {
                                actor.setChecked(true);
                            }

                            addActor(actor);
                        }
                    } else {
                        if (planetNum == nowPlanet) {
                            BtnActor actor = new BtnActor(cell);

                            actor.setName(cell.getName());

                            actor.setTouchable(Touchable.enabled);
                            actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                            actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                                    cell.getTextureRegion().getRegionHeight());

                            addActor(actor);
                        }

                    }
                }
            }
        }
    }

    public void setState(String name, Boolean active) {
        for (Actor actor :getChildren()) {
            if (actor instanceof BtnActor) {
                if(actor.getName().equals(name)) {
                    if((actor.isTouchable() && !active) || (!actor.isTouchable() && active)) {
                        ((BtnActor) actor).setClick(!active);
                        actor.setTouchable(active ? Touchable.enabled : Touchable.disabled);
                    }
                    if(!actor.isTouchable() && !((BtnActor) actor).isClick())
                        ((BtnActor) actor).setClick(true);
                }
            }
        }
    }

    public Actor getActor(String name) {
        for (Actor actor :getChildren()) {
            if (actor instanceof BtnActor) {
                if(actor.getName() != null && actor.getName().equals(name)) {
                    return actor;
                }
            }
        }

        return null;
    }

}
