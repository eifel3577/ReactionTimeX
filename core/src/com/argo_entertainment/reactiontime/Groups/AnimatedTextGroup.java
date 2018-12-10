package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingConfig;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

class TypingLab extends TypingLabel {

    public TypingLab(CharSequence text, LabelStyle style) {
        super(text, style);
    }
    private boolean firstTime = true;

    @Override
    protected void saveOriginalText() {
        super.saveOriginalText();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(firstTime) {
            restart();
            firstTime = false;
        }
    }
}

public class AnimatedTextGroup extends Group {

    public AnimatedTextGroup(MapLayer layer, BitmapFont[] font){
        TypingConfig.INTERVAL_MULTIPLIERS_BY_CHAR.put('\'', 0);
        TypingConfig.INTERVAL_MULTIPLIERS_BY_CHAR.put('\n', 0);
        TypingConfig.DEFAULT_WAIT_VALUE = 0.5f;
        TypingConfig.DEFAULT_SPEED_PER_CHAR = 0.06f;

        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;
                    Label.LabelStyle label1Style = new Label.LabelStyle();

                    label1Style.font = cell.getProperties().get("size") != null ? font[Integer.parseInt(cell.getProperties().get("size").toString())] : font[36];
                    label1Style.fontColor = cell.getProperties().get("color") != null ? (Color) cell.getProperties().get("color") : Color.WHITE;

                    TypingLab label = new TypingLab(cell.getName(),label1Style);

                    label.setName(cell.getName());
                    label.setTouchable(Touchable.disabled);

                    label.setSize(cell.getRectangle().width, cell.getRectangle().height);
                    label.setPosition(cell.getRectangle().getX(), cell.getRectangle().getY());
                    label.setAlignment(Align.left);
                    label.setWrap(true);

                    addActor(label);
                }
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    OtherActor actor = new OtherActor(cell);


                    addActor(actor);

                }
            }
        }
    }

    public Actor getActor(String name) {
        for (Actor actor :getChildren()) {
            if(actor.getName().equals(name)) {
                return actor;
            }
        }

        return new Actor();
    }

    public void setLabel(String name, String value) {
        for (Actor actor :getChildren()) {
            if (actor instanceof TypingLab) {
                TypingLab label = (TypingLab) actor;
                if(actor.getName().equals(name)) {
                    label.setText(value);
                    label.restart(value);
                    label.setVisible(true);
                }
            }
        }
    }

    public void setLabel(String name, String value, Integer align) {
        for (Actor actor :getChildren()) {
            if (actor instanceof TypingLab) {
                TypingLab label = (TypingLab) actor;
                if(actor.getName().equals(name)) {
                    label.setAlignment(align);
                    label.setText(value);
                    label.setVisible(true);
                    label.restart();
                }
            }
        }
    }

    public void setLabelTutorial(String name, String value, Integer align) {
        for (Actor actor :getChildren()) {
            if (actor instanceof TypingLab) {
                TypingLab label = (TypingLab) actor;
                if(actor.getName().equals(name)) {
                    label.setAlignment(align);
                    label.setText(value);
                }
            }
        }
    }

    private Action restartLabel = new Action(){
        @Override
        public boolean act(float delta){
            TypingLabel label = (TypingLabel) actor;
            if(label != null) {
                label.setVisible(true);
                label.restart();
            }

            return true;
        }
    };

    public void setVisible(int id, Boolean value, float duration) {
        String name = "element_text_" + id;
        for (Actor actor :getChildren()) {
            if (actor instanceof TypingLabel) {
                TypingLab label = (TypingLab) actor;

                float pauseDur = id == 1? 5f : 0.2f;
                float division = id == 1? 2.3f : 9f;

                if(actor.getName().equals(name) && value) {
                    label.clearActions();
                    label.addAction(Actions.sequence(
                            Actions.delay(pauseDur),
                            restartLabel,
                            Actions.fadeIn(0.4f),
                            Actions.delay(duration - (duration / division)),
                            Actions.fadeOut(0.5f)
                    ));
                }
            }
        }
    }

    // отключает видимость у всех акторов с типом TypingLab 
    public void hideAll() {
        for (Actor actor :getChildren()) {
            if (actor instanceof TypingLabel) {
                TypingLab label = (TypingLab) actor;
                label.setVisible(false);
            }
        }
    }

}
