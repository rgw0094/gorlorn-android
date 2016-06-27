package gorlorn.UI;

import android.graphics.Canvas;
import android.graphics.Paint;

import gorlorn.Bitmaps;
import gorlorn.Gorlorn;

/**
 * Fancy effect to "summon" the hero at the start of a game to create a nice visual transition from the menu to the game
 * <p/>
 * Created by Rob on 6/24/2016.
 */
public class HeroSummonEffect
{
    private enum Phase
    {
        FadeOutTitle,
        BeamDescending,
        FadeInHero,
        BeamAscending
    }

    private Gorlorn _gorlorn;
    private Phase _phase;
    private float _titleOpacity = 1.0f;
    private float _heroOpacity;
    private float _beamWidth;
    private float _beamX;
    private float _beamY;
    private float _beamSpeed;
    private Paint _titlePaint;
    private Paint _beamPaint;
    private Paint _heroPaint;

    /**
     * Constructs a new HeroSummonEffect.
     *
     * @param renderLoop        The RenderLoop that is drawing this object.
     * @param comingFromMenu    True if the hero is being summoned after coming from the menu, as opposed to the death screen.
     */
    public HeroSummonEffect(Gorlorn renderLoop, boolean comingFromMenu)
    {
        _gorlorn = renderLoop;

        if (comingFromMenu)
        {
            //If we are coming from the menu, we will phase out the title graphic for a smoother transition
            _phase = Phase.FadeOutTitle;
        }
        else
        {
            _phase = Phase.BeamDescending;
        }

        _beamSpeed = (float) _gorlorn.getYFromPercent(6.0f);
        _beamWidth = renderLoop.getXFromPercent(0.1f);
        _beamX = ((float) renderLoop.ScreenWidth - _beamWidth) / 2.0f;

        _beamPaint = new Paint();
        _beamPaint.setARGB(140, 247, 255, 109);

        _heroPaint = new Paint();
        _heroPaint.setARGB(255, 255, 255, 255);

        _titlePaint = new Paint();
        _titlePaint.setARGB(255, 255, 255, 255);
    }

    /**
     * Updates the summon effect. Return whether or not it is complete.
     *
     * @param dt
     * @return
     */
    public boolean update(float dt)
    {
        switch (_phase)
        {
            case FadeOutTitle:
                _titleOpacity -= 1.0f * dt;
                if (_titleOpacity <= 0.0f)
                {
                    _titleOpacity = 0.0f;
                    _phase = Phase.BeamDescending;
                }
                break;

            case BeamDescending:
                _beamY += _beamSpeed * dt;
                if (_beamY >= _gorlorn.ScreenHeight)
                {
                    _beamY = _gorlorn.ScreenHeight;
                    _phase = Phase.FadeInHero;
                }
                break;

            case FadeInHero:
                _heroOpacity += 1.0f * dt;
                if (_heroOpacity >= 1.0f)
                {
                    _heroOpacity = 1.0f;
                    _phase = Phase.BeamAscending;
                }
                break;

            case BeamAscending:
                _beamY -= _beamSpeed * dt;
                if (_beamY <= 0.0f)
                    return true;
                break;
        }
        return false;
    }

    public void draw(Canvas canvas)
    {
        if (_phase == Phase.FadeOutTitle)
        {
            _titlePaint.setAlpha((int) (_titleOpacity * 255.0f));
            canvas.drawBitmap(Bitmaps.Title, _gorlorn.getXFromPercent(0.15f), _gorlorn.getYFromPercent(0.15f), _titlePaint);
        }

        canvas.drawRect(_beamX, 0, _beamX + _beamWidth, _beamY, _beamPaint);

        _heroPaint.setAlpha((int) (_heroOpacity * 255.0f));
//        ColorFilter filter = new LightingColorFilter(Color.RED, (int) (100.0f * _backgroundOpacity));
//        _heroPaint.setColorFilter(filter);

        canvas.drawBitmap(
                Bitmaps.Hero,
                _gorlorn.Hero.X - (float) _gorlorn.Hero.Width / 2.0f,
                _gorlorn.Hero.Y - (float) _gorlorn.Hero.Height / 2.0f,
                _heroPaint);
    }
}