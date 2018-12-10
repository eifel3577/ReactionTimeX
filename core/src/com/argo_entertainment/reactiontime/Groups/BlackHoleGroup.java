package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.HoleActor;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class BlackHoleGroup extends Group {

    public BlackHoleGroup(){

    }

    public BlackHoleGroup(MapLayer layer){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    HoleActor actor = new HoleActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.disabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());

                    actor.setLight(false);
                    addActor(actor);
                }
            }
        }
    }

}