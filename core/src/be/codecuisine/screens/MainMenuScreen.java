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
package be.codecuisine.screens;

import be.codecuisine.Constants;
import be.codecuisine.GameStateManager;
import be.codecuisine.GluttoGame;
import be.codecuisine.game.scores.HighScore;
import be.codecuisine.game.scores.HighScores;
import be.codecuisine.graphics.rendering.UILine;
import be.codecuisine.graphics.rendering.UIRendering;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author kenne
 */
public class MainMenuScreen implements Screen {

    private static Music menuMusic;
    private final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/Glutto/Glutto_Blinking.png"));
    private final Texture logo = new Texture(Gdx.files.internal("sprites/logo.png"));
    private final TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 5, spriteSheet.getHeight());
    private final Animation<TextureRegion> animation;
    private float totalTime;
    private float textFlashTimer;
    private float textFlashDuration = 2000;
    private float angleX;
    private float angleY = 90;

    private final OrthographicCamera camera;
    private final GluttoGame main;
    private final HighScores scores;

    public MainMenuScreen(GluttoGame main) {
        scores = new HighScores();
        GameStateManager.SetGameState(GameStateManager.GameState.MAIN_MENU);
        this.main = main;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        int openEyedFrames = 100;
        TextureRegion[] frames = new TextureRegion[openEyedFrames + (tmp.length * tmp[0].length)];
        int index = 0;
        for (int i = 0; i < openEyedFrames; i++) {
            frames[index++] = tmp[0][0];
        }

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Core Descent v0_9.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();

        totalTime = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        totalTime += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        UIRendering.INSTANCE.batch.setProjectionMatrix(camera.combined);

        UIRendering.INSTANCE.batch.begin();
        angleX += delta * 2;
        angleY += delta * 2.5;
        float spriteScale = 10;
        TextureRegion currentFrame = animation.getKeyFrame(totalTime, true);

        Vector2 drawPosition = new Vector2(
                (Gdx.graphics.getWidth() / 2) - 1.3f * (currentFrame.getRegionWidth() * spriteScale),
                (Gdx.graphics.getHeight() / 2) - (currentFrame.getRegionHeight() * spriteScale)
        );

        drawPosition.y += MathUtils.sin(angleY) * 50f;
        drawPosition.x += MathUtils.sin(angleX) * 25f;

        UIRendering.INSTANCE.batch.draw(logo, 30, 350, logo.getWidth() / 2, logo.getHeight() / 2);

        UIRendering.INSTANCE.batch.draw(currentFrame,
                drawPosition.x, drawPosition.y,
                currentFrame.getRegionWidth() * spriteScale,
                currentFrame.getRegionHeight() * spriteScale
        );
        UIRendering.INSTANCE.batch.end();

        textFlashTimer -= (delta * 1000);
        ArrayList<UILine> lines = new ArrayList<>();

        if (textFlashTimer > 1000 && totalTime > 1) {
            lines.add(new UILine("Click anywhere to start ...", 450, 100));
        }
        if (textFlashTimer <= 0) {
            textFlashTimer = textFlashDuration;
        }

        UIRendering.renderLines(lines, 1);
        lines.clear();

        lines.add(new UILine("Highscores", 25, 325));
        int index = 1;
        for (HighScore score : scores.getScores()) {
            lines.add(new UILine(index + ".", 25, 300 - (index * 25)));
            lines.add(new UILine(score.getName(), 75, 300 - (index * 25)));
            lines.add(new UILine(Constants.formatScore(score.getValue()), 250, 300 - (index * 25)));
            index++;
        }

        UIRendering.renderLines(lines, 1);

        if (Gdx.input.isTouched() && totalTime > 3) {
            menuMusic.stop();
            main.setScreen(new GameScreen(main));
            //   dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
//        main.resize(width, height);
    }

    @Override
    public void pause() {
        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        spriteSheet.dispose();
    }

}
