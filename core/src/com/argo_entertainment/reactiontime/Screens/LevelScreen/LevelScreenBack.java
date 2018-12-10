package com.argo_entertainment.reactiontime.Screens.LevelScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.GameElementActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.GameField.GameFieldScreen;
import com.argo_entertainment.reactiontime.GameField.GameFieldScreenX;
import com.argo_entertainment.reactiontime.Groups.AnimatedTextGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.GameElementGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Actors.GameObject;
import com.argo_entertainment.reactiontime.Screens.SolarMap2D.SolarMap2D;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.graphics.VertexAttributes;
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
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class LevelScreenBack implements Screen, GestureDetector.GestureListener {


    private ReactionTimeClass parent;

    private OrthographicCamera camera;

    public PerspectiveCamera cam;
    public CameraInputLevel camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;


    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private SpriteBatch batch;

    private ButtonGroup btnGroup;
    private Actor selActor = null;

    private TextGroup textGroup;


    private AnimationController controller;
    private ModelInstance planetInstance;
    private Boolean go_animation = false;

    private String planet_name;
    private Screen back_screen;

    private int lvl_type = 1;
    private int planet_num = 1;
    private int element_num = 0;
    Integer[][] savedEl;

    private Material selectedMaterial, normalMaterial, closedMaterial;

    private SpineAnimationsStage spineAnimationsStage, topSpineAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private AnimatedTextGroup animatedTextGroup;
    private GameElementGroup levelDoneElement;
    private SpineAnimationActor button_start;

    private Stage stage, bottomStage, topStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport, fitViewportBottom;
    private ScalingViewport fitViewportTop;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public LevelScreenBack(ReactionTimeClass game, OrthographicCamera game_camera, Screen back , String planet, int planet_num, int type){
        // back.dispose();
        parent = game;
        camera = game_camera;
        back_screen = back;
        planet_name = planet;
        this.planet_num = planet_num;
        lvl_type = type;

    }

    DirectionalLight light;
    PointLight light2;
    SpotLight light3;

    @Override
    public void show() {
        //загрузка 3d планеты
        parent.assetManager.load("level_select/3d_planets/"+planet_num+".g3db", Model.class);
        parent.assetManager.finishLoading();

        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);
        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));

        light2 = new PointLight().set(new Color(255, 255, 255 , 1), new Vector3(2f, 3f, -7.134f), 5f);
        //environment.add(light2);

        cam = new PerspectiveCamera(67, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        cam.position.set(2f, 3f, -7.134f);
        cam.lookAt(0f,-1.5f,0f);
        cam.near = 0.1f;
        cam.far = 200f;
        cam.update();

        camController = new CameraInputLevel(cam);


        savedEl = parent.getSavedElements(planet_num);

        Model planet = parent.assetManager.get("level_select/3d_planets/"+planet_name, Model.class);

        for(int i = 0; i< planet.materials.size; i++) {
            final Material modelMaterial = planet.materials.get(i);

            modelMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Specular, Color.WHITE));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Emissive, Color.WHITE));
            modelMaterial.set(new ColorAttribute(ColorAttribute.Ambient, Color.WHITE));

            String [] id = modelMaterial.id.split("_");
            if(id[0].equals("button")) {
                String[] other = id[1].split("\\.");
                if(other.length > 1) id[1] = other[0];
                if (id[1].equals("closed")) {
                    closedMaterial = modelMaterial;
                }
                if (id[1].equals("passed")) {
                    normalMaterial = modelMaterial;
                }
                if (id[1].equals("play") || (id.length > 2 && id[2].equals("play"))) {
                    selectedMaterial = modelMaterial;
                }
            }
        }

        for (int i = 0; i < planet.nodes.size; i++) {
            String id = planet.nodes.get(i).id;
            GameObject instance = new GameObject(planet, id, true);

            Vector3 posit = new Vector3();
            instance.transform.getTranslation(posit);
            //instance.transform.setTranslation(new Vector3(posit.x,posit.y + 1.35f, posit.z));


            if(instance.getName().equals("button_1")) {
                instance.materials.get(2).set(closedMaterial);
            } else if(instance.getName().split("_")[0].equals("button")) {
                instance.materials.get(1).set(closedMaterial);
            }

            if(instance.getName().split("_")[0].equals("button")) {
                int buttonId;
                String val = instance.getName().split("_")[1];

                if (val.contains(".")){
                    buttonId = (int) Float.parseFloat(val);
                } else {
                    buttonId = Integer.parseInt(val);
                }

                if (savedEl.length >= buttonId) {
                    if (savedEl[buttonId - 1][0].equals(savedEl[buttonId - 1][1])) {
                        if (instance.getName().equals("button_1")) {
                            instance.materials.get(2).set(normalMaterial);
                        } else if (instance.getName().split("_")[0].equals("button")) {
                            instance.materials.get(1).set(normalMaterial);
                        }
                    }

                    instances.add(instance);
                }
            } else {
                instances.add(instance);
            }
        }

        go_animation = true;


        TiledMap tiledMap = parent.tiledMap("level_select/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);


        stage = new Stage(fitViewport);
        topStage = new Stage(fitViewportTop);
        bottomStage = new Stage(fitViewportBottom);

        BackgroundGroup btnBack = new BackgroundGroup(tiledMap.getLayers().get("bottom_back"));
        bottomStage.addActor(btnBack);

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("menu_back"));
        btnBack.findActor("menu_back").setTouchable(Touchable.disabled);
        stage.addActor(btnBack);

        btnBack = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        topStage.addActor(btnBack);

        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        topStage.addActor(textGroup);

        ButtonGroup btnGroup = new ButtonGroup(tiledMap.getLayers().get("top_back"), true);
        topStage.addActor(btnGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        stage.addActor(btnGroup);

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
                actor.setWidth(135);
                actor.setHeight(135);

                if (elementNum > savedEl.length) {
                    actor.setVisible(false);
                } else {
                    actor.setState(savedEl[elementNum - 1][0].equals(savedEl[elementNum - 1][1]));
                }
                bottomStage.addActor(actor);
            }
        }



        textGroup.setLabel("score", "" + parent.getNumbers("high_score"));



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
                if(cell.getName().equals("button_start")){
                    button_start = actor;
                    button_start.setVisible(false);
                    spineAnimationsStage.addActor(button_start);
                } else {
                    spineAnimationsStage.addActor(actor);
                }
            }
        }

        topSpineAnimationsStage = new SpineAnimationsStage(fitViewport);

        objects = tiledMap.getLayers().get("top_anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;
                TextureAtlas atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas);
                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                topSpineAnimationsStage.addActor(actor);
            }
        }

        DefaultShader.defaultCullFace = 0;

        activeBackgroundStage = new ActiveBackgroundStage(new FillViewport(1080, 1920, camera), parent.getBackObj());

        levelDoneElement = new GameElementGroup(tiledMap.getLayers().get("elements"), parent.getElementsSprite(planet_num, 1));
        levelDoneElement.setAll();
        levelDoneElement.setVisible(false);
        stage.addActor(levelDoneElement);

        animatedTextGroup = new AnimatedTextGroup(tiledMap.getLayers().get("menu_text"), fonts);
        stage.addActor(animatedTextGroup);
        setText(0);

        //bottomStage.setDebugAll(true);
        setControls();
    }

    private String[][] planetTexts;

    private void setText(int element_num) {
        planetTexts = parent.getText(planet_num);
        animatedTextGroup.setLabel("element_name", planetTexts[element_num][0]);
        animatedTextGroup.setLabel("element_text", planetTexts[element_num][1]);
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

    private boolean panNow = false;

    @Override
    public void render(float delta) {
        if(panNow)
         camController.update(delta);

        cam.update();
        light2.setPosition(cam.position);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        topStage.act(delta);
        stage.act(delta);
        bottomStage.act(delta);

        topSpineAnimationsStage.act(delta);
        spineAnimationsStage.act(delta);
        activeBackgroundStage.act(delta);

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            activeBackgroundStage.draw();

            modelBatch.begin(cam);
            modelBatch.setCamera(cam);
                modelBatch.render(instances, environment);
            modelBatch.end();
        batch.end();


        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);
        batch.begin();

            topStage.draw();
            topSpineAnimationsStage.draw(fitViewportTop.getCamera().combined);

        batch.end();


        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);

        batch.begin();

            stage.draw();

            spineAnimationsStage.draw(fitViewport.getCamera().combined);

        batch.end();

        fitViewportBottom.apply();
        batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);

        batch.begin();

            bottomStage.draw();

        batch.end();



        // controller.paused = !go_animation;
        //textGroup.setLabel("score", "FPS: " + Gdx.graphics.getFramesPerSecond());
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

        bottomStage.getViewport().setScreenPosition(fitViewportBottom.getScreenX(),
                fitViewportBottom.getScreenY() - fitViewport.getBottomGutterHeight());

        Gdx.app.log("getTopGutterHeight", "" + fitViewport.getTopGutterHeight());
        Gdx.app.log("getIOSSafeAreaInsets", parent.getIOSSafeAreaInsets().toString());
        Gdx.app.log("offset", offset + "");
        Gdx.app.log("fitViewportTop getScreenY", fitViewportTop.getScreenY() + "");

        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        parent.assetManager.load("level_select/3d_planets/"+planet_num+".g3db", Model.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        spineAnimationsStage.dispose();
        topSpineAnimationsStage.dispose();
        activeBackgroundStage.dispose();
        parent.assetManager.unload("level_select/3d_planets/"+planet_num+".g3db");
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
        multiplexer.addProcessor(camController);
        multiplexer.addProcessor(ip);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    private void goBack() {
        parent.setScreen(new SolarMap2D(parent, camera, null, planet_num));
    }

    private boolean findActor(Vector2 coords){
        Vector2 pos = stage.screenToStageCoordinates(coords);
        selActor = (BtnActor) stage.hit(pos.x, pos.y, true);
        if(selActor != null) {
            checkBtnClick();
            return true;
        } else {
            //pos = topStage.screenToStageCoordinates(coords);
            selActor = (BtnActor) topStage.hit(pos.x, pos.y, true);
            if(selActor != null) {
                checkBtnClick();
                return true;
            }  else {
                selecting = getObject(coords.x, coords.y);
                return selecting >= 0;
            }
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        selActor = (BtnActor) stage.hit(pos.x, pos.y, true);
        if(selActor == null) {
            selActor = (BtnActor) topStage.hit(pos.x, pos.y, true);
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return findActor(new Vector2(x, y));
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
        panNow = true;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        panNow = false;
        //findActor(new Vector2(x, y));
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

    /*public int getObject (float screenX, float screenY) {
        Gdx.app.log("getObject screenX screenY", new Vector2(screenX, screenY).toString());
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        String name_item = "";
        for (int i = 0; i < instances.size; i++) {
            final GameObject instance = (GameObject) instances.get(i);

            instance.getBounds().getCenter(position);
            position.add(instance.center);
            //instance.transform.getTranslation(position);

            boolean haveInter = Intersector.intersectRayBoundsFast(ray, position, instance.getDimensions());
            if(haveInter) Gdx.app.log("init", "GameObject NAME >>>  " + instance.getName());
            if(haveInter && instance.getName().split("_")[0].equals("button")) {
                result = i;
                name_item = instance.getName();
                String[] button = instance.getName().split("_");

                *//*Gdx.app.log("init", " ************* ");

                Gdx.app.log("init", "GameObject CENTER >>>  X -" + instance.getBounds().getCenterX() + " Y - " + instance.getBounds().getCenterY() + " Z - " + instance.getBounds().getCenterZ());
                Gdx.app.log("init", "GameObject Radius >>>  " + instance.getRadius());

                Gdx.app.log("init", " ############## ");*//*


                if(selectedLevel != null){
                    if (selectedLevel.getName().equals("button_1")) {
                        if(!savedEl[element_num - 1][0].equals(savedEl[element_num - 1][1]))
                            selectedLevel.materials.get(2).set(closedMaterial);
                        else
                            selectedLevel.materials.get(2).set(normalMaterial);
                    } else if(selectedLevel.getName().split("_")[0].equals("button")) {
                        if(!savedEl[element_num - 1][0].equals(savedEl[element_num - 1][1]))
                            selectedLevel.materials.get(1).set(closedMaterial);
                        else
                            selectedLevel.materials.get(1).set(normalMaterial);

                    }
                }

                element_num = Integer.parseInt(button[1]);

                if (instance.getName().equals("button_1")) {
                    instance.materials.get(2).set(selectedMaterial);
                } else if(button[0].equals("button")) {
                    instance.materials.get(1).set(selectedMaterial);
                }

                button_start.setVisible(true);

                setText(element_num);

                GameElementActor elementActor = (GameElementActor) levelDoneElement.getChildren().first();
                elementActor.setTextureRegion(parent.getElementsSprite(planet_num, element_num));
                levelDoneElement.setVisible(true);


                selectedLevel = instance;
                //break;
            }
        }
        return result;
    }*/

    public int getObject (float screenX, float screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < instances.size; ++i) {
            final GameObject instance = (GameObject)instances.get(i);
            position = Vector3.Zero;
            instance.transform.getTranslation(position);
            position.add(instance.center);
            //position.add(new Vector3(0, -1.6f, 0)); //Make love, not war

            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)
                continue;
            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
            if (distance >= 0f && dist2 > distance)
                continue;
            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                Gdx.app.log("init", "GameObject NAME >>>  " + instance.getName());
                Gdx.app.log("init", "GameObject distance >>>  " + distance + " ");
                distance = dist2;
            }
        }

        if(result >= 0) {
            GameObject instance = (GameObject) instances.get(result);

            String name_item = "";
            if (instance.getName().split("_")[0].equals("button")) {
                name_item = instance.getName();
                String[] button = instance.getName().split("_");


                if (selectedLevel != null) {
                    if (selectedLevel.getName().equals("button_1")) {
                        if (!savedEl[element_num - 1][0].equals(savedEl[element_num - 1][1]))
                            selectedLevel.materials.get(2).set(closedMaterial);
                        else
                            selectedLevel.materials.get(2).set(normalMaterial);
                    } else if (selectedLevel.getName().split("_")[0].equals("button")) {
                        if (!savedEl[element_num - 1][0].equals(savedEl[element_num - 1][1]))
                            selectedLevel.materials.get(1).set(closedMaterial);
                        else
                            selectedLevel.materials.get(1).set(normalMaterial);

                    }
                }

                element_num = Integer.parseInt(button[1]);

                if (instance.getName().equals("button_1")) {
                    instance.materials.get(2).set(selectedMaterial);
                } else if (button[0].equals("button")) {
                    instance.materials.get(1).set(selectedMaterial);
                }


                if (element_num > 1) {
                    Integer[] beforeElement = parent.getSavedElements(planet_num, element_num - 1);
                    if (beforeElement[0].equals(beforeElement[1])) {
                        button_start.setVisible(true);
                    } else {
                        button_start.setVisible(false);
                    }
                } else {
                    button_start.setVisible(true);
                }

                setText(element_num);

                GameElementActor elementActor = (GameElementActor) levelDoneElement.getChildren().first();
                elementActor.setTextureRegion(parent.getElementsSprite(planet_num, element_num));
                levelDoneElement.setVisible(true);


                selectedLevel = instance;
                //break;
            }
        }
        return result;
    }


    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();

            if (type.equals("start") && element_num > 0) {

                parent.setScreen(new GameFieldScreenX(parent, camera, lvl_type, planet_name, planet_num, element_num));
                this.dispose();
                }

            if (type.equals("back")) {
                parent.setScreen(back_screen);
                this.dispose();
            }

            if (type.equals("market")) {
                parent.setScreen(new ShopScreen(parent, camera, this));
                this.dispose();
            }

            if (type.equals("watch_video")) {
                parent.setScreen(new LevelDone(parent, camera, this, planet_name, planet_num, element_num));
                this.dispose();
            }
        }
    }
}