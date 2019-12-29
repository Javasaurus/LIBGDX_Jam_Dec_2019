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
package be.codecuisine.game.enemies.projectile;

import be.codecuisine.game.AudioManager;
import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collider;
import be.codecuisine.game.collision.ColliderPool;
import be.codecuisine.game.collision.Collision;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.game.collision.impl.CircleCollider;
import be.codecuisine.graphics.effects.Explosion;
import be.codecuisine.graphics.rendering.Renderable;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class BaseProjectile extends Renderable implements Collidable, GameBehavior {
    
    private boolean isDestroyed = false;
    
    private Vector2 position;
    private Vector2 direction;
    private float speed = 5f;
    private Collider collider;
    private float radius = 10;
    private int damage;
    
    public BaseProjectile(Vector2 position, Vector2 direction, float speed, int damage, float radius) {
        this.position = new Vector2(position);
        this.direction = new Vector2(direction);
        this.speed = speed;
        this.damage = damage;
        this.radius = radius;
        this.collider = new CircleCollider(this, position, radius);
        this.collider.setMyCollisionLayer(CollisionLayer.PROJECTILE);
        this.collider.getCollisionLayers().add(CollisionLayer.PLAYER);
//        this.collider.getCollisionLayers().add(CollisionLayer.PADDLE);
        RenderingPool.INSTANCE.add(this);
        ProjectilePool.INSTANCE.add(this);
        AudioManager.PlaySound(AudioManager.LASER);
    }
    
    @Override
    public void drawSprite(Batch batch) {
        //maybe later add rocket sprites
    }
    
    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.circle(collider.getPosition().x, collider.getPosition().y, radius);
    }
    
    @Override
    public Collider getCollider() {
        return this.collider;
    }
    
    @Override
    public void onCollisionEnter(Collision collision) {
        if ( collision.getOther(this).getCollider().getMyCollisionLayer() == CollisionLayer.PLAYER) {
            die(true);
        }
    }
    
    public void update(float t) {
        position = new Vector2(position).add(new Vector2(direction).scl(speed * t));
        if (position.x < -10 || position.x > Gdx.graphics.getWidth() + 10 || position.y < -10 || position.y > Gdx.graphics.getHeight() + 10) {
            die(false);
        }
        this.collider.getPosition().x = position.x;
        this.collider.getPosition().y = position.y;
        
    }
    
    public void die(boolean explode) {
        ColliderPool.INSTANCE.remove(this);
        RenderingPool.INSTANCE.remove(this);
        ProjectilePool.INSTANCE.remove(this);
        //do die effects
        if (explode) {
            Explosion explosion = new Explosion(position, false);
            explosion.setAutoDestroy(true);
            explosion.setScale(1);
            explosion.setPlayBackSpeed(1f);
        }
        isDestroyed = true;
    }
    
    @Override
    public Integer getZ() {
        return 500;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    @Override
    public Vector2 getPosition() {
        return position;
    }
    
    @Override
    public float getHP() {
        return -1;
    }
    
    @Override
    public boolean isBackground() {
        return false;
    }
}
