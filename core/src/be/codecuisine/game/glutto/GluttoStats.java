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
package be.codecuisine.game.glutto;

import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author kenne
 */
public class GluttoStats {

    private static int GLUTTO_HEALTH = 1;
    private static int GLUTTO_MAX_HEALTH = 10;
    public static int GLUTTO_SCORE = 0;

    private static long TimeSinceLastHit;

    public static int GLUTTO_HEALTH() {
        return GLUTTO_HEALTH;
    }

    public static void RESET_GLUTTO_HEALTH() {
        GLUTTO_HEALTH = GLUTTO_MAX_HEALTH;
    }

    public static int GLUTTO_MAX_HEALTH() {
        return GLUTTO_MAX_HEALTH;
    }

    public static void DMG_GLUTTO(int amount) {
        if (TimeUtils.timeSinceMillis(TimeSinceLastHit) > .5f) {
            GLUTTO_HEALTH -= amount;
            TimeSinceLastHit = TimeUtils.millis();
        }
        if (GLUTTO_HEALTH < 0) {
            GLUTTO_HEALTH = 0;
        }
        if (GLUTTO_HEALTH > GLUTTO_MAX_HEALTH) {
            GLUTTO_HEALTH = GLUTTO_MAX_HEALTH;
        }
    }

    public static float GLUTTO_RATIO() {
        return (float) (GLUTTO_HEALTH) / GLUTTO_MAX_HEALTH;
    }

}
