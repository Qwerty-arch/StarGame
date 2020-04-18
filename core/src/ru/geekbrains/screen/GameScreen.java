package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.pool.ParticlePool;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Bonus;
import ru.geekbrains.sprites.Bullet;
import ru.geekbrains.sprites.ButtonNewGame;
import ru.geekbrains.sprites.Enemy;
import ru.geekbrains.sprites.EnemyBoss;
import ru.geekbrains.sprites.GameOver;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.sprites.TrackingStar;
import ru.geekbrains.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private enum State {PLAYING, PAUSE, BOSS, GAME_OVER}

    private static final int STAR_COUNT = 150;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final String BOSS = "HP BOSS: ";
    private static final String RECORD = "RECORD: ";


    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private TextureAtlas atlasBoss;
    private TextureAtlas atlasBonus;

    private TrackingStar[] stars;

    private MainShip mainShip;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyBoss enemyBoss;
    private ExplosionPool explosionPool;
    private BonusPool bonusPool;
    private ParticlePool particlePool;
    private EnemyEmitter enemyEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosion;
    private State state;
    private State prevState;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;
    private StringBuilder sbHPBoss;
    private StringBuilder sbRecord;

    private int frags;
    private int record;



    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlasBoss = new TextureAtlas(Gdx.files.internal("textures/boss_ship.atlas"));
        atlasBonus = new TextureAtlas(Gdx.files.internal("textures/bonus.atlas"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosion);
        bonusPool = new BonusPool(atlasBonus);
        particlePool = new ParticlePool(atlas);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bonusPool, particlePool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        sbHPBoss = new StringBuilder();
        sbRecord = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
        state = State.PLAYING;
        prevState = State.PLAYING;
        frags = 0;
    }

    public void startNewGame() {
        state = State.PLAYING;
        mainShip.startNewGame(worldBounds);
        frags = 0;
        enemyBoss.startNewBoss(worldBounds);
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        bonusPool.freeAllActiveObjects();
       // particlePool.freeAllActiveObjects();
    }

    @Override
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        enemyBoss.resize(worldBounds);
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        atlasBoss.dispose();
        atlasBonus.dispose();

        bulletPool.dispose();
        enemyPool.dispose();
      //  particlePool.dispose();

        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();

        explosion.dispose();

        font.dispose();
        super.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING || state == State.BOSS) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if  (state == State.PLAYING || state == State.BOSS) {
            mainShip.keyUp(keycode);
        }
        return false;
    }
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if  (state == State.PLAYING || state == State.BOSS) {
            mainShip.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if  (state == State.PLAYING || state == State.BOSS) {
            mainShip.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
            stars = new TrackingStar[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new TrackingStar(atlas, mainShip.getV());
            }
            gameOver = new GameOver(atlas);
            enemyBoss = new EnemyBoss(atlasBoss, atlas, bulletPool, explosionPool, laserSound, mainShip);
            buttonNewGame = new ButtonNewGame(atlas, this);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        bonusPool.updateActiveSprites(delta);
    //    particlePool.updateActiveSprites(delta);
        switch (state) {
            case PLAYING:
                 mainShip.update(delta);
                 bulletPool.updateActiveSprites(delta);
                 enemyPool.updateActiveSprites(delta);
                 enemyEmitter.generate(delta, frags);
                 particlePool.update(delta);
                break;
            case BOSS:
                mainShip.update(delta);
                enemyBoss.update(delta);
                bulletPool.updateActiveSprites(delta);
                particlePool.update(delta);
                break;
            case GAME_OVER:
                buttonNewGame.update(delta);
            break;
        }

    }

    private void checkCollisions() {
       if (state == State.GAME_OVER) {
           return;
       }

        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        List<Bonus> bonusList = bonusPool.getActiveObjects();

        if (state == State.PLAYING) {
            for (Enemy enemy : enemyList) {
                if (enemy.isDestroyed()) {
                    continue;
                }
                float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
                if (mainShip.pos.dst(enemy.pos) < minDist) {
                    enemy.destroy();
                    frags++;
                    cheakBossTime();
                    cheakRecord();
                    mainShip.damage(enemy.getDamage());
                }
                for (Bullet bullet : bulletList) {
                    if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                        continue;
                    }
                    if (enemy.isBulletCollision(bullet)) {
                        enemy.damage(bullet.getDamage());
                        bullet.destroy();
                        if (enemy.isDestroyed()) {
                            frags++;
                            cheakRecord();
                            cheakBossTime();
                        }
                    }
                }
            }
        }


        if (state == State.PLAYING || state == State.BOSS) {
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }

        for (Bonus bonus : bonusList) {
            if (bonus.isDestroyed()) {
                continue;
            }
            if (mainShip.isBonusCollision(bonus)) {
                mainShip.damage(bonus.getDamage());
                bonus.destroy();
            }

        }

        if (state == State.BOSS) {
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() == enemyBoss || bullet.isDestroyed()) {
                    continue;
                }
                if (enemyBoss.isBulletCollision(bullet)) {
                    enemyBoss.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemyBoss.isDestroyed()) {
                        frags++;
                        cheakRecord();
                    }
                }
            }
        }

        if (enemyBoss.isDestroyed()) {
           state = State.PLAYING;

        }

        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    private void cheakBossTime() {
        if (frags % 10 == 0) {
            if (enemyBoss.isDestroyed()) {
                enemyBoss.startNewBoss(worldBounds);
            }
            List<Enemy> enemyList = enemyPool.getActiveObjects();
            state = State.BOSS;
            for (Enemy enemy : enemyList) {
                enemy.destroy();
            }
        }
    }

    private void cheakRecord() {
        if (record < frags) {
            record = frags;
        }
    }



    private void freeAllDestroyed() {
     //   particlePool.freeAllDestroyedActiveObjects();
        bulletPool.freeAllDestroyedActiveObjects();
        bonusPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state) {
            case PLAYING:
         //       particlePool.drawActiveSprites(batch);
                mainShip.draw(batch);
                enemyPool.drawActiveSprites(batch);
                bulletPool.drawActiveSprites(batch);
                particlePool.render(batch);
                break;
            case BOSS:
        //        particlePool.drawActiveSprites(batch);
                mainShip.draw(batch);
                enemyBoss.draw(batch);
                bulletPool.drawActiveSprites(batch);
                particlePool.render(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                buttonNewGame.draw(batch);
                break;
        }
        explosionPool.drawActiveSprites(batch);
        bonusPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        sbHPBoss.setLength(0);
        sbRecord.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
        if (state == State.BOSS) {
            font.draw(batch, sbHPBoss.append(BOSS).append(enemyBoss.getHp()), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN * 10, Align.left);
        }
        if (state == State.GAME_OVER) {
            font.draw(batch, sbRecord.append(RECORD).append(record), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN * 20, Align.center);
        }
    }
}
