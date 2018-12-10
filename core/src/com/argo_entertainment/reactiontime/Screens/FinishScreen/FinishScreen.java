package com.argo_entertainment.reactiontime.Screens.FinishScreen;

import com.argo_entertainment.reactiontime.Actors.BtnActor;
import com.argo_entertainment.reactiontime.Groups.ButtonGroup;
import com.argo_entertainment.reactiontime.Groups.TextGroup;
import com.argo_entertainment.reactiontime.ReactionTimeClass;
import com.argo_entertainment.reactiontime.Screens.ShopScreen.ShopScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.FillViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class FinishScreen implements Screen, GestureDetector.GestureListener {
    private ReactionTimeClass parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Screen back;

    private SpriteBatch batch;
    private Stage stage;
    private ButtonGroup btnGroup;
    private TextGroup textGroup;

    public FinishScreen(ReactionTimeClass game, OrthographicCamera game_camera, Screen back){
        // back.dispose();
        parent = game;
        camera = game_camera;
        this.back = back;
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {
        TiledMap tiledMap = parent.tiledMap("game_finish/map.tmx");
        BitmapFont[] font = parent.getFonts();

        batch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

        stage = new Stage(new FillViewport(1080, 1920, camera));

        btnGroup = new ButtonGroup();
        for (MapObject object : tiledMap.getLayers().get("btn").getObjects()) {
            if (object instanceof TextureMapObject) {
                TextureMapObject cell = (TextureMapObject) object;

                BtnActor actor = new BtnActor(cell);

                actor.setName(cell.getName());

                actor.setTouchable(Touchable.enabled);
                actor.setOrigin(cell.getTextureRegion().getRegionWidth() / 2, cell.getTextureRegion().getRegionHeight() / 2);
                actor.setBounds(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(),
                        cell.getTextureRegion().getRegionHeight());
                actor.setClick(parent.getSettings(cell.getName()));

                btnGroup.addActor(actor);
            } else if (object instanceof RectangleMapObject) {
                RectangleMapObject cell = (RectangleMapObject) object;

                BtnActor actor = new BtnActor(cell);

                actor.setName(cell.getName());

                actor.setTouchable(Touchable.enabled);
                actor.setOrigin(cell.getRectangle().width / 2, cell.getRectangle().height / 2);
                actor.setBounds(cell.getRectangle().x, cell.getRectangle().y, cell.getRectangle().width,
                        cell.getRectangle().height);

                btnGroup.addActor(actor);
            }
        }
        stage.addActor(btnGroup);


        textGroup = new TextGroup(tiledMap.getLayers().get("txt"), font);
        stage.addActor(textGroup);
        textGroup.setLabel("coins", "" + parent.getNumbers("coins"));

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

        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.end();
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

    private BtnActor selActor;

    private void checkBtnClick() {
        if(selActor != null) {
            parent.tapSound();

            String type = selActor.getName();
            if (type.equals("back")) {
                goBack();
            }

            if (type.equals("market"))
                parent.setScreen(new ShopScreen(parent, camera, this));
        }
    }



    private void goBack(){
        parent.setScreen(back);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = stage.screenToStageCoordinates(new Vector2(x, y));
        selActor = (BtnActor) btnGroup.hit(pos.x, pos.y, true);

        if(selActor != null) {
            selActor.setClick(!selActor.isClick());
            parent.setSettings(selActor.getName(), selActor.isClick());
        }

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(selActor != null) {
            if(selActor.type.equals("btn"))
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
            if(selActor.type.equals("btn"))
                checkBtnClick();
        }
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
