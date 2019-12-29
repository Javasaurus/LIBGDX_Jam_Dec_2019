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
package be.codecuisine.graphics.effects;

import be.codecuisine.game.math.VectorUtil;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kenne
 */
public class PlanetExplosion {

    private float totalTime = 0;

    public boolean finished = false;

    HashMap<Explosion, Float> explosionTimers = new HashMap<>();

    public PlanetExplosion(Vector2 position, float radius, float duration) {

        float tmp = duration;
        while (tmp > 0) {
            tmp -= MathUtils.random(0, 100);
            Vector2 tmpPosition = VectorUtil.RandomVector(position, MathUtils.random(0, radius));
            Explosion explosion = new Explosion(tmpPosition);
            explosion.play=false;
            explosion.setAutoDestroy(true);
            explosion.setPlayBackSpeed(1f);
            explosion.setScale(MathUtils.random(1, 4));
            explosionTimers.put(explosion, tmp);
            if (explosionTimers.size() > 50) {
                break;
            }
        }

    }

    public void update(float t) {
        totalTime += t;

        boolean isFinished = true;
        for (Map.Entry<Explosion, Float> explosion : explosionTimers.entrySet()) {
            if (explosion.getValue() < totalTime * 1000) {
                explosion.getKey().play = true;
            }

            if (isFinished && !explosion.getKey().isDestroyed) {
                isFinished = false;
            }
        }

        if (!finished) {
            finished = isFinished;
        }
    }

}
