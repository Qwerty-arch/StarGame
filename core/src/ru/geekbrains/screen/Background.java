package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {

    private Texture textureCosmos;

    public Background() {
        this.textureCosmos = new Texture("cosmos.jpg");
    }


    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
    }

    public void update(float dt) {

    }

}


