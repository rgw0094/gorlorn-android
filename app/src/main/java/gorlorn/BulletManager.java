package gorlorn;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;

import gorlorn.Entities.Bullet;
import gorlorn.Entities.Enemy;
import gorlorn.Entities.Entity;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * Created by Rob on 1/14/2016.
 */
public class BulletManager
{
    private GorlornActivity _gorlornActivity;
    private Bitmap _projectileSprite;
    private LinkedList<Bullet> _bullets = new LinkedList<>();
    private float _speed;
    private int _chainCountToSpawnHeart = Constants.StartingChainCountToSpawnHeart;

    public BulletManager(GorlornActivity gorlornActivity)
    {
        _gorlornActivity = gorlornActivity;
        _speed = ((_gorlornActivity.ScreenWidth + _gorlornActivity.ScreenHeight) / 2.0f) * Constants.BulletSpeed;
        _projectileSprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.bullet, Constants.BulletDiameter);
    }

    public void FireBullet(float originX, float originY, double angle)
    {
        _bullets.add(new Bullet(_gorlornActivity, _projectileSprite, originX, originY, _speed, angle, 1, 0));
    }

    public void update(float dt)
    {
        LinkedList<Bullet> deadBullets = new LinkedList<>();
        LinkedList<Bullet> newBullets = new LinkedList<>();

        for (Bullet bullet : _bullets)
        {
            if (bullet.update(dt))
            {
                deadBullets.add(bullet);
            }

            //Check if the bullet hit an enemy
            Enemy killedEnemy = _gorlornActivity.EnemyManager.TryKillEnemy(bullet);
            if (killedEnemy != null)
            {
                _gorlornActivity.Hud.addPoints(bullet.X, bullet.Y, bullet.ChainCount);
                deadBullets.add(bullet);

                if (bullet.ChainCount > _chainCountToSpawnHeart)
                {
                    _gorlornActivity.HeartManager.spawnHeart((int) bullet.X, (int) bullet.Y);
                    _chainCountToSpawnHeart++;
                }

                //Fire 3 short-lived bullets in random angles when an enemy is killed
                for (int i = 0; i < 3; i++)
                {
                    double angle = _gorlornActivity.Random.nextDouble() * Math.PI * 2.0;
                    newBullets.add(new Bullet(_gorlornActivity, _projectileSprite, killedEnemy.X, killedEnemy.Y, _speed, angle, bullet.ChainCount + 1, 250));
                }
            }
        }

        for (Bullet deadBullet : deadBullets)
        {
            _bullets.remove(deadBullet);
        }

        for (Bullet newBullet : newBullets)
        {
            _bullets.add(newBullet);
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