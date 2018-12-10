package com.argo_entertainment.reactiontime.Stages;

import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

//Stage это процессор событий,которые будут исполнять участники данного Stage.Когда Stage получает входящие события,он запускает их
//на своих участников.Основной метод Stage это act().Этот метод вызывается в единицу времени и обновляет поведение всех участников
//данного Stage
//этот Stage будет работать только с филлвьюпортом
public class BackgroundStage extends Stage {
    private long startTime;

    //устанавливает текущее время
    public BackgroundStage(FillViewport cam) {
        super(cam);
        startTime = TimeUtils.millis();
    }

    //принимает имя и флаг состояния.Затем создает BackgroundGroup.Проходит по всем акторам в группе.Если имя актора соответствует пришедшему
    //в параметре,то идет проверка флага состояния.Если true,то актор становится прозрачным, если false то становится непрозрачным
    public void setBgState (String name, boolean state){
        BackgroundGroup backGroup = (BackgroundGroup) getActors().get(0);
        for (Actor actor :backGroup.getChildren()) {
            if(actor.getName().equals(name)) {
                if(state)
                    actor.addAction(fadeIn(0));
                else
                    actor.addAction(fadeOut(0));
            }
        }
    }


    //TODO не понял
    public void backSkill (String name, boolean state){
        BackgroundGroup backGroup = (BackgroundGroup) getActors().get(0);
        for (Actor actor :backGroup.getChildren()) {
            if(actor.getName().equals(name)) {
                if(state)
                    actor.addAction(fadeIn(1f, Interpolation.linear));
                else
                    actor.addAction(fadeOut(1.5f));
            }
        }
    }

    public void flashSkill (){

    }

    public void boomSkill (){

    }

    //возвращает актора по введенным координатам
    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        return super.hit(stageX, stageY * -1, touchable);
    }



    //получает время прошедшее со стартового времени.Если прошло более 800 миллисекунд,устанавливает
    //текущее время как новое стартовое время
    @Override
    public void act(float DeltaTime) {
        super.act(DeltaTime);

        float timeLapsed = TimeUtils.timeSinceMillis(startTime);

        if(timeLapsed > 800){
            startTime = TimeUtils.millis();
        }
    }

    //получает группу акторов BackgroundGroup,проходит по всем акторам в этой группе,делает всех непрозрачными
    public void setNormal() {
        BackgroundGroup backGroup = (BackgroundGroup) getActors().get(0);
        for (Actor actor :backGroup.getChildren()) {
            actor.addAction(fadeOut(0));
        }
    }
}
