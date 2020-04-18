package ru.geekbrains.sprites;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class Bonus extends Sprite {

    private Rect worldBounds;
    private final Vector2 v = new Vector2(0,-0.25f);
    private int damage;


    public Bonus(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("bonus"), 1, 1, 1);
    }

    public void set(Vector2 pos, float height, Rect worldBounds) {
        this.pos.set(pos);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        damage = -10;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
          if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }
}