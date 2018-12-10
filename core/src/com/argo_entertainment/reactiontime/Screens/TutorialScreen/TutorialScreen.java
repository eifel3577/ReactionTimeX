package com.argo_entertainment.reactiontime.Screens.TutorialScreen;

import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Actors.StateActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.StateGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class TutorialScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private int activeTutorialStage = 0;

    private StateGroup btnGroup;

    private SpineAnimationActor animationActor;
    private PolygonSpriteBatch polygonSpriteBatch;

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            if(text.matches("[a-zA-Z0-9]*")) {
                parent.setSettings("player_name", text);
                Gdx.app.log("player_name", text);
                parent.setScreen(new MainMenuScreen(parent, camera, null));
            } else {
                parent.setSettings("player_name", "Reaction Player");
                parent.setScreen(new MainMenuScreen(parent, camera, null));
            }
        }

        @Override
        public void canceled () {
        }
    }

    public TutorialScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;
    }

    SkeletonRenderer renderer;
    TextureAtlas atlas;
    SkeletonRendererDebug debug;

    @Override
    public void show() {
        TiledMap tiledMap = parent.tiledMap("tutorial/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

        stage = new Stage(new FillViewport(1080, 1920, camera));

        BackgroundGroup bgGroup = new BackgroundGroup(tiledMap.getLayers().get("menu"));
        stage.addActor(bgGroup);

        btnGroup = new StateGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);
        btnGroup.setState("state_1", 1);
        btnGroup.setState("state_2", 0);
        btnGroup.setState("state_3", 0);
        btnGroup.setState("next", 0);

        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.

        debug = new SkeletonRendererDebug();
        debug.setBoundingBoxes(false);
        debug.setRegionAttachments(false);

        polygonSpriteBatch = new PolygonSpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.

        atlas = new TextureAtlas(Gdx.files.internal("tutorial/anim/explanation_screen1.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(1.2f);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("tutorial/anim/explanation_screen1.json"));

        animationActor = new SpineAnimationActor((RectangleMapObject) tiledMap.getLayers().get("anim").getObjects().get(0), skeletonData);

        stage.addActor(animationActor);

        /*stage.addAction(sequence(
                Actions.hide(),
                moveBy(parent.h, 0),
                Actions.show(),
                moveBy(-parent.h, 0, parent.menu_animation_speed, Interpolation.swingOut)
        ));*/

        InputMultiplexer multiplexer;

        Gdx.input.setCatchBackKey(true); //@ToDo: Review: place at function
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

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Gdx.graphics.getDeltaTime());

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        stage.draw();

        batch.end();


        camera.update();
        polygonSpriteBatch.getProjectionMatrix().set(camera.combined);

        polygonSpriteBatch.begin();
            animationActor.draw(polygonSpriteBatch); // Draw the skeleton images.
        polygonSpriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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


    private StateActor selActor;

    private Action switchScreenAction = new Action(){
        @Override
        public boolean act(float delta){
            parent.setScreen(back);
            return true;
        }
    };

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            if (type.equals("next") || type.equals("control"))
            {
                if(activeTutorialStage < 2){
                    next();
                } else goBack();
            }

            if (type.equals("close")|| type.equals("finish"))
                goBack();
        }
    }



    private void goBack(){
        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Enter a nick to save the game results", "", "Reaction Player");
    }



    private void next(){
        activeTutorialStage++;

        btnGroup.setState("state_" + (activeTutorialStage + 1), 1);

        if(activeTutorialStage == 2) {
            btnGroup.setState("next", 1);
        }
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("tutorial/anim/explanation_screen"+ (activeTutorialStage + 1) +".atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        //json.setScale(1.5f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("tutorial/anim/explanation_screen"+ (activeTutorialStage + 1) +".json"));

        animationActor.setAnimation(json, skeletonData);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (StateActor) stage.hit(pos.x, pos.y, true);

        if(selActor == null) {
            Actor stage_actor = stage.hit(pos.x, pos.y, false);
            if(stage_actor == null) {

                if(activeTutorialStage < 2){
                    next();
                } else goBack();
            }
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
        if(selActor != null) {
            checkBtnClick();
        }
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
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
