package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class TextureActor extends Actor {
    public Texture texture = null;
    // определяет прямоугольную область текстуры
    private TextureRegion textureRegion;
    private Float speed = 20f;
    public String type = "btn";

    private Vector2 dimension;
    private Vector2 position;

    public TextureActor(Texture texture) {
        this.texture = texture;
        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0, texture.getWidth(), texture.getHeight());
        position = new Vector2(0,0);
    }

    //метод по отрисовке актора
    public void draw(Batch batch, float parentAlpha) {
        Color temp;
        temp = batch.getColor();

        batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

        batch.draw(textureRegion, position.x, position.y, textureRegion.getRegionWidth() / 2, textureRegion.getRegionHeight() / 2,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, 0);

        batch.setColor(temp);
    }


}
