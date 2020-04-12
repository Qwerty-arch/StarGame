package ru.geekbrains.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class GameOver extends Sprite {

    private final Game game;

    public GameOver(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("message_game_over"));
        this.game = null;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.075f);
        setTop(0.1f);
    }
}
