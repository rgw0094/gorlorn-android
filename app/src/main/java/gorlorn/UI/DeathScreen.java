package gorlorn.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import java.util.Date;

import gorlorn.Gorlorn;
import gorlorn.activities.R;

/**
 * The screen and effect that is displayed when the hero dies.
 * <p/>
 * Created by Rob on 6/14/2016.
 */
public class DeathScreen
{
    private enum Phase
    {
        BackgroundFadeIn,
        TextFadeIn,
        RedFlash,
        Done
    }

    private int _textFadeDurationMs = 200;
    private int _redFlashDuration = 100;

    private Gorlorn _gorlorn;
    private long _timeEnteredPhaseMs;
    private float _backgroundOpacity = 0.0f;
    private Phase _currentPhase;
    private Paint _textPaint;
    private Paint _heroPaint = new Paint();
    private Button _tryAgainButton;

    public DeathScreen(Gorlorn gorlorn)
    {
        _gorlorn = gorlorn;

        _heroPaint.setARGB(255, 255, 255, 255);

        _textPaint = new Paint();
        _textPaint.setARGB(255, 255, 255, 255);
        _textPaint.setStyle(Paint.Style.FILL);
        _textPaint.setTextAlign(Paint.Align.CENTER);
        _textPaint.setAntiAlias(true);
        _textPaint.setTextSize(200);

        _tryAgainButton = new Button(gorlorn, R.drawable.tryagain, 0.35f, 0.35f / 4.25f, gorlorn.getXFromPercent(0.78f), gorlorn.getYFromPercent(0.9f));
    }

    public void show()
    {
        enterPhase(Phase.BackgroundFadeIn);
    }

    public void update(float dt)
    {
        if (_currentPhase == Phase.BackgroundFadeIn)
        {
            _backgroundOpacity += 0.5f * dt;
            if (_backgroundOpacity >= 1.0f)
            {
                _backgroundOpacity = 1.0f;
                enterPhase(Phase.TextFadeIn);
            }
        }
        else if (_currentPhase == Phase.TextFadeIn)
        {
            if (new Date().getTime() >= _timeEnteredPhaseMs + _textFadeDurationMs)
            {
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
            _tryAgainButton.update();
            if (_tryAgainButton.isClicked())
            {
                _gorlorn.startGame();
            }
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawARGB((int) (255.0f * _backgroundOpacity), 0, 0, 0);

        if (_currentPhase == Phase.RedFlash)
        {
            float flashPercent = Math.min(1.0f, (float) (new Date().getTime() - _timeEnteredPhaseMs) / (float) _redFlashDuration);
            canvas.drawARGB((int) (200.0f * flashPercent), 255, 0, 0);
        }

        ColorFilter filter = new LightingColorFilter(Color.RED, (int) (100.0f * _backgroundOpacity));
        _heroPaint.setColorFilter(filter);

        float x = _gorlorn.Hero.X - (float) _gorlorn.Hero.Width / 2.0f;
        float y = _gorlorn.Hero.Y - (float) _gorlorn.Hero.Height / 2.0f;
        canvas.drawBitmap(_gorlorn.Hero.Sprite, x, y, _heroPaint);

        if (_currentPhase == Phase.TextFadeIn)
        {
            float fadeInPercent = Math.min(1.0f, (float) (new Date().getTime() - _timeEnteredPhaseMs) / (float) _textFadeDurationMs);
            canvas.drawText("YOU HAVE DIED", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(fadeInPercent * 0.5f), _textPaint);
        }
        else if (_currentPhase == Phase.RedFlash || _currentPhase == Phase.Done)
        {
            canvas.drawText("YOU HAVE DIED", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.5f), _textPaint);
            if (_currentPhase == Phase.Done)
            {
                _tryAgainButton.draw(canvas);
            }
        }
    }

    private void enterPhase(Phase phase)
    {
        _currentPhase = phase;
        _timeEnteredPhaseMs = new Date().getTime();
    }
}
