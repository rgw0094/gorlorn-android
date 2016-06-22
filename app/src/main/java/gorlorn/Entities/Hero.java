package gorlorn.Entities;

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
    private float _energyPercent = 1.0f;


    /**
     * Constructs a new Hero.
     *
     * @param gorlornActivity
     */
    public Hero(GorlornActivity gorlornActivity)
    {
        super(gorlornActivity.createBitmapByWidthPercent(R.drawable.hero, Constants.HeroDiameter));

        _gorlornActivity = gorlornActivity;
        _speed = (float)_gorlornActivity.ScreenWidth * Constants.HeroSpeed;
        _acceleration = (float)gorlornActivity.ScreenWidth * Constants.HeroAcceleration;

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

    public float getEnergyPercent()
    {
        return _energyPercent;
    }

    @Override
    public boolean update(float dt)
    {
        boolean canMove = !getIsBlinking() || new Date().getTime() > _timeStartedBlinkingMs + Constants.PlayerFrozenOnHitMs;

        //Update movement - the player will have a quick acceleration, unless they are already moving in which case they can change direction instantly
        if (canMove && _gorlornActivity.Hud.isLeftPressed())
        {
//            if (Vx > 0.0f)
//                Vx = -_speed;
//            else if (Vx == 0.0f)
                Ax = -_acceleration;
        }
        else if (canMove && _gorlornActivity.Hud.isRightPressed())
        {
//            if (Vx < 0.0f)
//                Vx = _speed;
//            else if (Vx == 0.0f)
                Ax = _acceleration;
        }
        else
        {
            Vx = 0.0f;
        }

        super.update(dt);

        //Keep the hero within the game area
        X = Math.min(Math.max(X, _gorlornActivity.GameArea.left + Width * 0.5f), _gorlornActivity.GameArea.right - (Width * 0.5f));

//        if (_energyPercent >= Constants.EnergyPerShot)
//        {
        if (!getIsBlinking())
        {
            long now = new Date().getTime();
            if (_gorlornActivity.Hud.isFirePressed() && now - _lastShotFiredMs > Constants.MinShotIntervalMs)
            {
                _lastShotFiredMs = now;
                _gorlornActivity.BulletManager.FireBullet(X, Y, Math.PI * 1.5);
                _energyPercent -= Constants.EnergyPerShot;
            }
        }
        //}

        _energyPercent = Math.min(1.0f, _energyPercent + (Constants.EnergyRegen * dt));

        return false;
    }
}
