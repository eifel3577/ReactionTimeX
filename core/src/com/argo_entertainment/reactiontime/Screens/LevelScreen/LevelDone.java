package com.argo_entertainment.reactiontime.Screens.LevelScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.ElementActor;
import com.argo_entertainment.reactiontime.Actors.GameElementActor;
import com.argo_entertainment.reactiontime.Actors.GameObject;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.GameField.GameFieldScreen;
import com.argo_entertainment.reactiontime.Groups.AnimatedTextGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.GameElementGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class LevelDone implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private ButtonGroup btnGroup;
    private AnimatedTextGroup txtGroup;
    private Actor selActor = null;

    private String planet_name;
    private Screen back_screen;

    private int lvl_type = 1;
    private int planet_num = 1;
    private int element_num = 0;
    Integer[][] savedEl;


    private SpineAnimationsStage spineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private GameElementGroup levelDoneElement;
    private SpineAnimationActor button_start;

    private Stage stage, stage_2, bottomStage, topStage;
    private Stage txtStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport, fitViewportBottom;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;


    String[][] planetTexts;

    public LevelDone(ReactionTimeClass game, OrthographicCamera game_camera, Screen back , String planet, int planet_num, int type){
        // back.dispose();
        parent = game;
        camera = game_camera;
        back_screen = back;
        planet_name = planet;
        this.planet_num = planet_num;
        lvl_type = type;

        parent.assetManager.load("planet_done/map.tmx", TiledMap.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void show() {
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);
        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        savedEl = parent.getSavedElements(planet_num);

        TiledMap tiledMap = parent.tiledMap("planet_done/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        planetTexts = parent.getText(planet_num);

        batch = new SpriteBatch();

        stage = new Stage(fitViewport);
        txtStage = new Stage(fitViewport);
        stage_2 = new Stage(fitViewport);
        topStage = new Stage(fitViewportTop);
        bottomStage = new Stage(fitViewportBottom);

        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("bottom_back"));
        bottomStage.addActor(btnBack);

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("menu_back"));
        btnBack.findActor("menu_back").setTouchable(Touchable.disabled);
        stage.addActor(btnBack);

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        topStage.addActor(btnBack);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);

        txtGroup = new AnimatedTextGroup(tiledMap.getLayers().get("menu_text"), fonts);
        txtStage.addActor(txtGroup);


        String nowPlanetName = planetTexts[0][0];


        txtGroup.setLabel("element_text", "You have succefully acomplished mission on planet " + nowPlanetName + ".", Align.center);
        txtGroup.setLabel("unlock_text", "CLICK TO OPEN ACCESS", Align.center);
        txtGroup.setLabel("unlock_text_2", "2000 RC", Align.center);

        txtGroup.getActor("unlock_text").addAction(Actions.sequence(
                Actions.fadeOut(0f),
                Actions.delay(2f),
                Actions.fadeIn(1f)
        ));
        txtGroup.getActor("unlock_text_2").addAction(Actions.sequence(
                Actions.fadeOut(0f),
                Actions.delay(2f),
                Actions.fadeIn(1f)
        ));



        MapObjects objects = tiledMap.getLayers().get("gameEl").getObjects();


        for(MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;

                String[] name = cell.getName().split("_");
                int elementNum = Integer.parseInt(name[1]);
                GameElementActor actor = new GameElementActor(cell, parent.getElementsSprite(planet_num, elementNum));

                actor.setOrigin(67.5f, 67.5f);
                actor.setBounds(cell.getX(), cell.getY(), 135,
                        135);

                if (elementNum > savedEl.length) {
                    actor.setVisible(false);
                } else {
                    actor.setState(savedEl[elementNum - 1][0].equals(savedEl[elementNum - 1][1]));
                }
                bottomStage.addActor(actor);
            }
        }

        objects = tiledMap.getLayers().get("planets").getObjects();


        for(MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;
                ElementActor actor = new ElementActor(cell);

                actor.setName(cell.getName());

                actor.setTouchable(Touchable.disabled);
                actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                        cell.getTextureRegion().getRegionHeight());

                float width = cell.getTextureRegion().getRegionWidth() * cell.getScaleX();
                float heigt = cell.getTextureRegion().getRegionHeight() * cell.getScaleY();


                actor.setWidth(width);
                actor.setHeight(heigt);

                //actor.setScale(cell.getScaleX(), cell.getScaleY());

                actor.setOrigin(width / 2, heigt / 2);

                actor.setX(cell.getX());
                actor.setY(cell.getY());


                if(cell.getName().equals("now_planet")) {
                    actor.setState(planet_num, -1);
                } else {
                    actor.setState(planet_num + 1, -1);
                }

                if(!cell.getName().equals("name")){

                    actor.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(
                            Actions.scaleBy(0.1f, 0.1f, 0.9f, Interpolation.linear),
                            Actions.scaleBy(-0.1f, -0.1f, 1.5f, Interpolation.swing)
                    )));

                }


                stage_2.addActor(actor);
            }
        }



        spineAnimationsStage = new SpineAnimationsStage(fitViewport);

        objects = tiledMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);

                if(cell.getName().equals("station_1")){
                    actor.animationState.setAnimation(1,"active2", false);
                }

                if(cell.getName().equals("button_start")){
                    button_start = actor;
                    spineAnimationsStage.addActor(button_start);
                } else {
                    spineAnimationsStage.addActor(actor);
                }
            }
        }

        DefaultShader.defaultCullFace = 0;

        activeBackgroundStage = new ActiveBackgroundStage(new FillViewport(1080, 1920, camera), parent.getBackObj());

        setControls();



        //stage_2.setDebugAll(true    );
    }

    double x, y;
    float timer = 0;


    public static final float TIME_TO_SCROLL = 2.0f;

    private float mTimer;
    private float mVelocityY;
    private final float mLowerPosition = 960.0f;
    private final float mUpperPosition = 8626f;
    private Quaternion rotation;
    private float velocitySum = 0;

    @Override
    public void render(float delta) {
        Gdx.app.log("zoom",  " *** ");

        batch.setProjectionMatrix(camera.combined);

        // controller.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        topStage.act(delta);
        txtStage.act(delta);
        stage.act(delta);
        stage_2.act(delta);
        bottomStage.act(delta);

        spineAnimationsStage.act(delta);
        activeBackgroundStage.act(delta);

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            activeBackgroundStage.draw();
        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            topStage.draw();

        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            stage.draw();

            spineAnimationsStage.draw(fitViewport.getCamera().combined);

            stage_2.draw();

        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            txtStage.draw();

        batch.end();

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();

            bottomStage.draw();

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        fitViewportBottom.update(width, height);
        fitViewportTop.update(width, height);
        fillViewport.update(width, height, true);

        int offset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        topStage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
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
        spineAnimationsStage.dispose();
        activeBackgroundStage.dispose();

        parent.assetManager.unload("planet_done/map.tmx");
    }

    private Vector3 position = new Vector3();
    private int selected = -1, selecting = -1;


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
        Gdx.input.setCatchBackKey(true);
    }

    private void goBack() {
        parent.setScreen(back_screen);
        this.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = btnGroup.hit(pos.x, pos.y, true);

        if(selActor == null) {
            pos = bottomStage.screenToStageCoordinates(new Vector2(x, y));
            selActor = bottomStage.hit(pos.x, pos.y, true);
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
    public boolean fling(float velocityX, float velocityY, int
            button) {
        if(selActor != null) {
            checkBtnClick();
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

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

    private final Vector3 pos = new Vector3();
    private GameObject selectedLevel = null;

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            String[] detailedName = selActor.getName().split("_");

            if (type.equals("next")) {
                int field_type = 1 + (int) (Math.random() * 5);

                String extens = "g3db";
                int num = planet_num + 1;
                parent.setScreen(new LevelScreenBack(parent, camera, back_screen, num + "." + extens, num, field_type));
                this.dispose();
            }

            if(detailedName[0].equals("element")){
                int numEl = Integer.parseInt(detailedName[1]);
                setText(numEl);
            }
        }
    }

    private void setText(int element_num) {
        txtGroup.setLabel("element_text", planetTexts[element_num][1]);
    }
}