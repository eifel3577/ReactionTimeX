package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.GameElementActor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GameElementGroup extends Group {
    private int state = 1;

    public GameElementGroup(MapLayer layer, TextureRegion elementText){ //@ToDo: Вписать тип елементов для подвтаки в рамку правильных иконок
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    GameElementActor actor = new GameElementActor(cell, elementText);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.disabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());

                    addActor(actor);
                }
            }
            setState(state);
        }
    }

    int activatedCount = 0;
    public void setCountSelected(int count) {
        if(count != activatedCount) {
            activatedCount = count;
            for (Actor actor : getChildren()) {
                if (actor instanceof GameElementActor) {
                    if (state == 2) {
                        if (actor.getName().equals("game_el_"+count)) {
                            ((GameElementActor) actor).setElField(true);
                        }
                    }
                }
            }
        }
    }

    public Vector2 getElementPosition() {
        for (Actor actor :getChildren()) {
            if (actor instanceof GameElementActor) {
                if(actor.getName().equals("game_el_"+ (activatedCount + 1))){
                    return new Vector2(actor.getX(), actor.getY());
                }
            }
        }
        return null;
    }

    public Actor getElement(String name) {
        for (Actor actor :getChildren()) {
            if (actor instanceof GameElementActor) {
                if(actor.getName().equals(name)){
                    return actor;
                }
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public void setAll() {
        for (Actor actor :getChildren()) {
            if (actor instanceof GameElementActor) {
                actor.setVisible(true);
                ((GameElementActor) actor).setElemField(true);
                ((GameElementActor) actor).setElField(true);
            }
        }
    }

    public void setState(int state) {
        this.state = state;
        for (Actor actor :getChildren()) {
            if (actor instanceof GameElementActor) {
                if(state == 1) {
                    if(actor.getName().equals("timer_bg")) {
                        actor.setVisible(true);
                    } else {
                        actor.setVisible(false);
                    }
                }

                if (state == 2) {
                    int game_el_count = 2; //@ToDo: Динамическое количество необходимых елементов
                    if(actor.getName().equals("game_el_1") || actor.getName().equals("game_el_2")) {
                        actor.setVisible(true);
                        ((GameElementActor) actor).setElemField(true);
                        if(actor.getName().equals("game_el_1")){
                            actor.clearActions();
                            actor.addAction(Actions.sequence(
                                    Actions.fadeIn(0.2f),
                                    Actions.moveBy(-(actor.getWidth()), 0, 0.4f, Interpolation.bounceOut)
                            ));
                        }
                        if(actor.getName().equals("game_el_2")){
                            actor.clearActions();
                            actor.addAction(Actions.sequence(
                                    Actions.fadeIn(0.2f),
                                    Actions.moveBy((actor.getWidth()), 0, 0.4f, Interpolation.bounceOut)
                            ));
                        }
                    } else if(!actor.getName().equals("timer_bg")) {
                        actor.setVisible(false);
                    }
                }

                if (state == 3) {
                    if(actor.getName().equals("finish_el_1")) {
                        actor.setVisible(true);
                        ((GameElementActor) actor).setElemField(true);
                        ((GameElementActor) actor).setElField(true);
                    } else {
                        actor.setVisible(false);
                    }

                    if(actor.getName().equals("finish_el_back")) {
                        actor.setVisible(true);
                    }
                }
            }
        }
    }

}
