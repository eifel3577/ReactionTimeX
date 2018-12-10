package com.argo_entertainment.reactiontime.GameField.Skill;

import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class FreezeGroup extends Group {

    public FreezeGroup(MapLayer layer, BitmapFont[] font){
        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    OtherActor actor = new OtherActor(cell);

                    actor.addAction(sequence(
                            fadeOut(0)
                    ));

                    addActor(actor);

                }
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;
                    Label.LabelStyle label1Style = new Label.LabelStyle();

                    label1Style.font = cell.getProperties().get("size") != null ? font[Integer.parseInt(cell.getProperties().get("size").toString())] : font[36];
                    label1Style.fontColor = cell.getProperties().get("color") != null ? (Color) cell.getProperties().get("color") : Color.WHITE;

                    Label label = new Label(cell.getName(),label1Style);

                    label.setName(cell.getName());
                    label.setTouchable(Touchable.disabled);

                    label.setSize(cell.getRectangle().width, cell.getRectangle().height);
                    label.setPosition(cell.getRectangle().x,cell.getRectangle().y);
                    label.setAlignment(Align.center);

                    label.setText("5");


                    label.addAction(sequence(
                            fadeOut(0)
                    ));

                    addActor(label);
                }
            }
            startAnimation(false, 0);
        }
    }


    public void startAnimation(boolean start_end, Integer timing){
        Actor[] actors = this.getChildren().begin();
        if(start_end){
            for(Actor animActor : actors){
                if(animActor != null) {
                    if(animActor.getName().equals("ice_btn_bg") || animActor.getName().equals("ice_head_bg")){
                        animActor.addAction(parallel(
                                //Actions.moveBy(0, -500, 0.5f, Interpolation.exp10In),
                                fadeIn(0.3f)
                        ));
                    }

                    if(animActor.getName().equals("mid_ice") || animActor.getName().equals("r_timer_txt")){
                        animActor.addAction(sequence(
                            fadeIn(0.3f)
                        ));
                    }
                }
            }
        } else {
            for(Actor animActor : actors){
                if(animActor != null) {
                    if(animActor.getName().equals("ice_btn_bg") || animActor.getName().equals("ice_head_bg")){
                        animActor.addAction(parallel(
                                //Actions.moveBy(0, 500, 0.4f, Interpolation.exp10In),
                                fadeOut(0.4f)
                        ));
                    }

                    if(animActor.getName().equals("mid_ice") || animActor.getName().equals("r_timer_txt")){
                        animActor.addAction(sequence(
                                fadeOut(0.3f)
                        ));
                    }
                }
            }
        }
    }

    public void setLabel(Integer value) {
        for (Actor actor :getChildren()) {
            if(actor.getName().equals("r_timer_txt")) {
                Label label_ch = (Label) actor;
                label_ch.setText(value.toString());
            }
        }
    }

}
