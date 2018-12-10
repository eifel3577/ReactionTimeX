package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class TextGroup extends Group {

    public TextGroup(MapLayer layer, BitmapFont font){
        int row_height = 48;

        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;
                    Label.LabelStyle label1Style = new Label.LabelStyle();

                    label1Style.font = font;
                    label1Style.fontColor = cell.getProperties().get("color") != null ? (Color) cell.getProperties().get("color") : Color.WHITE;

                    Label label = new Label(cell.getName(),label1Style);

                    label.setName(cell.getName());
                    label.setTouchable(Touchable.disabled);

                    label.setSize(cell.getRectangle().width, cell.getRectangle().height);
                    label.setPosition(cell.getRectangle().x,cell.getRectangle().y);
                    label.setAlignment(Align.center);

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

    public TextGroup(MapLayer layer, BitmapFont[] font){
        int row_height = 48;

        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
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

    public void setBackPlanet(int planetNum, ReactionTimeClass parent) {
        for (Actor element :getChildren()) {
            if (element instanceof OtherActor) {
                OtherActor actor = (OtherActor) element;
                String[] name = actor.getName().split("_");
                if (name[0].equals("label")) {
                    int elementNum = Integer.parseInt(name[2]);
                    Integer[] elements = parent.getSavedElements(planetNum, elementNum);

                    if (elements[0].equals(elements[1])) {
                        actor.setState(1);
                    } else {
                        actor.setState(0);
                    }
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

    public void setLabelColor(String name, Color value) {
        for (Actor actor :getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                if(actor.getName().equals(name)) {
                    label.getStyle().fontColor = value;
                }
            }
        }
    }
    public void setLabel(String name, String value) {
        for (Actor actor :getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                if(actor.getName().equals(name)) {
                    label.setText(value);
                    label.setVisible(true);
                }
            }
        }
    }

    public void setLabel(String name, String value, Integer align) {
        for (Actor actor :getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                if(actor.getName().equals(name)) {
                    label.setAlignment(align);
                    label.setText(value);
                    label.setVisible(true);
                }
            }
        }
    }

    public void setVisible(String name, Boolean value) {
        for (Actor actor :getChildren()) {
            if (actor instanceof Label) {
                Label label = (Label) actor;
                if(actor.getName().equals(name)) {
                    label.setVisible(value);
                }
            }
        }
    }

}
