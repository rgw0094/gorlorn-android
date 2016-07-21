package gorlorn.Screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import java.util.Date;

import gorlorn.Bitmaps;
import gorlorn.Gorlorn;
import gorlorn.UI.Button;
import gorlorn.UI.FloatingNumbers;
import gorlorn.activities.R;

/**
 * The screen and effect that is displayed when the hero dies.
 * <p/>
 * Created by Rob on 6/14/2016.
 */
public class DeathScreen extends ScreenBase
{
    private enum Phase
    {
        BackgroundFadeIn,
        RedFlash,
        Done
    }

    private int _redFlashDuration = 90;

    private long _timeEnteredPhaseMs;
    private float _backgroundOpacity = 0.0f;
    private Phase _currentPhase;
    private Paint _heroPaint = new Paint();
    private Paint _highScorePaint;
    private FloatingNumbers _highScoreFloatingNumbers;

    /**
     * Constructs a new DeathScreen.
     *
     * @param gorlorn
     * @param newHighScore Whether or not the user achieved a new high score this game
     */
    public DeathScreen(Gorlorn gorlorn, boolean newHighScore)
    {
        super(gorlorn);
        if (newHighScore)
        {
            _highScorePaint = gorlorn.createTextPaint(0.10f);
            _highScorePaint.setTextAlign(Paint.Align.CENTER);
            _highScorePaint.setARGB(255, 95, 152, 234);
            _highScoreFloatingNumbers = new FloatingNumbers(gorlorn, gorlorn.getGameStats().score, gorlorn.getYFromPercent(0.225f), 0.04f, 0.12f, 95, 152, 234);
        }

        _heroPaint.setARGB(255, 255, 255, 255);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
        enterPhase(Phase.BackgroundFadeIn);
    }

    @Override
    public boolean leave()
    {
        return true;
    }

    @Override
    public boolean update(float dt)
    {
        if (_currentPhase == Phase.BackgroundFadeIn)
        {
            _backgroundOpacity += 0.5f * dt;
            if (_backgroundOpacity >= 1.0f)
            {
                _backgroundOpacity = 1.0f;
                enterPhase(Phase.RedFlash);
            }
        }
        else if (_currentPhase == Phase.RedFlash)
        {
            if (new Date().getTime() >= _timeEnteredPhaseMs + _redFlashDuration)
            {
                enterPhase(Phase.Done);
            }
        }
        else if (_currentPhase == Phase.Done)
        {
            if (_gorlorn.getHud().isClicked())
            {
                _gorlorn.startGame();
            }

            if (_highScoreFloatingNumbers != null)
            {
                _highScoreFloatingNumbers.update(dt);
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);
        _gorlorn.getBulletManager().Draw(canvas);
        _gorlorn.getEnemyManager().draw(canvas);
        _gorlorn.getHero().draw(canvas);

        canvas.drawARGB((int) (255.0f * _backgroundOpacity), 0, 0, 0);

        if (_currentPhase == Phase.RedFlash)
        {
            float flashPercent = Math.min(1.0f, (float) (new Date().getTime() - _timeEnteredPhaseMs) / (float) _redFlashDuration);
            canvas.drawARGB((int) (200.0f * flashPercent), 255, 0, 0);
        }

        ColorFilter filter = new LightingColorFilter(Color.RED, (int) (100.0f * _backgroundOpacity));
        _heroPaint.setColorFilter(filter);

        float x = _gorlorn.getHero().X - (float) _gorlorn.getHero().Width / 2.0f;
        float y = _gorlorn.getHero().Y - (float) _gorlorn.getHero().Height / 2.0f;
        canvas.drawBitmap(_gorlorn.getHero().Sprite, x, y, _heroPaint);

        if (_currentPhase == Phase.RedFlash || _currentPhase == Phase.Done)
        {
            //canvas.drawText("YOU HAVE DIED", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.5f), _textPaint);
            if (_currentPhase == Phase.Done)
            {
                canvas.drawBitmap(Bitmaps.DeathText,
                        0.5f * ((float) _gorlorn.ScreenWidth - (float) Bitmaps.DeathText.getWidth()),
                        0.5f * ((float) _gorlorn.ScreenHeight - (float) Bitmaps.DeathText.getHeight()), null);

                //_tryAgainButton.draw(canvas);

                if (_highScoreFloatingNumbers != null)
                {
                    canvas.drawText("New High Score!", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.1f), _highScorePaint);
                    _highScoreFloatingNumbers.draw(canvas);
                }
            }
        }
    }

    private void enterPhase(Phase phase)
    {
        _currentPhase = phase;
        _timeEnteredPhaseMs = new Date().getTime();
    }
}