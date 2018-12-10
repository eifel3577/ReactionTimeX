package com.argo_entertainment.reactiontime.Screens.PersonalScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PersonalScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;

    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;


    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private SpriteBatch batch;
    private Stage stage;

    private ButtonGroup btnGroup;
    private BtnActor selActor = null;

    private TextGroup textGroup;
    private String debugText = "";

    private AnimationController controller;
    private ModelInstance shipInstance;
    private ModelInstance planetInst;
    private AnimationController planetAnimation;
    private Boolean go_animation = false;

    public PersonalScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
    }

    @Override
    public void show() {
        Gdx.app.log("debug param - ", GL20.GL_MAX_TEXTURE_SIZE + " GL20.GL_MAX_TEXTURE_SIZE");
        Gdx.app.log("debug param - ", GL30.GL_MAX_TEXTURE_SIZE + " GL30.GL_MAX_TEXTURE_SIZE");
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(Color.ORANGE, 0f, 0f, 0f));
        environment.add(new PointLight().set(Color.RED, new Vector3(10f, 10f, 10), 100f));
        environment.add(new PointLight().set(Color.BLUE, new Vector3(0f, 0, 0), 100f));
        environment.add(new PointLight().set(Color.GREEN, new Vector3(-10f, 10, 0), 100f));
        environment.add(new PointLight().set(Color.YELLOW, new Vector3(00f, -10, -10), 100f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(-10f, 17f, -20f);
        cam.lookAt(0f,0f,0f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);

        Model ship = parent.assetManager.get("3d_models/ship_anim.g3db", Model.class);
        ship.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

        shipInstance = new ModelInstance(ship);
        instances.add(shipInstance);

        controller = new AnimationController(shipInstance);
        controller.setAnimation("Plane02|Plane02Action",-1, new AnimationController.AnimationListener(){

            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
                // controller.queue("Plane02|Plane02Action",-1,1f,null,0f);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });

        TiledMap tiledMap = parent.tiledMap("3d_models/map/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);


        stage = new Stage(new FitViewport(1080, 1920, camera));

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);


        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        stage.addActor(textGroup);



        InputMultiplexer multiplexer;
        GestureDetector gd = new GestureDetector(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);

        // stage.setDebugAll(true);

        Gdx.input.setCatchBackKey(true);
        DefaultShader.defaultCullFace = 0;
    }

    @Override
    public void render(float delta) {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
// You need to call update on the animation controller so it will advance the animation.  Pass in frame delta
        controller.update(Gdx.graphics.getDeltaTime());
        planetAnimation.update(Gdx.graphics.getDeltaTime());

        modelBatch.begin(cam);
            modelBatch.render(instances, environment);

        modelBatch.end();


        stage.act(Gdx.graphics.getDeltaTime());

        batch.begin();
            stage.draw();

        batch.end();

        controller.paused = !go_animation;
        planetAnimation.paused = !go_animation;

        debugText = "~~~ debug 3d scene ~~~\n\r";
        debugText += "Animation - " +go_animation+"\n\r";
        debugText += "Camera position \n\r"+cam.position+"\n\r";
        debugText += "Model position \n\r"+instances.first().transform+"\n\r";

        textGroup.setLabel("debug", debugText);
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
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor != null) {
            selActor.setClick(true);
        }
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

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();

            if (type.equals("back"))
                parent.setScreen(new MainMenuScreen(parent, camera, this));

            if (type.equals("anim")) {
                go_animation = !go_animation;
            }

            if (type.equals("switch")) {
                if(instances.first() == shipInstance){
                    instances.clear();
                    instances.add(planetInst);
                } else{
                    instances.clear();
                    instances.add(shipInstance);
                }
            }
        }
    }
}
