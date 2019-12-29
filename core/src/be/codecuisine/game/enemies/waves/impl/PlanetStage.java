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
package be.codecuisine.game.enemies.waves.impl;

import be.codecuisine.game.enemies.waves.BaseEnemyWave;
import static be.codecuisine.game.GameLogic.totalTime;
import be.codecuisine.graphics.effects.PlanetExplosion;
import be.codecuisine.graphics.planet.PlanetVisuals;
import be.codecuisine.graphics.planet.objects.SpaceBody;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author kenne
 */
public class PlanetStage extends BaseEnemyWave {

    private SpaceBody planet;
    private PlanetExplosion explosion;
    private long waveStartTime;
    private float yInit;

    public PlanetStage(int waveNr) {
        super(waveNr);
        yInit = 2 * Gdx.graphics.getHeight() / 3;
    }

    @Override
    public void LaunchWave() {
        planet = PlanetVisuals.INSTANCE.CreatePlanet();
        waveStartTime = TimeUtils.millis();
    }

    @Override
    public void EndWave() {
        planet.dispose();
        PlanetVisuals.INSTANCE.removeObject(planet);
    }

    @Override
    public boolean evaluateWave() {
        if (explosion != null) {
            return explosion.finished && (TimeUtils.timeSinceMillis(waveStartTime) > 3000);
        }
        return false;
    }

    @Override

    public void update(float dt) {

        if (planet.isDestroyed) {
            if (explosion == null) {
                explosion = new PlanetExplosion(new Vector2(
                        planet.getPosition().x + planet.getRadius(),
                        planet.getPosition().y + planet.getRadius()
                ), planet.getRadius(), 5000);
            }
            explosion.update(dt);
        } else {
            //planet auto updates, but in this wave we can have big spaceblocks fall down to try and hurt the player...
            planet.getPosition().y = Math.max(yInit, planet.getPosition().y - 50 * dt);
            planet.setPosition(new Vector2((float) (planet.getPosition().x + Math.sin(totalTime * 2)), planet.getPosition().y));
        }
    }

}
