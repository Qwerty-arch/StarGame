package ru.geekbrains.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public class Enemy extends Ship {

    private Vector2 acceleration;

    public Enemy(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        acceleration = new Vector2();
    }

    @Override
    public void update(float delta) {
        //super.update(delta);
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
        if (getTop() - 0.03f >= worldBounds.getTop() - 0.03f) {
            pos.mulAdd(acceleration, delta);
        } else {
            pos.mulAdd(v, delta);
            reloadTimer += delta;
            if (reloadTimer >= reloadInterval) {
                reloadTimer = 0f;
                shoot();
            }
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            Vector2 speed,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            Sound shootSound,
            int hp,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.acceleration.set(speed);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(v0);
        setHeightProportion(height);
    }
}
