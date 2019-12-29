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
package be.codecuisine.game.enemies.waves;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kenne
 */
public class GridWaveFactory {
    
    private static final int[][] LEVEL_INIT = new int[1][1];
    
    private static int[][] LEVEL_R(int width, int height) {
        int[][] randomLevel = new int[width][height];
        for (int i = 0; i < randomLevel.length; i++) {
            for (int j = 0; j < randomLevel[0].length; j++) {
                randomLevel[i][j] = MathUtils.random(0, 1);
            }
        }
        return randomLevel;
    }
    
    public static int[][] GenerateLevel(int waveNr) {
        if (waveNr < 0) {
            return LEVEL_INIT;
        }
        
        if (waveNr < 10) {
            return GenerateHeartShape(1);
        }
        
        if (waveNr < 20) {
            return GenerateHeartShape(2);
        }
        
        return GenerateHeartShape(3);
        
    }
    
    private static int[][] GenerateHeartShape(int waveNr) {
        final int N = waveNr;
        HashMap<Integer, ArrayList<Integer>> tmpMap = new HashMap<>();
        int row = 0;
        for (int i = 0; i < N; i++) {
            ArrayList<Integer> rowValues = new ArrayList<>();
            for (int j = 0; j <= 4 * N; j++) {
                double d1 = Math.sqrt(Math.pow(i - N, 2) + Math.pow(j - N, 2));
                double d2 = Math.sqrt(Math.pow(i - N, 2) + Math.pow(j - 3 * N, 2));
                
                if (d1 < N + 0.5 || d2 < N + 0.5) {
                    rowValues.add(1);
                } else {
                    rowValues.add(0);
                }
            }
            tmpMap.put(row, rowValues);
            row++;
        }
        
        for (int i = 1; i < 2 * N; i++) {
            ArrayList<Integer> rowValues = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                rowValues.add(0);
            }
            
            for (int j = 0; j < 4 * N + 1 - 2 * i; j++) {
                rowValues.add(1);
            }
            
            tmpMap.put(row, rowValues);
            //       System.out.println("Row is "+rowValues.size());
            row++;
        }
        
        int[][] grid = new int[tmpMap.size()][tmpMap.get(0).size()];
        for (int i = 0; i < tmpMap.size(); i++) {
            for (int j = 0; j < tmpMap.get(i).size(); j++) {
                grid[i][j] = tmpMap.get(i).get(j);
                //            System.out.print(grid[i][j]);
            }
            //         System.out.println();
        }
        
        return grid;
    }
    
}
