package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

//Актор основная сущность в 2d. Актер имеет положение, прямоугольный размер, начало, масштаб, поворот, индекс Z и цвет.
//Позиция начинается с нижнего левого края.Прорисоывается в единицу времени в методе act(). К актору применяются текущие действия
//в методе fire()
public class AnimationActor extends Actor {

    //прямоугольный обьект карты
    private RectangleMapObject cell_rect;
    //обьект карты содержащий текстуру
    public TextureMapObject cell = null;
    //тип
    public String type = "btn";
    //Animation  хранит список объектов, представляющих анимированную последовательность, например. для бега или прыжка.
    Animation<TextureRegion> boomAnimation;

    //2D-прямоугольник, определяемый его угловой точкой в ​​левом нижнем углу, и его экстенты в x (ширина) и y (высота).
    private Rectangle bounds;

    float stateTime = 0;
    //флаг анимации включен
    private boolean animation = true;
    //ширина и высота
    int width , height;

    //принимает обьект карты содержащий текстуру,инициирует его,устанавливает его размеры,имя,кликабельность,получает список текстур и отдает их в список анимации
    //устанавливает stateTime
    public AnimationActor(TextureMapObject cell) {
        this.cell = cell;
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());

        setName(cell.getName());
        setTouchable(Touchable.disabled);
        setOrigin(cell.getOriginX(), cell.getOriginY());
        setPosition(cell.getX(), cell.getY());
        setWidth(cell.getTextureRegion().getRegionWidth());
        setHeight(cell.getTextureRegion().getRegionHeight());
        setScale(cell.getScaleX(), cell.getScaleY());

        width = cell.getTextureRegion().getRegionWidth();
        height = cell.getTextureRegion().getRegionHeight();

        TextureRegion[] textureRegion = TextureRegion.split(cell.getTextureRegion().getTexture(), width, height)[0];

        boomAnimation = new Animation<TextureRegion>(0.10f, textureRegion);

        stateTime = MathUtils.random(0,10f);
    }

    //возвращает TextureMapObject
    public TextureMapObject getCell() {
        return this.cell;
    }


    //сохраняет прошедшее время анимации
    @Override
    public void act(float delta) {
        super.act(delta);
        if(animation) {
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        }
    }

    //изменение позиции
    @Override
    protected void positionChanged() {
        bounds.setX((int)getX());
        bounds.setY((int)getY());
        super.positionChanged();
    }


    //отрисовка анимации
    public void draw(Batch batch, float parentAlpha) {
        if(cell != null) {
            Color temp;
            temp = batch.getColor();

            batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));
            if(animation){
                // Get current frame of animation for the current stateTime
                TextureRegion currentFrame = boomAnimation.getKeyFrame(stateTime, true);

                batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            }

            batch.setColor(temp);
        }
    }


}