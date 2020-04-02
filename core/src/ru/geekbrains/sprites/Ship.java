package ru.geekbrains.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class Ship extends Sprite {

    private Vector2 speedRight;
    private Vector2 speedLeft;
    private Rect worldBounds;
   // boolean leftMove;
   // boolean rightMove;

    public Ship(TextureAtlas atlas) throws GameException {
        super(new TextureRegion(atlas.findRegion("main_ship"), 0,0,197,290));
        this.speedRight = new Vector2(-0.01f,0f);
        this.speedLeft = new Vector2(0.01f,0f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.2f);
        pos.set(worldBounds.pos);
        setLeft(worldBounds.getLeft() + 0.27f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            logicEntryConditionsLeftAndSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            logicEntryConditionsRightAndSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            logicEntryConditionsLeftAndSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            logicEntryConditionsRightAndSpeed();
        }
        /*{
            if (leftMove)
            {
                pos.add(speedLeft);
            }
            if (rightMove)
            {
                pos.add(speedRight);
            }
        }*/
    }

   /* public void setLeftMove(boolean t)
    {
        if(rightMove && t) rightMove = false;
        leftMove = t;
    }
    public void setRightMove(boolean t)
    {
        if(leftMove && t) leftMove = false;
        rightMove = t;
    }*/

    private void logicEntryConditionsRightAndSpeed() {
        pos.add(speedLeft);
        if (getLeft()+ 0.07f >= worldBounds.getRight() ) {
            setLeft(worldBounds.getLeft() + 0.5f);
            setBottom(worldBounds.getBottom() + 0.05f);
        }
    }

    private void logicEntryConditionsLeftAndSpeed() {
        pos.add(speedRight);
        if (getRight() <= worldBounds.getLeft() + 0.07f) {
            setLeft(worldBounds.getLeft());
            setBottom(worldBounds.getBottom() + 0.05f);
        }
    }
}
