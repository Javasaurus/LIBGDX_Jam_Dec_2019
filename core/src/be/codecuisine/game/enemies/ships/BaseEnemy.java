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
package be.codecuisine.game.enemies.ships;

import be.codecuisine.game.AudioManager;
import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.ai.AIBehavior;
import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collider;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.game.collision.ColliderPool;
import be.codecuisine.game.collision.Collision;
import be.codecuisine.game.collision.impl.RectangleCollider;
import be.codecuisine.game.glutto.GluttoHead;
import be.codecuisine.game.glutto.GluttoStats;
import be.codecuisine.graphics.effects.Explosion;
import be.codecuisine.graphics.rendering.Renderable;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenne
 */
public abstract class BaseEnemy extends Renderable implements Collidable, GameBehavior {

    protected float totalTimeAlive = 0;

    public boolean isDestroyed = false;
    public int health = 5;
    public int maxHealth = 5;
    public float speed = 50;

    public Vector2 shipPosition;
    public Vector2 gridPosition;
    public Vector2 targetPosition;
    protected Vector2 direction;
    protected Collider collider;

    protected boolean lockedToGrid = true;

    protected float sizeX = 1;
    protected float sizeY = 1;

    protected float flash = 0;
    protected float flashDuration = .05f;

    protected List<AIBehavior> ai;

    public BaseEnemy(float sizeX, float sizeY) {
        this.direction = new Vector2(0, -1);
        this.shipPosition = new Vector2();
        this.collider = new RectangleCollider(this, shipPosition, 32 * sizeX, 32 * sizeY);
        this.collider.setMyCollisionLayer(CollisionLayer.ENEMY);
        this.collider.getCollisionLayers().add(CollisionLayer.PLAYER);
        this.collider.isEnabled = true;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.ai = new ArrayList<>();
    }

    public float getRotationAngle() {
        float rotationRad = direction.angleRad();
        return (rotationRad * MathUtils.radiansToDegrees) + 90;
    }

    public void update(float t) {
        if (!ai.isEmpty()) {
            for (AIBehavior ai : ai) {
                ai.update(t);
                this.shipPosition = ai.getPositionDirective();
                this.direction = ai.getDirectionDirective();
                ai.executeAction();
            }
        }
        flash -= t;
        totalTimeAlive += t;
        this.collider.getPosition().x = shipPosition.x;
        this.collider.getPosition().y = shipPosition.y;
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onCollisionEnter(Collision collision) {
        Collidable collidable = collision.getOther(this);
        Collider otherCollider = collidable.getCollider();
        if (otherCollider.getMyCollisionLayer() == CollisionLayer.PLAYER) {
            if (collidable instanceof GluttoHead) {
                //that means I got slapped by the tentacle...?
                if (!((GluttoHead) collidable).isDocked && flash <= 0) {
                    health--;
                    AudioManager.PlaySound(AudioManager.HIT);
                    flash = flashDuration;
                    if (health <= 0) {
                        //remove from enemy pool
                        Die();
                    }
                }
            }
        }

    }

    @Override
    public void drawShape(ShapeRenderer renderer) {
        /*  RectangleCollider col = ((RectangleCollider) collider);
        renderer.rect(shipPosition.x - col.getWidth() / 2,
                shipPosition.y - col.getHeight() / 2,
                col.getWidth() / 2,
                col.getHeight() / 2,
                col.getWidth(),
                col.getHeight(),
                1,
                1, getRotationAngle());*/
    }

    @Override
    public void drawSprite(Batch batch) {
        TextureRegion textureRegion = getTextureToDraw();
        if (textureRegion == null) {
            return;
        }

        RectangleCollider col = ((RectangleCollider) collider);

        textureRegion.getTexture().bind();

        batch.setColor(flash > 0 ? Color.YELLOW : getColor());

        /*  ShaderProgram shader = Shaders.SHIP_SHADER;
        shader.setUniformf("flash", MathUtils.random(-2, 1));
        shader.begin();
        batch.setShader(shader);*/
        batch.draw(textureRegion,
                shipPosition.x - col.getWidth() / 2,
                shipPosition.y - col.getHeight() / 2,
                col.getWidth() / 2,
                col.getHeight() / 2,
                col.getWidth(),
                col.getHeight(),
                1,
                1, getRotationAngle());

        batch.setShader(null);
        //    shader.end();

        batch.setColor(Color.WHITE);
    }

    protected abstract TextureRegion getTextureToDraw();

    public float getTotalTimeAlive() {
        return totalTimeAlive;
    }

    public void setTotalTimeAlive(float totalTimeAlive) {
        this.totalTimeAlive = totalTimeAlive;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Vector2 getPosition() {
        return shipPosition;
    }

    public void setPosition(Vector2 position) {
        this.shipPosition = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSizeX() {
        return sizeX;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public Vector2 getShipPosition() {
        return shipPosition;
    }

    public void setShipPosition(Vector2 shipPosition) {
        this.shipPosition = shipPosition;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public boolean isLockedToGrid() {
        return lockedToGrid;
    }

    public void setLockedToGrid(boolean lockedToGrid) {
        this.lockedToGrid = lockedToGrid;
    }

    public Vector2 getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Vector2 gridPosition) {
        this.gridPosition = gridPosition;
    }

    public void Die() {
        ColliderPool.INSTANCE.remove(this);
        RenderingPool.INSTANCE.remove(this);
        //do die effects
        Explosion explosion = new Explosion(shipPosition, false);
        explosion.setAutoDestroy(true);
        explosion.setScale(3);
        explosion.setPlayBackSpeed(1f);
        GluttoStats.GLUTTO_SCORE += GetPoints();
        isDestroyed = true;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getHP() {
        return -1;
    }

    @Override
    public boolean isBackground() {
        return false;
    }

    @Override
    public Integer getZ() {
        return 450;
    }

    public Color getColor() {
        if (health == maxHealth) {
            return Color.WHITE;
        }
        switch (health) {
            case 1:
                return Color.RED;
            case 2:
                return Color.PINK;
            default:
                return Color.WHITE;
        }
    }

    public abstract int GetPoints();

}
