package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Ship;
import ru.geekbrains.sprites.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private Ship ship;

    private TextureAtlas atlas;
    private TextureAtlas atlasShip;

    private Star[] stars;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlasShip = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        initSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        ship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        atlasShip.dispose();
        super.dispose();
    }

 /*   @Override
    public boolean keyDown(int keycode) {
        //return super.keyDown(keycode);
        {
            switch (keycode)
            {
                case Input.Keys.LEFT:
                    ship.setLeftMove(true);
                    break;
                case Input.Keys.RIGHT:
                    ship.setRightMove(true);
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean keyUp(int keycode) {

        //return super.keyUp(keycode);
        {
            switch (keycode)
            {
                case Input.Keys.LEFT:
                    ship.setLeftMove(false);
                    break;
                case Input.Keys.RIGHT:
                    ship.setRightMove(false);
                    break;
            }
            return true;
        }
    }*/

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            ship = new Ship(atlasShip);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        ship.update(delta);
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        ship.draw(batch);
        batch.end();
    }
}

