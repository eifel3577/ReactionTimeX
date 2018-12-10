package com.argo_entertainment.reactiontime.Screens.IntroScreen;

import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Actors.StateActor;
import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.Groups.AnimatedTextGroup;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.StateGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialGameFieldScreen;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

//интро слайды с текстом про главного героя
public class IntroScreen implements Screen, GestureDetector.GestureListener {
    //зависимость Game класса
    private ReactionTimeClass parent;
    
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Stage stage;

    //текущий слайд 0
    private int activeTutorialStage = 0;
    
    private SpineAnimationActor animationActor;
    private PolygonSpriteBatch polygonSpriteBatch;
    private ActiveBackgroundStage activeBackgroundStage;

    private AnimatedTextGroup animatedTextGroup;

    private FillViewport fillViewport;
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;
    //максимальное количество слайдов 3
    private int screenCount = 3;
    private float animationDuration = 0f;
    private StateActor saleActor;
    private TextureAtlas atlas;
    private float deltaSum = 0;

    // инициализация зависимости Game и камеры, загрузка TextureAtlas из ассетов(для прорисовки полей под текстами)
    public IntroScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;

        parent.assetManager.load("intro/anim/screen_1.atlas", TextureAtlas.class);
        parent.assetManager.load("intro/anim/screen_2.atlas", TextureAtlas.class);
        parent.assetManager.load("intro/anim/screen_3.atlas", TextureAtlas.class);
        parent.assetManager.load("intro/anim/screen_4.atlas", TextureAtlas.class);
        parent.assetManager.finishLoading();
    }

    

    //вызывается при активизиции экрана.инициализация конфигупаций экрана,SpriteBatch,загрузка карты ,
    //загрузка шрифтов, инициализация SpriteBatch,настройка stage по размерам экрана,инициализация poligoneSpriteBatch
    //загрузка атласа для первого слайда,загрузка инфо для анимации для первого слайда,инициализация анимационного актора
    //получение инфы по продолжительности анимации,добавление анимационного актора в stage.установка InputMultiplexer
    //(слушатель событий инпута), устанавливает что аппаратная кнопка Назад будет работать, устанавливает что кнопка 
    //Назад будет вызывать метод goBack (будет появлятся окно с возможностью ввода имени пользователя),добавление группы акторов animatedTextGroup в stage,
    @Override
    public void show() {

        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        TiledMap tiledMap = parent.tiledMap("intro/map.tmx");
        BitmapFont fonts[] = parent.getFonts();

        batch = new SpriteBatch();

        stage = new Stage(new FillViewport(1080, 1920, camera));

        polygonSpriteBatch = new PolygonSpriteBatch();

        atlas = parent.assetManager.get("intro/anim/screen_1.atlas");
        SkeletonJson json = new SkeletonJson(atlas);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("intro/anim/screen_1.json"));

        animationActor = new SpineAnimationActor((RectangleMapObject) tiledMap.getLayers().get("anim").getObjects().get(1), skeletonData, false);

        animationDuration = skeletonData.getAnimations().first().getDuration();


        animationActor.animationState.addListener(next);

        stage.addActor(animationActor);

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

        animatedTextGroup = new AnimatedTextGroup(tiledMap.getLayers().get("menu_text"), fonts);
        stage.addActor(animatedTextGroup);

        animatedTextGroup.hideAll();
        setText();

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());
    }

    private AnimationState.AnimationStateListener next = new AnimationState.AnimationStateListener() {
        @Override
        public void start(AnimationState.TrackEntry entry) {

        }

        @Override
        public void interrupt(AnimationState.TrackEntry entry) {

        }

        @Override
        public void end(AnimationState.TrackEntry entry) {
            //setText();
        }

        @Override
        public void dispose(AnimationState.TrackEntry entry) {

        }

        @Override
        public void complete(AnimationState.TrackEntry entry) {

            if(activeTutorialStage < screenCount){
                next();
            } else goBack();
        }

        @Override
        public void event(AnimationState.TrackEntry entry, Event event) {

        }
    };

    
    //очистка экрана и установка фона,увеличение deltasum на delta,обновление акторов в stage и activeBackgroundStage,
    // установка применения fillviewPort, установка камеры под размер игрового мира,отрисовка ActiveBackDroundStage, 
    // отрисовка PoligonSpriteBatch (SpriteBatch для прорисовки космонавта),отрисовка Stage
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        deltaSum += delta;
        stage.act(delta);

        activeBackgroundStage.act(delta);

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        
        batch.begin();
            
            activeBackgroundStage.draw();

        batch.end();


        camera.update();
        polygonSpriteBatch.getProjectionMatrix().set(camera.combined);

        polygonSpriteBatch.begin();
            animationActor.draw(polygonSpriteBatch); // Draw the skeleton images.
            //animationActorShip.draw(polygonSpriteBatch); // Draw the skeleton images.
        polygonSpriteBatch.end();


        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();

            stage.draw();

        batch.end();
    }

    //при изменении экрана и запуске после вызова Pause.обновление vieport для stage
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    //вызывается перед  отображением,инициализация atlas из ассетов, прорисовка космонавта
    @Override
    public void resume() {

        atlas = parent.assetManager.get("intro/anim/screen_1.atlas");
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.

        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("intro/anim/screen_1.json"));

        animationActor.setAnimation(json, skeletonData);
        animationActor.animationState.addListener(next);

        animationDuration = skeletonData.getAnimations().first().getDuration();
    }

    //когда screen уходит с экрана освободить ресурсы
    @Override
    public void hide() {
        this.dispose();
    }

    //освободить ресурсы
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        atlas.dispose();
        System.gc();
    }


    // по касанию экрана идет звук касания, если это не последний слайд интро, то срабатывает next если последний 
    //то срабатывает goBack
    private void checkBtnClick() {
        parent.tapSound();

        if(activeTutorialStage < screenCount){
            next();
        } else goBack();
    }

    //слушатель ввода текста.проверяет корректность ввода имени игрока, записывает в префы настроек,запускает 
    //экран TutorialGameFieldScrean,если имя некорректно или не введено,пишет в префы настроек "Reaction Player"
    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            if(text.matches("[a-zA-Z0-9]*") && text.length() > 0) {
                parent.setSettings("player_name", text);
                Gdx.app.log("player_name", text);
                parent.setScreen(new TutorialGameFieldScreen(parent, camera, 1, 1, 1));
            } else {
                parent.setSettings("player_name", "Reaction Player");
                parent.setScreen(new TutorialGameFieldScreen(parent, camera, 1, 1, 1));
            }

            //удаляет атласы из ассетов
            parent.assetManager.unload("intro/anim/screen_1.atlas");
            parent.assetManager.unload("intro/anim/screen_2.atlas");
            parent.assetManager.unload("intro/anim/screen_3.atlas");
            parent.assetManager.unload("intro/anim/screen_4.atlas");
        }

        //если ввод не выполнен по умолчанию имя игрока Reaction Player
        @Override
        public void canceled () {
            parent.setSettings("player_name", "Reaction Player");
            parent.setScreen(new TutorialGameFieldScreen(parent, camera, 1, 1, 1));
        }
    }

    //играть музыку заставки, инициализирует слушатель ввода имени пользователя, появляется окно диалога для ввода 
    //имени пользователя
    private void goBack(){
        Assets.playMusic(Assets.introSound);
        parent.startMusic();

        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Enter a nick to save the game results", "", "Reaction Player");
    }


    // увеличивает счетчик слайдов, подгружает атлас для соответствующего слайда и рисует анимацию для слайда 
    private void next(){
        deltaSum = 0;
        activeTutorialStage++;

        atlas = parent.assetManager.get("intro/anim/screen_"+ (activeTutorialStage + 1) +".atlas");
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.

        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("intro/anim/screen_"+ (activeTutorialStage + 1) +".json"));

        animationActor.setAnimation(json, skeletonData);
        animationActor.animationState.addListener(next);

        animationDuration = skeletonData.getAnimations().first().getDuration();
        setText();
    }

    //загружает текст для слайда, animatedTextGroup кликабельно
    private void setText() {
        int stage = (activeTutorialStage + 1);
        String tutorialTexts = parent.getTutorialText(activeTutorialStage);
        animatedTextGroup.setVisible(stage, true, animationDuration);
        animatedTextGroup.setLabelTutorial("element_text_" + stage, tutorialTexts, Align.center);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        /*Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (StateActor) stage.hit(pos.x, pos.y, true);

        if(selActor == null) {
            Actor stage_actor = stage.hit(pos.x, pos.y, false);
            if(stage_actor == null) {

                if(activeTutorialStage < screenCount){
                    next();
                } else goBack();
            }
        }*/
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        /*if(selActor != null) {
            checkBtnClick();
        }*/
        return false;
    }

    //долгое нажатие
    @Override
    public boolean longPress(float x, float y) {
        /*if(selActor != null) {
            checkBtnClick();
        }*/
        goBack();
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
