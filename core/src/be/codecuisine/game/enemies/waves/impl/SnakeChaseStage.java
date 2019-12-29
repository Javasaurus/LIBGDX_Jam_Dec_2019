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

import be.codecuisine.game.GameLogic;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.game.enemies.ships.impl.BasicSnakeShip;
import be.codecuisine.game.enemies.waves.BaseEnemyWave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;

/**
 *
 * @author kenne
 */
public class SnakeChaseStage extends BaseEnemyWave {

    private long waveStartTime;
    private ArrayList<BasicSnakeShip> enemies = new ArrayList<>();

    public SnakeChaseStage(int waveNr) {
        super(waveNr);
    }

    @Override
    public void LaunchWave() {
        waveStartTime = TimeUtils.millis();
        Vector2 start = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 500);
        //if there are more than 30 elements in a snake, divide them in 2 etc etc
        int sectionsCount = 4 + waveNr * 2;
        int parts = sectionsCount / 20;
        parts = Math.max(1, parts);
        int partsPerChain = sectionsCount / parts;
        for (int i = 0; i < parts; i++) {
            enemies.add(new BasicSnakeShip(start, GameLogic.paddle, partsPerChain, 2f, 2f, MathUtils.random(-1, 4)));
            enemies.get(i).health = Math.min(5, 5 + (waveNr / 5));
            for (BaseEnemy enemy : enemies.get(i).chain) {
                enemy.health = Math.min(1, 1 + (waveNr / 5));
                enemy.maxHealth = enemy.health;
            }

        }
    }

    @Override
    public void EndWave() {
        //
    }

    @Override
    public void update(float dt) {
        for (BasicSnakeShip enemy : enemies) {
            enemy.update(dt);
        }
    }

    @Override
    public boolean evaluateWave() {
        for (BasicSnakeShip enemy : enemies) {
            if (!enemy.isDestroyed) {
                return false;
            }
        }
        //here it means there are no more enemies known...
        return (TimeUtils.timeSinceMillis(waveStartTime) > 3000);

    }

}
