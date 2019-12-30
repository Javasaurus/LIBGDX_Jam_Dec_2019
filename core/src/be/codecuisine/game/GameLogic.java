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
package be.codecuisine.game;

import be.codecuisine.GameStateManager;
import be.codecuisine.game.collision.ColliderPool;
import be.codecuisine.game.glutto.GluttoBody;
import be.codecuisine.game.enemies.grid.EnemyGrid;
import be.codecuisine.game.enemies.projectile.ProjectilePool;
import be.codecuisine.game.enemies.waves.BaseEnemyWave;
import be.codecuisine.game.enemies.waves.impl.ChaseStage;
import be.codecuisine.game.enemies.waves.impl.GridStage;
import be.codecuisine.game.enemies.waves.impl.PlanetStage;
import be.codecuisine.game.enemies.waves.impl.SnakeChaseStage;
import be.codecuisine.game.glutto.GluttoHead;
import be.codecuisine.game.glutto.GluttoStats;
import be.codecuisine.graphics.effects.Explosion;
import be.codecuisine.graphics.planet.PlanetVisuals;
import be.codecuisine.graphics.rendering.RenderingPool;
import be.codecuisine.game.math.VectorUtil;
import be.codecuisine.graphics.rendering.UILine;
import be.codecuisine.graphics.rendering.UIRendering;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenne
 */
public class GameLogic implements Disposable {

    public static Music waveMusic;

    public static float totalTime = 0;
    public static int currentWaveNr = 0;

    private final PlanetVisuals spaceLevel;

    public static GluttoBody paddle;
    public final GluttoHead glutto;

    private final RenderingPool renderingPool;

    private BaseEnemyWave currentWave;
    private String waveText = "";
    private long currentWaveMessageTimer;

    public GameLogic() {
        renderingPool = RenderingPool.INSTANCE;

        spaceLevel = PlanetVisuals.INSTANCE;
        paddle = new GluttoBody();
        glutto = new GluttoHead(paddle);

        renderingPool.add(spaceLevel);
        renderingPool.add(paddle);
        renderingPool.add(glutto);

        glutto.position = paddle.getCenterPosition((float) (2.5f * glutto.getRadius()));

        currentWave = new GridStage(-1);
        currentWave.LaunchWave();

        waveMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Grey Sector v0_85.mp3"));
        waveMusic.setLooping(true);
        waveMusic.play();

    }

    public void playDeathMusic() {
        waveMusic.stop();
        waveMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/sailor_waltz_with_water_effects_c64_style.ogg"));
        waveMusic.setLooping(true);
        waveMusic.play();
    }

    public void update(float t) {
        totalTime += t;
        updatePlanet(t);
        updateGlutto(t);

        ProjectilePool.INSTANCE.updateProjectiles(t);
        ColliderPool.INSTANCE.updateCollisions();

        currentWave.update(t);
        if (currentWave.evaluateWave()) {
            currentWave.EndWave();
            //WAVE FINISHED
            currentWaveNr++;
            currentWave = GetNextWave(currentWaveNr);
            currentWave.LaunchWave();
            currentWaveMessageTimer = 120;
        }

        currentWaveMessageTimer -= t;
        if (currentWaveMessageTimer > 0) {
            //       System.out.println(currentWaveMessageTimer);
            List<UILine> lines = new ArrayList<UILine>();
            lines.add(new UILine("Wave " + currentWaveNr, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));
            if (!waveText.isEmpty()) {
                lines.add(new UILine(waveText, Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() / 2) - 50f));
            }
            UIRendering.renderLines(lines, 1.5f);
        }

        if (GluttoStats.GLUTTO_HEALTH() <= 0) {
            AudioManager.PlaySound(AudioManager.DEATH);
            ExplodeGlutto();
            playDeathMusic();
            GameStateManager.SetGameState(GameStateManager.GameState.DEAD);
        }

    }

    private void ExplodeGlutto() {

        for (int i = 0; i < 7; i++) {
            Vector2 RandomVector = VectorUtil.RandomVector(paddle.getCenterPosition(0), paddle.getRadius());
            Explosion explosion = new Explosion(RandomVector, false);
            explosion.setAutoDestroy(true);
            explosion.setScale(MathUtils.random(1, 3));
            explosion.setPlayBackSpeed(1f);

        }
    }

    private void updatePlanet(float delta) {
        spaceLevel.update(delta);
    }

    private void updateGlutto(float delta) {
        glutto.update(delta);
        paddle.update(delta);
    }

    @Override
    public void dispose() {
        spaceLevel.dispose();
        ColliderPool.INSTANCE.clear();
        RenderingPool.INSTANCE.clear();
        ProjectilePool.INSTANCE.clear();
    }

    private BaseEnemyWave GetNextWave(int currentWaveNr) {
        waveText = "";

        //every 5 stages we allow the player to rest up
        if (currentWaveNr % 5 == 0) {
            waveText = "Devour the planet to regain health !";
            return new PlanetStage(currentWaveNr);
        }

        if (currentWaveNr % 5 == 1) {
            return new GridStage(currentWaveNr);
        }

        if (currentWaveNr % 5 == 2) {
            if (currentWaveNr < 15) {
                if (MathUtils.randomBoolean()) {
                    waveText = "!!! Needlers incoming !!!";
                    return new ChaseStage(currentWaveNr, 5 + (currentWaveNr / 3) + currentWaveNr % 3, 0);
                } else {
                    waveText = "!!! Glutto, The Kamikaze's incoming !!!";
                    return new ChaseStage(currentWaveNr, 0, Math.min(8, 1 + (currentWaveNr / 5)));
                }
            } else {
                waveText = "!!! Break out the dodging skills, Glutto !!!";
                return new ChaseStage(currentWaveNr, 5 + (currentWaveNr / 3) + currentWaveNr % 3, Math.min(8, 1 + (currentWaveNr / 5)));
            }
        }

        if (currentWaveNr % 5 == 3) {
            return new GridStage(currentWaveNr);
        }

        if (currentWaveNr % 5 == 4) {
            waveText = "!!! You're on your own,  Glutto  !!!";
            return new SnakeChaseStage(currentWaveNr);
        }
        return new GridStage(currentWaveNr);
    }
}
