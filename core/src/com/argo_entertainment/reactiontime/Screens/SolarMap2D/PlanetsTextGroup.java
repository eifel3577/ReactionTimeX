package com.argo_entertainment.reactiontime.Screens.SolarMap2D;

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

public class PlanetsTextGroup extends Group {

    public PlanetsTextGroup(MapLayer layer, BitmapFont font){
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

    public PlanetsTextGroup(MapLayer layer, BitmapFont[] font, ReactionTimeClass parent){
        int row_height = 48;

        if(layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject cell = (RectangleMapObject) object;

                    String[] name = cell.getName().split("_");
                    int planetNum = Integer.parseInt(name[1]);
                    Integer[][] elements = parent.getSavedElements(planetNum);

                    Label.LabelStyle label1Style = new Label.LabelStyle();

                    label1Style.font = cell.getProperties().get("size") != null ? font[Integer.parseInt(cell.getProperties().get("size").toString())] : font[36];
                    label1Style.fontColor = cell.getProperties().get("color") != null ? (Color) cell.getProperties().get("color") : Color.WHITE;

                    Label label = new Label(cell.getName(), label1Style);

                    label.setName(cell.getName());
                    label.setTouchable(Touchable.disabled);

                    label.setSize(cell.getRectangle().width, cell.getRectangle().height);
                    label.setPosition(cell.getRectangle().x,cell.getRectangle().y);
                    label.setAlignment(Align.center);
                    label.setPosition(label.getX() - 15, label.getY() - 15);

                    if(name[0].equals("count")) {
                        label.setText("/"+6);
                    } else {
                        int elementsCatches = 0;
                        for(int i = 0; i < 6; i++) {
                            if(elements[i][0].equals(elements[i][1])) {
                                elementsCatches++;
                            }
                        }
                        label.setText(elementsCatches+"");
                    }

                    addActor(label);
                }
                if (object instanceof TextureMapObject) {
                    TextureMapObject cell = (TextureMapObject) object;

                    OtherActor actor = new OtherActor(cell);

                    String[] name = cell.getName().split("_");
                    int planetNum = Integer.parseInt(name[2]);
                    Integer[][] elements = parent.getSavedElements(planetNum);

                    int elementsCatches = 0;
                    for(int i = 0; i < 6; i++) {
                        if(elements[i][0].equals(elements[i][1])) {
                            elementsCatches++;
                        }
                    }

                    if(elementsCatches == 6) {
                        actor.setState(1);
                    }

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
