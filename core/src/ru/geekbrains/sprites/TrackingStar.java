package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.exception.GameException;

public class TrackingStar extends Star {

    private final Vector2 trackingV;

    private final Vector2 sumV = new Vector2();

    public TrackingStar(TextureAtlas atlas, Vector2 trackingV) throws GameException {
        super(atlas);
        this.trackingV = trackingV;
    }

    @Override
    public void update(float delta) {
        sumV.setZero().mulAdd(trackingV, 0.2f).rotate(180).add(v);
        pos.mulAdd(sumV, delta);
        checkAndHandleBounds();
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0;
            setHeightProportion(STAR_HEIGHT);
        } else {
            setHeightProportion(getHeight() + 0.0001f);
        }
    }
}
