package com.argo_entertainment.reactiontime.GameField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class TiledMapObjActor extends Actor {

    private TextureMapObject celli;
    String color_tx_str;
    int type_tx_str;
    private Color temp;

    private ShapeRenderer shapeRenderer;
    Sprite sprite;
    public Texture texture;
    TextureRegion textureRegion;
    Texture texture_collect = null;
    boolean multicolor = false;
    Rectangle bounds;

    Animation<TextureRegion> nowAnimation;
    Animation<TextureRegion> boomAnimation;
    Animation<TextureRegion> flashAnimation;
    Animation<TextureRegion> disposeAnimation;
    float stateTime = 0;

    public TiledMapObjActor(TextureMapObject celli, Texture[] textures) {
        this.celli = celli;

        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

        int width, height, posX, posY;
        Texture boomAnim, flashAnim, disposeAnim;

        width = 107;
        height = 107;
        posX = 0;
        posY = 0;

        disposeAnim = textures[7];
        boomAnim = textures[8];
        flashAnim = textures[9];

        TextureRegion[] boomTmp = TextureRegion.split(boomAnim, width, height)[0];
        TextureRegion[] flashTmp = TextureRegion.split(flashAnim, width, height)[0];
        TextureRegion[] disposeTmp = TextureRegion.split(disposeAnim, width, height)[0];

        boomAnimation = new Animation<TextureRegion>(0.027f, boomTmp);
        flashAnimation = new Animation<TextureRegion>(0.027f, flashTmp);
        disposeAnimation = new Animation<TextureRegion>(0.027f, disposeTmp);

        type_tx_str = 1;
    }

    public String getCell() {
        return this.color_tx_str;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.getTouchable() != Touchable.enabled) return null;
        if(bounds.overlaps(new Rectangle(x, y, bounds.width, bounds.height)))
        {
            return this;
        }
        return super.hit(x, y, touchable);
    }

    public void setXY(float pX, float pY) {
        // setPosition(pX, pY);
        bounds.setX((int) pX);
        bounds.setY((int) pY);
    }
    private boolean animation = false;

    public void skillAnim(String name){
        animation = true;
        if(name.equals("boom")){
            nowAnimation = boomAnimation;
        }

        if(name.equals("flash")){
            nowAnimation = flashAnimation;
        }

        if(name.equals("dispose")){
            nowAnimation = disposeAnimation;
        }
    }

    public void stopAnim(){
        animation = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(animation) {
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

            if(stateTime > 0.3f){
                animation = false;
                stateTime = 0;
            }
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        temp = batch.getColor();

        batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

        if(animation){
            // Get current frame of animation for the current stateTime
            TextureRegion currentFrame = nowAnimation.getKeyFrame(stateTime, true);

            batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        } else {
            if(!multicolor) {
                if(texture == null) {

                    batch.draw(textureRegion, getX(), getY(),
                            getOriginX(), getOriginY(), 135, 135, getScaleX(), getScaleY(), getRotation());
                } else {

                    batch.draw(new TextureRegion(texture, 0, 0, this.texture.getWidth(), this.texture.getHeight()), getX(), getY(),
                            getOriginX(), getOriginY(), 135, 135, getScaleX(), getScaleY(), getRotation());
                }
            }

            if(texture_collect != null){
                batch.draw(new TextureRegion(texture_collect, 0, 0, 135, 135), getX(), getY(), getOriginX(), getOriginY(), this.texture_collect.getWidth(), this.texture_collect.getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }
        // Get current frame of animation for the current stateTime



        batch.setColor(temp);
    }



}