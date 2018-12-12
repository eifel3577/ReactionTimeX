package com.argo_entertainment.reactiontime.GameField;

import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.GameField.Skill.ScreenShake;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialGameFieldScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.hide;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.show;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

//игровая сцена.актеры по этой сцене это игоровые элементы - которые в игре надо двигать пальцем
public class TiledMapStage extends Stage {
    //игровой элемент
    public TiledMapObjActor selActor;
    //координаты игрового элемента
    private Vector2 selPos;
    //счет
    public int SCORE;

    public int BH;
    //вибрация включена
    public boolean VIBRO = true;
    //пауза отключена
    public boolean PAUSE = false;

    public long generCount = 1;
    //стартовое время для заполнения экранов элементами
    private long startTime;
    //стартовое время для отключения элементов
    private long startTime2;
    private long startTime3;
    //текстуры
    private Texture[] textures;
    private Texture[] elements;
    private Texture[][] specials;

    //скилы по дефолту отключены
    //замедление??
    public boolean slowDown = false;
    public int slowDown_steps = 0;

    public boolean flash = false;
    public int flashUnit_count = 0;
    public boolean boom = false;

    //эффект дрожания экрана
    private ScreenShake screenShake;

    private GameFieldScreen parent;

    private int catchGameElement = 0;
    private int gameElement = 0;
    int generatedGameElement = 0;

    //набор фрагментов текстур
    private TextureRegion elementTexture;

    private int elementNum = 0;
    //левелы
    private int[][] hardLevels;

    int dragState = 0;

    public TiledMapStage(Viewport cam, Texture[] element, Texture[][] special, Texture[] texture, int[][] hardLvl, TextureRegion gameElement, int elementNum, GameFieldScreen parent) {
        super(cam);
        startTime = TimeUtils.millis();
        textures = texture;
        elements = element;
        specials = special;
        this.screenShake = new ScreenShake();
        this.hardLevels = hardLvl;
        SCORE = 0;
        this.elementTexture = gameElement;
        this.elementNum = elementNum - 1;
    }

    //принимает скрин тренировочная игра
    public TiledMapStage(Viewport cam, Texture[] element, Texture[][] special, Texture[] texture, int[][] hardLvl, TextureRegion gameElement, int elementNum, TutorialGameFieldScreen parent) {
        super(cam);
        startTime = TimeUtils.millis();
        textures = texture;
        elements = element;
        specials = special;
        this.screenShake = new ScreenShake();
        SCORE = 0;
        this.elementTexture = gameElement;
        this.elementNum = elementNum - 1;
        this.hardLevels = hardLvl;
    }

    public TiledMapStage(Viewport cam, Texture[] element, Texture[][] special, Texture[] texture, int[][] hardLvl, TextureRegion gameElement, int elementNum, GameFieldScreenX parent) {
        super(cam);
        startTime = TimeUtils.millis();
        textures = texture;
        elements = element;
        specials = special;
        this.screenShake = new ScreenShake();
        SCORE = 0;
        this.elementTexture = gameElement;
        this.elementNum = elementNum - 1;
        this.hardLevels = hardLvl;
    }


    public void slowDown (int steps){
        slowDown = true;
        slowDown_steps = steps;
    }

    public void flashSkill (){
        flash = true;
        flashUnit_count = 0;
    }

    public void boomSkill (){
        boom = true;
    }

    private int oldBH = -1;

    //выполняется каждую единицу времени
    @Override
    public void act(float DeltaTime) {
        super.act(DeltaTime);
        //эффект дрожания
        screenShake.update(DeltaTime, (OrthographicCamera) getCamera());

        //время прошедшее со стартового
        float timeLapsed = TimeUtils.timeSinceMillis(startTime);

        //если время прошедшее со стартового больше времени уровня
        //устанавливаем стартовое время текущим
        if(timeLapsed > hardLevels[BH][2]){
            startTime = TimeUtils.millis();
            //если замедление(?) отключено
            if(!slowDown)
            {
                //генерит нового актера
              newObjects(hardLevels[BH][1]);
            }
            //если скил замедления включен ,slowDown_steps уменьшается на 1
            if(slowDown) slowDown_steps--;
        }


        timeLapsed = TimeUtils.timeSinceMillis(startTime3);

        int iteration = MathUtils.random(4, 10);

        if(timeLapsed > (hardLevels[BH][2] * iteration)){
            startTime3 = TimeUtils.millis();
            /*if(!slowDown || (slowDown && slowDown_steps == 4))
            {
              if(elementCatch == 1)
                setCatchElement(4);
            }*/
            if(slowDown) slowDown_steps--;
        }


        timeLapsed = TimeUtils.timeSinceMillis(startTime2);

        if(hardLevels[elementNum][4] > 0 && timeLapsed > hardLevels[elementNum][4]){
            startTime2 = TimeUtils.millis();
            if((!slowDown || (slowDown && slowDown_steps == 4)) && oldBH != BH) {
              disableObjects(hardLevels[elementNum][5]);
            }
            if(slowDown) slowDown_steps--;
        }

        if(slowDown_steps == 0)
            slowDown = false;


        oldBH = BH;

        if(generatedGameElement != gameElement && elementCatch == 2) {
        }
        setCatchElement(gameElement);
    }

    TiledMapObjActor[] disabledActors = null;


    //отключение элементов.помещаются в список отключенных и делаются неактивными
    private void disableObjects(int count){
        Array<Actor> actors = getActors();
        actors.shuffle();
        enableObjects();

        disabledActors = new TiledMapObjActor[count];

        int disActors_count = 0;
        for (int i = 0; i < actors.size && disActors_count < count; i++) {
            TiledMapObjActor actor = (TiledMapObjActor)actors.get(i);
            if (actor.isTouchable()) {
                disabledActors[disActors_count] = actor;
                actor.setTouchable(Touchable.disabled);

                disActors_count++;

                int random_number1 = 14 + (int) (Math.random() * 2);

                actor.multicolor = false;
                actor.texture = textures[random_number1];
                actor.color_tx_str = "cell_" + random_number1 + "_disabled";
                actor.type_tx_str = 0;

                actor.setName("cell_" + random_number1 + "_disabled");
                actor.texture_collect = null;

                float duration = slowDown ? (0.2f * slowDown_steps) : 0.1f;
                actor.addAction(
                        sequence(
                                scaleTo(1f, 1f, duration),
                                show(),
                                touchable(Touchable.disabled),
                                scaleTo(1.25f, 1.25f, duration, Interpolation.smooth),
                                scaleTo(1f, 1f, duration, Interpolation.smooth2)
                        ));
            }
        }
    }

    int elementCatch = 1;

    public void setCatch(boolean elementCatch){
        if(elementCatch)
            this.elementCatch = 1;
        else
            this.elementCatch = 0;
    }


    public void setCatchElement(int count){
        Gdx.app.log("112","count = "+ String.valueOf(count));
        gameElement = count;
        Array<Actor> actors = getActors();
        actors.shuffle();

        int randGenCount = 0;
        Gdx.app.log("112","generatedGameElement = "+ String.valueOf(generatedGameElement));
        if(generatedGameElement < count) {

            for (int i = 0; i < actors.size && randGenCount < count; i++) {
                if(generatedGameElement == count) break;

                TiledMapObjActor actor = (TiledMapObjActor) actors.get(i);

                if (!actor.isVisible()) {

                    actor.multicolor = false;
                    actor.texture = null;
                    actor.textureRegion = elementTexture;
                    actor.setName("cell_9_9");

                    float duration = slowDown ? (0.2f * slowDown_steps) : 0.1f;
                    actor.addAction(
                            sequence(
                                    scaleTo(1f, 1f, duration),
                                    show(),
                                    touchable(Touchable.enabled),
                                    scaleTo(1.25f, 1.25f, duration, Interpolation.smooth),
                                    scaleTo(1f, 1f, duration, Interpolation.smooth2)
                            ));
                    generatedGameElement++;
                    randGenCount++;
                }
            }
        } else {
            int countEl = 0;
            for (int i = 0; i < actors.size; i++) {
                TiledMapObjActor actor = (TiledMapObjActor) actors.get(i);
                if(actor != null && actor.getName() != null && actor.getName().equals("cell_9_9")){
                    countEl++;
                }
            }

            countEl += (catchGameElement * 2);

            if(countEl < generatedGameElement)
                generatedGameElement = countEl;
            else elementCatch = 2;
        }

    }

    public void genCatchElement(int count){
        Array<Actor> actors = getActors();
        actors.shuffle();

        int randGenCount = 0;
        for (int i = 0; i < actors.size && randGenCount < count; i++) {
            if(generatedGameElement == count) break;
            TiledMapObjActor actor = (TiledMapObjActor) actors.get(i);

            if (!actor.isVisible()) {
                randGenCount++;

                actor.multicolor = false;
                actor.texture = null;
                actor.textureRegion = elementTexture;
                actor.setName("cell_9_9");

                float duration = slowDown ? (0.2f * slowDown_steps) : 0.1f;
                actor.addAction(
                        sequence(
                                scaleTo(1f, 1f, duration),
                                show(),
                                touchable(Touchable.enabled),
                                scaleTo(1.25f, 1.25f, duration, Interpolation.smooth),
                                scaleTo(1f, 1f, duration, Interpolation.smooth2)
                        ));
                generatedGameElement++;
            }
        }

    }

    //координаты позиции игрового элемента
    private Vector2 game_el_position = null;

    public void setGameElementPosition(Vector2 position){
        game_el_position = position;
    }

    public int getCatchElement(){
        return gameElement;
    }

    public int getCatchedElement(){
        return catchGameElement;
    }

    //включение игровых элементов
    private void enableObjects(){
        //если список отключенных элементов непустой,проходит по нему и по очереди делает каждый элемент включенным
        //ставит ему type_tx_str в 1
        if(disabledActors != null) {
            int disActors_count = 0;
            for (int i = 0; i < disabledActors.length; i++) {
                TiledMapObjActor actor = disabledActors[disActors_count];
                disabledActors[disActors_count] = null;
                disActors_count++;
                actor.type_tx_str = 1;

                actor.setName("cell_1_0");
                actor.addAction(
                        sequence(
                                scaleTo(1.25f, 1.25f, 0.2f, Interpolation.smooth),
                                scaleTo(0.75f, 0.75f, 0.2f, Interpolation.smooth2),
                                hide(),
                                touchable(Touchable.enabled)
                        ));
            }
        }
        //обнуляет список отключенных элементов
        disabledActors = null;
    }

    int multiCount = 0;

    //генерит новый игровой элемент на игровом поле
    public void newObjects(int count){
        //получает актеров
        Array<Actor> actors = getActors();
        //перемешивает актеров
        actors.shuffle();
        //в зависимости от уровня разный multiCount
        if(BH == 1) multiCount = 2;
        if(BH == 3) multiCount = 4;

        //случайное число от 1 до 1000
        int special = 1 + (int) (Math.random() * 1000);
        //счетчик новых игровых элементов
        int newActors_count = 0;

        //проходит по игровым элементам
        for (int i = 0; i < actors.size && newActors_count < count; i++) {
            //приводит каждый элемент к типу TiledMapObjActor
            TiledMapObjActor actor = (TiledMapObjActor)actors.get(i);

            //если элемент невидимый
            if (!actor.isVisible()){
                //увеличиваем счетчик новых элементов на 1
                newActors_count++;
                //случайное число от 1 до 5
                int random_number1 = 1 + (int) (Math.random() * 4);
                //multicolor отключен
                actor.multicolor = false;
                //устанавливаем для элемента случайную текстуру из массива elements
                actor.texture = elements[random_number1];
                //случайный цвет текста (?)
                actor.color_tx_str = "cell_" + random_number1 + "_0";

                //если левел 1 или больше ,а special от 401 до 599 то установка в texture_collect случайного значения из specials
                //установка названия игрового элемента
                if(BH >= 1 && special > 400 && special < 600) { //@ToDo: Rewrite this shit like normal logic -_-

                    int random_spec = 1 + (int) (Math.random() * multiCount);

                    if(random_spec == 4) {
                        actor.multicolor = true;
                        actor.texture_collect = specials[random_spec][1];
                    } if(random_spec == 3 || random_spec == 2) {
                        actor.texture_collect = specials[random_spec][1];
                        actor.texture = elements[1];
                        actor.setName("cell_" + 1 + "_" + random_spec);
                    } else {
                        actor.texture_collect = specials[random_spec][random_number1];
                        actor.setName("cell_" + random_number1 + "_" + random_spec);
                    }

                    special = 0;
                } else {
                    actor.setName("cell_" + random_number1 + "_0");
                    actor.texture_collect = null;
                }

                float duration = slowDown ? (0.2f * slowDown_steps) : 0.1f;
                //действие игрового элемента.При появлении он сначала увеличивается потом снова
                //уменьшается
                actor.addAction(
                        sequence(
                                scaleTo(1f, 1f, duration),
                                show(),
                                touchable(Touchable.enabled),
                                scaleTo(1.25f, 1.25f, duration, Interpolation.smooth),
                                scaleTo(1f, 1f, duration, Interpolation.smooth2)
                        ));
            }
        }
        //обновляет значение generCount,так как появился новый игровой элемент
        generCount = newActors_count;
    }

    //делает все элементы невидимыми
    public void clearObjects(int count){
        Array<Actor> actors = getActors();
        actors.shuffle();

        int newActors_count = 0;
        for (int i = 0; i < actors.size && newActors_count < count; i++) {
            TiledMapObjActor actor = (TiledMapObjActor)actors.get(i);
            if (actor.isVisible()){
                newActors_count++;
                actor.setVisible(false);
            }
        }

        generCount = newActors_count;
    }

    //взрыв
    private void boomDestroy(Actor center){
        int startPosX, startPosY, PosX, PosY;

        startPosX = (int) center.getX() - (int) center.getWidth() + (int) center.getOriginX();
        startPosY = (int) center.getY() - (int) center.getHeight() + (int) center.getOriginY();
        PosX = startPosX;
        PosY = startPosY;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                TiledMapObjActor findActor = (TiledMapObjActor) super.hit(PosX, PosY, true);
                if(findActor != null) {
                    findActor.skillAnim("boom");
                    findActor.setTouchable(Touchable.disabled);
                    findActor.addAction(
                            sequence(
                                    scaleTo(1.25f, 1.25f, 0.2f, Interpolation.smooth),
                                    scaleTo(0.75f, 0.75f, 0.2f, Interpolation.smooth2),
                                    hide(),
                                    touchable(Touchable.enabled)
                            ));
                    SCORE++;

                    if(findActor.getName().equals("cell_9_9")) {
                        generatedGameElement--;
                    }
                }

                PosX += 135;
            }

            PosY += 135;
            PosX = startPosX;
        }

        Assets.playSound(Assets.boombSound);

        if(VIBRO) {
            Gdx.input.vibrate(30);
            screenShake.shake(30, 700);
            Gdx.input.vibrate(30);
        }


        boom = false;
        selActor = null;
    }

    //молния
    //получает элемент который был в ячейке куда перетаскивали
    private void flashDestroy(Actor center, boolean vert) {
        int startPosX, startPosY, PosX, PosY;

        startPosX = (int) center.getX() + (int) center.getOriginX();
        startPosY = (int) center.getY() + (int) center.getOriginY();
        PosX = startPosX;
        PosY = startPosY;

        //8 раз
        for(int i = 0; i < 8; i++){
            //
            TiledMapObjActor findActor = (TiledMapObjActor) super.hit(PosX, PosY, true);
            if(findActor != null) {
                findActor.skillAnim("flash");
                findActor.setTouchable(Touchable.disabled);
                findActor.addAction(
                        sequence(
                                scaleTo(1.25f, 1.25f, 0.2f, Interpolation.smooth),
                                scaleTo(0.75f, 0.75f, 0.2f, Interpolation.smooth2),
                                hide(),
                                touchable(Touchable.enabled)
                        ));
                SCORE++;

                if(findActor.getName().equals("cell_9_9")) {
                    generatedGameElement--;
                }
            }

            if (vert) PosY += 135; else PosX += 135;
        }

        PosX = startPosX;
        PosY = startPosY;

        for(int j = 0; j < 8; j++){
            TiledMapObjActor findActor = (TiledMapObjActor) super.hit(PosX, PosY, true);
            if(findActor != null) {
                findActor.skillAnim("flash");
                findActor.setTouchable(Touchable.disabled);
                findActor.addAction(
                        sequence(
                                scaleTo(1.25f, 1.25f, 0.2f, Interpolation.smooth),
                                scaleTo(0.75f, 0.75f, 0.2f, Interpolation.smooth2),
                                hide(),
                                touchable(Touchable.enabled)
                        ));
                SCORE++;

                if(findActor.getName().equals("cell_9_9")) {
                    generatedGameElement--;
                }
            }

            if (vert) PosY -= 135; else PosX -= 135;
        }
        if(VIBRO) {
            Gdx.input.vibrate(30);
            screenShake.shake(30, 700);
            Gdx.input.vibrate(30);
        }

        Assets.playSound(Assets.flash_1);


        flash = false;
        selActor = null;
    }

    //нажатие на элемент
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (selActor != null) {
            //устанавливает для элемента координаты
            selActor.setPosition(selPos.x, selPos.y);
            selActor.setTouchable(Touchable.enabled);
        }

        if(!PAUSE) {

            //координаты элемента
            Vector2 pos = screenToStageCoordinates(new Vector2(screenX, screenY));
            //получает элемент по этим координатам
            selActor = (TiledMapObjActor) super.hit(pos.x, pos.y, true);


            if (selActor != null) {
                //состояние 1
                dragState = 1;

                if (boom) {
                    boomDestroy(selActor);
                } else {

                    int index = super.getActors().indexOf(selActor, false);

                    //позиция нажатого элемента
                    selPos = new Vector2(selActor.getX(), selActor.getY());
                    selActor.setTouchable(Touchable.disabled);
                    //нажатый элемент увеличивается
                    selActor.addAction(
                            scaleTo(1.25f, 1.25f)
                    );
                }
            } else if (boom) {
                boom = false;
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    //перетаскивание
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(!PAUSE) {

            //координаты куда идет перетаскивание
            Vector2 pos = screenToStageCoordinates(new Vector2(screenX, screenY));
            if (selActor != null) {
                selActor.setPosition(pos.x - selActor.getOriginX(), pos.y - selActor.getOriginY());
                selActor.setXY(pos.x - selActor.getOriginX(), pos.y - selActor.getOriginY());

                //обьект который находится на том месте куда идет перетаскивание
                TiledMapObjActor with = (TiledMapObjActor) super.hit(pos.x, pos.y, false);

                if (flash) {
                    with = (TiledMapObjActor) super.hit(pos.x, pos.y, true);
                    if (with != null && with.getName() != null && selActor != null && with.getName().equals(selActor.getName())) {
                        with.skillAnim("flash");

                        with.setTouchable(Touchable.disabled);
                        with.addAction(
                                sequence(
                                        scaleTo(1.25f, 1.25f, 0.2f, Interpolation.smooth),
                                        scaleTo(0.75f, 0.75f, 0.2f, Interpolation.smooth2),
                                        hide(),
                                        touchable(Touchable.enabled)
                                ));
                        SCORE++;
                        flashUnit_count++;
                        Assets.playSound(Assets.flash_2);
                        if (VIBRO) Gdx.input.vibrate(10);
                    }
                }

                //если на там месте куда идет перетаскивание есть элемент и у него есть название и его тип 0 (то есть он не неактивноый)
                if (with != null && with.getName() != null && with.type_tx_str == 0) {


                    if(selActor.getName().equals("cell_9_9")) {
                        selActor.addAction(
                                sequence(
                                        scaleTo(1f, 1f, 0.05f, Interpolation.smooth2),
                                        //перемещение
                                        moveTo(selPos.x, selPos.y, 0.10f, Interpolation.swingOut),
                                        touchable(Touchable.enabled)
                                ));

                    } else {

                        selActor.skillAnim("flash");

                        selActor.addAction(
                                sequence(
                                        scaleTo(1.25f, 1.25f, 0.05f, Interpolation.smooth),
                                        scaleTo(0.75f, 0.75f, 0.05f, Interpolation.smooth2),
                                        hide(),
                                        moveTo(selPos.x, selPos.y)
                                ));

                        String[] name = selActor != null ? selActor.getName().split("_") : null;
                        if(name != null && name[2].equals("9")) {
                            selActor.addAction(show());
                        }

                    }
                    //состояние 0
                    dragState = 0;
                    //перетаскиваемый элемент сброшен
                    selActor = null;
                    //звук и вибрация
                    Assets.playSound(Assets.wallSound);
                    if (VIBRO) Gdx.input.vibrate(20);
                }
            }
        }

        return super.touchDragged(screenX, screenY, pointer);
    }



    //отпускание пальца
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //координаты отпускания
        Vector2 pos = screenToStageCoordinates(new Vector2(screenX, screenY));
        //элемент который в этих координатах
        TiledMapObjActor with = (TiledMapObjActor) super.hit(pos.x, pos.y, true);

        //не включена бомба
        if(!boom) {
            //если включена молния и есть перетаскиваемый элемент
            if(flash && selActor != null) {
                flash = false;
                if(flashUnit_count != 0) {
                    selActor.addAction(
                            sequence(
                                    scaleTo(1.25f, 1.25f, 0.05f, Interpolation.smooth),
                                    scaleTo(0.75f, 0.75f, 0.05f, Interpolation.smooth2),
                                    hide(),
                                    moveTo(selPos.x, selPos.y)
                            ));
                }
            }


            //имя перетаскиваемого элемента
            String[] name = selActor != null ? selActor.getName().split("_") : null;
            //имя элемента который был в точке куда перетащили
            String[] withName = with != null ? with.getName().split("_") : null;

            //если имена и того и другого заканчиваются на 11
            boolean isMultiSel = (name != null && name[2].equals("11"));
            boolean isMultiWith = (withName != null && withName[2].equals("11"));


            //если названия обоих соответствуют (желтый-желтый и тп) или названия одного или другого заканчиваются на 11
            if(with != null && with.getName() != null && selActor != null && (with.getName().equals(selActor.getName()) || isMultiSel || isMultiWith)){
                //случай если названия одного или другого заканчиваются на 11
                if(isMultiSel || isMultiWith){
                    //включается на перетаскиваемом элементе анимация dispose
                    selActor.skillAnim("dispose");
                    //включается на элементе который в месте перетаскивания анимация dispose
                    with.skillAnim("dispose");
                }
                //отключается на элементе который в месте перетаскивания кликабельность
                with.setTouchable(Touchable.disabled);
                with.addAction(
                        sequence(
                                scaleTo(1.25f, 1.25f, 0.05f, Interpolation.smooth),
                                scaleTo(0.75f, 0.75f, 0.05f, Interpolation.smooth2),
                                hide(),
                                touchable(Touchable.enabled)
                        ));
                //состояние 3
                dragState = 3;
                //название перетаскиваемого элемента заканчивается на 9
                if(name[2].equals("9")) {

                    //MoveToAction - класс экшн перемещения актера из его текущей позиции в конкретную позицию
                    MoveToAction action = new MoveToAction();
                    action.setPosition(game_el_position.x , game_el_position.y);
                    action.setDuration(0.4f);
                    action.setInterpolation(Interpolation.exp5In);
                    //действия на перетаскиваемом элементе в последовательности
                    selActor.addAction(sequence(
                            scaleTo(1.5f, 1.5f, 0.2f, Interpolation.smooth),
                            action,
                            scaleTo(1f, 1f, 0.2f, Interpolation.smooth),
                            hide(),
                            moveTo(selPos.x, selPos.y)
                    ));
                    SCORE++;
                    if(VIBRO) Gdx.input.vibrate(10);
                    Assets.playSound(Assets.clickSound);
                    //увеличение счетчика очков
                    catchGameElement++;
                    Assets.playVoiceRandom();
                }
                //название перетаскиваемого элемента не заканчивается на 0
                else if(!name[2].equals("0")) {
                    //действия на перетаскиваемом элементе в последовательности
                    selActor.addAction(
                            sequence(
                                    scaleTo(1.25f, 1.25f, 0.05f, Interpolation.smooth),
                                    scaleTo(0.75f, 0.75f, 0.05f, Interpolation.smooth2),
                                    hide(),
                                    moveTo(selPos.x, selPos.y)
                            ));
                    //название перетаскиваемого элемента заканчивается на 1
                    if(name[2].equals("1")){

                        int sum_score = 0;
                        //проходит по всем элементам
                        for (Actor actor : getActors()) {

                            if(actor.getName() != null && (actor.getName().equals("cell_" + name[1] + "_0") || actor.getName().equals("cell_" + name[1] + "_10"))) {
                                sum_score++;

                                TiledMapObjActor findActor = (TiledMapObjActor) actor;
                                //запуск анимации бомба
                                findActor.skillAnim("boom");
                                findActor.setTouchable(Touchable.disabled);

                                actor.addAction(
                                        sequence(
                                                scaleTo(1.25f, 1.25f, 0.05f, Interpolation.linear),
                                                scaleTo(0.75f, 0.75f, 0.05f, Interpolation.linear),
                                                hide()
                                        ));
                            }
                        }
                        SCORE+=sum_score;

                        Assets.playSound(Assets.boombSound);

                        if(VIBRO) {
                            Gdx.input.vibrate(30);
                            screenShake.shake(30, 700);
                            //parent.boomSound();
                            Gdx.input.vibrate(30);
                        }
                    }
                    //название перетаскиваемого элемента заканчивается на 3
                    if(name[2].equals("3")){
                        flashDestroy(with, false);
                    }
                    //название перетаскиваемого элемента заканчивается на 2
                    if(name[2].equals("2")){
                        flashDestroy(with, true);
                    }
                } else {
                    selActor.addAction(
                            sequence(
                                    scaleTo(1.25f, 1.25f, 0.05f, Interpolation.smooth),
                                    scaleTo(0.75f, 0.75f, 0.05f, Interpolation.smooth2),
                                    hide(),
                                    moveTo(selPos.x, selPos.y)
                            ));
                    SCORE++;
                    dragState = 3;
                    if(VIBRO) Gdx.input.vibrate(10);
                    Assets.playSound(Assets.clickSound);
                }

            } else if (selActor != null) {
                MoveToAction action = new MoveToAction();
                action.setPosition(selPos.x, selPos.y);
                action.setDuration(0.075f);
                action.setInterpolation(Interpolation.linear);
                selActor.addAction(sequence(
                        action,
                        scaleTo(1f, 1f)
                ));
                dragState = 0;
                selActor.setTouchable(Touchable.enabled);
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        return super.hit(stageX, stageY * -1, touchable);
    }
}
