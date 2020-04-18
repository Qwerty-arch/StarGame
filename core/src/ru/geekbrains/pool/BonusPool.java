package ru.geekbrains.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.sprites.Bonus;

public class BonusPool extends SpritesPool<Bonus> {

    private final TextureAtlas atlas;

    public BonusPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected Bonus newObject() {
        try {
            return new Bonus(atlas);
        } catch (GameException e) {
            throw new RuntimeException("Не удалось получить текстуру взрыва");
        }
    }
}
