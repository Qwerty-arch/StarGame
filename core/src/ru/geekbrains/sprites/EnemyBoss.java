package ru.geekbrains.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class EnemyBoss extends Ship {

    private static final float V_LEN = -0.5f;
    private static final int HP = 60;
    private static final float SHIP_HEIGHT = 0.4f;
    private static final float BOTTOM_MARGIN = 0.2f;
    private Vector2 savePosition;
    private MainShip mainShip;


    public EnemyBoss(TextureAtlas atlas, TextureAtlas atlasBullet, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound, MainShip mainShip) throws GameException {
        super(atlas.findRegion("boss_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shootSound = shootSound;
        this.mainShip = mainShip;
        bulletRegion = atlasBullet.findRegion("bulletEnemy");
        savePosition = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        v = new Vector2(0,-0.1f);
        v0 = new Vector2();
        reloadInterval = 0.9f;
        reloadTimer = reloadInterval;
        bulletHeight = 0.07f;
        damage = 15;
        hp = HP;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getTop());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        savePosition.set(mainShip.pos.x, mainShip.pos.y);
        bulletPos.set(pos.x, pos.y - getHalfHeight()/2);
        bulletV.set(savePosition.cpy().sub(bulletPos)).setLength(V_LEN);

        autoShoot(delta);

        if (getTop() <= worldBounds.getTop()) {
            v.set(v0);
            autoShoot(delta);
        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public void startNewBoss(Rect worldBounds) {
        flushDestroy();
        hp = HP;
        pos.y = worldBounds.getTop() + BOTTOM_MARGIN;
        v.set(0,-0.1f);
    }

     public boolean isBulletCollision(Rect bullet) {
         return !(bullet.getRight() < getLeft()
                 || bullet.getLeft() > getRight()
                 || bullet.getBottom() > getTop()
                 || bullet.getTop() < pos.y);
    }
}