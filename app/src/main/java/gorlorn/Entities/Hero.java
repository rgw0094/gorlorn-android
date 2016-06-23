package gorlorn.Entities;

import android.graphics.Canvas;

import java.text.MessageFormat;
import java.util.Date;

import gorlorn.Constants;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * Represents the hero that must slay the evil monsters
 * Created by Rob on 1/13/2016.
 */
public class Hero extends Entity
{
    private GorlornActivity _gorlornActivity;
    private float _speed;
    private float _acceleration;
    private long _lastShotFiredMs;
    private float _healthPercent = 1.0f;
    private HeroDirection _direction = HeroDirection.None;


    /**
     * Constructs a new Hero.
     *
     * @param gorlornActivity
     */
    public Hero(GorlornActivity gorlornActivity)
    {
        super(gorlornActivity.createBitmapByWidthPercent(R.drawable.hero, Constants.HeroDiameter));

        _gorlornActivity = gorlornActivity;
        _speed = (float) _gorlornActivity.ScreenWidth * Constants.HeroSpeed;
        _acceleration = (float) gorlornActivity.ScreenWidth * Constants.HeroAcceleration;

        MaxV = _speed;
        X = gorlornActivity.ScreenWidth * .5f;
        Y = gorlornActivity.getYFromPercent(0.995f - Constants.HeroDiameter);
    }

    public void dealDamage(float damagePercent)
    {
        //The hero is invincible while recovering from previous damage!
        if (getIsBlinking())
            return;

        _healthPercent = Math.max(0.0f, _healthPercent - damagePercent);

        //Start blinking!
        startBlinking(Constants.PlayerBlinksOnHitMs);

        if (_healthPercent <= 0.0f)
        {
            _healthPercent = 0.0f;
            _gorlornActivity.showDeathScreen();
        }
    }

    /**
     * Restores a hearts worth of health!
     */
    public void restoreHealth()
    {
        _healthPercent = Math.min(1.0f, _healthPercent + Constants.HeartHealthRestore);

        //TODO: do fancy effect with red particles falling
    }

    public float getHealthPercent()
    {
        return _healthPercent;
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        if (_gorlornActivity.IsDebugMode)
        {
            //Show movement state
            canvas.drawText(MessageFormat.format("Dir  {0}", _direction), 10, 520, _gorlornActivity.Hud._scorePaint);
        }
    }

    @Override
    public boolean update(float dt)
    {
        boolean canMove = !getIsBlinking() || new Date().getTime() > _timeStartedBlinkingMs + Constants.PlayerFrozenOnHitMs;

        //Update movement - the player will have a quick acceleration, but stop instantly
        if (canMove && _gorlornActivity.Hud.isLeftPressed())
        {
            if (_direction == HeroDirection.Right)
            {
                Vx = 0;
            }
            Ax = -_acceleration;
            _direction = HeroDirection.Left;
        }
        else if (canMove && _gorlornActivity.Hud.isRightPressed())
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
        X = Math.min(Math.max(X, _gorlornActivity.GameArea.left + Width * 0.5f), _gorlornActivity.GameArea.right - (Width * 0.5f));

        if (!getIsBlinking())
        {
            long now = new Date().getTime();
            if (now - _lastShotFiredMs > Constants.MinShotIntervalMs)
            {
                _lastShotFiredMs = now;
                _gorlornActivity.BulletManager.FireBullet(X, Y, Math.PI * 1.5);
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
