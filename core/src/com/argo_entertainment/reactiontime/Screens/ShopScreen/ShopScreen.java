package com.argo_entertainment.reactiontime.Screens.ShopScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.ElementGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.Screens.PurchasesScreen.PurchasesScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import java.lang.annotation.Repeatable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class ShopScreen implements Screen, GestureDetector.GestureListener {

    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private Stage top_stage;

    private ButtonGroup btnGroup;
    private BackgroundGroup bgGroup;
    private TextGroup textGroup;
    private TextGroup upTextGroup;
    private ElementGroup elementGroup;


    private Integer skills[] = new Integer[3];
    private Integer coins = 0;

    private Integer planetNum = 1;
    private Integer elementNum = 1;
    private Boolean closed = false;


    private SpineAnimationsStage spineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private ScalingViewport fitViewportTop;

    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    private TextureAtlas atlas;

    public ShopScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;
        parent.assetManager.load("shop/shop_map.tmx", TiledMap.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void show() {

        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        TiledMap tiledMap = parent.tiledMap("shop/shop_map.tmx");
        BitmapFont[] font = parent.getFonts();

        batch = new SpriteBatch();
        //летающие планеты заднего фона
        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());


        stage = new Stage(fitViewport);
        top_stage = new Stage(fitViewportTop);

        //верхняя часть экрана синяя
        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        top_stage.addActor(btnBack);
        //кнопки + назад и сундук
        ButtonGroup topBtnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        top_stage.addActor(topBtnGroup);
        //количество очков
        upTextGroup = new TextGroup(tiledMap.getLayers().get("top_txt"), font);
        top_stage.addActor(upTextGroup);


        //кнопки влево вправо и шесть кнопок товаров
        bgGroup = new BackgroundGroup(tiledMap.getLayers().get("menu_bg"));
        stage.addActor(bgGroup);

        //синие круги товаров
        elementGroup = new ElementGroup(tiledMap.getLayers().get("elements"), parent.getBigElements(1,1), true);
        stage.addActor(elementGroup);

        //кнопка самой планеты с красным полем unlock,и красные поля под каждой планетой
        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);
        //синие кружочки возле каждого товара и белая надпись количество RM
        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), font);
        textGroup.setBackPlanet(planetNum, parent);
        stage.addActor(textGroup);

        //пауза 0.1
        float pause = 0.1f;

        //проходит по синим кругам товаров
        for(Actor elementActor : elementGroup.getChildren()){
            String[] name = elementActor.getName().split("_");

            int num = -1;

            if(name[0].equals("element")) {
                num = Integer.parseInt(name[1]);

                setActionElement(elementActor, pause, num);

                pause += 0.1f;
            } else {

                setActionElement(elementActor, 0, num);
            }

            ((ElementActor) elementActor).setState(planetNum, num);
        }

        spineAnimationsStage = new SpineAnimationsStage(fitViewportTop);

        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                spineAnimationsStage.addActor(actor);
            }
        }

        //установка обработки нажатия кнопки Назад
        setControls();
        //загружает количество коинов
        coins = parent.getNumbers("coins");
    }

    private Integer[][] elementsPrice = new Integer[][]{
            {
                5,10,15,30,40,100
            },{
                10,15,20,40,60,150
            },{
                10,15,20,40,60,150
            },{
                20,25,40,80,100,200
            },{
                20,25,40,80,100,200
            },{
                25,30,50,100,120,250
            },{
                25,30,50,100,120,250
            },{
                30,40,50,120,150,300
            },{
                30,40,50,120,150,300
            }
    };

    //прорисовка каждую единицу времени
    @Override
    public void render(float delta) {
        //все актеры с именем closed видимы или невидимы в зависимости от флага closed
        btnGroup.findActor("closed").setVisible(closed);
        //устанавливает для каждого синего круга соотвтетсвующую сумму RM
        textGroup.setLabel("elem_1", elementsPrice[planetNum - 1][0] + " RM");
        textGroup.setLabel("elem_2", elementsPrice[planetNum - 1][1] + " RM");
        textGroup.setLabel("elem_3", elementsPrice[planetNum - 1][2] + " RM");
        textGroup.setLabel("elem_4", elementsPrice[planetNum - 1][3] + " RM");
        textGroup.setLabel("elem_5", elementsPrice[planetNum - 1][4] + " RM");
        textGroup.setLabel("elem_6", elementsPrice[planetNum - 1][5] + " RM");
        //устанавливает значения 0/1, 2/2 и тд
        textGroup.setLabel("elem_count_1", parent.getSavedElements(planetNum, 1)[0] + "/" + parent.getSavedElements(planetNum, 1)[1]);
        textGroup.setLabel("elem_count_2", parent.getSavedElements(planetNum, 2)[0] + "/" + parent.getSavedElements(planetNum, 2)[1]);
        textGroup.setLabel("elem_count_3", parent.getSavedElements(planetNum, 3)[0] + "/" + parent.getSavedElements(planetNum, 3)[1]);
        textGroup.setLabel("elem_count_4", parent.getSavedElements(planetNum, 4)[0] + "/" + parent.getSavedElements(planetNum, 4)[1]);
        textGroup.setLabel("elem_count_5", parent.getSavedElements(planetNum, 5)[0] + "/" + parent.getSavedElements(planetNum, 5)[1]);
        textGroup.setLabel("elem_count_6", parent.getSavedElements(planetNum, 6)[0] + "/" + parent.getSavedElements(planetNum, 6)[1]);

        //пишет количество коинов
        upTextGroup.setLabel("coins", "" + coins);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        //act для всех актеров во всех сценах
        stage.act(delta);
        top_stage.act(delta);
        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);

        //прорисовка
        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            activeBackgroundStage.draw();
        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            stage.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            top_stage.draw();
            spineAnimationsStage.draw(fitViewportTop.getCamera().combined);

        batch.end();
    }

    //обновления видовых экранов
    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        fitViewportTop.update(width, height);
        fillViewport.update(width, height, true);

        int offset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        top_stage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                offset);
        Gdx.app.log("getTopGutterHeight", "" + fitViewport.getTopGutterHeight());
        Gdx.app.log("getIOSSafeAreaInsets", parent.getIOSSafeAreaInsets().toString());
        Gdx.app.log("offset", offset + "");
        Gdx.app.log("fitViewportTop getScreenY", fitViewportTop.getScreenY() + "");
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        top_stage.dispose();
        spineAnimationsStage.dispose();
        atlas.dispose();
    }

    private BtnActor selActor;

    //обработка клика
    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();
            //получает тип актера
            String type = selActor.getName();
            if (type.equals("back")) {
                goBack();
            }
            if (type.equals("shop"))
                parent.setScreen(new PurchasesScreen(parent, camera, this));

            if (type.equals("next"))
                if(planetNum < 9) changePlanet(planetNum + 1); else changePlanet(1);
            if (type.equals("prev"))
                if(planetNum > 1) changePlanet(planetNum - 1); else changePlanet(9);

            //при закрытии экрана магазина сохраняет значения
            if(type.equals("closed") && !parent.isClosedPlanet(planetNum - 1)) {

                for(int i=1; i<=6; i++) {
                    int num = i;
                    Integer[] el = parent.getSavedElements(planetNum - 1, num);
                    int count = el[1];
                    if (el[0] < el[1] && coins >= (elementsPrice[planetNum - 1][num - 1] * count)) {
                        parent.inclSettings("all_items", count);
                        parent.setSavedElement(planetNum - 1, num, count);
                        parent.inclSettings("coins", (elementsPrice[planetNum - 1][num - 1] * count) * -1);
                        coins = parent.getNumbers("coins");
                    }
                }

                changePlanet(planetNum);
            }

            String[] name = type.split("_");
            if(name.length == 3 && name[1].equals("buy") && !parent.isClosedPlanet(planetNum)){
                int num = Integer.parseInt(name[2]);
                Integer[] el = parent.getSavedElements(planetNum, num);
                if(el[0] < el[1] && coins >= elementsPrice[planetNum - 1][num - 1]) {
                    parent.inclSettings("all_items", 1);
                    parent.setSavedElement(planetNum, num, el[0] + 1);
                    parent.inclSettings("coins", elementsPrice[planetNum - 1][num - 1] * -1);
                    coins = parent.getNumbers("coins");
                    textGroup.setBackPlanet(planetNum, parent);
                }
            }
        }
    }

    //TODO хз что делает
    private void setActionElement(Actor elementActor, float pause, int num) {
        //очищает актера от всех действий
        elementActor.clearActions();

        if (pause != 0f) {

            elementActor.addAction(sequence(
                    fadeOut(0), scaleTo(0,0), delay(pause),
                    parallel(fadeIn(0.2f, Interpolation.linear), scaleTo(1.1f,1.1f,0.2f, Interpolation.swing))
            ));


            textGroup.getActor("label_bg_" + num).addAction(sequence(
                    fadeOut(0), scaleTo(0,0), delay(pause),
                    parallel(fadeIn(0.2f, Interpolation.linear), scaleTo(1f,1f,0.2f, Interpolation.swing))
            ));

            pause += 0.1f;

            textGroup.getActor("elem_" + num).addAction(sequence(
                    fadeOut(0), scaleTo(0,0), delay(pause),
                    parallel(fadeIn(0.25f, Interpolation.linear), scaleTo(1f,1f,0.25f, Interpolation.pow2))
            ));
            textGroup.getActor("elem_count_" + num).addAction(sequence(
                    fadeOut(0), scaleTo(0,0), delay(pause),
                    parallel(fadeIn(0.3f, Interpolation.linear), scaleTo(1f,1f,0.3f, Interpolation.pow2))
            ));
        } else {

            elementActor.addAction(sequence(
                    fadeOut(0), scaleTo(0,0), delay(0.1f),
                    parallel(fadeIn(0.1f, Interpolation.linear), scaleTo(1f,1f,0.3f, Interpolation.swing))
            ));
        }
    }

    //получает номер планеты
    private void changePlanet(int planet){
        planetNum = planet;
        float pause = 0.1f;
        elementGroup.clearActions();

        for(Actor elementActor : elementGroup.getChildren()){
            String[] name = elementActor.getName().split("_");

            int num = -1;
            if(name[0].equals("element")) {
                num = Integer.parseInt(name[1]);

                setActionElement(elementActor, pause, num);


                pause += 0.1f;
            } else {
                setActionElement(elementActor, 0, num);
            }

            ((ElementActor) elementActor).setState(planetNum, num);
        }

        btnGroup.getActor("closed").clearActions();
        btnGroup.getActor("closed").addAction(sequence(
                fadeOut(0), scaleTo(0,0), delay(0.1f),
                parallel(fadeIn(0.1f, Interpolation.linear), scaleTo(1f,1f,0.3f, Interpolation.swing))
        ));


        if(planet > 1) {
            closed = false;
            for(int i = 1; i<=6; i++) {
                Integer[] el = parent.getSavedElements(planet - 1, i);
                if(!el[0].equals(el[1])){
                    closed = true;
                    break;
                }
            }

        } else {
            closed = false;
        }


        textGroup.setBackPlanet(planetNum, parent);

    }

    //обработка нажатия на кнопке Назад
    private void setControls() {
        InputMultiplexer multiplexer;

        Gdx.input.setCatchBackKey(true);
        InputProcessor ip = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK){
                    goBack();
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };
        GestureDetector gd = new GestureDetector(this);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        multiplexer.addProcessor(ip);
        Gdx.input.setInputProcessor(multiplexer);
    }

    //возврат на предыдущий скрин
    private void goBack() {
        parent.setScreen(back);
    }

    //получает координаты тапа
    private void findActor(Vector2 coords){
        Vector2 pos = stage.screenToStageCoordinates(coords);
        selActor = (BtnActor) stage.hit(pos.x, pos.y, true);
        if(selActor != null) {
            //обрабатывает клик
            checkBtnClick();
        } else {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
            if(selActor != null) checkBtnClick();
        }
    }

    //нажатие на экран, инициализирует актера типа BtnActor
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) stage.hit(pos.x, pos.y, true);
        if(selActor == null) {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
        }
        return false;
    }

    //касание экрана
    @Override
    public boolean tap(float x, float y, int count, int button) {
        findActor(new Vector2(x, y));
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(selActor != null) {
            checkBtnClick();
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        findActor(new Vector2(x, y));
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
