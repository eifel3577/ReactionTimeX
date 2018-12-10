package com.argo_entertainment.reactiontime.GameField;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.argo_entertainment.reactiontime.Actors.HoleActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.GameField.Skill.BoomGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.FlashGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.FreezeGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.ScreenShake;
import com.argo_entertainment.reactiontime.Groups.BackObjGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.ElementGroup;
import com.argo_entertainment.reactiontime.Groups.GameElementGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.LevelDone;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.LevelScreenBack;
import com.argo_entertainment.reactiontime.Screens.SettingsMenu.SettingsMenuScreen;
import com.argo_entertainment.reactiontime.Screens.SolarMap2D.SolarMap2D;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.BackgroundStage;
import com.argo_entertainment.reactiontime.Stages.GameElementsStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

//главный игровой экран
public class GameFieldScreenX implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer pauseMapRenderer;
    private OrthogonalTiledMapRenderer finalMapRenderer;
    private OrthogonalTiledMapRenderer doneMapRenderer;
    private OrthogonalTiledMapRenderer gameOverMapRenderer;


    private boolean finalGame = false;
    private boolean finalBonus = false;
    private Stage final_stage;
    private TextGroup finalTextGroup;
    private ButtonGroup finalBtnGroup;

    private SpriteBatch batch;

    private Stage pause_btn_stage;
    private boolean ppause = false;
    private boolean pause = false;

    private Stage levelDoneStage;
    private Stage gameOverStage;

    TextGroup pauseTextGroup;
    InputMultiplexer multiplexer;

    private boolean finished = false;
    private int BLACK_HOLES = 0;
    private int lastUp;

    private boolean flash = false;
    private boolean boom = false;
    private boolean slow = false;

    private Texture[] elements;

    private ScreenShake screenShake;
    private float baseX, baseY;

    public int[][] hardLevels = new int[9][6];

    float width = 1080, height = 1920, aspectRatio;

    int planetNum, elementNum;
    String planetName;
    int elementsType = 1;


    private SpineAnimationsStage finalSpineAnimationsStage;
    private ElementGroup finalElementGroup;

    private SpineAnimationsStage levelDoneSpineAnimationsStage;

    private SpineAnimationsStage gameOverSpineAnimationsStage;


    float middleBoomAlpha = 0f;

    private float[][] elementsTimer = new float[][] {
            {60f, 15f, 30f, 30f},
            {120f, 15f, 30f, 30f},
            {150f, 15f, 30f, 30f},
            {180f, 15f, 30f, 30f},
            {210f, 15f, 30f, 30f},
            {240f, 15f, 30f, 30f},
            {270f, 15f, 30f, 30f},
            {300f, 15f, 30f, 15f},
            {330f, 15f, 30f, 15f},
    };

    private int[] coinsByOne = new int[] {
            1,1,1,1,2,2,4,4,4
    };

    private BackgroundStage backgroundStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport, fitViewportBottom;
    private ScalingViewport fitViewportTop;

    private Stage topBtn, topTxt, topBack;
    private TextGroup topTextGroup;


    private Stage midBtn, midTxt, midBack;
    private TextGroup midTextGroup;
    private BackgroundGroup guiBack;

    private GameElementsStage gameElementsStage;
    private GameElementGroup gameElementGroup;

    private TiledMapStage stage;
    Label labelRC;
    private Stage RCStage;
    private SpineAnimationsStage plusAnimationsStage;
    private SpineAnimationActor plusCoin;

    private Stage bottomBtn, bottomTxt;
    private TextGroup bottomTextGroup;
    private SpineAnimationsStage bottomSpineAnimationsStage;

    private Stage freezeTopBack;
    private FreezeGroup freezeTopGroup;
    private Stage freezeMidBack;
    private FreezeGroup freezeMidGroup;


    private Stage boomTopBack;
    private BoomGroup boomTopGroup;
    private Stage boomMidBack;
    private BoomGroup boomMidGroup;

    private Stage backgroundBoomStage;
    private BoomGroup boomAnimBackGroup;

    private Stage flashTopBack;
    private FlashGroup flashTopGroup;
    private Stage flashMidBack;
    private FlashGroup flashMidGroup;

    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public GameFieldScreenX(ReactionTimeClass game, OrthographicCamera game_camera, int type, String planet_name, int planet, int element) {
        parent = game;
        camera = game_camera;

        planetName = planet_name;

        planetNum = planet;

        elementNum = element;

        camera.viewportWidth = width / 2;
        camera.viewportHeight = height / 2;

        camera.position.set(new Vector2(width / 2,height / 2), 0);
        camera.update();



        //настройка скорости игры
        for(int i = 0; i < 4; i++){
            elementsTimer[planetNum - 1][i] += elementsTimer[planetNum - 1][i] * (0.3 * (elementNum - 1));
        }

        // Hard Levels init
        hardLevels[0][1] = 3; //Elements generate count
        hardLevels[0][2] = 1100; //Elements generate time
        hardLevels[0][3] = 30; //Special Elements generate time @ToDo
        hardLevels[0][4] = 0; //Disable timing
        hardLevels[0][5] = 0; //Disable count

        hardLevels[1][1] = 3;
        hardLevels[1][2] = 1200;
        hardLevels[1][3] = 20;
        hardLevels[1][4] = 34000;
        hardLevels[1][5] = 3;

        hardLevels[2][1] = hardLevels[3][1] = 2;
        hardLevels[2][2] = hardLevels[3][2] = 1000;
        hardLevels[2][3] = hardLevels[3][3] = 20;
        hardLevels[2][4] = hardLevels[3][4] = 18000;
        hardLevels[2][5] = hardLevels[3][5] = 4;

        hardLevels[4][1] = hardLevels[5][1] = hardLevels[6][1] = 3;
        hardLevels[4][2] = hardLevels[5][2] = hardLevels[6][2] = 1000;
        hardLevels[4][3] = hardLevels[5][3] = hardLevels[6][3] = 20;
        hardLevels[4][4] = hardLevels[5][4] = hardLevels[6][4] = 15000;
        hardLevels[4][5] = hardLevels[5][5] = hardLevels[6][5] = 6;

        hardLevels[7][1] = 4;
        hardLevels[7][2] = 700;
        hardLevels[7][3] = 10;
        hardLevels[7][4] = 9000;
        hardLevels[7][5] = 8;

        elements = parent.getElements(type);

        elementsType = type;
        savedEl = parent.getSavedElements(planetNum, elementNum);
    }


    private Integer[] savedEl = new Integer[2];

    @Override
    public void show() {


        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();

        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);
        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        screenShake = new ScreenShake();
        //разные карты (карта игры,карта паузы,финальная карта,карта прохождения уровня,карта конца игры)
        TiledMap tiledMap, pauseMap, finalMap, levelDone, gameOver;

        batch = new SpriteBatch();
        tiledMap = parent.tiledMap("game_field.tmx");
        pauseMap = parent.tiledMap("pause/map.tmx");
        levelDone = parent.tiledMap("level_done/map.tmx");
        gameOver = parent.tiledMap("game_over/map.tmx");

        finalMap = parent.tiledMap("finish/finish_map.tmx");
        Texture[] textures = parent.getTextures();
        Texture[][] specials = parent.getSpecial();
        BitmapFont fonts[] = parent.getFonts();

        //разные рендереры для обработки разных карт
        pauseMapRenderer = new OrthogonalTiledMapRenderer(pauseMap);
        finalMapRenderer = new OrthogonalTiledMapRenderer(finalMap);
        doneMapRenderer = new OrthogonalTiledMapRenderer(levelDone);
        gameOverMapRenderer = new OrthogonalTiledMapRenderer(gameOver);


        //@ToDo:TOP ----------------------------------------------------------------------------------

        topBack = new Stage(fitViewportTop);
        topBtn = new Stage(fitViewportTop);
        topTxt = new Stage(fitViewportTop);

        BackgroundGroup topGuiBack = new BackgroundGroup( tiledMap.getLayers().get("top_gui_back"));
        topBack.addActor(topGuiBack);

        BackgroundGroup energy = new BackgroundGroup( tiledMap.getLayers().get("energy"));
        topBack.addActor(energy);

        ButtonGroup btnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        topBtn.addActor(btnGroup);

        topTextGroup = new TextGroup(tiledMap.getLayers().get("top_txt"), fonts);
        topTxt.addActor(topTextGroup);

        //@ToDo:MID ----------------------------------------------------------------------------------

        RCStage = new Stage(fitViewport);
        plusAnimationsStage = new SpineAnimationsStage(fitViewport);
        stage = new TiledMapStage(fitViewport, elements, specials, textures, hardLevels, parent.getElementsSprite(planetNum, elementNum), elementNum, this);
        midBack = new Stage(fitViewport);
        midBtn = new Stage(fitViewport);
        midTxt = new Stage(fitViewport);

        guiBack = new BackgroundGroup( tiledMap.getLayers().get("mid_gui_back"));
        midBack.addActor(guiBack);
        guiBack.getActor("mid_boom").setColor(1,1,1, middleBoomAlpha);


        MapObjects objects = tiledMap.getLayers().get("mid_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;
                //получает строку например "1_reaction_coin" в зависимости от уровня
                String plusCoinName = coinsByOne[planetNum - 1] + "_reaction_coin";
                //загружает атлас для этой строки
                TextureAtlas atlas = parent.assetManager.get("anim/"+plusCoinName+"/"+plusCoinName+".atlas");

                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                json.setScale(2f);
                //читает анимационный json из атласа
                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+plusCoinName+"/"+plusCoinName+".json"));

                plusCoin = new SpineAnimationActor(cell, skeletonData);
                plusCoin.setAnimationState(false);
                plusCoin.setAnimationLoop(false);
                plusCoin.setVisible(false);
                //отслеживание события анимации
                plusCoin.animationState.addListener(new AnimationState.AnimationStateListener() {
                    @Override
                    public void start(AnimationState.TrackEntry entry) {

                    }

                    @Override
                    public void interrupt(AnimationState.TrackEntry entry) {

                    }

                    @Override
                    public void end(AnimationState.TrackEntry entry) {

                    }

                    @Override
                    public void dispose(AnimationState.TrackEntry entry) {

                    }

                    //по завершении анимации актер не виден
                    @Override
                    public void complete(AnimationState.TrackEntry entry) {
                        //plusCoin.setVisible(false);
                        plusCoin.setVisible(true);
                    }

                    @Override
                    public void event(AnimationState.TrackEntry entry, Event event) {

                    }
                });
                //добавляет актера в сцену
                plusAnimationsStage.addActor(plusCoin);
            }
        }

        //текст количество набранных очков
        midTextGroup = new TextGroup(tiledMap.getLayers().get("mid_txt"), fonts);
        midTxt.addActor(midTextGroup);


        Label.LabelStyle label1Style = new Label.LabelStyle();

        label1Style.font = fonts[72];
        label1Style.fontColor = Color.WHITE;

        //настройка текста
        labelRC = new Label("+1",label1Style);

        labelRC.setName("1RC");
        labelRC.setTouchable(Touchable.disabled);

        labelRC.setSize(130, 130);
        labelRC.setPosition(0,0);
        labelRC.setAlignment(Align.center);
        labelRC.addAction(fadeOut(0));

        RCStage.addActor(labelRC);

        //ячейки под элементы
        BackObjGroup midBackObjGroup = new BackObjGroup( tiledMap.getLayers().get("other"), elements[0]);
        midBtn.addActor(midBackObjGroup);
        //желтые ячейки под элементы
        objects = tiledMap.getLayers().get("obj").getObjects();
        for(MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                //получаем из желтых ячеек конкретную желтую ячейку
                TextureMapObject cell = (TextureMapObject) object;
                //создаем актера типа TiledMapObjActor  и передаем ему обьект желтую ячейку и массив текстур
                TiledMapObjActor actor = new TiledMapObjActor(cell, textures);

                //размеры актера
                actor.setOrigin(67.5f, 67.5f);
                actor.setBounds(cell.getX(), cell.getY(), 135,
                        135);
                //видимость случайна или видымый или нет
                int visible = (int) (Math.random() * 1);

                actor.setVisible(visible != 0);
                //получаем текстуру из массива текстур elements (значки для игровых элементов,который передвигаются пальцем)
                actor.texture = elements[1];
                //добавление актера в сцену
                stage.addActor(actor);
            }
        }

        gameElementsStage = new GameElementsStage(fitViewport);
        //группа актеров gameElementGroup
        gameElementGroup = new GameElementGroup(tiledMap.getLayers().get("game_elements"), parent.getElementsSprite(planetNum, elementNum));
        //добавление группы актеров в сцену
        gameElementsStage.addActor(gameElementGroup);
        //получает массив сохраненных элементов по номеру планеты
        gameElementCount = parent.getSavedElements(planetNum)[elementNum - 1][1];

        //@ToDo:BOTTTOM ----------------------------------------------------------------------------------

        bottomBtn = new Stage(fitViewportBottom);
        bottomTxt = new Stage(fitViewportBottom);
        bottomSpineAnimationsStage = new SpineAnimationsStage(fitViewportBottom);
        //группа акторов (кнопки заморозить,бомба,время внизу экрана)
        ButtonGroup bottomBtnGroup = new ButtonGroup(tiledMap.getLayers().get("bottom_btn"));
        bottomBtn.addActor(bottomBtnGroup);

        objects = tiledMap.getLayers().get("bottom_skill").getObjects();

        //получение json-ов для отрисовки анимации кнопок заморозить,бомба,время.актеры кнопки будут иметь тип SpineAnimationActor
        //добавление актеров кнопок в сцену bottomSpineAnimationsStage
        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                bottomSpineAnimationsStage.addActor(actor);
            }
        }


        //группа актеров (тексты на кнопках заморозить,бомба,время)
        bottomTextGroup = new TextGroup(tiledMap.getLayers().get("bottom_txt"), fonts);
        //добавление актеров в сцену bottomTxt
        bottomTxt.addActor(bottomTextGroup);


        //@ToDo:FREEZE ----------------------------------------------------------------------------------
        freezeTopBack = new Stage(fitViewportTop);
        freezeMidBack = new Stage(fitViewport);
        //снег вверху экрана
        freezeTopGroup = new FreezeGroup(tiledMap.getLayers().get("top_freeze"), fonts);
        //анимация снега
        freezeTopGroup.startAnimation(false, 0);
        //добавление актеров в сцену freezeTopBack
        freezeTopBack.addActor(freezeTopGroup);

        //снег по бокам игрового поля
        freezeMidGroup = new FreezeGroup(tiledMap.getLayers().get("mid_freeze"), fonts);
        freezeMidGroup.startAnimation(false, 0);
        freezeMidBack.addActor(freezeMidGroup);


        //@ToDo:BOOOM ----------------------------------------------------------------------------------
        //взрыв
        boomTopBack = new Stage(fitViewportTop);
        boomMidBack = new Stage(fitViewport);
        backgroundBoomStage = new Stage(fillViewport);

        boomTopGroup = new BoomGroup(tiledMap.getLayers().get("top_boom"), fonts);
        boomTopGroup.startAnimation(false);
        boomTopBack.addActor(boomTopGroup);

        boomMidGroup = new BoomGroup(tiledMap.getLayers().get("mid_boom"), fonts);
        boomMidGroup.startAnimation(false);
        boomMidBack.addActor(boomMidGroup);

        boomAnimBackGroup = new BoomGroup(tiledMap.getLayers().get("background_animation"));
        boomAnimBackGroup.setVisible(false);
        backgroundBoomStage.addActor(boomAnimBackGroup);

        //@ToDo:FLASH ----------------------------------------------------------------------------------
        //молнии
        flashTopBack = new Stage(fitViewportTop);
        flashMidBack = new Stage(fitViewport);

        flashTopGroup = new FlashGroup(tiledMap.getLayers().get("top_flash"), fonts);
        flashTopGroup.startAnimation(false);
        flashTopBack.addActor(flashTopGroup);

        flashMidGroup = new FlashGroup(tiledMap.getLayers().get("mid_flash"), fonts);
        flashMidGroup.startAnimation(false);
        flashMidBack.addActor(flashMidGroup);


        //@ToDo:BACKGROUND ----------------------------------------------------------------------------------
        //задний фон
        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());
        backgroundStage = new BackgroundStage(fillViewport);

        BackgroundGroup bgGroup = new BackgroundGroup(tiledMap.getLayers().get("background"));
        backgroundStage.addActor(bgGroup);
        backgroundStage.setNormal();


        //@ToDo:OTHER ----------------------------------------------------------------------------------
        pause_btn_stage = new Stage(fillViewport);
        final_stage = new Stage(fillViewport);

        ButtonGroup pauseBtnGroup = new ButtonGroup(pauseMap.getLayers().get("btn"));
        pauseTextGroup = new TextGroup(pauseMap.getLayers().get("txt"), fonts);
        pause_btn_stage.addActor(pauseBtnGroup);
        pause_btn_stage.addActor(pauseTextGroup);


        BackgroundGroup finalBgGroup = new BackgroundGroup(finalMap.getLayers().get("menu_bg"));
        final_stage.addActor(finalBgGroup);

        finalBtnGroup = new ButtonGroup(finalMap.getLayers().get("btn"));
        final_stage.addActor(finalBtnGroup);

        finalTextGroup = new TextGroup(finalMap.getLayers().get("txt"), fonts);
        final_stage.addActor(finalTextGroup);

        GameElementGroup finishElement = new GameElementGroup(finalMap.getLayers().get("game_el"), parent.getElementsSprite(planetNum, elementNum));
        finishElement.setAll();
        final_stage.addActor(finishElement);

        finalElementGroup = new ElementGroup(finalMap.getLayers().get("elements"), parent.getElementsSprite(1,1));
        final_stage.addActor(finalElementGroup);

        for(Actor elementActor : finalElementGroup.getChildren()){
            int num = -1;
            ((ElementActor) elementActor).setState(planetNum, num);
        }

        finalSpineAnimationsStage = new SpineAnimationsStage(fillViewport);

        objects = finalMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                finalSpineAnimationsStage.addActor(actor);
            }
        }

        levelDoneStage = new Stage(fillViewport);
        ButtonGroup levelDoneBtnGroup = new ButtonGroup(levelDone.getLayers().get("btn"));
        levelDoneStage.addActor(levelDoneBtnGroup);

        GameElementGroup levelDoneElement = new GameElementGroup(levelDone.getLayers().get("game_el"), parent.getElementsSprite(planetNum, elementNum));
        levelDoneElement.setAll();
        levelDoneStage.addActor(levelDoneElement);

        levelDoneSpineAnimationsStage = new SpineAnimationsStage(fillViewport);

        objects = levelDone.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas);
                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));

                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                levelDoneSpineAnimationsStage.addActor(actor);
            }
        }

        gameOverStage = new Stage(fillViewport);
        ButtonGroup gameOverBtn = new ButtonGroup(gameOver.getLayers().get("btn"));
        gameOverStage.addActor(gameOverBtn);


        gameOverSpineAnimationsStage = new SpineAnimationsStage(fillViewport);

        objects = gameOver.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas);
                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));

                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);

                if(actor.getName().equals("100_continue"))
                    actor.animationState.setAnimation(0, "active", false);

                gameOverSpineAnimationsStage.addActor(actor);
            }
        }

        baseX = fitViewport.getCamera().position.x;
        baseY = fitViewport.getCamera().position.y;

        setTouchDetector();
        setData();
        //btn_stage.setDebugAll(true);
    }

    private BtnActor selActor;

    private void restartGame(){
        this.dispose();
        this.show();
    }

    private void setTouchDetector() {
        Gdx.input.setCatchBackKey(true);
        InputProcessor ip = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK){
                    parent.setScreen(new MainMenuScreen(parent, camera, null));
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
        multiplexer.addProcessor(final_stage);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(ip);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setData() {

        stage.VIBRO = parent.getSettings("vibro");
        pause = false;
        ppause = false;
        finalGame = false;
        finalBonus = false;

        BLACK_HOLES = 0;
        stage.BH = BLACK_HOLES;
        lastUp = 35;

        stage.setCatch(false);

        flashProcent = 1.0f;
        boomProcent = 1.0f;
        slowProcent = 1.0f;
        levelTimer = elementsTimer[planetNum - 1][0];
        elementTimer = elementsTimer[planetNum - 1][1];
        finished = false;
        gameOver = false;
        level_done = false;
        isLevel_done = false;
        gameElementCount = parent.getSavedElements(planetNum)[elementNum - 1][1];
    }

    private void freezeSound() {
        Assets.playSound(Assets.freezeSound);
    }

    public void boomSound() {
        Assets.playSound(Assets.boombSound);
    }

    public void flashSound(int id) {
        Sound flash = parent.assetManager.get("sounds/Thunder"+ id +".mp3");
        flash.play(parent.VOLUME);
    }

    private boolean gamePause() {
        return (!pause && !ppause && !finalGame && !(isLevel_done && !level_done));
    }

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();

            if(type.equals("slow") && slowProcent == 0.0f && parent.getNumbers("skill_3") > 0){
                slow = true;
                stage.slowDown(10);

                backgroundStage.backSkill("back_ice", true);
                animationEnd = false;
                freezeSound();
                freezeTopGroup.startAnimation(true, 0);
                freezeMidGroup.startAnimation(true, 0);

                parent.inclSettings("skill_3", -1);
                bottomTextGroup.setLabel("skill_c_3", "" + parent.getNumbers("skill_3"));
            }

            if(type.equals("boom") && boomProcent == 0.0f && parent.getNumbers("skill_2") > 0){
                boom = true;
                stage.boomSkill();

                backgroundStage.backSkill("back_boom", true);
                boomAnimationEnd = false;
                animationTime = 0;
                boomUsed = true;
                boomTopGroup.startAnimation(true);
                boomMidGroup.startAnimation(true);
                boomAnimBackGroup.setVisible(true);

                parent.inclSettings("skill_2", -1);
                bottomTextGroup.setLabel("skill_c_2", "" + parent.getNumbers("skill_2"));
            }

            if(type.equals("flash") && flashProcent == 0.0f && parent.getNumbers("skill_1") > 0){
                flash = true;
                stage.flashSkill();
                flashTopGroup.startAnimation(true);
                flashMidGroup.startAnimation(true);

                backgroundStage.backSkill("back_flash", true);
                flashAnimationEnd = false;
                animationTime = 0;
                flashUsed = true;

                parent.inclSettings("skill_1", -1);
                bottomTextGroup.setLabel("skill_c_1", "" + parent.getNumbers("skill_1"));
            }

            if(type.equals("watch_video")){
                if(parent.getNumbers("coins") >= 100) {
                    parent.inclSettings("coins", -100);

                    stage.clearObjects(21);
                    gameOver = false;
                    ppause = false;
                }
            }

            if(type.equals("game_over_finish")){

                if(planetNum < 9 && !parent.isClosedPlanet(planetNum + 1)) {
                    int field_type = 1 + (int) (Math.random() * 5);

                    String extens = "g3db";
                    int num = planetNum + 1;
                    parent.setScreen(new LevelDone(parent, camera,  new MainMenuScreen(parent, camera, null), planetNum + "." + extens,planetNum,field_type));
                    return;
                } else {
                    parent.setScreen(new LevelScreenBack(parent, camera, new SolarMap2D(parent, camera, new MainMenuScreen(parent, camera, null), planetNum),planetNum + ".g3db", planetNum, elementsType));
                }
            }

            if(type.equals("level_done_close")){
                level_done = true;
            }

            if(type.equals("pause")){
                pause = !pause;
            }

            if(type.equals("back")){
                pause = false;
            }

            if(type.equals("reload")){
                /*ppause = !ppause;

                stage.setDebugAll(ppause);
                gameElementsStage.setDebugAll(ppause);
                btn_stage.setDebugAll(ppause);
                pause_btn_stage.setDebugAll(ppause);
                skill_stage.setDebugAll(ppause);*/

                this.dispose();
                this.show();
                // @ToDo: Un-comment on release
            }

            if(type.equals("menu"))
                parent.setScreen(new MainMenuScreen(parent, camera, this));

            if(type.equals("restart"))
            {
                this.dispose();
                this.show();
            }

            if(type.equals("close"))
            {
                ppause = false;
            }

            if(type.equals("play_video"))
            {
                stage.clearObjects(20);
                ppause = false;
            }

            if(type.equals("play_video_final") && !finalBonus) {
                BLACK_HOLES++;
                finalBonus = true;
            }

            if(type.equals("settings"))
                parent.setScreen(new SettingsMenuScreen(parent, camera, this));

            if(type.equals("solar"))
                parent.setScreen(new LevelScreenBack(parent, camera, new SolarMap2D(parent, camera, new MainMenuScreen(parent, camera, null), planetNum), planetName, planetNum, elementNum));

        }
    }

    private long lastUpdate = 0L;
    private float flashProcent = 1.0f;
    private float boomProcent = 1.0f;
    private float slowProcent = 1.0f;

    private boolean animationEnd = true;
    private boolean boomAnimationEnd = true;
    private boolean flashAnimationEnd = true;

    private float animationTime = 0;

    private boolean boomUsed = false;
    private boolean flashUsed = false;
    private int flashUnit_count = 0;
    private long generCount = 0;

    private float levelTimer = 2.5f * 60;
    private float elementTimer = 0;
    private int gameElementCount;

    private boolean level_done = false, isLevel_done = false;
    private boolean gameOver = false;

    private int lastScore = 0;

    private Color[] elementsColors = new Color[] {
      Color.WHITE,
      new Color(240 / 255f, 48 / 255f, 134 / 255f, 1),
      new Color(252 / 255f, 232 / 255f, 49 / 255f, 1),
      new Color(106 / 255f, 189 / 255f, 23 / 255f, 1),
      new Color(0, 87 / 255f, 210 / 255f, 1),
    };

    @Override
    public void render(float delta) {

        bottomTextGroup.setLabel("skill_c_1", parent.getNumbers("skill_1").toString());
        bottomTextGroup.setLabel("skill_c_2", parent.getNumbers("skill_2").toString());
        bottomTextGroup.setLabel("skill_c_3", parent.getNumbers("skill_3").toString());

        guiBack.getActor("mid_boom").setColor(1,1,1, middleBoomAlpha);

        float DeltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fitViewport.getCamera().position.x = baseX;
        fitViewport.getCamera().position.y = baseY;


        topTextGroup.setLabel("score", "" + stage.SCORE);

        activeBackgroundStage.act(delta);
        backgroundStage.act(DeltaTime);
        backgroundBoomStage.act(animationTime);

        bottomSpineAnimationsStage.act(delta);
        midBack.act(delta);
        freezeTopBack.act(delta);
        freezeMidBack.act(delta);

        boomTopBack.act(delta);
        boomMidBack.act(delta);

        flashTopBack.act(delta);
        flashMidBack.act(delta);

        plusAnimationsStage.act(delta);
        RCStage.act(delta);


        if(gamePause()) {
            stage.PAUSE = false;
            stage.act(DeltaTime);
        } else {
            stage.PAUSE = true;
        }

        if(stage.generCount == 0  && gamePause()) {
            if(!finished)  {
                if(!gameOver) Assets.playVoice(Assets.gameOver);
                gameOver = true;

                if(parent.getNumbers("coins") < 100) {
                    if(planetNum < 9 && !parent.isClosedPlanet(planetNum + 1)) {
                        int field_type = 1 + (int) (Math.random() * 5);

                        String extens = "g3db";
                        int num = planetNum + 1;
                        parent.setScreen(new LevelDone(parent, camera,  new MainMenuScreen(parent, camera, null), planetNum + "." + extens,planetNum,field_type));
                        return;
                    } else {
                        parent.setScreen(new LevelScreenBack(parent, camera, new SolarMap2D(parent, camera, new MainMenuScreen(parent, camera, null), planetNum),planetNum + ".g3db", planetNum, elementsType));
                    }
                }/* else {
                    gameOverSpineAnimationsStage.getActor("100_continue").;
                }*/
            } else {
                finalGame = true;
                backgroundStage.setBgState("pause_bg", true);
                parent.setSettings("last_score", stage.SCORE);

                BLACK_HOLES += parent.getNumbers("black_holes");
                //int reaction_coins = stage.SCORE / 10;

                parent.inclSettings("all_items", gameElementCount);
                parent.inclSettings("done_levels", 1);
                parent.inclSettings("all_points", stage.SCORE);
                Assets.playVoiceRandom();

                //@ToDo: PlayVoice when skipAd false - нужно переписать (-_-)

                if (stage.SCORE > parent.getNumbers("high_score")) {
                    parent.setSettings("high_score", stage.SCORE);
                    parent.flushSettings();
                }
            }
        }

        generCount = stage.generCount;
        float X = 1.5f;

        if (stage.SCORE >= (int) (lastUp * X)){
            BLACK_HOLES++;
            stage.BH = BLACK_HOLES;
            HoleActor find_actor;

            int BH = (BLACK_HOLES % 4);

            if(BH % 2 == 0){
                Assets.playVoiceRandom();
            }

            lastUp = stage.SCORE;
        }

        if(lastScore != stage.SCORE && stage.selActor != null) {
            int color = Integer.parseInt(stage.selActor.getName().split("_")[1]);

            lastScore = stage.SCORE;
            labelRC.clearActions();
            labelRC.setPosition(stage.selActor.getX(), stage.selActor.getY());
            labelRC.getStyle().fontColor = color <= 4? elementsColors[color] : elementsColors[0];
            labelRC.addAction(parallel(sequence(
                    fadeOut(0),
                    fadeIn(0.5f),
                    fadeOut(0.5f)
            ), moveBy(0, 100, 1f)));

            if(stage.SCORE % 10 == 0) {
                plusCoin.setPositionAnim(stage.selActor.getX() + (stage.selActor.getWidth() / 2), stage.selActor.getY() + (stage.selActor.getHeight() / 2));
                plusCoin.setVisible(true);
                plusCoin.setAnimationState(true);

                parent.inclSettings("coins", coinsByOne[planetNum - 1]);

                Assets.playSound(Assets.moneySound);
            }

            if(stage.dragState == 3)
                middleBoomAlpha += 0.05f;
        }

        if(stage.dragState == 0)
            middleBoomAlpha = 0f;

        if(middleBoomAlpha >= 1f) {
            Assets.playVoiceRandom();
            middleBoomAlpha = 0f;
            int rndSkill = MathUtils.random(1,3);

            parent.inclSettings("skill_" + rndSkill, 1);
        }

        if((boomAnimationEnd || flashAnimationEnd) && gamePause())
            animationTime+=DeltaTime;
        else
            animationTime = 0;


        if (System.currentTimeMillis() - lastUpdate > 50L && (!pause && !finalGame)) {
            float speedUpdate = 0.005f;
            if(flash)
                flashProcent += speedUpdate;

            if(boom ) {
                boomProcent += speedUpdate;
                screenShake.update(delta, camera);
            }

            if(slow)
                slowProcent += speedUpdate;

            if (flashProcent >= 1.0f) {
                flashProcent = 0.0f;

                backgroundStage.backSkill("back_flash", false);
                flashTopGroup.startAnimation(false);
                flashMidGroup.startAnimation(false);
                flash = false;
            }

            if (boomProcent >= 1.0f) {
                boomProcent = 0.0f;

                backgroundStage.backSkill("back_boom", false);
                boomTopGroup.startAnimation(false);
                boomMidGroup.startAnimation(false);
                boom = false;
            }

            if (slowProcent >= 1.0f) {
                slowProcent = 0.0f;

                backgroundStage.backSkill("back_ice", false);
                freezeTopGroup.startAnimation(false, 0);
                freezeMidGroup.startAnimation(false, 0);
                slow = false;
            }

            lastUpdate = System.currentTimeMillis();
        }


        int generatedElement = stage.generatedGameElement ;
        if(gamePause() && !savedEl[0].equals(savedEl[1])) {
            if(levelTimer >= 0) {
                String hours = (int) levelTimer / 60 + ":";
                String min = ((levelTimer % 60) <= 10) ? ("0" + (int) levelTimer % 60) : "" + (int) levelTimer % 60;
                midTextGroup.setLabel("timer", hours + min);
                if(levelTimer / 60 == 0 && levelTimer % 60 <= 10)
                    midTextGroup.setLabelColor("timer", Color.RED);
                else
                    midTextGroup.setLabelColor("timer", Color.GREEN);
                levelTimer -= delta;
            } else if (levelTimer < 0 && elementTimer != -20 && generatedElement <= 4) {

                if(generatedElement <= 0 && gameElementGroup.getState() < 2) {
                    gameElementGroup.setState(2);
                    stage.setCatchElement(1);
                }

                elementTimer -= delta;


                String hours = (int) elementTimer / 60 + ":";
                String min = ((elementTimer % 60) <= 10) ? ("0" + (int) elementTimer % 60) : "" + (int) elementTimer % 60;
                midTextGroup.setLabel("timer", hours + min);
                if(elementTimer % 60 <= 10)
                    midTextGroup.setLabelColor("timer", Color.RED);
                else
                    midTextGroup.setLabelColor("timer", Color.GREEN);

                if (generatedElement == 4) {
                    elementTimer = -20;
                    gameElementGroup.getElement("timer_bg").setVisible(false);
                    midTextGroup.getActor("timer").setVisible(false);
                }


                if(elementTimer <= 0 && generatedElement <= 3 && generatedElement > 0){
                    elementTimer = elementsTimer[planetNum - 1][generatedElement];
                    stage.setCatchElement(generatedElement + 1);
                }

                stage.setCatch(true);
                stage.setGameElementPosition(gameElementGroup.getElementPosition());
            }



            if(stage.getCatchedElement() == 2) {
                finished = true;
                gameElementGroup.setState(3);
                parent.setSavedElement(planetNum, elementNum, gameElementCount);
                isLevel_done = true;
            } else {
                gameElementGroup.setCountSelected(stage.getCatchedElement());
                stage.setGameElementPosition(gameElementGroup.getElementPosition());
            }
        }


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            activeBackgroundStage.draw();


            if(stage.slowDown) {
                backgroundStage.draw();

                int timer = (int)(stage.slowDown_steps / 2);
                freezeMidGroup.setLabel(timer);
                if(stage.slowDown_steps == 2 && !animationEnd)
                {
                    animationEnd = true;
                    backgroundStage.backSkill("back_ice", false);
                    freezeTopGroup.startAnimation(false, 0);
                    freezeMidGroup.startAnimation(false, 0);
                }
            }

            if(boomUsed){
                backgroundStage.draw();
                boomAnimBackGroup.setVisible(true);
                backgroundBoomStage.draw();

                if(!stage.boom && !boomAnimationEnd){
                    boomAnimationEnd = true;
                    backgroundStage.backSkill("back_boom", false);
                    boomTopGroup.startAnimation(false);
                    boomMidGroup.startAnimation(false);
                    boomAnimBackGroup.setVisible(false);
                }

                if(!stage.boom && animationTime > 3f){
                    boomUsed = false;
                }
            }

            if(flashUsed){
                backgroundStage.draw();

                if(stage.flashUnit_count > flashUnit_count)
                {
                    flashUnit_count = stage.flashUnit_count;
                    int sound;
                    if(flashUnit_count % 2 != 0){
                        sound = 1;
                    } else  sound = 2;

                    flashSound(sound);
                }

                if(!stage.flash && !flashAnimationEnd){
                    flashAnimationEnd = true;
                    backgroundStage.backSkill("back_flash", false);
                    flashTopGroup.startAnimation(false);
                    flashMidGroup.startAnimation(false);
                }

                if(!stage.flash && animationTime > 3f){
                    flashUsed = false;
                    flashUnit_count = 0;
                }
            }

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);


        batch.begin();

            topBack.draw();
            freezeTopBack.draw();
            boomTopBack.draw();
            flashTopBack.draw();
            topTxt.draw();

            //topStage.draw();
            //topSpineAnimationsStage.draw(fitViewportTop.getCamera().combined);

        batch.end();


        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();
            if(!savedEl[0].equals(savedEl[1]) || stage.getCatchedElement() > 0) {
                gameElementsStage.act(delta);
                gameElementsStage.draw(); //@ToDo: Засунуть под паузу
            } else {
                midTextGroup.setVisible("timer", false);
            }

            midBack.draw();
            freezeMidBack.draw();
            boomMidBack.draw();
            flashMidBack.draw();

            midBtn.draw();
            stage.draw();

            RCStage.draw();

            midTxt.draw();

            plusAnimationsStage.draw(fitViewport.getCamera().combined);


        batch.end();

        fitViewportBottom.apply();
        batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);

        batch.begin();

            bottomSpineAnimationsStage.draw(fitViewportBottom.getCamera().combined);
            bottomTxt.draw();

        batch.end();

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            if(pause){
                pauseMapRenderer.setView((OrthographicCamera) fillViewport.getCamera());
                pauseMapRenderer.render();

                pauseTextGroup.setLabel("score", "" + stage.SCORE);
                pause_btn_stage.act(delta);
                pause_btn_stage.draw();
            }

            if(gameOver){
                gameOverMapRenderer.setView((OrthographicCamera) fillViewport.getCamera());
                gameOverMapRenderer.render();

                gameOverStage.act(DeltaTime);
                gameOverStage.draw();

                gameOverSpineAnimationsStage.act(delta);
                gameOverSpineAnimationsStage.draw(fillViewport.getCamera().combined);
            }

            if(isLevel_done && !level_done){
                doneMapRenderer.setView((OrthographicCamera) fillViewport.getCamera());
                doneMapRenderer.render();

                levelDoneStage.act(DeltaTime);
                levelDoneStage.draw();
                levelDoneSpineAnimationsStage.act(delta);
                levelDoneSpineAnimationsStage.draw(fillViewport.getCamera().combined);
            }

            if(finalGame) {
                backgroundStage.draw();
                finalMapRenderer.setView((OrthographicCamera) fillViewport.getCamera());
                finalMapRenderer.render();

                finalTextGroup.setLabel("score", "" + stage.SCORE);
                final_stage.act(DeltaTime);
                final_stage.draw();

                finalSpineAnimationsStage.act(delta);
                finalSpineAnimationsStage.draw(fillViewport.getCamera().combined);

            }
        batch.end();
    }



    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        fitViewportTop.update(width, height);
        fitViewportBottom.update(width, height);
        fillViewport.update(width, height, true);

        int offset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        topBack.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                offset);
        boomTopBack.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                offset);
        freezeTopBack.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                offset);

        offset = fitViewport.getBottomGutterHeight() - (int) parent.getIOSSafeAreaInsets().y;
        bottomSpineAnimationsStage.getViewport().setScreenPosition(fitViewportBottom.getScreenX(),
                fitViewportBottom.getScreenY() - offset);

        Gdx.app.log("getTopGutterHeight", "" + fitViewport.getTopGutterHeight());
        Gdx.app.log("getBottomGutterHeight", "" + fitViewport.getBottomGutterHeight());
        Gdx.app.log("getIOSSafeAreaInsets", parent.getIOSSafeAreaInsets().toString());
        Gdx.app.log("offset", offset + "");
        Gdx.app.log("fitViewportTop getScreenY", fitViewportTop.getScreenY() + "");
        Gdx.app.log("fitViewportBottom getScreenY", fitViewportBottom.getScreenY() + "");

        camera.update();
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
        freezeTopBack.dispose();
        freezeMidBack.dispose();
        boomTopBack.dispose();
        boomMidBack.dispose();
        flashTopBack.dispose();
        flashMidBack.dispose();
        pauseMapRenderer.dispose();
        finalSpineAnimationsStage.dispose();
        bottomSpineAnimationsStage.dispose();
        levelDoneSpineAnimationsStage.dispose();
        plusAnimationsStage.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        if(pause) selActor = (BtnActor) pause_btn_stage.hit(pos.x, pos.y, true);
        else if(finalGame) selActor = (BtnActor) finalBtnGroup.hit(pos.x, pos.y, true);
        else if(gameOver) selActor = (BtnActor) gameOverStage.hit(pos.x, pos.y, true);
        else if(isLevel_done && !level_done) selActor = (BtnActor) levelDoneStage.hit(pos.x, pos.y, true);
        else selActor = (BtnActor) topBtn.hit(pos.x, pos.y, true);

        if(selActor == null) {
            pos = bottomBtn.screenToStageCoordinates(new Vector2(x, y));
            selActor = (BtnActor) bottomBtn.hit(pos.x, pos.y, true);
        }

        if(selActor != null) {
            selActor.setClick(true);
        }


        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(selActor != null) {
            checkBtnClick();
        }
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
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        if(pause) selActor = (BtnActor) pause_btn_stage.hit(pos.x, pos.y, true);
        else if(finalGame) selActor = (BtnActor) finalBtnGroup.hit(pos.x, pos.y, true);
        else selActor = (BtnActor) topBtn.hit(pos.x, pos.y, true);

        if(selActor == null) {
            pos = bottomBtn.screenToStageCoordinates(new Vector2(x, y));
            selActor = (BtnActor) bottomBtn.hit(pos.x, pos.y, true);
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(selActor != null) {
            checkBtnClick();
        }

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
