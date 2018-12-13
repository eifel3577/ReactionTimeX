package com.argo_entertainment.reactiontime;

/*
	Author: ArgoMedia (Vlad Volovik)
 */
import com.argo_entertainment.reactiontime.LoadingScreen.LoadingScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

import jdk.nashorn.internal.parser.JSONParser;

// наследует Game который имплементит ApplicationListener для связи libgdx с событиями жизненного цикла Андроид
public class ReactionTimeClass extends Game {
	
	//создает массивы текстур 
	private Texture[] backObj = new Texture[20];
	private Texture[] textures = new Texture[20];
	private Texture[][] elements = new Texture[10][20];
	private Texture[][] special = new Texture[10][20];

	//инициирует  AssetManager для доступа к ассетам
	public AssetManager assetManager = new AssetManager();
	//инициирует Preferences для доступа к настройкам
	private Preferences prefs;
	//ширина
	public float w;
	//высота
	public float h;
	//скорость анимации
	public float menu_animation_speed = 0.7f;

	public ActionResolver actionResolver;

	//массив для сохраненных Элементов	
	private Integer[][][] savedGameElements = new Integer[9][10][2];

	//текстуры
	private Texture gameElements;
	private Texture gameBigElements;
	private Array planetText;
	private Array tutorialText;
	public float VOLUME = 1f;
	
	public ReactionTimeClass(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}

	//вызывается один раз при старте приложения
	//вызывается при запуске приложения.Определяет высоту и ширину игрового мира, устанавливает камеру,настраивает камеру
	//для работы с размерами игрового мира,утанавливает стартовую позицию камеры (центр экрана),достает настройки,
	//если настроек сохраненных нет, пишет дефолтные и сохраняет в savedGameElements,если есть сохраненные то пишет 
	//сохраненные в savedGameelements,устанавливает текущим экраном LoadingScreen и передает в LoadingScreen инстанс 
	//Game и обьект камеры, достает сохраненный текст из файла planet-info и пишет его в массив planetText,загужает текст
	//туториала из файла
	@Override
	public void create () {
		w = 1080;
		h = 1920;
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.position.set(new Vector2(w/2,h/2), 0);
		camera.zoom = 1;
		camera.update();
		prefs = Gdx.app.getPreferences("gameData");
		loadSavedElement();
		setScreen(new LoadingScreen(this, camera));
		setTexts();
		setTutorialTexts();
	}

	
	//достает json из файла planet-info ,конвертит полученный  json в массив, присваивает полученный массив массиву 
	//planetText
	private void setTexts(){
        	Json texts = new Json();
		planetText = texts.fromJson(Array.class, Gdx.files.internal("text/planet_info.json"));
	}

	//получает интовое значение планеты,конвертит инфу по планете в двухмерный массив, возвращает двухмерный массив
	//
	public String[][] getText(int planet){
		String[][] result = new String[7][2];

		JsonValue planetObj = (JsonValue) planetText.get(planet - 1);
		
		result[0][0] = planetObj.child.asString();
		planetObj = planetObj.child.next();

		result[0][1] = planetObj.asString();
		planetObj = planetObj.next();

		int size = planetObj.size;

		for (int i = 1; i <= size; i++){
			JsonValue itemObj = planetObj.get(i - 1);
			result[i][0] = itemObj.get(0).asString();
			result[i][1] = itemObj.get(1).asString();
		}

		return result;
	}

	
	//заполняет массив  tutorialtext данными из файла intro-info
	private void setTutorialTexts(){
        	Json texts = new Json();
		tutorialText = texts.fromJson(Array.class, Gdx.files.internal("text/intro_info.json"));
	}

	// возвращает значение из tutorialText по позиции
	public String getTutorialText(int step){
		return (String) tutorialText.get(step);
	}

	//возвращает из префа имя по указанному строковому иденту
	public String getStrings(String name){
		return prefs.getString(name, "");
	}

	//возвращает из префа число по указанному строковому иденту
	public Integer getNumbers(String name){
		return prefs.getInteger(name, 0);
	}

	//возвращает флаг были ли установлены настройки по указанному иденту
	public Boolean getSettings(String name){
		return prefs.getBoolean(name, true);
	}

	//возвращает флаг есть ли закрытые планеты
	//true если есть
	public Boolean isClosedPlanet(Integer planet) {
		Boolean closed = false;

		if(planet >= 2) {
			for(int i = 1; i<=6; i++) {
				Integer[] el = getSavedElements(planet - 1, i);

				if(!el[0].equals(el[1])) {
					closed = true;
					break;
				}
			}
		}

		return closed;
	}

	//возвращает интовый номер текущей игровой планеты
	public Integer nowPlayPlanet() {
		Integer nowPlanet = 1;

		for(int i=1; i < 10; i++) {
			if(isClosedPlanet(i)) {
				break;
			}
			nowPlanet = i;
		}

		return nowPlanet;
	}

	//возвращает из префов имя игрока
	public String getPlayerName(){
		return prefs.getString("player_name", "player_name");
	}

	//кладет в преф значение настроек стринг
	public void setSettings(String name, String value){
		prefs.putString(name, value);
		prefs.flush();
		/*if (name.equals("high_score")) {
            if (!actionResolver.getSignedInGPGS()) actionResolver.submitScoreGPGS(value);
        }*/
	}

	//кладет в преф значение настроек инт
	public void setSettings(String name, Integer value){
		prefs.putInteger(name, value);
		prefs.flush();
		if (name.equals("high_score")) {
            if (!actionResolver.getSignedInGPGS()) actionResolver.submitScoreGPGS(value);
        }
	}

	//прибавляет к сохраненному в префе значению значение value
	public void inclSettings(String name, Integer value){
		Integer old = prefs.getInteger(name, 0);
		prefs.putInteger(name, old + value);
		prefs.flush();
	}

	//кладет в преф значение воспроизводится ли звук
	public void setSettings(String name, Boolean value){
		prefs.putBoolean(name, value);
		prefs.flush();

		if(name.equals("sound"))
			if(value) startSound(); else stopSound();
		if(name.equals("music"))
			//test
			if(value) Assets.resumeMusic(); else Assets.disableMusic();
			//if(value) startMusic(); else stopMusic();
	}

	//фиксирует запись в преф
	public void flushSettings(){
		prefs.flush();
	}

	//возвращает TileMap карту слоев по указанному уровню
	public TiledMap tiledMap(String level){
		return assetManager.get(level);
	}

	//возвращает BitmapFont по указанному уровню
	public BitmapFont getFont(String level){
		return assetManager.get(level);
	}

	//загружает в массив обьекты BitmapFont из ассетов , возвращает массив 
	public BitmapFont[] getFonts(){
		BitmapFont fonts[] = new BitmapFont[100];
		fonts[24] = assetManager.get("bitmapfont/Fredoka_One_24.fnt");
		fonts[36] = assetManager.get("bitmapfont/Fredoka_One_36.fnt");

		fonts[48] = assetManager.get("bitmapfont/Fredoka_One_48.fnt");

		fonts[49] = assetManager.get("bitmapfont/Fredoka_One_gold_48.fnt");

		fonts[72] = assetManager.get("bitmapfont/Fredoka_One_72.fnt");


		fonts[64] = assetManager.get("bitmapfont/Fredoka_One_white_64.fnt");
		fonts[65] = assetManager.get("bitmapfont/Fredoka_One_green_64.fnt");
		fonts[66] = assetManager.get("bitmapfont/Fredoka_One_yellow_64.fnt");
		fonts[67] = assetManager.get("bitmapfont/Fredoka_One_pink_64.fnt");

		return fonts;
	}

	//загружает текстуры textures
	public void setTextures(Texture[] textures) {
		this.textures = textures;
	}
	//загружает текстуры elenments
	public void setElements(Texture[][] textures) {
		this.elements = textures;
	}
	// загружает текстуры  special
	public void setSpecial(Texture[][] textures) {
		this.special = textures;
	}
	//   загружает текстуры gameElements
	public void setGameElements(Texture texture) {
		gameElements = texture;
	}
	//загружает текстуры gameBigElements
	public void setBigGameElements(Texture texture) {
		gameBigElements = texture;
	}

	// возвращает текстуры
	public Texture[] getTextures() {
		return textures;
	}
	public Texture[] getElements(int type) {
		return elements[type];
	}
	public Texture[][] getSpecial() {
		return special;
	}

	//класс TextureRegion описывает прямоугольник внутри текстуры и используется для рисования только части текстуры
	//метод принимает в параметры инт планеты и инт элемента.Возвращает TextureRegion (части планет) по номеру планеты
	//и по элементу
	public TextureRegion getBigElements(int planet, int element) {
		TextureRegion elementTexture = new TextureRegion(gameBigElements);
		elementTexture.setRegion(((planet - 1) * 333), ((element - 1) * 333), 333, 333);
		return elementTexture;
	}

	
	public TextureRegion getElementsSprite(int planet, int element) {
		TextureRegion elementTexture = new TextureRegion(gameElements);
		elementTexture.setRegion(((planet - 1) * 135), ((element - 1) * 135), 135, 135);
		return elementTexture;
	}

	
	public void setSavedElement (int planet_num, int element_num, int count) {
		savedGameElements[planet_num - 1][element_num - 1][0] = count;

		updateGameElementsSave();
	}

	//очищает префы
	public void clearPrefs(){
		prefs.clear();
		prefs.flush();
	}

	//загружает значения из префов,если сохраненных значений нет,то пишет значения по умолчанию и обновляет
	public void loadSavedElement () {
		String gameElementsSaved = prefs.getString("countGameElements");

			if(gameElementsSaved.length() < 1 ) {
			savedGameElements = new Integer[][][]{
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					},
					{
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2},
							{0,2}
					}
			};

			updateGameElementsSave();
		}
		else {
			String[] gameElementsSavedPlanets = gameElementsSaved.split(">");
			for(int i=0; i <= 8; i++) {
				String[] row = gameElementsSavedPlanets[i].split("<");
				for(int j=0; j <= (row.length - 1); j++) {
					String[] el = row[j].split("#");
					for(int k=0; k <= (el.length - 1); k++) {
						savedGameElements[i][j][k] = Integer.parseInt(el[k]);
					}
				}
			}
		}
	}

	//обновляет строку с элементами в префе
	private void updateGameElementsSave(){
		StringBuilder result = new StringBuilder();
		for (Integer[][] row : savedGameElements){
			for (Integer[] el : row){
				for (Integer count : el){
					if(count != null)
						result.append(count.toString()).append("#");
				}
				result.append("<");
			}
			result.append(">");
		}

		prefs.putString("countGameElements", result.toString());
		prefs.flush();
	}

	//возвращает массив savedElements по номеру планеты
	public Integer[][] getSavedElements (int planet_num) {
		return savedGameElements[planet_num - 1];
	}

	
	public Integer[] getSavedElements (int planet_num, int element_num) {
		return savedGameElements[planet_num - 1][element_num - 1];
	}

	//устанавливает массив текстур backObj
	public void setBackObj(Texture[] backObj) {
		this.backObj = backObj;
	}
	//возвращает массив текстур backObj
	public Texture[] getBackObj() {
		return backObj;
	}

	
	public Vector2 getIOSSafeAreaInsets() {
		return actionResolver.getSceenOffset();
	}

	//включает одиночные звуки
	public void startSound() {
		VOLUME = 1f;
		Assets.setSound(true);
	}

	//выключает одиночные звуки
	public void stopSound() {
		VOLUME = 0f;
		Assets.setSound(false);
	}

	//включает фоновую музыку
	public void startMusic() {
		Assets.setMusic(true);
	}



	//ставит фоновую музыку на паузу
	public void pauseMusic() {
		Assets.setSound(false);
	}

	//выключает фоновую музыку
	public void stopMusic() {
		Assets.setMusic(false);
	}

	//включает интро
	public void introSound() {
		Assets.playMusic(Assets.introSound);
	}

	//звук по касанию экрана
	public void tapSound() {
		Assets.playSound(Assets.clickSound);
	}

	//звук при покупке
	public void buySound() {
		Assets.playSound(Assets.inAppOk);
	}

	
	@Override
	public void render () {
		super.render();
		// Gdx.app.log("act", "FPS: " + Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void dispose () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void pause () {
		 //pauseMusic();
	}
}
