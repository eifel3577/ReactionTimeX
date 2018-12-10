package com.argo_entertainment.reactiontime.Stages;

import com.argo_entertainment.reactiontime.Groups.ActiveBackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

//Stage это процессор событий,которые будут исполнять участники данного Stage.Когда Stage получает входящие события,он запускает их
//на своих участников.Основной метод Stage это act().Этот метод вызывается в единицу времени и обновляет поведение всех участников
//данного Stage
public class ActiveBackgroundStage extends Stage {
    //стартовое время
    private long startTime;

    private ActiveBackgroundGroup group;

    //ширина и высота 
    public float width = 1080 , height = 1920;

    //принимает фитвьюпорт камеры(размер сцены масштабируется чтобы соответствовать экрану,не меняя пропорций,
    // но могут добавляться черные полосы с каждой стороны) и массив текстур,устанавливает текущее время в миллисекундах,активизирует группу ActiveBackGroundGroup
    //и добавляет эту группу в Stage
    public ActiveBackgroundStage(FillViewport cam, Texture[] textures) {
        super(cam);
        startTime = TimeUtils.millis();
        group = new ActiveBackgroundGroup(textures);
        addActor(group);
    }


    public ActiveBackgroundStage(FitViewport cam, Texture[] textures) {
        super(cam);
        startTime = TimeUtils.millis();
        group = new ActiveBackgroundGroup(textures);
        addActor(group);
    }

    //принимает экстендвьюпорт камеры(размер сцены масштабируется чтобы соответствовать экрану,не меняя пропорций,
    // но более короткмй размер Stage может увеличиваться чтобы заполнить экран) и массив текстур,устанавливает текущее время в миллисекундах,активизирует группу ActiveBackGroundGroup
    //и добавляет эту группу в Stage
    public ActiveBackgroundStage(ExtendViewport cam, Texture[] textures) {
        super(cam);
        startTime = TimeUtils.millis();
        group = new ActiveBackgroundGroup(textures);
        addActor(group);
    }


    @Override
    public void act(float DeltaTime) {
        super.act(DeltaTime);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
