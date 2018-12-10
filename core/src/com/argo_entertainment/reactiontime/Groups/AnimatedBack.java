package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BackgroundActor;
import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class AnimatedBack extends Group {

    public AnimatedBack(){

    }

    public AnimatedBack(MapLayer layer){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    BackgroundActor actor = new BackgroundActor(cell);

                    actor.setName(cell.getName());

                    actor.setTouchable(Touchable.disabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth() * cell.getScaleX(),
                            cell.getTextureRegion().getRegionHeight() * cell.getScaleY());
                    //actor.setScale(cell.getScaleX(), cell.getScaleY());

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

}
