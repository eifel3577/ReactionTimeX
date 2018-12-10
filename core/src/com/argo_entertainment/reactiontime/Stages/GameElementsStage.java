package com.argo_entertainment.reactiontime.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

//Stage это процессор событий,которые будут исполнять участники данного Stage.Когда Stage получает входящие события,он запускает их
//на своих участников.Основной метод Stage это act().Этот метод вызывается в единицу времени и обновляет поведение всех участников
//данного Stage
//сцена игровых элементов
public class GameElementsStage extends Stage {

    //стартовое время
    private long startTime;
    //состояние равно 1
    private int state = 1;

    //устанавливает текущее время как стартовое
    public GameElementsStage(Viewport cam) {
        super(cam);
        startTime = TimeUtils.millis();
    }

    //устанавливает состояние
    public void setState (String name, int state) {
        this.state = state;
    }

    //получает время прошедшее со стартового времени.Если прошло более 0,8 секунды,устанавливает
    //текущее время как новое стартовое время
    @Override
    public void act(float DeltaTime) {
        super.act(DeltaTime);

        float timeLapsed = TimeUtils.timeSinceMillis(startTime);

        if(timeLapsed > 800){
            startTime = TimeUtils.millis();
        }
    }

    @Override
    public void draw() {
        super.draw();

    }
}
