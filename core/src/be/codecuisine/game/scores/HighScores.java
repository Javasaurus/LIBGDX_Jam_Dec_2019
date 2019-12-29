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
package be.codecuisine.game.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author kenne
 */
public class HighScores {

    private Preferences prefs = Gdx.app.getPreferences("HighScores");

    private ArrayList<HighScore> scores = new ArrayList<>();

    public HighScores() {
        loadScore();
    }

    private void loadScore() {
        for (int i = 0; i < 10; i++) {
            HighScore mockScore = getMockScore(i);
            long scoreValue = prefs.getLong("player_s" + i, mockScore.getValue());
            String scoreName = prefs.getString("player_n" + i, mockScore.getName());
            HighScore score = new HighScore(scoreName, scoreValue);
            scores.add(score);
        }
        Collections.sort(scores);

    }

    public ArrayList<HighScore> getScores() {
        return scores;
    }

    public boolean isHighScore(long score) {
        return score > scores.get(scores.size() - 1).getValue();
    }

    public void saveHighScore(String name, long value) {
        scores.add(new HighScore(name, value));
        Collections.sort(scores);
        save();
    }

    private void save() {
        for (int i = 0; i < scores.size(); i++) {
            prefs.putLong("player_s" + i, scores.get(i).getValue());
            prefs.putString("player_n" + i, scores.get(i).getName());
        }
        prefs.flush();
    }

    private HighScore getMockScore(int i) {
        switch (i) {
            case 0:
                return new HighScore("Dr. JavaSaurus", i*1000);
            case 1:
                return new HighScore("Chuck Norris", i*1000);
            case 2:
                return new HighScore("Santaclaus", i*1000);
            case 3:
                return new HighScore("Lara Croft",i*1000);
            case 4:
                return new HighScore("Notch", i*1000);
            case 5:
                return new HighScore("Forky", i*1000);
            case 6:
                return new HighScore("Captain Kirk", i*1000);
            case 7:
                return new HighScore("Glutto", i*1000);
            case 8:
                return new HighScore("Hulk Hogan", i*1000);
            case 9:
                return new HighScore("BananaPoopyPants", i*1000);
        }
        return new HighScore("Nobody", 0);
    }

}
