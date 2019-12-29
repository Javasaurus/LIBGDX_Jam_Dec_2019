/* 
 * Copyright 2019 kenne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.codecuisine.graphics.planet.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Star extends SpaceObject {

    private float totalTime = 0;
    private float scrollspeed;
    private float scale = 1;
    private float baseScale;
    private float oscillationSpeed;

    public Star(Sprite sprite) {
        super(sprite);
        init();
    }

    private void init() {
        getPosition().set(new Vector2(getSprite().getX(), getSprite().getY()));
        this.baseScale = (float) (1f + (Math.random() * 2));
        this.oscillationSpeed = (float) (.24f + Math.random() * 5);
        this.scrollspeed = MathUtils.random(.5f, 25f);
    }

    @Override
    public void update(float delta) {
        totalTime += delta;
        scale = baseScale + (float) Math.sin(totalTime * oscillationSpeed);
        getSprite().setScale(scale, scale);

        getPosition().y -= scrollspeed * delta;
        if (getPosition().y < -10) {
            init();
            getPosition().y = Gdx.graphics.getHeight() + MathUtils.random(5, 15);
            getPosition().x = MathUtils.random(0, Gdx.graphics.getWidth());
        }

        getSprite().setPosition(getPosition().x, getPosition().y);
    }

    @Override
    public boolean isBackground() {
        return true;
    }

    @Override
    public Integer getZ() {
        return -999;
    }

}
