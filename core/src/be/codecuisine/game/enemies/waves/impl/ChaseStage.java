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
import be.codecuisine.game.enemies.ships.impl.BasicBombEnemy;
import be.codecuisine.game.enemies.ships.impl.BasicNeedleEnemy;
import be.codecuisine.game.enemies.waves.BaseEnemyWave;
import be.codecuisine.game.glutto.GluttoBody;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;

/**
 *
 * @author kenne
 */
public class ChaseStage extends BaseEnemyWave {

    private ArrayList<BaseEnemy> enemies = new ArrayList<>();
    private long waveStartTime;
    private final int amountNeedlers;
    private final int amountBombers;

    public ChaseStage(int waveNr, int amountNeedlers, int amountBombers) {
        super(waveNr);
        this.amountNeedlers = amountNeedlers;
        this.amountBombers = amountBombers;
    }

    @Override
    public void LaunchWave() {
        waveStartTime = TimeUtils.millis();
        Vector2 start = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 500);
        for (int i = 0; i < amountNeedlers; i++) {
            start.y += 50;
            BaseEnemy enemy = new BasicNeedleEnemy(
                    new Vector2(start.x, start.y + (i * 50)),
                    GameLogic.paddle,
                    1.5f,
                    1.5f);
            enemy.health = 1;
            enemy.maxHealth = enemy.health;
            RenderingPool.INSTANCE.add(enemy);
            enemies.add(enemy);
        }
        start = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 500);
        for (int i = 0; i < amountBombers; i++) {
            start.y += 50;
            BaseEnemy enemy = new BasicBombEnemy(
                    new Vector2(start.x, start.y + (i * 50)),
                    GameLogic.paddle,
                    2.5f,
                    2.5f);
            enemy.health = 3;
            enemy.maxHealth = enemy.health;
            RenderingPool.INSTANCE.add(enemy);
            enemies.add(enemy);
        }

    }

    @Override
    public void EndWave() {
        //
    }

    @Override
    public void update(float dt) {
        for (BaseEnemy enemy : enemies) {
            if (enemy != null && !enemy.isDestroyed) {
                enemy.update(dt);
            }
        }

    }

    @Override
    public boolean evaluateWave() {
        for (BaseEnemy enemy : enemies) {
            if (enemy != null && !enemy.isDestroyed) {
                return false;
            }
        }
        //here it means there are no more enemies known...
        return (TimeUtils.timeSinceMillis(waveStartTime) > 3000);

    }

}
