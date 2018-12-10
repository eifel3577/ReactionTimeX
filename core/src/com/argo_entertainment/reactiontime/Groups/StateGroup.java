package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.StateActor;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class StateGroup extends Group {

    public StateGroup(MapLayer layer){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    StateActor actor = new StateActor(cell);

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

    public void setState(String name, Integer state) {
        for (Actor actor :getChildren()) {
            if (actor instanceof StateActor) {
                if(actor.getName().equals(name)) {
                    ((StateActor) actor).setState(state);
                }
            }
        }
    }

}
