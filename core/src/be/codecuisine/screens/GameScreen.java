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
import be.codecuisine.game.GameLogic;
import be.codecuisine.game.Input;
import be.codecuisine.game.glutto.GluttoStats;
import be.codecuisine.game.scores.HighScores;
import be.codecuisine.graphics.rendering.RenderingPool;
import be.codecuisine.graphics.rendering.UILine;
import be.codecuisine.graphics.rendering.UIRendering;
import be.codecuisine.screens.dialog.HighScoreDialog;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author kenne
 */
public class GameScreen implements Screen {

    private GameLogic gameLogic;
    private OrthographicCamera screenCamera;
    private final GluttoGame main;
    private boolean resetToMenu;
    private final HighScores scores;

    public GameScreen(GluttoGame main) {
        GameStateManager.SetGameState(GameStateManager.GameState.PLAYING);
        this.main = main;

        gameLogic = new GameLogic();

        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screenCamera.update();

        Gdx.input.setInputProcessor(new Input());

        GluttoStats.RESET_GLUTTO_HEALTH();
        GluttoStats.GLUTTO_SCORE = 0;

        scores = new HighScores();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (resetToMenu) {
            GameLogic.waveMusic.stop();
            main.setScreen(new MainMenuScreen(main));
            dispose();
        }

        //###RENDERING
        RenderingPool.INSTANCE.render(screenCamera);
        UIRendering.INSTANCE.batch.setProjectionMatrix(screenCamera.combined);

        //###UPDATING
        switch (GameStateManager.CURRENT_STATE()) {
            case PLAYING:
                GameLogic.waveMusic.play();
                Gdx.input.setCursorCatched(true);
                gameLogic.update(delta);
                if (Input.IsKeyDown(Keys.ESCAPE) && GameStateManager.CanChangeState()) {
                    Input.StoreMousePosition();
                    GameStateManager.SetGameState(GameStateManager.GameState.PAUSED);
                }
                break;
            case PAUSED:
                GameLogic.waveMusic.pause();
                Gdx.input.setCursorCatched(false);
                if (Input.IsKeyDown(Keys.ESCAPE) && GameStateManager.CanChangeState()) {
                    Input.ReStoreMousePosition();
                    gameLogic.paddle.position = new Vector2(Input.STORED_MOUSE_POSITION);
                    GameStateManager.SetGameState(GameStateManager.GameState.PLAYING);
                }
                ArrayList<UILine> pauseLines = new ArrayList<>();
                pauseLines.add(new UILine("~PAUSED ~", (Gdx.graphics.getWidth() / 2) - 50, Gdx.graphics.getHeight() / 2));

                UIRendering.renderLines(pauseLines, 2);
                break;

            case DEAD:

                Gdx.input.setCursorCatched(false);

                if (scores.isHighScore(GluttoStats.GLUTTO_SCORE) && !HighScoreDialog.ACTIVE) {
                    HighScoreDialog listener = new HighScoreDialog(this, GluttoStats.GLUTTO_SCORE);
                    Gdx.input.getTextInput(listener, "Highscore Achieved : " + GluttoStats.GLUTTO_SCORE, "Your Name", "");
                } else if (!HighScoreDialog.ACTIVE) {
                    ArrayList<UILine> deadLines = new ArrayList<>();
                    deadLines.add(new UILine("Game Over", (Gdx.graphics.getWidth() / 2) - 50, Gdx.graphics.getHeight() / 2));
                    deadLines.add(new UILine("Click anywhere to restart...", (Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - 50f));

                    if (Gdx.input.isTouched()) {
                        GoBackToMenu();
                    }

                    UIRendering.renderLines(deadLines, 2);
                    break;
                }
        }

        renderUI();

    }

    public void GoBackToMenu() {
        resetToMenu = true;
    }

    private void renderUI() {
        UIRendering.renderRectangle(true, Color.GREEN, 50, Gdx.graphics.getHeight() - 50, (int) (Math.max(0, GluttoStats.GLUTTO_RATIO()) * 100), 20);
        ArrayList<UILine> UILines = new ArrayList<>();
        UILines.add(new UILine(Constants.formatScore(GluttoStats.GLUTTO_SCORE), 50, Gdx.graphics.getHeight() - 75));
        UIRendering.renderLines(UILines, 1.1f);
    }

    @Override
    public void resize(int width, int height) {
//        main.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        gameLogic.dispose();
    }

}
