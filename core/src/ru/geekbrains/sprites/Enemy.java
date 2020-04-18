package ru.geekbrains.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.pool.ParticlePool;

public class Enemy extends Ship {

    private final Vector2 descentV;

    private float bonusHeight;
    private BonusPool bonusPool;
    private ParticlePool particlePool;
    private float timeMax;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, BonusPool bonusPool, ParticlePool particlePool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bonusPool = bonusPool;
        this.particlePool = particlePool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        bonusHeight = 0.05f;
        descentV = new Vector2(0, -0.3f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if (getTop() <= worldBounds.getTop()) {
            v.set(v0);
            autoShoot(delta);
            practiceShipAnimation();
        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    private void practiceShipAnimation() {
        for (int i = 0; i < 5; i++) {
            particlePool.setup(
                    pos.x, getTop(),
                    0, v.y * Rnd.nextFloat(0.7f, 4.4f),
                    timeMax,
                    0.0001f, 0.002f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 0.0f
            );
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            Sound shootSound,
            int hp,
            float height,
            float timeMax
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(descentV);
        setHeightProportion(height);
        this.timeMax = timeMax;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y);
    }

    @Override
    protected void boom() {
        super.boom();
        float type = (float) Math.random();
        if (type < 0.3f) {
            Bonus bonus = bonusPool.obtain();
            bonus.set(pos, bonusHeight, worldBounds);
        }
    }
}
