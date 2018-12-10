package com.argo_entertainment.reactiontime.Screens.SettingsMenu;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class SettingsMenuScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private Stage top_stage;
    private ButtonGroup btnGroup;
    private BackgroundGroup bgGroup;

    private SpineAnimationsStage spineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public SettingsMenuScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;
        Gdx.input.setCatchBackKey(true);
        parent.assetManager.load("settings/map.tmx", TiledMap.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void show() {
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        TiledMap tiledMap = parent.tiledMap("settings/map.tmx");

        batch = new SpriteBatch();
        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        stage = new Stage(fitViewport);
        top_stage = new Stage(fitViewportTop);


        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        top_stage.addActor(btnBack);

        ButtonGroup topBtnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        top_stage.addActor(topBtnGroup);

        BackgroundGroup backgroundGroup = new BackgroundGroup(tiledMap.getLayers().get("back"));
        stage.addActor(backgroundGroup);

        bgGroup = new BackgroundGroup(tiledMap.getLayers().get("menu_bg"));
        stage.addActor(bgGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);

        spineAnimationsStage = new SpineAnimationsStage(new FillViewport(1080, 1920, camera));

        MapObjects objects = tiledMap.getLayers().get("top_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".atlas"));
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                spineAnimationsStage.addActor(actor);
            }
        }


        setControls();

        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);
        stage.act(delta);
        top_stage.act(delta);

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

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        fitViewportTop.update(width, height);
        fillViewport.update(width, height, true);

        int offset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        top_stage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
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
        top_stage.dispose();
        activeBackgroundStage.dispose();
        spineAnimationsStage.dispose();
    }

    private BtnActor selActor;

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

    private void goBack() {
        parent.setScreen(back);
    }

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            if (type.equals("back")) {
                goBack();
            }
            if (type.equals("about"))
                Gdx.net.openURI("https://argomedia.app/");
                //parent.setScreen(new AboutScreen(parent, camera, this));
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor != null) {
            selActor.setClick(!selActor.isClick());
            parent.setSettings(selActor.getName(), selActor.isClick());
        }

        if(selActor == null) {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(selActor != null) {
            if(selActor.type.equals("btn"))
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
            if(selActor.type.equals("btn"))
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
