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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author kenne
 */
public class AudioManager {

    public static AudioManager INSTANCE = new AudioManager();

    public static int DEATH = 0;
    public static int EXPLODE = 1;
    public static int LASER = 2;
    public static int SUCK = 3;
    public static int HIT = 4;

    private static final Sound DEATH_S = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Death.wav"));
    private static final Sound EXPLODE_S = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Explosion.wav"));
    private static final Sound LASER_S = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Laser.wav"));
    private static final Sound SUCKING_S = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Sucking.wav"));
    private static final Sound HIT_S = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Hit.wav"));

    private AudioManager() {

    }

    public static void PlaySound(int sound) {
        switch (sound) {
            case 0:
                INSTANCE.PlayRandomPitch(DEATH_S);
                break;
            case 1:
                INSTANCE.PlayRandomPitch(EXPLODE_S);
                break;
            case 2:
                INSTANCE.PlayRandomPitch(LASER_S);
                break;
            case 3:
                INSTANCE.PlayRandomPitch(SUCKING_S);
                break;
            case 4:
                INSTANCE.PlayRandomPitch(HIT_S);
                break;
        }
    }

    public void PlayRandomPitch(Sound sound) {
        sound.setPitch(sound.play(1.0f), MathUtils.random(.5f, 2f));
    }

}
