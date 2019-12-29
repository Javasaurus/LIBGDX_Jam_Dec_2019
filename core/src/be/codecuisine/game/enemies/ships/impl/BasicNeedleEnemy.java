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
package be.codecuisine.game.enemies.ships.impl;

import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.ai.impl.ChasingPatroller;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.game.math.VectorUtil;
import be.codecuisine.graphics.effects.Explosion;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class BasicNeedleEnemy extends BaseEnemy {

    private static final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/ships/enemy_03.png"));
    private static final TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight());
    private final Animation<TextureRegion> animation;

    private GameBehavior target;
    private float playBackSpeed = .6f;
    private final ChasingPatroller chasingPatroller;
    private float arrivalRange;

    public BasicNeedleEnemy(Vector2 position, GameBehavior target, float sizeX, float sizeY) {
        super(sizeX, sizeY);
        this.arrivalRange = Math.max(sizeX * tmp[0][0].getRegionWidth(), sizeY * tmp[0][0].getRegionHeight());
        this.shipPosition = position;
        this.target = target;
        RenderingPool.INSTANCE.add(this);
        TextureRegion[] frames = new TextureRegion[tmp.length * tmp[0].length];
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);
        
        chasingPatroller = new ChasingPatroller(this, target, arrivalRange);
        this.ai.add(chasingPatroller);

    }

    @Override
    public void Die() {
        super.Die();
        for (int i = 0; i < 7; i++) {
            Vector2 RandomVector = VectorUtil.RandomVector(getPosition(), tmp[0][0].getRegionWidth() * sizeX);
            Explosion explosion = new Explosion(RandomVector, false);
            explosion.setAutoDestroy(true);
            explosion.setScale(MathUtils.random(1, 3));
            explosion.setPlayBackSpeed(1f);
        }
    }

    @Override
    public void update(float t) {
        super.update(t);
    }

    @Override
    public TextureRegion getTextureToDraw() {
        if (isDestroyed) {
            return null;
        }
        return (TextureRegion) animation.getKeyFrame(totalTimeAlive * playBackSpeed, true);
    }

    public GameBehavior getTarget() {
        return target;
    }

    public void setTarget(GameBehavior target) {
        this.target = target;
    }

    public float getArrivalRange() {
        return arrivalRange;
    }

    public void setArrivalRange(float arrivalRange) {
        this.arrivalRange = arrivalRange;
    }

    @Override
    public int GetPoints() {
        return 500;
    }

}
