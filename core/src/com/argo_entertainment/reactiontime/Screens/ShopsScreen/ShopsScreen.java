package com.argo_entertainment.reactiontime.Screens.ShopsScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.BuySkillScreen.BuySkillScreen;
import com.argo_entertainment.reactiontime.Screens.PurchasesScreen.PurchasesScreen;
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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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

public class ShopsScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private Stage top_stage;
    private Stage bottomStage;

    private ButtonGroup btnGroup;
    private BackgroundGroup bgGroup;
    private TextGroup textGroup;
    private TextGroup upTextGroup;


    private Integer skills[] = new Integer[3];
    private Integer coins = 0;

    private SpineAnimationsStage spineAnimationsStage;
    private SpineAnimationsStage topSpineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private FitViewport fitViewportBottom;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public ShopsScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;

        parent.assetManager.load("shops/shops_map.tmx", TiledMap.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void show() {

        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        TiledMap tiledMap = parent.tiledMap("shops/shops_map.tmx");
        BitmapFont[] font = parent.getFonts();

        batch = new SpriteBatch();
        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        bottomStage = new Stage(fillViewport);
        stage = new Stage(fitViewport);
        top_stage = new Stage(fitViewportTop);


        BackgroundGroup bottomStageBack = new BackgroundGroup(tiledMap.getLayers().get("bottom_back"));
        bottomStage.addActor(bottomStageBack);

        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        top_stage.addActor(btnBack);

        ButtonGroup topBtnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        top_stage.addActor(topBtnGroup);

        upTextGroup = new TextGroup(tiledMap.getLayers().get("top_txt"), font);
        top_stage.addActor(upTextGroup);

        bgGroup = new BackgroundGroup(tiledMap.getLayers().get("menu_bg"));
        stage.addActor(bgGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);

        for (Actor actor :
                btnGroup.getChildren()) {
            actor.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.parallel(
                    Actions.sequence(
                        Actions.scaleBy(0.15f, 0.15f, 0.8f, Interpolation.linear),
                        Actions.scaleBy(-0.15f, -0.15f, .8f, Interpolation.swing)
                    ),
                    Actions.sequence(
                        Actions.rotateBy(5, 0.4f, Interpolation.swing),
                        Actions.rotateBy(-10, 0.8f, Interpolation.swing),
                        Actions.rotateBy(5, 0.4f, Interpolation.swing)
                    )
            )));
        }

        spineAnimationsStage = new SpineAnimationsStage(fitViewport);

        topSpineAnimationsStage = new SpineAnimationsStage(fitViewportTop);

        MapObjects objects = tiledMap.getLayers().get("top_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                topSpineAnimationsStage.addActor(actor);
            }
        }

        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), font);
        stage.addActor(textGroup);


        setControls();

        coins = parent.getNumbers("coins");

        skills[0] = parent.getNumbers("skill_1");
        skills[1] = parent.getNumbers("skill_2");
        skills[2] = parent.getNumbers("skill_3");

        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        upTextGroup.setLabel("coins", "" + coins);


        /*SpineAnimationActor actor = (SpineAnimationActor) spineAnimationsStage.getActors().get(0);
        actor.setAnimationState(!(skills[0] <= 0));
        actor = (SpineAnimationActor) spineAnimationsStage.getActors().get(1);
        actor.setAnimationState(!(skills[1] <= 0));
        actor = (SpineAnimationActor) spineAnimationsStage.getActors().get(2);
        actor.setAnimationState(!(skills[2] <= 0));*/

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);
        topSpineAnimationsStage.act(delta);

        bottomStage.act(delta);
        stage.act(delta);
        top_stage.act(delta);

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            activeBackgroundStage.draw();
        batch.end();

        fitViewportBottom.apply();
        batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);
        batch.begin();
            bottomStage.draw();
        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            spineAnimationsStage.draw(fitViewport.getCamera().combined);
            stage.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            top_stage.draw();
            topSpineAnimationsStage.draw(fitViewportTop.getCamera().combined);

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

        offset = fitViewport.getBottomGutterHeight() - (int) parent.getIOSSafeAreaInsets().y;
        bottomStage.getViewport().setScreenPosition(fitViewportBottom.getScreenX(),
                fitViewportBottom.getScreenY() - offset);
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
        activeBackgroundStage.dispose();
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

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            if (type.equals("back")) {
                goBack();
            }
            if (type.equals("coins"))
                parent.setScreen(new PurchasesScreen(parent, camera, this));
            if (type.equals("skill"))
                parent.setScreen(new BuySkillScreen(parent, camera, this));
        }
    }

    private void goBack(){
        parent.setScreen(back);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);
        if(selActor == null) {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
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


        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);
        if(selActor == null) {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
        }

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

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
