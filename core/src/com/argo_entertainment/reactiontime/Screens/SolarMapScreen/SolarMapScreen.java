package com.argo_entertainment.reactiontime.Screens.SolarMapScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Screens.FinishScreen.FinishScreen;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.Screens.LevelScreen.LevelScreenBack;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Actors.GameObject;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class SolarMapScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;

    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<GameObject> instances = new Array<GameObject>();
    public Environment environment;

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

    private FillViewport fillViewport;
    private FitViewport extendViewport;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    private int lvl_type = 1;

    public SolarMapScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        back_screen = back;
    }

    @Override
    public void show() {
        extendViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1));
        //environment.set(new ColorAttribute(ColorAttribute.Specular, 1,1,1,1));
        // environment.add(new DirectionalLight().set(new Color(253f, 184f, 19f , 0.5f), 0, 0, -155));
        //environment.add(new PointLight().set(new Color(1, 1, 1 , 1f), new Vector3(0, 75, 50f), 100f));

        cam = new PerspectiveCamera(67, 1080, 1920);
        cam.position.set(0, 75, 150f);
        cam.lookAt(0,10,150f);

        //cam.rotate(new Quaternion().setEulerAngles(0,200, 0));
        // cam.rotate(cam.direction.crs(Vector3.X), 90);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // camController = new CameraInputController(cam);

        loadModels();

        TiledMap tiledMap = parent.tiledMap("solar_map/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();

        stage = new Stage(extendViewport);
        backStage = new Stage(fillViewport);

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("btn_back"));
        stage.addActor(btnBack);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);


        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        stage.addActor(textGroup);

        textGroup.setLabel("score", "" + parent.getNumbers("high_score"));


        spineAnimationsStage = new SpineAnimationsStage(extendViewport);

        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();

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

        InputMultiplexer multiplexer;
        GestureDetector gd = new GestureDetector(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        // multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);

        // stage.setDebugAll(true);

        Gdx.input.setCatchBackKey(true);
        DefaultShader.defaultCullFace = 0;


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
    }

    private void loadModels(){
        instances.clear();

        Material doneBtnMaterial = new Material();

        for (int j = 0; j <= 10; j++) {
            Model planet = parent.assetManager.get("solar_map/3d_models2/"+j+".g3db", Model.class);

            Material openPlanet = new Material();
            Material closedMaterial = new Material();
            for(int i = 0; i< planet.materials.size; i++) {
                final Material modelMaterial = planet.materials.get(i);
                modelMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                String[] modelId = modelMaterial.id.split("_");
                if(modelId.length >= 2 && modelId[1].equals("open")){
                    openPlanet = modelMaterial;
                }
                if(modelId.length >= 2 && modelId[1].equals("closed")){
                    closedMaterial = modelMaterial;
                }
                if(modelMaterial.id.equals("button _done.001")){
                    doneBtnMaterial = modelMaterial;
                }
            }

            for (int i = 0; i < planet.nodes.size; i++) {
                Node node = planet.nodes.get(i);
                String id = planet.nodes.get(i).id;
                String[] idDet = id.split("_");
                String[] idNum = new String[1];

                if(idDet.length > 1) {
                    idNum = idDet[idDet.length - 1].split("\\.");
                }

                if(idNum.length >= 2) {
                    idDet[idDet.length - 1] = idNum[0];
                }

                if(idDet.length < 2 || !(idDet[1].equals("play") || idDet[1].equals("lvl") || (idDet.length == 3 && idDet[2].equals("b")))) {

                    String name = "planet_" + j + "_" + planet.nodes.get(i).id;
                    GameObject instance = new GameObject(planet, id, true, name);

                    for(int t = 0; t < instance.materials.size; t++) {
                        final Material instanceMat = instance.materials.get(t);
                        String[] modelId = instanceMat.id.split("_");
                        if(modelId.length >= 2 && modelId[1].equals("closed") && !parent.isClosedPlanet(j)) {
                            instanceMat.set(openPlanet);
                        }
                    }

                    if(idDet.length >= 1 && idDet[0].equals("castle")) {
                        if(parent.isClosedPlanet(j)) instances.add(instance);
                    } else {
                        instances.add(instance);
                    }
                }

                /*if(idDet.length > 1 && idDet[1].equals("lvl")) {

                }*/

                if(!parent.isClosedPlanet(j) && (j < 9 && parent.isClosedPlanet(j + 1))){

                    if(idDet.length > 1 && idDet[1].equals("play")) {
                        String name = "planet_" + j + "_" + planet.nodes.get(i).id;
                        GameObject instance = new GameObject(planet, id, true, name);

                        instances.add(instance);
                    }
                } else {
                    if(idDet.length == 3 && idDet[2].equals("b")) {
                        String name = "planet_" + j + "_" + planet.nodes.get(i).id;
                        GameObject instance = new GameObject(planet, id, true, name);

                        if(!parent.isClosedPlanet(j)){
                            final Material instMat = instance.materials.get(0);
                            instMat.set(doneBtnMaterial);
                        }

                        instances.add(instance);
                    }
                }
            }
        }
    }

    double x, y;
    float timer = 0;


    public static final float TIME_TO_SCROLL = 2.0f;

    private float mTimer;
    private float mVelocityY;
    private final float mLowerPosition = -220f;
    private final float mUpperPosition = 150f;
    private Quaternion rotation;
    private float velocitySum = 0;

    @Override
    public void render(float delta) {
        // camController.update();
        //cam.position.y =  6.3f;

        if (mTimer > 0) {// if timer is not 0
            float acceleration_y = mVelocityY * delta;// calculate acceleration (the rate of change of velocity)
            mTimer -= delta;// decreasing timer
            mVelocityY -= acceleration_y;// decreasing velocity
            velocitySum += mVelocityY;

            cam.position.z -= mVelocityY;
            checkCameraPosition();
        }

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        backStage.act(delta);
        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            activeBackgroundStage.draw();

            modelBatch.begin(cam);

                modelBatch.render(instances, environment);

            modelBatch.end();
        batch.end();

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            backStage.draw();
        batch.end();

        extendViewport.apply();
        batch.setProjectionMatrix(extendViewport.getCamera().combined);

        batch.begin();


            stage.draw();
            spineAnimationsStage.draw(extendViewport.getCamera().combined);


        batch.end();

        // controller.paused = !go_animation;
        //textGroup.setLabel("score", "FPS: " + Gdx.graphics.getFramesPerSecond());
    }
    private void checkCameraPosition() {
        if (cam.position.z > mUpperPosition) {
            cam.position.z = mUpperPosition;
            mTimer = 0;
        }

        if (cam.position.z < mLowerPosition) {
            cam.position.z = mLowerPosition;
            mTimer = 0;
        }

        cam.lookAt(0,0, cam.position.z);
        cam.update();
    }
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        fillViewport.update(width, height, true);
        /*cam.viewportWidth = width;
        cam.viewportHeight = height;*/
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        loadModels();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
        spineAnimationsStage.dispose();
        activeBackgroundStage.dispose();
    }

    private Vector3 position = new Vector3();
    private int selected = -1, selecting = -1;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        mTimer = 0;
        Gdx.app.log("camera position", cam.position + " %%");
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
        } else {
            selecting = getObject((int) x, (int) y);
            return selecting >= 0;
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
        Gdx.app.log("FLING", "" + velocityY);
        mTimer = TIME_TO_SCROLL;
        mVelocityY = (velocityY / 30) * Gdx.graphics.getDeltaTime();

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Gdx.app.log("pos deltaX", "" + deltaY);
        float cam_y = cam.position.z;

        if (cam_y >= mLowerPosition && cam_y <= mUpperPosition) {
            cam.position.z -= deltaY / 10;
        }
        if (cam.position.z > mUpperPosition) cam.position.z = mUpperPosition;
        if (cam.position.z < mLowerPosition) cam.position.z = mLowerPosition;

        cam.lookAt(0,0, cam.position.z);
        cam.update();

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

    public int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        String name_item = "";
        for (int i = 0; i < instances.size; ++i) {
            final GameObject instance = instances.get(i);

            instance.transform.getTranslation(position);

            boolean haveInter = Intersector.intersectRayBoundsFast(ray, position, instance.getDimensions());
            if(haveInter) {
                result = i;
                name_item = instance.getName();

                /*Gdx.app.log("init", " ************* ");

                Gdx.app.log("init", "GameObject NAME >>>  " + instance.getName());
                Gdx.app.log("init", "GameObject CENTER >>>  X -" + instance.getBounds().getCenterX() + " Y - " + instance.getBounds().getCenterY() + " Z - " + instance.getBounds().getCenterZ());
                Gdx.app.log("init", "GameObject Radius >>>  " + instance.getRadius());

                Gdx.app.log("init", " ############## ");*/

                String[] planet = instance.getPlanetName().split("_");
                if(planet[0].equals("planet")){
                    goToPlanet(planet[1]);
                }
                break;
            }
        }
        return result;
    }

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();

            if (type.equals("back"))
                parent.setScreen(back_screen);
            if (type.equals("market"))
                parent.setScreen(new ShopScreen(parent, camera, this));
        }
    }


    private void goToPlanet(String type) {
        int num = Integer.parseInt(type);


        if(num > 0 && !parent.isClosedPlanet(num)) {
            int field_type = 1 + (int) (Math.random() * 5);

            String extens = "g3db";
            parent.setScreen(new LevelScreenBack(parent, camera, this, num + "." + extens,num,field_type));
        }
    }

    private void finishedSolar(){
        parent.setScreen(new FinishScreen(parent, camera, new MainMenuScreen(parent, camera, null)));
    }
}