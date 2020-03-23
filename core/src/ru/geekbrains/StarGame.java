package ru.geekbrains;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;


public class StarGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
	Texture dropImage;
	TextureRegion region;
	Rectangle bucket;
	Array<Rectangle> rainDrops;			// для хранения капель // каждый из экземпляров Rectangle хранит позицию и размер капли(ArrayList не пойдет, так подобные коллекции производят мусор различными способами, а этот хоть старается минимизировать мусор)
	long lastDropTime;					// чтобы отслеживать последнее появление капли

	@Override
	public void create () {

		camera = new OrthographicCamera();	// создаем камеру
		camera.setToOrtho(false, 800, 480);	// setToOrtho() позволяет убедится, что камера всегда показывает область мира игры размером 800/480(как виртуальное окно в наш мир)

		batch = new SpriteBatch();
		//img = new Texture("shocked-smiley.png");
		dropImage = new Texture("droplet.png");

		//region = new TextureRegion(img, 10, 10, 150, 150);

		rainDrops = new Array<Rectangle>();
		/*
		* Рождение первой капельки
		 */
		spawnRainDrop();
	}

	/*

	* spawnRainDrop()
	* Создает новый Rectangle
	* устанавливает его в случайной позиции в верхней части экрана
	* И добавляет в массив rainDrops
	*
	 */

	private void spawnRainDrop() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800 - 64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		rainDrops.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.begin();
		//batch.setColor(0.1f, 0.3f, 0.6f, 0.5f);
		//batch.draw(img, 0, 0);
		/*
		*	Капли нужно отображать на экране
		 */
		for (Rectangle raindrop : rainDrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		//batch.draw(region, 200, 200);
		batch.end();

		/*
		*
		* if проверяет, сколько времени прошло
		* с тех пор, как была создана новая капля
		* и если необходимо создавать ещё
		* одну новую каплю
		* PS Капли двигаются с постоянной скоростью в 200 пикселей/сек
		* Если капля находится ниже нижнего края экрана,
		* то мы удаляем её из массива
		*
		 */

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();
		Iterator<Rectangle> iter = rainDrops.iterator();
		while (iter.hasNext()) {
			Rectangle rainDrop = iter.next();
			rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (rainDrop.y + 64 < 0) {
				iter.remove();
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		dropImage.dispose();
	}
}