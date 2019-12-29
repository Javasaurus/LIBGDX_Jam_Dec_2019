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
package be.codecuisine.graphics.planet.generators;

import be.codecuisine.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import be.codecuisine.graphics.planet.PlanetVisuals;
import be.codecuisine.graphics.planet.objects.Star;

public class ObjectGenerator  {

    private PlanetVisuals scene;
    private int velDir;
    private int zDir;

    private ObjectGenerator() {

    }

    public ObjectGenerator(PlanetVisuals scene, int velDir, int zDir) {
        this.scene = scene;
        this.velDir = velDir;
        this.zDir = zDir;
    }

    public void setScene(PlanetVisuals scene) {
        this.scene = scene;
    }

    public Array<Star> createStars() {
        return createStars(MathUtils.random(20, 200));
    }

    public Array<Star> createStars(int starAmount) {
        Array<Star> stars = new Array<>();
        for (int i = 0; i < starAmount; i++) {
            Sprite star = new Sprite(PlanetVisuals.pixelTexture);
            star.setPosition(MathUtils.random(0, Constants.WIDTH), MathUtils.random(0, Constants.HEIGHT));
            star.setColor(Color.WHITE);

            if (MathUtils.randomBoolean(0.1f)) {
                star.setSize(2, 2);
            }

            stars.add(new Star(star));
        }

        for (Star star : stars) {
            scene.addStar(star);
        }

        return stars;
    }

    public int getVelDir() {
        return velDir;
    }

    public int getZDir() {
        return zDir;
    }

 
}
