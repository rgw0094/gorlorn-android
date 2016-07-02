package gorlorn;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import gorlorn.Entities.Bullet;
import gorlorn.Entities.Enemy;
import gorlorn.activities.R;

/**
 * Created by Rob on 1/14/2016.
 */
public class EnemyManager
{
    private Gorlorn _gorlorn;
    private LinkedList<Enemy> _enemies;
    private Bitmap _enemySprite;
    private long _timeLastEnemySpawnedMs;
    private float _enemySpawnIntervalMs = Constants.StartingEnemySpawnIntervalMs;
    private float _enemySpeed;
    private long _timeLastRateUpdateMs;

    public EnemyManager(Gorlorn gorlorn)
    {
        _gorlorn = gorlorn;
        _enemySpeed = Constants.EnemySpeed;
        _enemies = new LinkedList<>();
        _enemySprite = gorlorn.createBitmapByWidthPercent(R.drawable.enemy, Constants.EnemyDiameter);
    }

    /**
     * Kill the first enemy hit with the given bullet. Returns the enemy that was hit, or null if
     * no enemies were hit.
     *
     * @param bullet
     * @return
     */
    public Enemy TryKillEnemy(Bullet bullet)
    {
        for (Enemy enemy : _enemies)
        {
            //Don't let a bullet kill an enemy if its a chain reaction that spawned before the enemy,
            //in order to prevent an endless chain reaction once there's a ton of enemies
            if (!(bullet.isChainBullet() && bullet.isOlderThan(enemy)) && enemy.testHit(bullet))
            {
                _gorlorn.getGameStats().enemiesVanquished++;
                _enemies.remove(enemy);
                return enemy;
            }
        }
        return null;
    }

    /**
     * Draws the _bulletManager
     *
     * @param canvas The canvas upon which to draw the _bulletManager
     */
    public void draw(Canvas canvas)
    {
        for (Enemy enemy : _enemies)
        {
            enemy.draw(canvas);
        }

        if (Constants.IsDebugMode)
        {
            canvas.drawText("EDelay: " + new DecimalFormat("#.####").format(_enemySpawnIntervalMs), _gorlorn.getXFromPercent(0.001f), _gorlorn.getYFromPercent(0.6f), Gorlorn.DebugTextPaint);
            canvas.drawText("ESpeed: " + new DecimalFormat("#.####").format(_enemySpeed), _gorlorn.getXFromPercent(0.001f), _gorlorn.getYFromPercent(0.65f), Gorlorn.DebugTextPaint);
        }
    }

    /**
     * Updates the _bulletManager.
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

        if (now - _timeLastRateUpdateMs > 1000)
        {
            _enemySpawnIntervalMs *= Constants.EnemySpawnRateAcceleration;
            _enemySpeed += Constants.EnemySpeedIncrement;
            _timeLastRateUpdateMs = now;
        }
    }

    /**
     * Spawns an enemey off the top of the screen at a random X location with a random downward trajectory
     */
    private void SpawnEnemy()
    {
        Random random = new Random();

        double angle = GetEnemyAngle(random);
        float speed = ((_gorlorn.ScreenWidth + _gorlorn.ScreenHeight) / 2.0f) * _enemySpeed;
        float minX = _gorlorn.ScreenWidth * 0.2f;

        Enemy newEnemy = new Enemy(_gorlorn, _enemySprite);
        newEnemy.X = minX + random.nextFloat() * _gorlorn.ScreenWidth * 0.6f;
        newEnemy.Y = _gorlorn.getYFromPercent(0.025f);
        newEnemy.Vx = speed * (float) Math.cos(angle);
        newEnemy.Vy = speed * (float) Math.sin(angle);

        _enemies.add(newEnemy);
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