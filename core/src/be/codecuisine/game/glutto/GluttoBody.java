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

import be.codecuisine.Constants;
import be.codecuisine.game.AudioManager;
import be.codecuisine.game.GameBehavior;
import static be.codecuisine.game.GameLogic.totalTime;
import be.codecuisine.game.Input;
import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collider;
import be.codecuisine.game.collision.Collision;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.game.collision.impl.CircleCollider;
import be.codecuisine.game.collision.impl.RectangleCollider;
import be.codecuisine.game.enemies.projectile.BaseProjectile;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.graphics.rendering.Renderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class GluttoBody extends Renderable implements Collidable, GameBehavior {

    private static final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/Glutto/Glutto_Blinking.png"));
    private static final TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 5, spriteSheet.getHeight());
    private final Animation<TextureRegion> animation;

    private float spriteScale = 4;

    private float height = 10f;
    private float width;
    public Vector2 position;
    private float speed;
    private Collider collider;

    private int movementDirection;

    public GluttoBody() {
        this.height = 20f;
        this.width = 120f;
        this.position = new Vector2(Gdx.graphics.getWidth() / 2, 50f);
        this.speed = 1000f;
        this.collider = new CircleCollider(this, position, 50f);
        this.collider.setMyCollisionLayer(CollisionLayer.PLAYER);
        // this.collider.getCollisionLayers().add(CollisionLayer.PLAYER);
        this.collider.getCollisionLayers().add(CollisionLayer.PLANET);
        this.collider.getCollisionLayers().add(CollisionLayer.PROJECTILE);
        this.collider.getCollisionLayers().add(CollisionLayer.ENEMY);
        this.collider.isEnabled = true;
        this.collider.setPosition(new Vector2(position));

        int openEyedFrames = 50;
        TextureRegion[] frames = new TextureRegion[openEyedFrames + (tmp.length * tmp[0].length)];
        int index = 0;
        for (int i = 0; i < openEyedFrames; i++) {
            frames[index++] = tmp[0][0];
        }

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);

    }

    @Override
    public void drawShape(ShapeRenderer renderer) {
        //    renderer.setColor(Color.CYAN);
        //    renderer.circle(this.collider.getPosition().x, this.collider.getPosition().y, ((CircleCollider) this.collider).getRadius());
    }

    @Override
    public void drawSprite(Batch batch) {
        if (GluttoStats.GLUTTO_HEALTH() > 0) {
            TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(totalTime, true);
            batch.draw(currentFrame,
                    this.collider.getPosition().x - currentFrame.getRegionWidth() * spriteScale / 2,
                    this.collider.getPosition().y + 10f + -currentFrame.getRegionHeight() * spriteScale / 2,
                    currentFrame.getRegionWidth() * spriteScale,
                    currentFrame.getRegionHeight() * spriteScale
            );
        }
    }

    public Vector2 getCenterPosition(float yOffset) {
        return new Vector2(this.collider.getPosition().x, this.collider.getPosition().y + yOffset);
    }

    public void update(float dt) {

        CircleCollider rect = (CircleCollider) collider;
        movementDirection = 0;
        //keyInput
        if (Input.IsKeyDown(Constants.KEY_LEFT)) {
            if (this.position.x > 0) {
                this.position.x -= speed * dt;
                movementDirection = -1;
            }
        }

        if (Input.IsKeyDown(Constants.KEY_RIGHT)) {
            if (this.position.x < Gdx.graphics.getWidth() - rect.getRadius()) {
                this.position.x += speed * dt;
                movementDirection = 1;
            }
        }

        if (Input.IsKeyDown(Constants.KEY_DOWN)) {
            if (this.position.y > 0) {
                this.position.y -= speed * dt;
            }
        }
        if (Input.IsKeyDown(Constants.KEY_UP)) {
            if (this.position.y < Gdx.graphics.getHeight() - rect.getRadius()) {
                this.position.y += speed * dt;
            }
        }

        this.position = new Vector2(Input.GetMousePosition()).sub(new Vector2(getRadius() / 2, 0));

        this.collider.getPosition().x = position.x;
        this.collider.getPosition().y = position.y;
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onCollisionEnter(Collision collision) {
        if (collision.getOther(this).getCollider().getMyCollisionLayer() == CollisionLayer.PROJECTILE) {
            BaseProjectile proj = (BaseProjectile) collision.getOther(this);
            GluttoStats.DMG_GLUTTO(proj.getDamage());
            proj.die(true);
        }

        if (collision.getOther(this).getCollider().getMyCollisionLayer() == CollisionLayer.ENEMY) {
            BaseEnemy proj = (BaseEnemy) collision.getOther(this);
            GluttoStats.DMG_GLUTTO(3);
            proj.Die();
        }

    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public int getMovementDirection() {
        return movementDirection;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getHP() {
        return -1;
    }

    @Override
    public boolean isBackground() {
        return false;
    }

    public float getRadius() {
        return ((CircleCollider) collider).getRadius();
    }

    @Override
    public Integer getZ() {
        return 201;
    }

}
