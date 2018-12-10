package com.argo_entertainment.reactiontime.GameField.Skill;

import com.argo_entertainment.reactiontime.Actors.AnimationActor;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class FlashGroup extends Group {

    public FlashGroup(MapLayer layer, BitmapFont[] font){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    if(!object.getName().equals("anim_flash")) {
                        TextureMapObject cell = (TextureMapObject) object;

                        OtherActor actor = new OtherActor(cell);
                        actor.addAction(sequence(
                                fadeOut(0)
                        ));

                        addActor(actor);
                    } else {
                        TextureMapObject cell = (TextureMapObject) object;

                        AnimationActor actor = new AnimationActor(cell);
                        actor.addAction(sequence(
                                fadeOut(0)
                        ));

                        addActor(actor);
                    }
                }
            }
            startAnimation(false);
        }
    }

    public void startAnimation(boolean start_end){
        Actor[] actors = this.getChildren().begin();
        if(start_end){
            for(Actor animActor : actors){
                if(animActor != null) {
                    if(animActor.getName().equals("header_bg") ||
                       animActor.getName().equals("mid_flash") ||
                       animActor.getName().equals("btn_bg") ||
                       animActor.getName().equals("anim_flash")) {
                        animActor.addAction(sequence(
                                fadeIn(0.1f, Interpolation.swingIn)
                        ));
                    }
                }
            }
        } else {
            for(Actor animActor : actors){
                if(animActor != null) {
                    if(animActor.getName().equals("header_bg") ||
                            animActor.getName().equals("mid_flash") ||
                            animActor.getName().equals("btn_bg") ||
                            animActor.getName().equals("anim_flash")) {
                        animActor.addAction(sequence(
                                fadeOut(0.8f, Interpolation.swingIn)
                        ));
                    }
                }
            }
        }
    }

}
