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
package be.codecuisine.screens.dialog;

import be.codecuisine.game.scores.HighScores;
import be.codecuisine.screens.GameScreen;
import com.badlogic.gdx.Input.TextInputListener;

/**
 *
 * @author kenne
 */
public class HighScoreDialog implements TextInputListener {

    public static boolean ACTIVE = false;

    private final GameScreen screen;
    private final long value;

    public HighScoreDialog(GameScreen screen, long value) {
        ACTIVE = true;
        this.value = value;
        this.screen = screen;
    }

    @Override
    public void input(String text) {
        new HighScores().saveHighScore(text, value);
        screen.GoBackToMenu();
    }

    @Override
    public void canceled() {
        screen.GoBackToMenu();
    }
}
