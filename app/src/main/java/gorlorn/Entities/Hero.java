package gorlorn.Entities;

import android.graphics.Canvas;

import java.util.Date;

import gorlorn.Bitmaps;
import gorlorn.Constants;
import gorlorn.Gorlorn;

/**
 * Represents the hero that must slay the evil monsters
 * Created by Rob on 1/13/2016.
 */
public class Hero extends Entity
{
    private Gorlorn _gorlorn;
    private float _speed;
    private float _acceleration;
    private long _lastShotFiredMs;
    private float _health;
    private HeroDirection _direction = HeroDirection.None;


    /**
     * Constructs a new _hero.
     *
     * @param gorlorn
     */
    public Hero(Gorlorn gorlorn)
    {
        super(Bitmaps.Hero);

        _gorlorn = gorlorn;
        _speed = (float) _gorlorn.ScreenWidth * Constants.HeroSpeed;
        _acceleration = (float) gorlorn.ScreenWidth * Constants.HeroAcceleration;
        _health = Constants.PlayerHealth;

        MaxV = _speed;
        X = gorlorn.ScreenWidth * .5f;
        Y = gorlorn.getYFromPercent(0.995f - Constants.HeroDiameter);
    }

    public void dealDamage(float damage)
    {
        //The hero is invincible while recovering from previous damage!
        if (getIsBlinking())
            return;

        _health -= damage;

        //Start blinking!
        startBlinking(Constants.PlayerBlinksOnHitMs);

        if (_health <= 0.001f || Constants.DieInOneHit)
        {
            _health = 0.0f;
            _gorlorn.die();
        }

        //Fire a spray of bullets when hit
        _gorlorn.getBulletManager().fireBulletSpray(X, Y, 6, 0, (float)Math.PI * 1.1f, (float)Math.PI* 1.9f);
    }

    /**
     * Restores a hearts worth of health!
     */
    public void restoreHealth()
    {
        _health = Math.min(Constants.PlayerHealth, _health + Constants.HeartHealthRestore);

        //TODO: do fancy effect with red particles falling
    }

    public float getHealthPercent()
    {
        return _health / Constants.PlayerHealth;
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }

    @Override
    public boolean update(float dt)
    {
        boolean canMoveAndShoot = !getIsBlinking() || new Date().getTime() > _timeStartedBlinkingMs + Constants.PlayerFrozenOnHitMs;

        //Update movement - the player will have a quick acceleration, but stop instantly
        if (canMoveAndShoot && _gorlorn.getHud().isLeftPressed())
        {
            if (_direction == HeroDirection.Right)
            {
                Vx = 0;
            }
            Ax = -_acceleration;
            _direction = HeroDirection.Left;
        }
        else if (canMoveAndShoot && _gorlorn.getHud().isRightPressed())
        {
            if (_direction == HeroDirection.Left)
            {
                Vx = 0;
            }
            Ax = _acceleration;
            Ax = _acceleration;
            _direction = HeroDirection.Right;
        }
        else
        {
            Vx = 0.0f;
            Ax = 0.0f;
            _direction = HeroDirection.None;
        }

        super.update(dt);

        //Keep the hero within the game area
        X = Math.min(Math.max(X, Width * 0.5f), _gorlorn.ScreenWidth - (Width * 0.5f));

        if (canMoveAndShoot)
        {
            long now = new Date().getTime();
            if (now - _lastShotFiredMs > Constants.MinShotIntervalMs)
            {
                _lastShotFiredMs = now;
                _gorlorn.getBulletManager().fireBullet(X, Y, Math.PI * 1.5);
                _gorlorn.getGameStats().shotsFired++;
            }
        }

        return false;
    }

    private enum HeroDirection
    {
        Left,
        Right,
        None
    }
}
