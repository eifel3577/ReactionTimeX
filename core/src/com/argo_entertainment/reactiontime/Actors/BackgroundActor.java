package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

//Актор основная сущность в 2d. Актер имеет положение, прямоугольный размер, начало, масштаб, поворот, индекс Z и цвет.
//Позиция начинается с нижнего левого края.Прорисоывается в единицу времени в методе act(). К актору применяются текущие действия
//в методе fire()
public class BackgroundActor extends Actor {

    //прямоугольный обьект карты
    private RectangleMapObject cell_rect;
    //обьект карты содержащий текстуру
    public TextureMapObject cell = null;
    //тип
    public String type = "btn";

    public boolean click = false;

    private int oldPosX = 0;
    private int oldPosY = 0;
    //2D-прямоугольник, определяемый его угловой точкой в ​​левом нижнем углу, и его экстенты в x (ширина) и y (высота).
    private Rectangle bounds;

    //принимает обьект карты содержащий текстуру,инициирует его,устанавливает его размеры,инициирует координаты
    public BackgroundActor(TextureMapObject cell) {
        this.cell = cell;
        this.bounds=new Rectangle(cell.getX(), cell.getY(), cell.getTextureRegion().getRegionWidth(), cell.getTextureRegion().getRegionHeight());
        oldPosX = cell.getTextureRegion().getRegionWidth();
        oldPosY = cell.getTextureRegion().getRegionY();
    }

    //возвращает TextureMapObject
    public TextureMapObject getCell() {
        return this.cell;
    }


    @Override
    protected void positionChanged() {
        bounds.setX((int)getX());
        bounds.setY((int)getY());
        super.positionChanged();
    }

    //отрисовка
    public void draw(Batch batch, float parentAlpha) {
        if(cell != null) {
            Color temp;
            temp = batch.getColor();

            batch.setColor(new Color(temp.r, temp.g, temp.b, getColor().a));

            batch.draw(cell.getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            batch.setColor(temp);
        }
    }


}