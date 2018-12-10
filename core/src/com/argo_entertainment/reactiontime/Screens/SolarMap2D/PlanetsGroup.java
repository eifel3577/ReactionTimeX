package com.argo_entertainment.reactiontime.Screens.SolarMap2D;

import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class PlanetsGroup extends Group {


    public PlanetsGroup(MapLayer layer, ReactionTimeClass parent){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    String[] name = cell.getName().split("_");
                    int planetNum = Integer.parseInt(name[1]);

                    PlanetActor actor = new PlanetActor(cell);

                    actor.setName(cell.getName());

                    // actor.setTouchable(Touchable.disabled);
                    actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                    actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                            cell.getTextureRegion().getRegionHeight());


                    if(parent.isClosedPlanet(planetNum)) {
                        if(name[0].equals("planet"))
                            actor.setClose(true);
                    } else {
                        if(name[0].equals("close")) {
                            actor.setVisible(false);
                        }
                    }


                    addActor(actor);
                }
            }
        }
    }

    public void setPlanetView(int planetNum) {

    }

}
