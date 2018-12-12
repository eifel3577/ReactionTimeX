package com.argo_entertainment.reactiontime.Screens.PurchasesScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

//вызывается при нажатии на красную кнопку + в левом верхнем углу экрана MainMenu возле суммы денег
public class PurchasesScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private Stage top_stage;
    private Stage mapStage;
    private ButtonGroup btnGroup;
    private BackgroundGroup bgGroup;
    private TextGroup textGroup;
    private TextGroup listTextGroup;

    private Integer oldCoins = 0;


    private SpriteBatch particlesBatch;
    private ParticleEffect goParticleAnim;

    private SpineAnimationsStage spineAnimationsStage;
    private SpineAnimationsStage listAnimationsStage;
    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private ScalingViewport fitViewportTop;
    private ScalingViewport scalingViewport;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;
    TextureAtlas atlas;

    public PurchasesScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;
        parent.assetManager.load("buy_new/buy_map.tmx", TiledMap.class);
        parent.assetManager.load("anim/card1/card1.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/card2/card2.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/card3/card3.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/card4/card4.atlas", TextureAtlas.class);
        parent.assetManager.finishLoading();
    }

    @Override
    public void show() {
        Gdx.app.log("navigation", "PurchasesScreen");
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);

        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        scalingViewport = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);
        TiledMap tiledMap = parent.tiledMap("buy_new/buy_map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        stage = new Stage(fitViewport);
        top_stage = new Stage(fitViewportTop);


        BackgroundGroup backgroundGroup = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        top_stage.addActor(backgroundGroup);

        ButtonGroup topBtnGroup = new ButtonGroup(tiledMap.getLayers().get("top_btn"));
        top_stage.addActor(topBtnGroup);

        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);
        top_stage.addActor(textGroup);

        spineAnimationsStage = new SpineAnimationsStage(fitViewportTop);

        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                spineAnimationsStage.addActor(actor);
            }
        }

        mapStage = new Stage(scalingViewport);

        /*bgGroup = new BackgroundGroup(tiledMap.getLayers().get("items_list"));
        mapStage.addActor(bgGroup);*/

        listAnimationsStage = new SpineAnimationsStage(scalingViewport);

        objects = tiledMap.getLayers().get("anim_list").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                atlas = parent.assetManager.get("anim/"+cell.getName()+"/"+cell.getName()+".atlas");
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                listAnimationsStage.addActor(actor);
            }
        }

        listTextGroup = new TextGroup(tiledMap.getLayers().get("txt_list"), fonts);
        mapStage.addActor(listTextGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn_list"));
        mapStage.addActor(btnGroup);

        setControls();

        //stage.setDebugAll(true);

        oldCoins = parent.getNumbers("coins");
        textGroup.setLabel("coins", parent.getNumbers("coins").toString());

        listTextGroup.setLabel("pack_1", "1000/", Align.right);
        listTextGroup.setLabel("price_1", "4$", Align.left);
        listTextGroup.setLabel("pack_2", "750/", Align.right);
        listTextGroup.setLabel("price_2", "3$", Align.left);
        listTextGroup.setLabel("pack_3", "500/", Align.right);
        listTextGroup.setLabel("price_3", "2$", Align.left);
        listTextGroup.setLabel("pack_4", "250/", Align.right);
        listTextGroup.setLabel("price_4", "1$", Align.left);

        TextureAtlas particlesAtlas = new TextureAtlas(Gdx.files.internal("particles/particles_pack.atlas"));

        particlesBatch = new SpriteBatch();
        goParticleAnim = new ParticleEffect();
        goParticleAnim.load(Gdx.files.internal("particles/coins_point.p"), particlesAtlas);
        checkCameraPosition();
    }


    public static final float TIME_TO_SCROLL = 2.0f;

    private float mTimer;
    private float mVelocityY;
    private final float mLowerPosition = 220f;
    private final float mUpperPosition = 960f;

    private Boolean animated = false;

    @Override
    public void render(float delta) {
        //отображаем количество коинов
        if( oldCoins < parent.getNumbers("coins"))
        {
            textGroup.setLabel("coins", parent.getNumbers("coins").toString());
        }


        if (mTimer > 0) {// if timer is not 0
            float acceleration_y = mVelocityY * delta;// calculate acceleration (the rate of change of velocity)
            mTimer -= delta;// decreasing timer
            mVelocityY -= acceleration_y;// decreasing velocity

            scalingViewport.getCamera().position.y += mVelocityY;
            checkCameraPosition();
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        activeBackgroundStage.act(delta);
        spineAnimationsStage.act(delta);
        listAnimationsStage.act(delta);
        stage.act(delta);
        top_stage.act(delta);
        goParticleAnim.update(delta);


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();

            activeBackgroundStage.draw();

        batch.end();

        scalingViewport.apply();
        batch.setProjectionMatrix(scalingViewport.getCamera().combined);
        batch.begin();

        listAnimationsStage.draw(scalingViewport.getCamera().combined);
        mapStage.draw();

        batch.end();

        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            top_stage.draw();
            spineAnimationsStage.draw(fitViewportTop.getCamera().combined);

        batch.end();

        /*fitViewport.apply();
        particlesBatch.setProjectionMatrix(fitViewport.getCamera().combined);
        if (animated) {
            particlesBatch.begin();
                goParticleAnim.draw(particlesBatch);
            particlesBatch.end();
        }

        if(goParticleAnim.isComplete()) {
            animated = false;
        }*/
    }

    @Override
    public void resize(int width, int height) {
        scalingViewport.update(width, height);
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
        mapStage.dispose();
        atlas.dispose();

        parent.assetManager.unload("anim/card1/card1.atlas");
        parent.assetManager.unload("anim/card2/card2.atlas");
        parent.assetManager.unload("anim/card3/card3.atlas");
        parent.assetManager.unload("anim/card4/card4.atlas");
    }


    private BtnActor selActor;


    private void checkBtnClick() {
        if(selActor != null) {
            String type = selActor.getName();
            if (type.equals("back")) {
                goBack();
            }
        }
    }

    private void goBack(){
        parent.setScreen(back);
        this.dispose();
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

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);
        multiplexer.addProcessor(ip);
        Gdx.input.setInputProcessor(multiplexer);
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("camera position", scalingViewport.getCamera().position + " %%");
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) top_stage.hit(pos.x, pos.y, true);
        if(selActor == null) {
            pos = mapStage.screenToStageCoordinates(new Vector2(x, y));
            selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(selActor != null) {
            parent.tapSound();

            String[] nameAct = selActor.getName().split("_");
            if(selActor != null && nameAct[0].equals("buy")) {

                Integer index = Integer.parseInt(nameAct[1]);

                Integer check = 0;
                switch (index) {
                    case 1: check = 1000; break;
                    case 2: check = 750; break;
                    case 3: check = 500; break;
                    case 4: check = 250; break;
                }


                Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

                goParticleAnim.getEmitters().first().setPosition(pos.x, pos.y);
                goParticleAnim.reset();
                goParticleAnim.start();

                // parent.actionResolver.buyCoins(check);
                parent.inclSettings("coins", check);
                parent.buySound();

                animated = true;
            }

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
        mTimer = TIME_TO_SCROLL;
        mVelocityY = (velocityY / 2) * Gdx.graphics.getDeltaTime();

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float cam_y = scalingViewport.getCamera().position.y;

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

}
