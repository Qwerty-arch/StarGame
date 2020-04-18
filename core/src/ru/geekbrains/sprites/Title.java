package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class Title extends Sprite {

    public Title(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("SpaceShooter"), 1, 1, 1);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.3f);
        setTop(worldBounds.getTop() - 0.3f);
    }
}
