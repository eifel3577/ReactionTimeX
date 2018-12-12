package com.argo_entertainment.reactiontime.MainMenu;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.OtherActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.BuySkillScreen.BuySkillScreen;
import com.argo_entertainment.reactiontime.Screens.IntroScreen.IntroScreen;
import com.argo_entertainment.reactiontime.Screens.PurchasesScreen.PurchasesScreen;
import com.argo_entertainment.reactiontime.Screens.RatingScreen.RatingScreen;
import com.argo_entertainment.reactiontime.Screens.SettingsMenu.SettingsMenuScreen;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.argo_entertainment.reactiontime.Screens.ShopsScreen.ShopsScreen;
import com.argo_entertainment.reactiontime.Screens.SolarMap2D.SolarMap2D;
import com.argo_entertainment.reactiontime.Screens.SolarMapScreen.SolarMapScreen;
import com.argo_entertainment.reactiontime.Screens.StatsScreen.StatsScreen;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialGameFieldScreen;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

// скрин меню
public class MainMenuScreen implements Screen, GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    //рендерит карты
    private OrthogonalTiledMapRenderer tiledMapRenderer;


    private SpriteBatch batch;
    private Stage stage;
    private Stage backStage;
    private Stage shareStage;

    //кнопка поделиться
    private ButtonGroup BtnshareGroup;
    private ButtonGroup btnGroup;
    private ButtonGroup otherGroup;
    private TextGroup textGroup;
    private BackgroundGroup modalBackGroup;
    private BackgroundGroup shareGroup;
    private BackgroundGroup quitGroup;


    private BtnActor selActor = null;



    PolygonSpriteBatch polygonSpriteBatch;
    private SpineAnimationActor animationActor;
    TextureAtlas atlas;

    private ActiveBackgroundStage activeBackgroundStage;

    private FillViewport fillViewport;
    private FitViewport extendViewport;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;

    public MainMenuScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
    }

    //установка ViewPort по размерам,рагрузка карты,загрузка шрифтов,загрузка рендерера для карты,инициализация 
    //всех stage,создание груп и передача им слоев карты,настройка анимации,добавление акторов в stage,установка 
    //обработчика касаний, установка текста,включение/отключение музыки, настройка анимированного актора
    @Override
    public void show() {

        extendViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        TiledMap tiledMap = parent.tiledMap("menu/menu_map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());

        stage = new Stage(extendViewport);

        shareStage = new Stage(extendViewport);

        backStage = new Stage(fillViewport);


        //кнопки
        BackgroundGroup backgroundGroup = new BackgroundGroup(tiledMap.getLayers().get("top_back"));

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        otherGroup = new ButtonGroup(tiledMap.getLayers().get("other"));
        //тексты очки и коины
        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), fonts);

        //общий фиолетовый цвет отключен и некликабелен
        modalBackGroup = new BackgroundGroup(tiledMap.getLayers().get("modalBack"));
        modalBackGroup.setVisible(false);
        modalBackGroup.setTouchable(Touchable.disabled);
        //всплывающее окно "Поделиться с друзьями на фб и твиттере отключено и некликабельно
        shareGroup = new BackgroundGroup(tiledMap.getLayers().get("share"));
        shareGroup.setVisible(false);
        shareGroup.setTouchable(Touchable.disabled);

        //окно выхода отключено и некликабельно
        quitGroup = new BackgroundGroup(tiledMap.getLayers().get("quit"));
        quitGroup.setVisible(false);
        quitGroup.setTouchable(Touchable.disabled);

        //анимация большой голубой планеты в левом верзхнем углу и серая скала внизу
        for(MapObject object : tiledMap.getLayers().get("anim").getObjects()) {
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;
                OtherActor actor = new OtherActor(cell);
                if(cell.getName().equals("planet")) {
                    //планета крутится вокруг себя
                    actor.addAction(repeat(RepeatAction.FOREVER,
                            rotateBy(360, 90f)
                            //rotateBy(360, 40f)
                    ));
                }
                if(cell.getName().equals("bottom_rock")) {
                    //скала движется влево и вправо
                    actor.addAction(repeat(RepeatAction.FOREVER, sequence(
                            moveBy(100f, -10, 14f),
                            moveBy(-100f, 10, 18f)
                    )));
                }
                activeBackgroundStage.addActor(actor);
            }
        }


        stage.addActor(backgroundGroup);

        stage.addActor(otherGroup);

        stage.addActor(btnGroup);
        stage.addActor(textGroup);

        backStage.addActor(modalBackGroup);

        shareStage.addActor(shareGroup);
        shareStage.addActor(quitGroup);

        InputMultiplexer multiplexer;
        GestureDetector gd = new GestureDetector(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gd);

        Gdx.input.setInputProcessor(multiplexer);

        textGroup.setLabel("score", "" + parent.getNumbers("high_score"));
        textGroup.setLabel("coins", parent.getNumbers("coins").toString());

        if(parent.getSettings("music"))
            parent.startMusic();
        else
            parent.stopMusic();

        /*stage.setDebugAll(true);
        backStage.setDebugAll(true);*/

        Gdx.input.setCatchBackKey(true);

        polygonSpriteBatch = new PolygonSpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("menu/anim/Play_button.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(2.3f);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("menu/anim/Play_button.json"));

        animationActor = new SpineAnimationActor((RectangleMapObject) tiledMap.getLayers().get("spine").getObjects().get(0), json, skeletonData);

        stage.addActor(animationActor);

    }


    //обновить и отрисовать
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        polygonSpriteBatch.getProjectionMatrix().set(extendViewport.getCamera().combined);
        stage.act(delta);
        backStage.act(delta);
        shareStage.act(delta);
        activeBackgroundStage.act(delta);



        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            activeBackgroundStage.draw();
        batch.end();

        extendViewport.apply();
        batch.setProjectionMatrix(extendViewport.getCamera().combined);
        batch.begin();

            polygonSpriteBatch.begin();
                animationActor.draw(polygonSpriteBatch); // Draw the skeleton images.
            polygonSpriteBatch.end();

            stage.draw();

        batch.end();

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
            backStage.draw();
        batch.end();

        extendViewport.apply();
        batch.setProjectionMatrix(extendViewport.getCamera().combined);
        batch.begin();

            shareStage.draw();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        fillViewport.update(width, height);
        extendViewport.update(width, height);

        stage.getViewport().setScreenPosition(extendViewport.getScreenX(), extendViewport.getScreenY());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        if(selActor != null) {
            selActor.setClick(false);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        tiledMapRenderer.dispose();
    }

    
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        if(btnGroup.isTouchable()){
            selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);
        } else if (shareGroup.isTouchable()) {
            selActor = (BtnActor) shareGroup.hit(pos.x, pos.y, true);
        } else {
            selActor = (BtnActor) quitGroup.hit(pos.x, pos.y, true);
        }

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
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(selActor != null) {
            selActor.setClick(false);
            checkBtnClick();
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        if(selActor != null) {
            selActor.setClick(false);
        }

        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor != null) {
            selActor.setClick(true);
            checkBtnClick();

        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));

        if(selActor != null) {
            selActor.setClick(false);
        }

        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor != null) {
            selActor.setClick(true);
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

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            if (type.equals("achiv"))
                parent.actionResolver.silentLoginGPGS();

            if (type.equals("share")) {
                modalBackGroup.setVisible(!modalBackGroup.isVisible());
                shareGroup.setVisible(!shareGroup.isVisible());
                if(shareGroup.isVisible()) {
                    shareGroup.setTouchable(Touchable.enabled);
                    btnGroup.setTouchable(Touchable.disabled);
                }
                else {
                    shareGroup.setTouchable(Touchable.disabled);
                    btnGroup.setTouchable(Touchable.enabled);
                }
            }

            if (type.equals("quit")) {
                modalBackGroup.setVisible(!modalBackGroup.isVisible());
                quitGroup.setVisible(!quitGroup.isVisible());
                if(quitGroup.isVisible()) {
                    quitGroup.setTouchable(Touchable.enabled);
                    btnGroup.setTouchable(Touchable.disabled);
                }
                else {
                    quitGroup.setTouchable(Touchable.disabled);
                    btnGroup.setTouchable(Touchable.enabled);
                }
            }

            if (type.equals("cancel")) {
                modalBackGroup.setVisible(false);
                quitGroup.setVisible(false);
                quitGroup.setTouchable(Touchable.disabled);
                btnGroup.setTouchable(Touchable.enabled);
            }
            if (type.equals("ok")) {
                Gdx.app.exit();
            }

            if (type.equals("play"))
                parent.setScreen(new SolarMap2D(parent, camera, this, parent.nowPlayPlanet()));
            //настройки
            else if (type.equals("settings"))
                parent.setScreen(new SettingsMenuScreen(parent, camera, this));
            //маркет
            else if (type.equals("market"))
                parent.setScreen(new ShopScreen(parent, camera, this));

            else if (type.equals("personal"))
                parent.setScreen(new StatsScreen(parent, camera, this));

            else if (type.equals("initScore"))
                parent.setScreen(new StatsScreen(parent, camera, this));
            //рекорды
            else if (type.equals("playScrore"))
                parent.setScreen(new RatingScreen(parent, camera, this));
            else if (type.equals("skill"))
                parent.setScreen(new ShopsScreen(parent, camera, this));
            else if (type.equals("achiv")) {
                parent.stopMusic();
                Assets.playMusic(Assets.introSound);
                parent.setScreen(new IntroScreen(parent, camera, null));
            }
            else if (type.equals("shop") || type.equals("shop_bg"))
                parent.setScreen(new PurchasesScreen(parent, camera, this));
        }
    }
}
