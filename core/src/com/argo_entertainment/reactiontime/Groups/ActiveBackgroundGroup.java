package com.argo_entertainment.reactiontime.Groups;

import com.argo_entertainment.reactiontime.Actors.ActiveBackgroundActor;
import com.argo_entertainment.reactiontime.Actors.BackgroundActor;
import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.TextureActor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

//класс group является актором который может содержать других акторов
//вращение и масштабирование  group отражается на его дочерних акторах.Делегирует рисование и события ввода 
//соответствующим дочерним акторам

public class ActiveBackgroundGroup extends Group {

    //конструктор принимает текстуры
    public ActiveBackgroundGroup(Texture[] textures){

        TextureActor back_actor = new TextureActor(textures[0]);
        back_actor.setName("back");
        back_actor.setTouchable(Touchable.disabled);
        addActor(back_actor);


        for(int i=0; i < 45; i++) {
            int text = MathUtils.random(9, 11);
            ActiveBackgroundActor actor = new ActiveBackgroundActor(textures[text]);
            actor.setName("star_" + i);
            actor.setTouchable(Touchable.disabled);
            addActor(actor);
        }

        for(int i=0; i < 25; i++) {
            int text = MathUtils.random(1, 8);
            ActiveBackgroundActor actor = new ActiveBackgroundActor(textures[text], i, 30f);
            actor.setName("rock_" + i);
            actor.setTouchable(Touchable.disabled);
            addActor(actor);
        }
    }

}
