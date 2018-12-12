package com.argo_entertainment.reactiontime.Screens.StatsScreen;

import com.argo_entertainment.reactiontime.Actors.BackgroundActor;
import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.ActiveBackgroundGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.Screens.BuySkillScreen.BuySkillScreen;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.CameraInputLevel;
import com.argo_entertainment.reactiontime.Screens.PurchasesScreen.PurchasesScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
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
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class StatsScreen implements Screen, GestureDetector.GestureListener {
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


    private Integer skills[] = new Integer[3];


    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    private ModelInstance shipInstance;

    private ActiveBackgroundStage activeBackgroundStage;
    private SpineAnimationsStage spineAnimationsStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public StatsScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){

        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;

    }

    @Override
    public void show() {
        Gdx.app.log("navigation", "StatsScreen");
        parent.assetManager.load("stat/stat_map.tmx", TiledMap.class);
        parent.assetManager.load("3d_models/ship/ship_1.g3db", Model.class);
        parent.assetManager.finishLoading();

        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        TiledMap tiledMap = parent.tiledMap("stat/stat_map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();
        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        stage = new Stage(fitViewport);
        top_stage = new Stage(fitViewportTop);
        bottomStage = new Stage(fillViewport);


        BackgroundGroup bottomStageBack = new BackgroundGroup(tiledMap.getLayers().get("bottom_back"));
        bottomStage.addActor(bottomStageBack);

        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        top_stage.addActor(btnBack);

        ButtonGroup topBtnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        top_stage.addActor(topBtnGroup);

        bgGroup = new BackgroundGroup(tiledMap.getLayers().get("menu_bg"));
        stage.addActor(bgGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);

        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        stage.addActor(textGroup);


        textGroup.setLabel("player_name", "" + parent.getPlayerName());
        textGroup.setLabel("levels", "" + parent.getNumbers("done_levels"));
        textGroup.setLabel("all_points", "" + parent.getNumbers("all_points"));
        textGroup.setLabel("items", "" + parent.getNumbers("all_items"));
        textGroup.setLabel("best_score", "" + parent.getNumbers("high_score"));

        textGroup.setLabel("coins", parent.getNumbers("coins").toString());


        stage.addAction(sequence(
                Actions.hide(),
                moveBy(0, -parent.h),
                Actions.show(),
                moveBy(0, parent.h, parent.menu_animation_speed, Interpolation.swingOut)
        ));

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1f));
        //environment.add(new PointLight().set(Color.WHITE, new Vector3(0, 25, 0), 400f));


        cam = new PerspectiveCamera(72, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 2.8f, 7.7f);
        cam.lookAt(0f,3,0f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        Model ship = parent.assetManager.get("3d_models/ship/ship_1.g3db", Model.class);

        for(int i = 0; i< ship.materials.size; i++) {
            final Material modelMaterial = ship.materials.get(i);
            modelMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

            modelMaterial.set(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Specular, Color.WHITE));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Emissive, Color.WHITE));
        }
        shipInstance = new ModelInstance(ship);
        instances.add(shipInstance);


        spineAnimationsStage = new SpineAnimationsStage(new FillViewport(1080, 1920, camera));

        MapObjects objects = tiledMap.getLayers().get("top_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
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

     private Boolean animateCamera = true;
    private float angle = 2f;

    @Override
    public void render(float delta) {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        top_stage.act(delta);
        bottomStage.act(delta);
        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            activeBackgroundStage.draw();
            bottomStage.draw();
        batch.end();

        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            modelBatch.begin(cam);

                modelBatch.render(instances, environment);

            modelBatch.end();

            stage.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            top_stage.draw();
            spineAnimationsStage.draw(fitViewportTop.getCamera().combined);

        batch.end();

        if(animateCamera) {
            cam.rotateAround(new Vector3(0f,3,0f), Vector3.Y, angle);
            cam.update();
        } else { angle = 0; }
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
        modelBatch.dispose();
        stage.dispose();
        activeBackgroundStage.dispose();
        spineAnimationsStage.dispose();

        parent.assetManager.unload("stat/stat_map.tmx");
        parent.assetManager.unload("3d_models/ship/ship_1.g3db");
    }


    private BtnActor selActor;

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
            if (type.equals("back")) {
                goBack();
            }

            if (type.equals("shop")) {
                parent.setScreen(new PurchasesScreen(parent, camera, this));
            }

            if (type.equals("market")) {
                parent.setScreen(new ShopScreen(parent, camera, this));
            }


            if (type.equals("gift")) {
                parent.clearPrefs();
            }
        }
    }


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



        camController = new CameraInputLevel(cam);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        multiplexer.addProcessor(ip);
        multiplexer.addProcessor(camController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void goBack() {
        parent.setScreen(new MainMenuScreen(parent, camera, null));
        this.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("camera position", cam.position + " %%");
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor == null) {
            selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
        }
        animateCamera = false;
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
        if(velocityX > 0) angle = -2; else  angle = 2;
        return false;
    }

    private float lastPanX = 0f;
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(lastPanX > x) angle = -2; else  angle = 2;
        lastPanX = x;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(selActor != null) {
            checkBtnClick();
        }
        animateCamera = true;
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
