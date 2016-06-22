package gorlorn;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import gorlorn.Entities.Enemy;
import gorlorn.Entities.Entity;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * Created by Rob on 1/14/2016.
 */
public class EnemyManager
{
    private GorlornActivity _gorlornActivity;
    private LinkedList<Enemy> _enemies;
    private Bitmap _enemySprite;
    private long _timeLastEnemySpawnedMs;
    private long _enemySpawnIntervalMs = Constants.StartingEnemySpawnIntervalMs;
    private float _enemySpeed;

    public EnemyManager(GorlornActivity gorlornActivity)
    {
        _gorlornActivity = gorlornActivity;
        _enemySpeed = Constants.EnemySpeed;
        _enemies = new LinkedList<>();
        _enemySprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.enemy, Constants.EnemyDiameter);
    }

    /**
     * Kill the first enemy hit with the given bullet. Returns the enemy that was hit, or null if
     * no enemies were hit.
     *
     * @param bullet
     * @return
     */
    public Enemy TryKillEnemy(Entity bullet)
    {
        for (Enemy enemy : _enemies)
        {
            if (enemy.testHit(bullet))
            {
                _enemies.remove(enemy);
                return enemy;
            }
        }
        return null;
    }

    /**
     * Draws the BulletManager
     *
     * @param canvas The canvas upon which to draw the BulletManager
     */
    public void draw(Canvas canvas)
    {
        for (Enemy enemy : _enemies)
        {
            enemy.draw(canvas);
        }
    }

    /**
     * Updates the BulletManager.
     *
     * @param dt The time since the last update
     */
    public void update(float dt)
    {
        long now = new Date().getTime();
        if (now - _timeLastEnemySpawnedMs > _enemySpawnIntervalMs)
        {
            _timeLastEnemySpawnedMs = now;
            SpawnEnemy();
        }

        LinkedList<Enemy> deadEnemies = new LinkedList<>();
        for (Enemy enemy : _enemies)
        {
            if (enemy.update(dt))
            {
                deadEnemies.add(enemy);
            }
        }

        for (Enemy deadEnemy : deadEnemies)
        {
            _enemies.remove(deadEnemy);
        }
    }

    /**
     * Spawns an enemey off the top of the screen at a random X location with a random downward trajectory
     */
    private void SpawnEnemy()
    {
        Random random = new Random();

        double angle = GetEnemyAngle(random);
        float speed = ((_gorlornActivity.ScreenWidth + _gorlornActivity.ScreenHeight) / 2.0f) * _enemySpeed;
        float minX = _gorlornActivity.GameArea.left + _gorlornActivity.GameArea.width() * 0.2f;

        Enemy newEnemy = new Enemy(_gorlornActivity, _enemySprite);
        newEnemy.X = minX + random.nextFloat() * _gorlornActivity.GameArea.width() * 0.6f;
        newEnemy.Y = _gorlornActivity.getYFromPercent(0.025f);
        newEnemy.Vx = speed * (float) Math.cos(angle);
        newEnemy.Vy = speed * (float) Math.sin(angle);

        _enemies.add(newEnemy);
        _enemySpawnIntervalMs *= Constants.EnemySpawnRateAcceleration;
        _enemySpeed *= Constants.EnemySpeedMultiplier;
    }

    private double GetEnemyAngle(Random random)
    {
        boolean left = random.nextDouble() < 0.5;
        if (left)
        {
            return Math.PI * .2 + random.nextDouble() * Math.PI * 0.2;
        }
        else
        {
            return Math.PI * .6 + random.nextDouble() * Math.PI * 0.2;
        }
    }
}