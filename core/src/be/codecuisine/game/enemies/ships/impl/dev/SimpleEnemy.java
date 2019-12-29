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
package be.codecuisine.game.enemies.ships.impl.dev;

import be.codecuisine.game.ai.impl.SimplePatroller;
import be.codecuisine.game.enemies.projectile.BaseProjectile;
import be.codecuisine.game.enemies.ships.BaseEnemy;
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
public class SimpleEnemy extends BaseEnemy {

    private static final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/ships/enemy_01.png"));
    private static final TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight());
    private final Animation<TextureRegion> animation;

    private float playBackSpeed = .6f;

    public SimpleEnemy(Vector2 position, float sizeX, float sizeY) {
        super(sizeX, sizeY);
        this.shipPosition = position;
        RenderingPool.INSTANCE.add(this);
        TextureRegion[] frames = new TextureRegion[tmp.length * tmp[0].length];
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);

        this.ai.add(new SimplePatroller(this));

    }

    @Override
    public void update(float t) {
        super.update(t);
        if (!isDestroyed && isLockedToGrid() && MathUtils.randomBoolean(.001f)) {
            BaseProjectile projectile = new BaseProjectile(new Vector2(getPosition()), new Vector2(0, -1), speed, 1, 4);
        }
    }

    @Override
    public TextureRegion getTextureToDraw() {

        if (isDestroyed) {
            return null;
        }
        return (TextureRegion) animation.getKeyFrame(totalTimeAlive * playBackSpeed, true);
    }

    @Override
    public int GetPoints() {
        return 25;
    }

}
