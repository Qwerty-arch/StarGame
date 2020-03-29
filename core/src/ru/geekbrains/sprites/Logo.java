package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {

    private static final float V_LEN = 0.005f;
    private Texture bg;
    private Vector2 speed;
    private Vector2 touch1;
    private Vector2 tmp;

    public Logo(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        this.speed = new Vector2();
        this.touch1 = new Vector2();
        this.tmp = new Vector2();
    }

    @Override
    public void update(float delta) {
        tmp.set(touch1);
        float remainingDistance = (tmp.sub(pos)).len();
        if (remainingDistance > V_LEN) {
            pos.add(speed);
             } else {
                speed.setZero();
                pos.set(touch1);
            }

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
        pos.set(worldBounds.pos);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        touch1.set(touch);
        speed.set(touch1.cpy().sub(pos)).setLength(V_LEN);
        return super.touchDown(touch, pointer, button);
    }
}
