package com.argo_entertainment.reactiontime.Actors;

import com.argo_entertainment.reactiontime.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

//анимационный актер
public class SpineAnimationActor extends Actor {
    //управление анимацией.Применяет анимации с течением времени, ставит в очередь анимации для последующего воспроизведения.
    public AnimationState animationState;
    //каркас анимационного обьекта
    public Skeleton skeleton;
    public SkeletonRenderer skeletonRenderer;
    public SkeletonRendererDebug debug;


    private Boolean animation = true;
    private Boolean loop = true;
    public Boolean stateAnim = false;

    SkeletonJson json = null;
    SkeletonData data = null;

    private Boolean flip = false;

    //принимает RectangleMapObject, json и данные
    public SpineAnimationActor(RectangleMapObject cell, SkeletonJson json, SkeletonData data) {
        this.json = json;
        this.data = data;
        //отключает для этого актера кликабельность
        setTouchable(Touchable.disabled);

        Float x = cell.getRectangle().getX(), y = cell.getRectangle().getY() + data.getHeight();

        //устанавливает координаты актера,имя и тд
        setWidth(cell.getRectangle().getWidth());
        setHeight(cell.getRectangle().getHeight());
        setPosition(cell.getRectangle().getX(), cell.getRectangle().getY());
        setName(cell.getName());

        setBounds(cell.getRectangle().getX(), cell.getRectangle().getY(), cell.getRectangle().getWidth(),
                cell.getRectangle().getHeight());

        //получает свойство flip если оно есть
        if(cell.getProperties().get("flip") != null)
            flip = cell.getProperties().get("flip", Boolean.class);

        skeletonRenderer = new SkeletonRenderer();
        //skeletonRenderer.setPremultipliedAlpha(true);

        debug = new SkeletonRendererDebug();
        debug.setBoundingBoxes(false);
        debug.setRegionAttachments(false);

        loadAnimation();
    }

    public SpineAnimationActor(RectangleMapObject cell, SkeletonData data) {
        this.data = data;
        setTouchable(Touchable.disabled);


        setWidth(data.getWidth());
        setHeight(data.getHeight());
        setName(cell.getName());

        if(cell.getProperties().get("flip") != null)
            flip = cell.getProperties().get("flip", Boolean.class);

        setBounds(cell.getRectangle().getX(), cell.getRectangle().getY(), cell.getRectangle().getWidth(),
                cell.getRectangle().getHeight());


        Float x = getX() + (getWidth() / 2), y = getY() + (getHeight() / 2);

        setPosition(x, y);

        skeletonRenderer = new SkeletonRenderer();


        debug = new SkeletonRendererDebug();
        debug.setBoundingBoxes(false);
        debug.setRegionAttachments(false);

        loadAnimation();
    }

    public SpineAnimationActor(RectangleMapObject cell, SkeletonData data, Boolean inLoop) {
        this.data = data;
        this.loop = inLoop;

        setTouchable(Touchable.disabled);

        setWidth(data.getWidth());
        setHeight(data.getHeight());
        setName(cell.getName());

        if(cell.getProperties().get("flip") != null)
            flip = cell.getProperties().get("flip", Boolean.class);

        setBounds(cell.getRectangle().getX(), cell.getRectangle().getY(), cell.getRectangle().getWidth(),
                cell.getRectangle().getHeight());


        Float x = getX() + (getWidth() / 2), y = getY() + (getHeight() / 2);

        setPosition(x, y);

        skeletonRenderer = new SkeletonRenderer();
;

        debug = new SkeletonRendererDebug();
        debug.setBoundingBoxes(false);
        debug.setRegionAttachments(false);

        loadAnimation();
    }

    public void setAnimation(SkeletonJson json, SkeletonData data){
        this.json = json;
        this.data = data;

        loadAnimation();
    }

    public void setAnimationState(boolean state) {
        animation = state;
    }
    public void setAnimationLoop(boolean state) {
        loop = state;
    }

    private void loadAnimation() {
        skeleton = new Skeleton(data);
        if(flip)
            skeleton.setFlipX(true);

        AnimationStateData stateData = new AnimationStateData(data);
        animationState = new AnimationState(stateData);

        animationState.setAnimation(0, "active", loop); // Run after the jump.
    }

    public void setPositionAnim(float x, float y) {
        Float xR = x + (getWidth() / 2), yR = y + (getHeight() / 2);
        setPosition(xR, yR);
    }

    public void draw(PolygonSpriteBatch batch) {
        //batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if(isVisible())
            skeletonRenderer.draw(batch, skeleton);
    }

    @Override
    public void act(float delta) {
        skeleton.setPosition(getX(), getY());


        if(animation)
            animationState.update(Gdx.graphics.getDeltaTime());

        animationState.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
        skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.


        super.act(delta);
    }
}