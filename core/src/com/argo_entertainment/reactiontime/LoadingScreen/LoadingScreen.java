package com.argo_entertainment.reactiontime.LoadingScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Actors.SpineAnimationActor;
import com.argo_entertainment.reactiontime.Assets;
import com.argo_entertainment.reactiontime.Groups.BackgroundGroup;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.MainMenu.MainMenuScreen;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.IntroScreen.IntroScreen;
import com.argo_entertainment.reactiontime.Screens.TutorialScreen.TutorialScreen;
import com.argo_entertainment.reactiontime.Stages.ActiveBackgroundStage;
import com.argo_entertainment.reactiontime.Stages.SpineAnimationsStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

//загрузочный экран, загружаются все ресурсы
public class LoadingScreen implements Screen {

    //ссылка на класс реализующий Game
    private ReactionTimeClass parent;
    //карта
    private TiledMap tiledMap;
    //камера
    private OrthographicCamera camera;
    //контейнер SpriteBatch для отрисовки
    private SpriteBatch batch;
    //временная метка
    private long startTime;
    //флаг загрузки по умолчанию отключен
    private boolean load = false;
    //массив текстур на 20 текстур
    private Texture[] textures = new Texture[20];
    //Stage это 2d сцена,содержащая иерархии акторов
    private Stage stage;
    //нижняя и верхняя сцена
    private Stage bottomStage, topStage;
    //группа акторов ButtonGroup
    ButtonGroup btnGroup;
    //для прорисоки фигуры космонавта
    Skeleton skeleton;
    //анимированная сцена
    AnimationState state;
    //анимированная задняя сцена
    private ActiveBackgroundStage activeBackgroundStage;
    private SpineAnimationsStage spineAnimationsStage;

    //сохраняет пропорции масштабируя мир до всего экрана (некоторые элементы могут быть не видны)
    private FillViewport fillViewport;
    //масштабирует мир до размеров экрана используя черные полосы
    private FitViewport fitViewport, fitViewportBottom;
    //видовой экран 
    private ScalingViewport fitViewportTop;
    //константы ширины и высоты
    private float VIRTUAL_WIDTH = 1080, VIRTUAL_HEIGHT = 1920;
    //прогресс
    float progress = 0;

    // инициирует зависимость Game и камеру
    public LoadingScreen(ReactionTimeClass game, OrthographicCamera game_camera){
        parent = game;
        camera = game_camera;
    }

    //метод вызывается когда скрин становится текущим.Устанавливает высоту.Настраивает отображение.инициирует карту.
    //инициирует сцены,инициирует разные группы акторов,доставая по ним инфу из слоев карты.Достает из карты обьекты для
    //анимированного слоя, настраивает отображение фигуры в анимированном слое.Запускает включение музыки 
    //и звуков в зависимости от того была ли она включена в настройках.ставит временную метку на текущее время.Заполняет 
    //SpriteBatch текстурами для отрисовки, заполняет текстурами массивы textures elements special gameElements
    //bigGameElements backObj в Game-классе, загружает  tmx  элементы в ассеты в TiledMap класс,загружает BitMapFont(обьект для
    //отображения текста) в BitMapFont класс, загружает в  ActiveBackGroundStage массив с backObj
    @Override
    public void show() {
        
        int height = 1920;
        if(Gdx.graphics.getHeight() > 1920) height = Gdx.graphics.getHeight();
        
        fitViewportTop = new ScalingViewport(Scaling.fillX, VIRTUAL_WIDTH, height);


        fitViewportBottom = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        fillViewport = new FillViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        
        tiledMap = new TmxMapLoader().load("loading/map.tmx");


        stage = new Stage(fitViewport);
        bottomStage = new Stage(fitViewportBottom);
        topStage = new Stage(fitViewportTop);
        
        BackgroundGroup backgroundGroup = new BackgroundGroup(tiledMap.getLayers().get("top_back"));
        topStage.addActor(backgroundGroup);

        backgroundGroup = new BackgroundGroup(tiledMap.getLayers().get("bottom_back"));
        bottomStage.addActor(backgroundGroup);

        btnGroup = new ButtonGroup(tiledMap.getLayers().get("btn"));
        bottomStage.addActor(btnGroup);

        spineAnimationsStage = new SpineAnimationsStage(fitViewportBottom);

        MapObjects objects = tiledMap.getLayers().get("anim").getObjects();

        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".atlas"));
                SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
                //json.setScale(2.3f);

                SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/"+cell.getName()+"/"+cell.getName()+".json"));
                SpineAnimationActor actor = new SpineAnimationActor(cell, skeletonData);
                actor.animationState.setAnimation(0, "active", false);


                spineAnimationsStage.addActor(actor);
            }
        }

        //TODO найти реализацию в классе ReactionTimeClass
        parent.actionResolver.silentLoginGPGS();
        Assets.load();

        if(parent.getSettings("music"))
            parent.startMusic();
        else
            parent.stopMusic();

        if(parent.getSettings("sound"))
            parent.startSound();
        else
            parent.stopSound();

        startTime = TimeUtils.millis();
        batch = new SpriteBatch();

        textures[7] = new Texture("game/dispose.png");
        textures[8] = new Texture("game/8.png");
        textures[9] = new Texture("game/flash_anim.png");
        textures[10] = new Texture("game/elements/skill_boom.png");
        textures[14] = new Texture("game/elements/disabled_1.png");
        textures[15] = new Texture("game/elements/disabled_2.png");
        textures[16] = new Texture("game/elements/disabled_3.png");

        textures[17] = new Texture("game/game_elements/game_element_1_field.png");
        parent.setTextures(textures);


        Texture[][] elements = new Texture[6][6];
        for(int i=1; i <= 5; i++) {
            for (int j = 0; j <= 4; j++) {
                elements[i][j] = new Texture("game/elements/" + i + "_" + j + ".png");
            }
        }
        parent.setElements(elements);

        Texture[][] special = new Texture[5][5];
        for(int i=1; i <= 3; i++) {
            for (int j = 1; j <= 4; j++) {
                special[i][j] = new Texture("game/special/special_" + i + "_" + j + ".png");
            }
        }
        special[4][1] = new Texture("game/elements/black_hole.png");
        parent.setSpecial(special);

        Texture elementsSprite = new Texture("game/game_elements/game_element.png");
        parent.setGameElements(elementsSprite);

        elementsSprite = new Texture("game/game_elements/game_big_element.png");
        parent.setBigGameElements(elementsSprite);

        //TODO test 1712
        //если в префе ранее не были сохранены значения,то по дефолту 5
        if(parent.getEnergyLevel()<5&&parent.getEnergyLevel()>=0) {
            parent.setEnergyLevel(5);
        }


        Texture[] backObj = new Texture[12];
        backObj[0] = new Texture("active_background/back.jpg");

        backObj[1] = new Texture("active_background/rock.png");
        backObj[2] = new Texture("active_background/rock_1.png");
        backObj[3] = new Texture("active_background/rock_2.png");
        backObj[4] = new Texture("active_background/rock_3.png");
        backObj[5] = new Texture("active_background/rock_4.png");
        backObj[6] = new Texture("active_background/rock_5.png");
        backObj[7] = new Texture("active_background/rock_6.png");
        backObj[8] = new Texture("active_background/rock_7.png");

        backObj[9] = new Texture("active_background/star_1.png");
        backObj[10] = new Texture("active_background/star_2.png");
        backObj[11] = new Texture("active_background/star_3.png");
        parent.setBackObj(backObj);


        parent.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        parent.assetManager.load("game_field.tmx", TiledMap.class);
        parent.assetManager.load("test2.tmx", TiledMap.class);

        parent.assetManager.load("shops/shops_map.tmx", TiledMap.class);
        parent.assetManager.load("menu/menu_map.tmx", TiledMap.class);

        parent.assetManager.load("pause/map.tmx", TiledMap.class);
        parent.assetManager.load("finish/finish_map.tmx", TiledMap.class);

        parent.assetManager.load("intro/map.tmx", TiledMap.class);
        parent.assetManager.load("tutorial/map.tmx", TiledMap.class);

        parent.assetManager.load("2_solar_map/map.tmx", TiledMap.class);

        parent.assetManager.load("level_done/map.tmx", TiledMap.class);
        parent.assetManager.load("level_select/map.tmx", TiledMap.class);
        parent.assetManager.load("game_over/map.tmx", TiledMap.class);
        parent.assetManager.load("game_finish/map.tmx", TiledMap.class);


        /*for (int j = 0; j <= 10; j++) {
            parent.assetManager.load("solar_map/3d_models2/"+j+".g3db", Model.class);
        }*/

        /*for (int j = 1; j <= 9; j++) {
            parent.assetManager.load("level_select/3d_planets/"+j+".g3db", Model.class);
        }*/

        parent.assetManager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        parent.assetManager.load("bitmapfont/Fredoka_One_24.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_36.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_48.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_72.fnt", BitmapFont.class);

        parent.assetManager.load("bitmapfont/Fredoka_One_gold_48.fnt", BitmapFont.class);

        parent.assetManager.load("bitmapfont/Fredoka_One_green_64.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_yellow_64.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_pink_64.fnt", BitmapFont.class);
        parent.assetManager.load("bitmapfont/Fredoka_One_white_64.fnt", BitmapFont.class);

        parent.assetManager.load("sounds/Thunder1.mp3", Sound.class);
        parent.assetManager.load("sounds/Thunder2.mp3", Sound.class);


        parent.assetManager.load("anim/gift_menu/gift_menu.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/100_continue/100_continue.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/1_reaction_coin/1_reaction_coin.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/2_reaction_coin/2_reaction_coin.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/4_reaction_coin/4_reaction_coin.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/button_finish/button_finish.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/button_next/button_next.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/button_start/button_start.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/done_planet/done_planet.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/Finish/Finish.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/blogger_congrat/blogger_congrat.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/game_over/game_over.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/Skill_bomb/Skill_bomb.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/Skill_flash/Skill_flash.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/skill_ice/skill_ice.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/system_done/system_done.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/watch_video/watch_video.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/station_1/station_1.atlas", TextureAtlas.class);
        parent.assetManager.load("anim/done_planet_button/done_planet_button.atlas", TextureAtlas.class);

        activeBackgroundStage = new ActiveBackgroundStage(fillViewport, parent.getBackObj());
    }

   
    //метод прорисовки LoadingScreen.очищает и делает заливку фона,получает значение прогресса из ассетов,устанавливает 
    //отображение актора BtnActor в зависимости от прогресса, в зависимости от того было ли введено имя игрока или нет 
    //отображает либо IntroScreen  или MainMenuScreen.Затем вызывает метод act у каждой сцены после чего метод act 
    //будет вызван у каждого актора каждой сцены.Метод  act прорисовывает актора каждую единицу времени.Затем настраивается 
    //применение матрицы к камере (для синхронизации касаний) и идет прорисовка SpriteBatch  для всех участков экрана
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        progress = parent.assetManager.getProgress();

        int prc = (int) (progress * 10);
        if (prc % 2 == 0) {
            int step = (prc / 2);
            BtnActor actor = (BtnActor) btnGroup.getChildren().get(0);
            actor.cell.getTextureRegion().setRegion(0, (int) actor.getHeight() * step, (int) actor.getWidth(), (int) actor.getHeight());
        }

        if(parent.assetManager.update()) {
            if(parent.getPlayerName().length() <= 0 || parent.getPlayerName().equals("player_name")) {
                parent.stopMusic();
                Assets.playMusic(Assets.introSound);
                parent.setScreen(new IntroScreen(parent, camera, this));
            } else
                parent.setScreen(new MainMenuScreen(parent, camera, null));
        }


        camera.update();

        stage.act(delta);
        topStage.act(delta);
        bottomStage.act(delta);

        spineAnimationsStage.act(delta);
        activeBackgroundStage.act(delta);

        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);

        batch.begin();
            activeBackgroundStage.draw();
        batch.end();


        fitViewportBottom.apply();
        batch.setProjectionMatrix(fitViewportBottom.getCamera().combined);

        batch.begin();

            bottomStage.draw();
            spineAnimationsStage.draw(fitViewportBottom.getCamera().combined);

        batch.end();


        fitViewportTop.apply();
        batch.setProjectionMatrix(fitViewportTop.getCamera().combined);

        batch.begin();

            topStage.draw();

        batch.end();

        Gdx.app.log("progress progress", progress + " %%");
    }

    
    private void load(){
        load = true;
    }

    //вызывается при смене конфигурации экрана,обновляет размеры,настраивает отступы внизу и вверху экрана
    //выводит инфу в лог
    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        fitViewportTop.update(width, height);
        fitViewportBottom.update(width, height);
        fillViewport.update(width, height, true);

        int topOffset = fitViewport.getTopGutterHeight() - (int) parent.getIOSSafeAreaInsets().x;
        topStage.getViewport().setScreenPosition(fitViewportTop.getScreenX(),
                topOffset);

        int bottomOffset = fitViewport.getTopGutterHeight() + (int) parent.getIOSSafeAreaInsets().y;
        bottomStage.getViewport().setScreenPosition(fitViewportBottom.getScreenX(),
                fitViewportBottom.getScreenY() - bottomOffset);


        Gdx.app.log("getTopGutterHeight", "" + fitViewport.getTopGutterHeight());
        Gdx.app.log("getIOSSafeAreaInsets", parent.getIOSSafeAreaInsets().toString());
        Gdx.app.log("offset", topOffset + "");
        Gdx.app.log("fitViewportTop getScreenY", fitViewportTop.getScreenY() + "");
        Gdx.app.log("WIDTH", Gdx.graphics.getWidth() + "");
        Gdx.app.log("HEIGHT", Gdx.graphics.getHeight() + "");
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    // вызывается когда экран теряет фокус
    @Override
    public void hide() {

    }

    // вызывается при уничтожении приложения
    @Override
    public void dispose() {
        batch.dispose();
        activeBackgroundStage.dispose();
        tiledMap.dispose();
    }
}
