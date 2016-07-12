package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import gorlorn.Gorlorn;

/**
 * A button!
 * <p/>
 * Created by Rob on 6/15/2016.
 */
public class Button
{
    private Bitmap _bitmap;
    private Gorlorn _gorlorn;
    private Rect _hitBox;
    private int _x;
    private int _y;
    private boolean _isClicked;

    public Button(Gorlorn gorlorn, Bitmap bitmap, int centerX, int centerY)
    {
        _gorlorn = gorlorn;
        _bitmap = bitmap;

        _x = centerX - _bitmap.getWidth() / 2;
        _y = centerY - _bitmap.getHeight() / 2;
        _hitBox = new Rect(_x, _y, _x + _bitmap.getWidth(), _y + _bitmap.getHeight());
    }

    public Button(Gorlorn gorlorn, int id, float widthPercent, float heightPercent, int centerX, int centerY)
    {
        this(gorlorn, gorlorn.createBitmap(id, gorlorn.getXFromPercent(widthPercent), gorlorn.getYFromPercent(heightPercent)), centerX, centerY);
    }

    /**
     * Gets whether or not the button was clicked this frame.
     *
     * @return
     */
    public boolean isClicked()
    {
        return _isClicked;
    }

    public void update()
    {
        _isClicked = _gorlorn.getHud().isClicked() && _hitBox.contains(_gorlorn.getHud().getClickX(), _gorlorn.getHud().getClickY());
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(_bitmap, _x, _y, null);
    }
}
