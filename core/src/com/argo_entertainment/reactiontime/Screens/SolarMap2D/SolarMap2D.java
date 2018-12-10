package com.argo_entertainment.reactiontime.Screens.SolarMap2D;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.GameObject;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.FinishScreen.FinishScreen;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.LevelScreenBack;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import java.awt.Label;

//большая карта с планетами,прокручиваемая
public class SolarMap2D implements Screen, GestureDetector.GestureListener {

    private ReactionTimeClass parent;

    private OrthographicCamera camera;

    public AssetManager assets;

    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private SpriteBatch batch;
    private Stage stage;

    private ButtonGroup btnGroup;
    private BtnActor selActor = null;

    private TextGroup textGroup;
    private BackgroundGroup btnBack;

    private Screen back_screen;

    private SpineAnimationsStage spineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;
    private Stage backStage;

    private Stage mapStage;
    private Stage mapStage2;
    private SpineAnimationsStage spineMapStage;

    private ScalingViewport scalingViewport;
    private ScalingViewport fitViewportTop;
    private FillViewport fillViewport;
    private FitViewport extendViewport;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    private int lvl_type = 1;

    private TextGroup textUnlockGroup;
    private PlanetsGroup planetsGroup;
    private ButtonGroup planetsBtnGroup;

    private Integer planet_num = 1;
    private Integer startCameraY = 1200;
    private Integer[] cameraStartPoints = new Integer[]{1200, 2082, 3012, 4086, 5012, 5901, 6897, 7933, 8752};

    public SolarMap2D(ReactionTimeClass game, OrthographicCamera game_camera, Screen back, Integer planetNum){
        // back.dispose();
        parent = game;
        camera = game_camera;
        back_screen = back;
        Gdx.app.log("offset", "Номер игровой планеты "+String.valueOf(planetNum));
        planet_num = planetNum;
    }
    int HEIGHTB = 0;

    @Override
    public void show() {
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        extendViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        scalingViewport = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        TiledMap tiledMap = parent.tiledMap("2_solar_map/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();

        stage = new Stage(extendViewport);
        backStage = new Stage(fitViewportTop);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);



        spineAnimationsStage = new SpineAnimationsStage(extendViewport);

        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;
                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                Gdx.app.log("offset", "anim/"+cell.getName()+"/"+cell.getName()+".atlas");

                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));

                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                spineAnimationsStage.addActor(actor);
            }
        }

        InputMultiplexer multiplexer;
        GestureDetector gd = new GestureDetector(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        // multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);

        // stage.setDebugAll(true);

        Gdx.input.setCatchBackKey(true);
        DefaultShader.defaultCullFace = 0;

        //если из 10 планет есть незакрытые, то finished false
        Boolean finished = true;
        for(int i=1; i<10; i++) {
            if(parent.isClosedPlanet(i)) {
                finished = false;
            }
        }

        if(finished)
            finishedSolar();

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        for(MapObject object : tiledMap.getLayers().get("fillBack").getObjects()) {
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;
                OtherActor actor = new OtherActor(cell);
                backStage.addActor(actor);
            }
        }

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("btn_back"));
        backStage.addActor(btnBack);


        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        backStage.addActor(textGroup);



        textGroup.setLabel("score", "" + parent.getNumbers("high_score"));

        mapStage = new Stage(scalingViewport);
        mapStage2 = new Stage(scalingViewport);

        BackgroundGroup otherPlanet = new BackgroundGroup(tiledMap.getLayers().get("map_other"));
        mapStage.addActor(otherPlanet);

        planetsGroup = new PlanetsGroup(tiledMap.getLayers().get("map_planets"), parent);
        mapStage.addActor(planetsGroup);

        planetsBtnGroup = new ButtonGroup(tiledMap.getLayers().get("map_btn"), parent);
        mapStage.addActor(planetsBtnGroup);

        PlanetsTextGroup textGroup = new PlanetsTextGroup(tiledMap.getLayers().get("map_text"), fonts, parent);
        mapStage.addActor(textGroup);

        spineMapStage = new SpineAnimationsStage(scalingViewport);

        objects = tiledMap.getLayers().get("map_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                String[] detailedName = cell.getName().split("_");

                TextureAtlas atlas;

                if(detailedName[0].equals("unlock"))
                    atlas = parent.assetManager.get("anim/station_1/station_1.atlas");
                else
                   atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");

                SkeletonJson json = new SkeletonJson(atlas);

                SkeletonData skeletonData;

                if(detailedName[0].equals("unlock"))
                    skeletonData = json.readSkeletonData(Gdx.files.internal("anim/station_1/station_1.json"));
                else
                    skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));

                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                spineMapStage.addActor(actor);
            }
        }


        textUnlockGroup = new TextGroup(tiledMap.getLayers().get("map_text_2"), fonts);
        mapStage2.addActor(textUnlockGroup);

        textUnlockGroup.setLabel("unlock_text_1", "200 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_2", "300 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_3", "300 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_4", "400 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_5", "600 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_6", "1000 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_7", "1500 RC", Align.center);
        textUnlockGroup.setLabel("unlock_text_8", "3000 RC", Align.center);

        startCameraY = cameraStartPoints[planet_num - 1];
        scalingViewport.getCamera().position.y = startCameraY;
    }

    double x, y;
    float timer = 0;


    public static final float TIME_TO_SCROLL = 2.0f;

    private float mTimer;
    private float mVelocityY;
    private final float mLowerPosition = 1200f;
    private final float mUpperPosition = 10100f;
    private Quaternion rotation;
    private float velocitySum = 0;

    @Override
    public void render(float delta) {

        //передвигает на позицию куда пролистнул юзер
        if (mTimer > 0) {// if timer is not 0

            float acceleration_y = mVelocityY * delta;// calculate acceleration (the rate of change of velocity)
            mTimer -= delta;// decreasing timer
            mVelocityY -= acceleration_y;// decreasing velocity
            velocitySum += mVelocityY;

            scalingViewport.getCamera().position.y += mVelocityY;
            checkCameraPosition();
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        mapStage.act(delta);
        mapStage2.act(delta);
        backStage.act(delta);
        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);
        spineMapStage.act(delta);


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();

            activeBackgroundStage.draw();

        batch.end();

        scalingViewport.apply();
        batch.setProjectionMatrix(scalingViewport.getCamera().combined);
        batch.begin();

            mapStage.draw();
            spineMapStage.draw(scalingViewport.getCamera().combined);
            mapStage2.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);
        batch.begin();

            backStage.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();


            stage.draw();
            spineAnimationsStage.draw(fitViewportTop.getCamera().combined);


        batch.end();

        checkAnimations();
    }

    private void checkCameraPosition() {
        if (scalingViewport.getCamera().position.y > mUpperPosition) {
            scalingViewport.getCamera().position.y = mUpperPosition;
            mTimer = 0;
        }

        if (scalingViewport.getCamera().position.y < mLowerPosition) {
            scalingViewport.getCamera().position.y = mLowerPosition;
            mTimer = 0;
        }

        scalingViewport.getCamera().update();
    }

    private void checkAnimations() {
        float cameraY = scalingViewport.getCamera().position.y;
        int nowPlanet = parent.nowPlayPlanet();

        for(int ac=0; ac < spineMapStage.getActors().size; ac++){
            SpineAnimationActor actor = (SpineAnimationActor) spineMapStage.getActors().get(ac);
            String[] detailedName = actor.getName().split("_");
            int unlockID = 0;
            if(detailedName[0].equals("unlock"))
              unlockID = Integer.parseInt(detailedName[1]);

            if(detailedName[0].equals("unlock") && actor.getY() < cameraY + 800 && nowPlanet - 1 == unlockID){
                if(!actor.stateAnim) {
                    actor.stateAnim = true;
                    actor.animationState.setAnimation(1,"active2", false);
                }
            } else if (detailedName[0].equals("unlock")) {
                if(actor.stateAnim) {
                    actor.animationState.setAnimation(0, "active", true);
                    actor.stateAnim = false;
                }
            }

            if(detailedName[0].equals("unlock") &&  unlockID < nowPlanet - 1) {
                actor.setVisible(false);
            }
        }

        for(int i = 0; i < textUnlockGroup.getChildren().size; i++) {
            Actor actor = textUnlockGroup.getChildren().get(i);
            String[] detailedName = actor.getName().split("_");

            int unlockID = 0;
            if(detailedName[0].equals("unlock"))
                unlockID = Integer.parseInt(detailedName[2]);

            if(detailedName[0].equals("unlock") && actor.getY() < cameraY + 500 && nowPlanet - 1 == unlockID){
                if(!actor.hasActions())
                    actor.addAction(Actions.sequence(Actions.delay(0.8f), Actions.fadeIn(0.3f)));
            } else if (detailedName[0].equals("unlock")) {
                if(!actor.hasActions())
                    actor.addAction(Actions.fadeOut(0f));
            }

            if(detailedName[0].equals("unlock") && unlockID < nowPlanet - 1) {
                actor.setVisible(false);
            } else {
                actor.setVisible(true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        scalingViewport.update(width, height);
        extendViewport.update(width, height);
        fitViewportTop.update(width, height);
        fillViewport.update(width, height, true);

        int offset = extendViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        backStage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
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
        assets.dispose();
        spineAnimationsStage.dispose();
        activeBackgroundStage.dispose();
    }

    private Vector3 position = new Vector3();
    private int selected = -1, selecting = -1;
    private Actor selPlanet;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        mTimer = 0;
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        Vector2 planetPos = mapStage.screenToStageCoordinates(new Vector2(x, y));

        selPlanet = planetsBtnGroup.hit(planetPos.x, planetPos.y, true);
        if(selPlanet == null)
            selPlanet = planetsGroup.hit(planetPos.x, planetPos.y, true);

        Gdx.app.log("camera position", scalingViewport.getCamera().position + " %%");
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        checkBtnClick();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        checkBtnClick();
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("FLING", "" + velocityY);

        mTimer = TIME_TO_SCROLL;
        mVelocityY = (velocityY / 2) * Gdx.graphics.getDeltaTime();

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        float cam_y = scalingViewport.getCamera().position.y;
        Gdx.app.log("pos camera", "" + cam_y);

        if (cam_y >= mLowerPosition && cam_y <= mUpperPosition) {
            scalingViewport.getCamera().position.y += deltaY;
        }
        if (scalingViewport.getCamera().position.y > mUpperPosition) scalingViewport.getCamera().position.y = mUpperPosition;
        if (scalingViewport.getCamera().position.y < mLowerPosition) scalingViewport.getCamera().position.y = mLowerPosition;

        scalingViewport.getCamera().update();

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        mVelocityY = 0;
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

            if (type.equals("back"))
                parent.setScreen(new MainMenuScreen(parent, camera, null));
            if (type.equals("market"))
                parent.setScreen(new ShopScreen(parent, camera, this));

            String[] detailedName = type.split("_");

            if(detailedName[0].equals("unlock")) {
                Gdx.app.log("UNLOCK ACTOR", selActor.getX() + "  _  " + selActor.getY());
                Gdx.app.log("CameraY", scalingViewport.getCamera().position.y + "");
            }

        } else if(selPlanet != null) {
            String[] name = selPlanet.getName().split("_");

            goToPlanet(name[name.length - 1]);
        }
    }


    private void goToPlanet(String type) {
        int num = Integer.parseInt(type);


        if(num > 0 && !parent.isClosedPlanet(num)) {
            int field_type = 1 + (int) (Math.random() * 5);

            String extens = "g3db";
            planet_num = num;
            parent.setScreen(new LevelScreenBack(parent, camera, this, num + "." + extens,num,field_type));
        }
    }

    //если все планеты пройдены,то переходит на FinishScreen
    private void finishedSolar(){
        parent.setScreen(new FinishScreen(parent, camera, new MainMenuScreen(parent, camera, null)));
    }
}