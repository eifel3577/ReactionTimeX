package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

//Актор основная сущность в 2d. Актер имеет положение, прямоугольный размер, начало, масштаб, поворот, индекс Z и цвет.
//Позиция начинается с нижнего левого края.Прорисоывается в единицу времени в методе act(). К актору применяются текущие действия
//в методе fire()
public class BackObjActor extends Actor {

    public Texture cell = null;

    //принимает текстуру,инициирует
    public BackObjActor(Texture cell) {
        this.cell = cell;
    }

    //отрисовка
    public void draw(Batch batch, float parentAlpha) {
        Color temp;
        temp = batch.getColor();

        batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

        batch.draw(new TextureRegion(cell, 0, 0, this.cell.getWidth(), this.cell.getHeight()), getX(), getY(),
                getOriginX(), getOriginY(), 135, 135, getScaleX(), getScaleY(), getRotation());

        batch.setColor(temp);
    }


}