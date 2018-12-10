package com.argo_entertainment.reactiontime.Screens.TutorialScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.argo_entertainment.reactiontime.Actors.HoleActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.GameField.Skill.BoomGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.FlashGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.FreezeGroup;
import com.argo_entertainment.reactiontime.GameField.Skill.ScreenShake;
import com.argo_entertainment.reactiontime.GameField.TiledMapObjActor;
import com.argo_entertainment.reactiontime.GameField.TiledMapStage;
import com.argo_entertainment.reactiontime.Groups.BackObjGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.ElementGroup;
import com.argo_entertainment.reactiontime.Groups.GameElementGroup;
import com.argo_entertainment.reactiontime.Groups.StateGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.LevelScreenBack;
import com.argo_entertainment.reactiontime.Screens.SettingsMenu.SettingsMenuScreen;
import com.argo_entertainment.reactiontime.Screens.SolarMapScreen.SolarMapScreen;
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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

//скрин обучения игре
public class TutorialGameFieldScreen implements Screen, GestureDetector.GestureListener {

    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private boolean finalGame = false;
    private boolean finalBonus = false;


    private BackgroundStage backgroundStage;

    private SpriteBatch batch;
    private TiledMapStage stage;
    private Stage btn_stage;
    private Stage top_btn_stage;
    private Stage tutorialFitstage;


    private boolean ppause = false;
    private boolean pause = false;

    private GameElementsStage gameElementsStage;
    private GameElementGroup gameElementGroup;

    InputMultiplexer multiplexer;

    private boolean finished = false;
    private int BLACK_HOLES = 0;
    private int lastUp;
    TextGroup textGroup;
    BackObjGroup backObjGroup;

    private boolean boom = false;

    private Texture[] elements;

    private float baseX, baseY;

    public int[][] hardLevels = new int[9][6];

    float width = 1080, height = 1920, aspectRatio;

    int planetNum, elementNum;
    String planetName;
    int elementsType = 1;


    private SpineAnimationsStage bottomSpineAnimationsStage;
    private SpineAnimationsStage bottomSpineAnimationsStageBoom;
    private ActiveBackgroundStage activeBackgroundStage;

    private FreezeGroup freezeGroup;
    private BoomGroup boomGroup;
    private BoomGroup boomAnimBackGroup;
    private Stage backgroundBoomStage;
    private FlashGroup flashGroup;

    private ButtonGroup btnGroup;

    private int tutorialStep = 0;
    private Boolean tutorial;
    private Stage tutorialStage;
    private BackgroundGroup tutorialHand;

    private FillViewport fillViewport;
    private FitViewport fitViewport, fitViewportBottom;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    /**инициализирует переменные камеры и Game-класса,устанавливает камеру по центру */
    public TutorialGameFieldScreen(ReactionTimeClass game, OrthographicCamera game_camera, int type, int planet, int element) {
        parent = game;
        camera = game_camera;

        planetNum = planet;
        elementNum = element;

        camera.viewportWidth = width / 2;
        camera.viewportHeight = height / 2;

        camera.position.set(new Vector2(width / 2,height / 2), 0);
        camera.update();


        // Hard Levels init
        hardLevels[0][1] = 0; //Elements generate count
        hardLevels[0][2] = 110000; //Elements generate time
        hardLevels[0][3] = 0; //Special Elements generate time @ToDo
        hardLevels[0][4] = 0; //Disable timing
        hardLevels[0][5] = 0; //Disable count

        hardLevels[1][1] = 3;
        hardLevels[1][2] = 1200;
        hardLevels[1][3] = 20;
        hardLevels[1][4] = 34000;
        hardLevels[1][5] = 3;

        elements = parent.getElements(type);
        elementsType = type;
    }


    @Override
    public void show() {

        //установка ширины под размеры девайса и настраивает видовые окна
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);
        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);



        TiledMap tiledMap, tutorialMap;

        batch = new SpriteBatch();
        //загружает карту test2
        tiledMap = parent.tiledMap("test2.tmx");
        //загружает инфо по текстурам и шрифтам
        Texture[] textures = parent.getTextures();
        Texture[][] specials = parent.getSpecial();
        BitmapFont fonts[] = parent.getFonts();
        //инициализирует OrthogonalTiledMapRenderer
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        //инициализирует TiledMapStage с запуском в тренировочном режиме и передает туда видовой экран fitViewport
        stage = new TiledMapStage(fitViewport, elements, specials, textures, hardLevels, parent.getElementsSprite(planetNum, elementNum), elementNum, this);

        //инициализирует Stage
        backgroundStage = new BackgroundStage(fillViewport);
        top_btn_stage = new Stage(fitViewportTop);
        btn_stage = new Stage(fitViewport);

        backgroundBoomStage = new Stage(fillViewport);

        bottomSpineAnimationsStage = new SpineAnimationsStage(fitViewportBottom);
        bottomSpineAnimationsStageBoom = new SpineAnimationsStage(fitViewportBottom);

        gameElementsStage = new GameElementsStage(fitViewportTop);

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        //инициализирует группы слоями из карты
        BackgroundGroup bgGroup = new BackgroundGroup(tiledMap.getLayers().get("background"));
        backgroundStage.addActor(bgGroup);
        backgroundStage.setNormal();

        BackgroundGroup guiBack = new BackgroundGroup( tiledMap.getLayers().get("gui_back"));
        btn_stage.addActor(guiBack.getChildren().get(1));
        top_btn_stage.addActor(guiBack.getChildren().get(0));

        freezeGroup = new FreezeGroup(tiledMap.getLayers().get("freeze"), fonts);
        freezeGroup.startAnimation(false, 0);
        btn_stage.addActor(freezeGroup);

        boomAnimBackGroup = new BoomGroup(tiledMap.getLayers().get("background_animation"));
        boomAnimBackGroup.setVisible(false);
        backgroundBoomStage.addActor(boomAnimBackGroup);

        boomGroup = new BoomGroup(tiledMap.getLayers().get("boom"), fonts);
        boomGroup.startAnimation(false);
        btn_stage.addActor(boomGroup.getChildren().get(1));
        top_btn_stage.addActor(boomGroup.getChildren().get(0));

        flashGroup = new FlashGroup(tiledMap.getLayers().get("flash"), fonts);
        flashGroup.startAnimation(false);
        btn_stage.addActor(flashGroup);

        backObjGroup = new BackObjGroup( tiledMap.getLayers().get("other"), elements[0]);
        btn_stage.addActor(backObjGroup);



        //инициализирует MapObjects анимационным слоем
        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();


        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                if(!cell.getName().equals("1_reaction_coin")) {

                    TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                    SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                    //json.setScale(2.3f);

                    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                    SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                    bottomSpineAnimationsStage.addActor(actor);

                    if(actor.getName().equals("Skill_bomb")) {
                        SpineAnimationActor actor_boom = new SpineAnimationActor(cell, skeletonData);
                        bottomSpineAnimationsStageBoom.addActor(actor_boom);
                    }
                }
            }
        }

        //игровые ячейки (по дефолту желтые)
        objects = tiledMap.getLayers().get("obj").getObjects();


        for(MapObject object : objects) {
            //для каждой ячейки
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;
                //получает актера по ячейке
                TiledMapObjActor actor = new TiledMapObjActor(cell, textures);
                //Устанавливает исходную позицию относительно нижнего левого угла актера.
                actor.setOrigin(67.5f, 67.5f);

                actor.setBounds(cell.getX(), cell.getY(), 135,
                        135);
                //видимость .или 0 или 1
                int visible = (int) (Math.random() * 1);
                actor.setVisible(visible != 0);
                //текстура для актера из массива elements
                actor.texture = elements[1];
                //добавляет актера в stage
                stage.addActor(actor);
            }
        }

        //добавляет в stage кнопки
        btnGroup = new ButtonGroup(tiledMap.getLayers().get("buttons"));
        btn_stage.addActor(btnGroup);


        textGroup = new TextGroup(tiledMap.getLayers().get("text"), fonts);
        btn_stage.addActor(textGroup);

        gameElementGroup = new GameElementGroup(tiledMap.getLayers().get("game_elements"), parent.getElementsSprite(planetNum, elementNum));
        gameElementsStage.addActor(gameElementGroup);

        gameElementCount = parent.getSavedElements(planetNum)[elementNum - 1][1];

        //тренировочная карта
        tutorialMap = parent.tiledMap("tutorial/map.tmx");

        tutorialStage = new Stage(fillViewport);
        BackgroundGroup tutorialBack = new BackgroundGroup(tutorialMap.getLayers().get("tutorial"));
        tutorialStage.addActor(tutorialBack);

        tutorialFitstage = new Stage(fitViewport);
        tutorialHand = new BackgroundGroup(tutorialMap.getLayers().get("obj"));
        tutorialFitstage.addActor(tutorialHand);
        tutorialHand.setVisible(false);


        BackgroundGroup bjGroup = new BackgroundGroup( tutorialMap.getLayers().get("field"));
        btn_stage.addActor(bjGroup);


        //tutorialStage.setDebugAll(true);
        setTouchDetector();
        setData(textGroup);
    }

    private BtnActor selActor;

    private void restartGame(){
        this.dispose();
        this.show();
    }

    private void firstStep(){
        int random_number1 = 1 + (int) (Math.random() * 4);
        Actor[] simpleActors = new Actor[2];
        int simpleActorsCount = 0;
        for(int i = 0; i < stage.getActors().size && simpleActorsCount < 2; i++) {
            Actor stageActor = stage.getActors().get(i);
            if(stageActor.getName() != null && stageActor.getName().equals("cell_" + random_number1 + "_0")) {
                simpleActors[simpleActorsCount] = stageActor;
                simpleActorsCount++;
            }
        }
        if(simpleActorsCount == 2) {
            Actor actorAction = tutorialHand.getChildren().first();
            actorAction.clearActions();
            actorAction.addAction(sequence(
                    Actions.hide(),
                    moveTo(simpleActors[0].getX(), simpleActors[0].getY()),
                    Actions.show(),
                    repeat(RepeatAction.FOREVER, sequence(
                            moveTo(simpleActors[0].getX() + 30, simpleActors[0].getY() - 40),
                            scaleTo(0.75f, 0.75f, 0.4f, Interpolation.swing),
                            moveTo(simpleActors[1].getX() + 30, simpleActors[1].getY() - 40, 1.5f, Interpolation.linear),
                            scaleTo(1f, 1f, 0.3f, Interpolation.swing)
                    ))
            ));
            tutorialStep = 1;
            tutorial = true;
            tutorialHand.setVisible(true);

        } else {
            delay(1f, new Action() {
                @Override
                public boolean act(float delta) {
                    firstStep();
                    return true;
                }
            });
        }
    }

    private void secondStep(){
        Actor[] simpleActors = new Actor[2];
        int simpleActorsCount = 1;

        simpleActors[0] = btnGroup.getActor("boom");

        for(int i = 10; i < stage.getActors().size && simpleActorsCount < 2; i++) {
            Actor stageActor = stage.getActors().get(i);
            if(stageActor.getName() != null) {
                simpleActors[simpleActorsCount] = stageActor;
                simpleActorsCount++;
            }
        }
        Actor actorAction = tutorialHand.getChildren().first();
        actorAction.clearActions();
        actorAction.addAction(sequence(
                Actions.hide(),
                moveTo(simpleActors[0].getX(), simpleActors[0].getY()),
                Actions.show(),
                repeat(RepeatAction.FOREVER, sequence(
                        moveTo(simpleActors[0].getX() + 70, simpleActors[0].getY() - 30),
                        scaleTo(0.75f, 0.75f, 0.5f, Interpolation.swing),
                        scaleTo(1f, 1f, 0.5f, Interpolation.swing),
                        moveTo(simpleActors[1].getX() + 30, simpleActors[1].getY() - 40, 1.75f, Interpolation.linear),
                        scaleTo(0.75f, 0.75f, 0.5f, Interpolation.swing),
                        scaleTo(1f, 1f, 0.5f, Interpolation.swing)
                ))
        ));
        tutorialStep = 2;
        tutorial = true;
    }

    private void thirdStep(){
        gameElementGroup.getElement("timer_bg").setVisible(false);
        textGroup.setVisible("timer", false);
        gameElementGroup.setState(2);
        stage.setCatch(true);
        stage.setGameElementPosition(gameElementGroup.getElementPosition());
        gameElementGroup.setCountSelected(stage.getCatchedElement());
        stage.genCatchElement(4);
        stage.setCatchElement(4);
        levelTimer = 0;

        Actor[] simpleActors = new Actor[2];
        int simpleActorsCount = 0;
        for(int i = 0; i < stage.getActors().size && simpleActorsCount < 2; i++) {
            Actor stageActor = stage.getActors().get(i);
            if(stageActor.getName() != null && stageActor.getName().equals("cell_9_9")) {
                simpleActors[simpleActorsCount] = stageActor;
                simpleActorsCount++;
            }
        }

        if(simpleActorsCount == 2) {
            Actor actorAction = tutorialHand.getChildren().first();
            actorAction.clearActions();
            actorAction.addAction(sequence(
                    Actions.hide(),
                    delay(1.5f),
                    moveTo(simpleActors[0].getX(), simpleActors[0].getY()),
                    Actions.show(),
                    repeat(RepeatAction.FOREVER, sequence(
                            moveTo(simpleActors[0].getX() + 30, simpleActors[0].getY() - 40),
                            scaleTo(0.75f, 0.75f, 0.4f, Interpolation.swing),
                            moveTo(simpleActors[1].getX() + 30, simpleActors[1].getY() - 40, 1.5f, Interpolation.linear),
                            scaleTo(1f, 1f, 0.3f, Interpolation.swing)
                    ))
            ));
            tutorialStep = 3;
            tutorial = true;
        } else {
            delay(2f, new Action() {
                @Override
                public boolean act(float delta) {
                    thirdStep();
                    return true;
                }
            });
        }
    }

    //по кнопке назад возврат на MainMenuScreen
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
        multiplexer.addProcessor(btn_stage);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(ip);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setData(TextGroup textGroup) {

        textGroup.setLabel("skill_c_1", "");
        textGroup.setLabel("skill_c_2", "");
        textGroup.setLabel("skill_c_3", "");

        stage.VIBRO = parent.getSettings("vibro");
        pause = false;
        ppause = false;
        finalGame = false;
        finalBonus = false;
        textGroup.setLabel("score", "" + stage.SCORE);

        BLACK_HOLES = 0;
        stage.BH = BLACK_HOLES;
        lastUp = 35;

        stage.setCatch(false);

        flashProcent = 1.0f;
        boomProcent = 1.0f;
        slowProcent = 1.0f;
        levelTimer = 0.05f * 60;
        finished = false;
        gameOver = false;
        level_done = false;
        isLevel_done = false;
        gameElementCount = parent.getSavedElements(planetNum)[elementNum - 1][1];

        stage.newObjects(20);
        tutorial = true;
        tutorialStep = 0;

        String hours = (int) levelTimer / 60 + ":";
        String min = ((levelTimer % 60) <= 10) ? ("0" + (int) levelTimer % 60) : "" + (int) levelTimer % 60;
        textGroup.setLabel("timer", hours + min);
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
        return (!pause && !ppause && !finalGame);
    }

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();

            if(type.equals("boom") && tutorialStep >= 2){
                boom = true;
                stage.boomSkill();

                backgroundStage.backSkill("back_boom", true);
                boomAnimationEnd = false;
                animationTime = 0;
                boomUsed = true;
                boomGroup.startAnimation(true);
                boomAnimBackGroup.setVisible(true);
            }
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
    private long generCount = 0;

    private float levelTimer = 2.5f * 60;
    private int gameElementCount;

    private boolean level_done = false, isLevel_done = false;
    private boolean gameOver = false;

    private int lastScore = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fitViewport.getCamera().position.x = baseX;
        fitViewport.getCamera().position.y = baseY;

        textGroup.setLabel("score", "" + stage.SCORE);
/*
        batch.setProjectionMatrix(stage.getCamera().combined);
        stage.getCamera().update();*/

        activeBackgroundStage.act(delta);
        backgroundStage.act(delta);
        backgroundBoomStage.act(delta);

        btn_stage.act(delta);
        top_btn_stage.act(delta);
        bottomSpineAnimationsStage.act(delta);
        bottomSpineAnimationsStageBoom.act(delta);

        stage.act(delta);
        gameElementsStage.act(delta);


        if (System.currentTimeMillis() - lastUpdate > 50L && (!pause && !finalGame)) {
            float speedUpdate = 0.005f;

            if(boom ) {
                boomProcent += speedUpdate;
            }

            if (boomProcent >= 1.0f) {
                boomProcent = 0.0f;

                backgroundStage.backSkill("back_boom", false);
                boomGroup.startAnimation(false);
                boom = false;
            }

            lastUpdate = System.currentTimeMillis();
        }

        if(gamePause()) {
            stage.PAUSE = false;
        } else {
            stage.PAUSE = true;

        }


        if(gamePause()) {
            if(levelTimer >= 0) {
                String hours = (int) levelTimer / 60 + ":";
                String min = ((levelTimer % 60) <= 10) ? ("0" + (int) levelTimer % 60) : "" + (int) levelTimer % 60;
                textGroup.setLabel("timer", hours + min);
            } else if (levelTimer <= 0 && stage.getCatchElement() == 0) {
                gameElementGroup.getElement("timer_bg").setVisible(false);
                textGroup.setVisible("timer", false);
                gameElementGroup.setState(2);
                stage.setCatch(true);
                stage.setGameElementPosition(gameElementGroup.getElementPosition());
                gameElementGroup.setCountSelected(stage.getCatchedElement());
            }


            if(stage.getCatchedElement() == gameElementCount) {
                finished = true;
                gameElementGroup.setState(3);
            } else {
                gameElementGroup.setCountSelected(stage.getCatchedElement());
                stage.setGameElementPosition(gameElementGroup.getElementPosition());
            }
        }

        if((boomAnimationEnd || flashAnimationEnd) && gamePause()) animationTime+=delta; else animationTime = 0;


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            activeBackgroundStage.draw();

            if(boomUsed){
                backgroundStage.draw();
                boomAnimBackGroup.setVisible(true);
                backgroundBoomStage.draw();

                if(!stage.boom && !boomAnimationEnd){
                    boomAnimationEnd = true;
                    backgroundStage.backSkill("back_boom", false);
                    boomGroup.startAnimation(false);
                    boomAnimBackGroup.setVisible(false);

                    if(tutorialStep == 2)
                        thirdStep();
                }

                if(!stage.boom && animationTime > 3f){
                    boomUsed = false;
                }
            }
        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            top_btn_stage.draw();
            gameElementsStage.draw(); //@ToDo: Засунуть под паузу

        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(stage.getCamera().combined);

        batch.begin();

            btn_stage.draw();
            stage.draw();

        batch.end();

        fitViewportBottom.apply();
        batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);

        batch.begin();

            bottomSpineAnimationsStage.draw(fitViewportBottom.getCamera().combined);

        batch.end();


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            if(tutorial) {

                tutorialHand.setVisible(true);
                tutorialStage.act(delta);
                tutorialStage.draw();

            } else {

                tutorialHand.setVisible(false);
                levelTimer -= delta;
            }
        batch.end();

        if(tutorialStep == 2){
            fitViewportBottom.apply();
            batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);

            batch.begin();

            bottomSpineAnimationsStageBoom.draw(fitViewportBottom.getCamera().combined);

            batch.end();
        }

        fitViewport.apply();
        batch.setProjectionMatrix(stage.getCamera().combined);

        batch.begin();

            if(tutorial) {

                tutorialFitstage.act(delta);
                tutorialFitstage.draw();

            }

        batch.end();



        generCount = stage.generCount;
        float X = 1.3f;

        if(tutorial) stage.BH = 0; else stage.BH = 1;

        if(tutorialStep == 0 && animationTime > 1) {
            firstStep();
        }

        if(tutorialStep == 1 && stage.SCORE >= 1) {
            secondStep();
        }

        if(tutorialStep == 3 && stage.getCatchedElement() > 1) {
            parent.setScreen(new MainMenuScreen(parent, camera, null));
        }
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
        fitViewportTop.update(width, height);
        fitViewportBottom.update(width, height);
        fillViewport.update(width, height, true);


        baseX = fitViewport.getCamera().position.x;
        baseY = fitViewport.getCamera().position.y;

        int offset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        top_btn_stage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                offset);
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
        tiledMapRenderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) btn_stage.hit(pos.x, pos.y, true);

        if(tutorial) tutorial = false;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(selActor != null) {
            selActor.setClick(false);
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

        selActor = (BtnActor) btn_stage.hit(pos.x, pos.y, true);

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
