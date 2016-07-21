package gorlorn;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.Random;

import gorlorn.Entities.Bullet;
import gorlorn.Entities.Enemy;
import gorlorn.Entities.Entity;
import gorlorn.activities.R;

/**
 * Spawns and controls bullets.
 * <p/>
 * Created by Rob on 1/14/2016.
 */
public class BulletManager
{
    private Gorlorn _gorlorn;
    private java.util.Random _random = new Random();
    private LinkedList<Bullet> _bullets = new LinkedList<>();
    private float _speed;
    private int _chainCountToSpawnHeart = Constants.StartingChainCountToSpawnHeart;

    /**
     * Constructs a new _bulletManager.
     *
     * @param gorlorn
     */
    public BulletManager(Gorlorn gorlorn)
    {
        _gorlorn = gorlorn;
        _speed = ((_gorlorn.ScreenWidth + _gorlorn.ScreenHeight) / 2.0f) * Constants.BulletSpeed;
    }

    /**
     * Fires a bullet starting at the given origin point and travelling at the given angle.
     *
     * @param originX
     * @param originY
     * @param angle
     */
    public void fireBullet(float originX, float originY, double angle)
    {
        fireBullet(originX, originY, angle, 1, 0);
    }

    /**
     * Fires a bullet starting at the given origin point and travelling at the given angle.
     *
     * @param originX
     * @param originY
     * @param angle
     * @param chainCount
     * @param lifeTimeMs
     */
    public void fireBullet(float originX, float originY, double angle, int chainCount, int lifeTimeMs)
    {
        _bullets.add(new Bullet(_gorlorn, chainCount == 1 ? Bitmaps.Bullet : Bitmaps.BulletCombo, originX, originY, _speed, angle, chainCount, lifeTimeMs));
    }

    /**
     * Fires a spray of bullets in random directions originating from the given point.
     *
     * @param originX
     * @param originY
     * @param numBullets
     */
    public void fireBulletSpray(float originX, float originY, int numBullets, int chainCount)
    {
        fireBulletSpray(originX, originY, numBullets, chainCount, 0, (float) Math.PI * 2.0f);
    }

    /**
     * Fires a spray of bullets in random directions originating from the given point.
     *
     * @param originX
     * @param originY
     * @param numBullets
     * @param chainCount
     * @param minAngle
     * @param maxAngle
     */
    public void fireBulletSpray(float originX, float originY, int numBullets, int chainCount, float minAngle, float maxAngle)
    {
        chainCount = (int) Math.min(Constants.MaxComboSize, chainCount + 1);

        for (int i = 0; i < numBullets; i++)
        {
            double angle = minAngle + _random.nextDouble() * (maxAngle - minAngle);
            fireBullet(originX, originY, angle, chainCount, Constants.ChainBulletLifeTimeMs);
        }
    }

    public void update(float dt)
    {
        LinkedList<Bullet> deadBullets = new LinkedList<>();
        LinkedList<Bullet> enemyKillingBullets = new LinkedList<>();

        for (Bullet bullet : _bullets)
        {
            if (bullet.update(dt))
            {
                deadBullets.add(bullet);
            }

            //Check if the bullet hit an enemy
            Enemy killedEnemy = _gorlorn.getEnemyManager().TryKillEnemy(bullet);
            if (killedEnemy != null)
            {
                _gorlorn.getHud().addPoints(bullet.X, bullet.Y, bullet.getChainCount());
                deadBullets.add(bullet);
                enemyKillingBullets.add(bullet);

                if (bullet.getChainCount() > _chainCountToSpawnHeart)
                {
                    _gorlorn.getHeartManager().spawnHeart((int) bullet.X, (int) bullet.Y);
                    _chainCountToSpawnHeart++;
                }
            }
            else if (bullet.Y - bullet.Height < 0)
            {
                //Kill bullets that go off the screen
                deadBullets.add(bullet);
            }
        }

        for (Bullet deadBullet : deadBullets)
        {
            _bullets.remove(deadBullet);
        }

        //Spawn a cluster of bullets for each enemy killed.
        for (Bullet enemyKillingBullet : enemyKillingBullets)
        {
            fireBulletSpray(enemyKillingBullet.X, enemyKillingBullet.Y, 3, enemyKillingBullet.getChainCount());
            _gorlorn.getGameStats().highestCombo = Math.max(enemyKillingBullet.getChainCount() + 1, _gorlorn.getGameStats().highestCombo);
        }
    }

    public void Draw(Canvas canvas)
    {
        for (Entity bullet : _bullets)
        {
            bullet.draw(canvas);
        }
    }
}