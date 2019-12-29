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
package be.codecuisine.graphics.effects;

import be.codecuisine.game.AudioManager;
import be.codecuisine.graphics.rendering.Renderable;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author kenne
 */
public class Explosion extends Renderable implements Disposable {

    private static final Texture explosionSheet = new Texture(Gdx.files.internal("sprites/explosion/explosion.png"));
    private static final TextureRegion[][] tmp = TextureRegion.split(explosionSheet, explosionSheet.getWidth() / 3, explosionSheet.getHeight() / 3);
    private Animation animation;
    private Vector2 position;

    public boolean isDestroyed = false;
    private float playBackSpeed = .01f;
    private float totalTime = 0;
    private boolean autoDestroy = true;
    private float scale = 1;
    public boolean play=true;

    private boolean playedSound = false;

    public Explosion(Vector2 position) {
        this(position, false);
    }

    public Explosion(Vector2 position, boolean isLooping) {
        initFrames();
        if (isLooping) {
            animation.setPlayMode(Animation.PlayMode.LOOP);
        } else {
            animation.setPlayMode(Animation.PlayMode.NORMAL);
        }
        this.position = position;
    }

    private void initFrames() {
        RenderingPool.INSTANCE.add(this);
        TextureRegion[] frames = new TextureRegion[tmp.length * tmp[0].length];
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);

    }

    @Override
    public void drawSprite(Batch batch) {
        if (!play || isDestroyed) {
            return;
        }

        if (!playedSound) {
            playedSound = false;
            AudioManager.PlaySound(AudioManager.EXPLODE);
        }

        totalTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(totalTime * playBackSpeed, true);
        batch.draw(currentFrame,
                position.x - (currentFrame.getRegionWidth() * scale) / 2,
                position.y - (currentFrame.getRegionHeight() * scale) / 2,
                currentFrame.getRegionWidth() * scale,
                currentFrame.getRegionHeight() * scale
        );
        if (autoDestroy && totalTime >= animation.getAnimationDuration() / playBackSpeed) {
            isDestroyed = true;
            this.dispose();
        }
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        RenderingPool.INSTANCE.remove(this);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getPlayBackSpeed() {
        return playBackSpeed;
    }

    public void setPlayBackSpeed(float playBackSpeed) {
        this.playBackSpeed = playBackSpeed;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isAutoDestroy() {
        return autoDestroy;
    }

    public void setAutoDestroy(boolean autoDestroy) {
        this.autoDestroy = autoDestroy;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public boolean isBackground() {
        return true;
    }

    @Override
    public Integer getZ() {
        return 999;
    }

}
