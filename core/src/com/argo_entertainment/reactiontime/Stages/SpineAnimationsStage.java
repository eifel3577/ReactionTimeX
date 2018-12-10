package com.argo_entertainment.reactiontime.Stages;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Actors.StateActor;
import com.argo_entertainment.reactiontime.Groups.ActiveBackgroundGroup;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//Stage это процессор событий,которые будут исполнять участники данного Stage.Когда Stage получает входящие события,он запускает их
//на своих участников.Основной метод Stage это act().Этот метод вызывается в единицу времени и обновляет поведение всех участников
//данного Stage
public class SpineAnimationsStage extends Stage {
    //стартовое время
    private long startTime;

    //группа акторов ActiveBackgroundGroup
    private ActiveBackgroundGroup group;

    //отрисовщик
    private PolygonSpriteBatch polygonSpriteBatch;

    //устанавливает стартовое время как текущее,устанавливает отрисовщик,принимает вьюпорт
    public SpineAnimationsStage(Viewport cam) {
        super(cam);
        startTime = TimeUtils.millis();
        polygonSpriteBatch = new PolygonSpriteBatch();
    }

    //устанавливает стартовое время как текущее,устанавливает отрисовщик,принимает фитвьюпорт
    public SpineAnimationsStage(FitViewport cam) {
        super(cam);
        startTime = TimeUtils.millis();
        polygonSpriteBatch = new PolygonSpriteBatch();
    }

    ////устанавливает стартовое время как текущее,устанавливает отрисовщик,принимает экстендвьюпорт
    // камеры(размер сцены масштабируется чтобы соответствовать экрану,не меняя пропорций,
    //    // но более короткмй размер Stage может увеличиваться чтобы заполнить экран)
    public SpineAnimationsStage(ExtendViewport cam) {
        super(cam);
        startTime = TimeUtils.millis();
        polygonSpriteBatch = new PolygonSpriteBatch();
    }

    //принимает имя и флаг состояния.Проходит по всем акторам в Stage.Если актор принадлежит к типу SpineAnimationActor
    //и его имя соответствует пришедшему в параметре,то если флаг true то актер  будет нарисован и получит сенсорные события.
    //если false то нет
    public void setVisibleActor(String name, Boolean state) {
        for (Actor actor : getActors()) {
            if (actor instanceof SpineAnimationActor) {
                if(actor.getName().equals(name)) {
                    ((SpineAnimationActor) actor).setVisible(state);
                }
            }
        }
    }


    @Override
    public void act(float DeltaTime) {
        super.act(DeltaTime);
    }


    //TODO Matrix4 что такое
    //устанавливает матрицу для прорисовщика,проходит по всем акторам в Stage.Если актор типа
    //SpineAnimationActor,то прорисовывает его с помощью прорисовщика
    public void draw(Matrix4 camera) {

        polygonSpriteBatch.getProjectionMatrix().set(camera);
        polygonSpriteBatch.begin();
            for(Actor actor : getActors()) {
                SpineAnimationActor spineActor = (SpineAnimationActor) actor;
                spineActor.draw(polygonSpriteBatch);
            }
        polygonSpriteBatch.end();
        super.draw();
    }

    //принимает строку name.Проходит по всем акторам в Stage.Сначала проверяет относится к типу SpineAnimationActor,
    //потом проверяет есть ли у актора имя и соответствует ли оно пришедшему в параметре.Если соответствует,
    //возвращает актора.Если нет возвращает null
    public SpineAnimationActor getActor(String name) {
        for (Actor actor :getActors()) {
            if(actor instanceof SpineAnimationActor) {
                SpineAnimationActor spineActor = (SpineAnimationActor) actor;
                if (actor.getName() != null && actor.getName().equals(name)) {
                    return spineActor;
                }
            }
        }

        return null;
    }
}
