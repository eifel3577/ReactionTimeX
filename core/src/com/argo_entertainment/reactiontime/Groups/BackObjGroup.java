package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BackObjActor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class BackObjGroup extends Group {


    public BackObjGroup(MapLayer layer, Texture texture){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    BackObjActor actor = new BackObjActor(texture);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.disabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());

                    addActor(actor);
                }
            }
        }
    }

}
