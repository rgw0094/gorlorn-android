package gorlorn.Screens;

import android.graphics.Canvas;

import gorlorn.Bitmaps;
import gorlorn.Gorlorn;

/**
 * Created by Rob on 6/28/2016.
 */
public class MenuScreen extends ScreenBase
{
    public MenuScreen(Gorlorn gorlorn)
    {
        super(gorlorn);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
        _gorlorn.toggleMenuControlVisibility(true);
    }

    @Override
    public boolean leave()
    {
        _gorlorn.toggleMenuControlVisibility(false);
        return true;
    }

    @Override
    public boolean update(float dt)
    {
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);
        canvas.drawBitmap(Bitmaps.Title, _gorlorn.getXFromPercent(0.15f), _gorlorn.getYFromPercent(0.15f), null);
    }
}
