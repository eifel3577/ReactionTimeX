package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

//Актор основная сущность в 2d. Актер имеет положение, прямоугольный размер, начало, масштаб, поворот, индекс Z и цвет.
//Позиция начинается с нижнего левого края.Прорисоывается в единицу времени в методе act(). К актору применяются текущие действия
//в методе fire()
public class ActiveBackgroundActor extends Actor {

    //изображение полученное из его оригинального формата(например PNG) и загруженное в GPU
    public Texture texture = null;
    // определяет прямоуголник внутри текстуры и используется для рисования только части текстуры
    private TextureRegion textureRegion;
    //cкорость
    private Float speed = 20f;
    //тип
    public String type = "btn";

    private int elementId;
    //2d вектор для конвертации координат нажатия из аппаратной области экрана (пиксели) в программную
    //это вектор измерения
    private Vector2 dimension;
    //вектор позиции
    private Vector2 position;
    //вектор ускорения
    private Vector2 velocity;
    //масштаб
    private float scale;
    //вращение
    private float rotation;
    //прозрачность
    private float alpha = 0f;
    //флаг есть ли прозрачность
    private Boolean isAlpha = false;
    //ширина и высота
    public float width = 1080 , height = 1920;

    //конструктор принимает текстуру,инициирует текстуру,заключает текстуру в прямоугольник TextureRegion,
    //устанавливает размеры TextureRegion по размеру текстуры
    public ActiveBackgroundActor(Texture texture) {
        this.texture = texture;
        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0, texture.getWidth(), texture.getHeight());
        initAlpha();
    }

    //конструктор принимает текстуру, интовый ид и float скорость.Инициирует elementId
    // инициирует текстуру,заключает текстуру в прямоугольник TextureRegion,
    //устанавливает размеры TextureRegion,устанавливает скорость как случайную между 5f и переданной в параметре
    public ActiveBackgroundActor(Texture texture, Integer id, Float speed) {
        elementId = id;
        this.texture = texture;
        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0, texture.getWidth(), texture.getHeight());
        this.speed = MathUtils.random(5f, speed);
        initMove();
    }

    //устанавливает вектор измерения с шириной 1080 , высотой 1920
    //если elementId четный
    //TODO определение позиции по координатам посмотреть на девайсе
    //устанавливает вектор ускорения со случайными значениями
    //устанавливает масштаб со случайными координатами
    //устанавливает вращение со случайными координатами
    ////устанавливает прозрачность в 1f
    private void initMove(){
        dimension = new Vector2(width, height);

        if(elementId % 2 == 0)
            position = new Vector2(MathUtils.random(-150, -100), MathUtils.random(-100, dimension.y));
        else
            position = new Vector2(MathUtils.random(-150, dimension.x + 150), MathUtils.random(dimension.y - 150, -100));

        velocity = new Vector2(MathUtils.random(-5, 5), MathUtils.random(-5, 5));

        scale = MathUtils.random(0.3f, 1.15f);
        rotation = MathUtils.random(0f, 360f);
        alpha = 1f;
    }

    //определяет вектор измерения с шириной 1080 , высотой 1920
    //определяет вектор позиции (по линии x случайное число между 0 и dimension.x,по линии y случайное число между 0 и dimension.y)
    //определяет вектор ускорения (случайные значения)
    //определяет случайный масштаб между 0.5f и 1.5f
    //определяет случайное вращение между 0f, 360f
    //ставит прозрачность true
    private void initAlpha(){
        dimension = new Vector2(width, height);
        position = new Vector2(MathUtils.random(0, dimension.x), MathUtils.random(0, dimension.y));
        velocity = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-0.5f, 0.5f));

        scale = MathUtils.random(0.5f, 1.5f);
        rotation = MathUtils.random(0f, 360f);
        isAlpha = true;
    }

    //действие в единицу времени
    //устанавливает вектор измерения с шириной 1080 , высотой 1920
    //если непрозрачно и позиция выходит за размеры экрана
    //TODO посмотреть на девайсе
    @Override
    public void act(float delta) {
        dimension = new Vector2(width, height);

        if(!isAlpha) {
            if (position.x > dimension.x + 200 || position.y > dimension.y + 200 || position.x < -200 || position.y < -200)
                initMove();

            position.sub((velocity.x * speed) * delta, (velocity.y * speed) * delta);
            rotation += delta * speed;
        } else {

            if(alpha < 0)
                initAlpha();

            if(alpha > 1)
                velocity.x *= -1;

            alpha += velocity.x * delta;
        }

        super.act(delta);
    }

    //отрисовка
    public void draw(Batch batch, float parentAlpha) {
        Color temp;
        temp = batch.getColor();

        batch.setColor(new Color(temp.r, temp.g, temp.b, alpha));

        batch.draw(textureRegion, position.x, position.y, textureRegion.getRegionWidth() / 2, textureRegion.getRegionHeight() / 2,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), scale, scale, rotation);

        batch.setColor(temp);
    }


}
