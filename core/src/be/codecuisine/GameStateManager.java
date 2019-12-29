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
package be.codecuisine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author kenne
 */
public class GameStateManager {

    public static GluttoGame INSTANCE;
    private static GameState CURRENT_STATE;
    public static long TIME_STATE_CHANGE;

    public enum GameState {
        MAIN_MENU, PAUSED, PLAYING, DEAD
    }

    public GameStateManager(GluttoGame main) {
        if (INSTANCE != null) {
            Gdx.app.error("Ambiguous GSM", "There already exists a game state manager...");
        }
        INSTANCE = main;
    }

    public static GameState CURRENT_STATE() {
        return CURRENT_STATE;
    }

    public static void SetGameState(GameState newState) {
        CURRENT_STATE = newState;
        TIME_STATE_CHANGE = TimeUtils.millis();;
    }

    public static boolean CanChangeState() {
        long timeSinceMillis = TimeUtils.timeSinceMillis((long) TIME_STATE_CHANGE);
        return (timeSinceMillis > 350);
    }

}
