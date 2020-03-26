package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 direction;
    private Vector2 normalVector;
    private float toX;
    private float toY;
    private int speed = 2;
    private float angle;
    private Background background;

    @Override
    public void show() {
        super.show();
        background = new Background();
        img = new Texture("1-12967-256.png");
        pos = new Vector2();
        v = new Vector2(0f, 0f);
        direction = new Vector2(0f, 0f);
        normalVector = new Vector2(0f, 1f);
        angle = 0.0f;   // лишняя часть код, для вторго варианта
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        toX = screenX - img.getWidth() / 2f;
        toY = Gdx.graphics.getHeight() - screenY - img.getHeight() / 2f;
        v.set(direction.set(toX - pos.x, toY - pos.y).nor());
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode >= 7 && keycode <= 16)
            speed = keycode - 7;

        return false;
    }

    private void update(float delta) {
      /*  if (Gdx.input.isKeyPressed(Input.Keys.A)) {       // тут всё работает
            angle += 180.0f * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * delta;
        }*/
        if ((int)toX == Math.round(pos.x) && (int)toY == Math.round(pos.y))
            v.set(0f, 0f);
            pos.add(v.cpy().scl(speed));

    }

    private void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
     //   batch.draw(img, pos.x - 128, pos.y - 128, 128, 128, 256, 256, 1, 1, angle, 0, 0, 256, 256, false, false);
        batch.draw(img, pos.x, pos.y);
        batch.end();
    }

}
