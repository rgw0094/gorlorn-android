package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gorlorn.Entities.Points;
import gorlorn.Constants;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * The gorlorn user interface during game mode.
 * <p/>
 * Created by Rob on 1/14/2016.
 */
public class HUD
{
    private GorlornActivity _gorlornActivity;
    private Rect _leftButtonHitBox;
    private Rect _rightButtonHitBox;
    private Bitmap _leftButtonSprite;
    private Bitmap _rightButtonSprite;
    private HealthBar _healthBar;
    private LinkedList<Points> _points = new LinkedList<>();
    public Paint _scorePaint;
    private Paint _highScorePaint;
    private List<Pointer> _pointers = new LinkedList<Pointer>();

    private boolean _isLeftPressed;
    private boolean _isRightPressed;
    private boolean _isPointerDown;
    private boolean _isClicked;
    private int _clickX;
    private int _clickY;

    /**
     * Constructs a new HUD (Heads up display). This user interface includes the movement and fire button,
     * and probably health and stats and stuff.
     *
     * @param gorlornActivity
     */
    public HUD(GorlornActivity gorlornActivity)
    {
        _gorlornActivity = gorlornActivity;

        //Set up the movement buttons
        int buttonHitBoxDiameter = gorlornActivity.getXFromPercent(Constants.ButtonDiameter);
        _leftButtonHitBox = new Rect(0, gorlornActivity.ScreenHeight - buttonHitBoxDiameter, buttonHitBoxDiameter, gorlornActivity.ScreenHeight);
        _rightButtonHitBox = new Rect(gorlornActivity.ScreenWidth - buttonHitBoxDiameter, gorlornActivity.ScreenHeight - buttonHitBoxDiameter, gorlornActivity.ScreenWidth, gorlornActivity.ScreenHeight);

        _leftButtonSprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.left_button, Constants.ButtonDiameter);
        _rightButtonSprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.right_button, Constants.ButtonDiameter);

        //TODO: factory for this
        _scorePaint = new Paint();
        _scorePaint.setColor(Color.WHITE);
        _scorePaint.setStyle(Paint.Style.FILL);
        _scorePaint.setAntiAlias(true);
        _scorePaint.setTextSize(_gorlornActivity.getYFromPercent(0.08f));

        _highScorePaint = new Paint();
        _highScorePaint.setColor(Color.WHITE);
        _highScorePaint.setStyle(Paint.Style.FILL);
        _highScorePaint.setAntiAlias(true);
        _highScorePaint.setTextSize(_gorlornActivity.getYFromPercent(0.05f));

        int healthBarLength = _gorlornActivity.getXFromPercent(Constants.HealthBarLength);
        int healthBarThickness = _gorlornActivity.getYFromPercent(Constants.HealthBarThickness);

        Paint healthPaint = new Paint();
        healthPaint.setARGB(255, 255, 0, 0);
        _healthBar = new HealthBar(
                Orientation.Horizontal,
                _gorlornActivity.getXFromPercent(0.99f) - healthBarLength,
                _gorlornActivity.getYFromPercent(0.01f), healthBarThickness,
                healthBarLength,
                healthPaint);

        Paint energyPaint = new Paint();
        energyPaint.setARGB(255, 0, 224, 216);
    }

    /**
     * Gets whether or not the move left button is currently pressed.
     */
    public boolean isLeftPressed()
    {
        return _isLeftPressed;
    }

    /**
     * Gets whether or not the move right button is currently pressed.
     */
    public boolean isRightPressed()
    {
        return _isRightPressed;
    }

    /*
     * Whether or not a "click" happened this frame - the user touched the screen at a single point where
     * they weren't touching last frame.
     */
    public boolean isClicked()
    {
        return _isClicked;
    }

    /**
     * If isClicked() is true, gets the x coordinate of the click event.
     *
     * @return
     */
    public int getClickX()
    {
        return _clickX;
    }

    /**
     * If isClicked() is true, gets the y coordinate of the click event.
     *
     * @return
     */
    public int getClickY()
    {
        return _clickY;
    }

    /**
     * Handles touch events.
     *
     * @param me
     */
    public void handleTouchEvent(MotionEvent me)
    {
        if (_isClicked)
            _isClicked = false;

        int action = MotionEventCompat.getActionMasked(me);
        int index = MotionEventCompat.getActionIndex(me);
        int id = me.getPointerId(index);
        int x = (int) me.getX(index);
        int y = (int) me.getY(index);

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                _pointers.add(new Pointer(id, x, y));

                if (_pointers.size() == 1 && !_isPointerDown)
                {
                    _isClicked = true;
                    _clickX = x;
                    _clickY = y;
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                Pointer pointerToRemove = null;
                for (Pointer pointer : _pointers)
                {
                    if (pointer.id == id)
                    {
                        pointerToRemove = pointer;
                        break;
                    }
                }
                _pointers.remove(pointerToRemove);
                break;

            case MotionEvent.ACTION_MOVE:
                for (Pointer pointer : _pointers)
                {
                    if (pointer.id == id)
                    {
                        pointer.x = x;
                        pointer.y = y;
                    }
                }
                break;
        }

        //Look at the latest pointer that's over a button
        _isLeftPressed = false;
        _isRightPressed = false;
        if (_pointers.size() > 0)
        {
            Pointer latestPointer = _pointers.get(_pointers.size() - 1);
            if (_leftButtonHitBox.contains(latestPointer.x, latestPointer.y))
            {
                _isLeftPressed = true;
            }
            else if (_rightButtonHitBox.contains(latestPointer.x, latestPointer.y))
            {
                _isRightPressed = true;
            }
        }

        _isPointerDown = _pointers.size() == 1;
    }

    /**
     * Adds points to the score when a bullet hits an enemy.
     *
     * @param chainCount The chain count of the bullet that hit.
     */
    public void addPoints(float x, float y, int chainCount)
    {
        long points = (long) Math.pow(2, chainCount);
        _gorlornActivity.Score += points;

        _points.add(new Points(_gorlornActivity, points, x, y));
    }

    /**
     * Updates the HUD.
     *
     * @param dt Time since the last update
     */
    public void update(float dt)
    {
        for (Iterator<Points> iterator = _points.iterator(); iterator.hasNext(); )
        {
            Points points = iterator.next();
            if (points.Update(dt))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Draws the HUD.
     *
     * @param canvas Canvas upon which to draw the HUD
     */
    public void draw(Canvas canvas)
    {
        canvas.drawText(MessageFormat.format("High Score: {0}", _gorlornActivity.HighScore), _gorlornActivity.getXFromPercent(0.01f), _gorlornActivity.getYFromPercent(0.065f), _highScorePaint);
        canvas.drawText(MessageFormat.format("Score: {0}", _gorlornActivity.Score), _gorlornActivity.getXFromPercent(0.01f), _gorlornActivity.getYFromPercent(0.14f), _scorePaint);

        drawButton(canvas, _leftButtonSprite, _leftButtonHitBox);
        drawButton(canvas, _rightButtonSprite, _rightButtonHitBox);

        if (_gorlornActivity.IsDebugMode)
        {
            //Show button states
            canvas.drawText(MessageFormat.format("Left  {0}", _isLeftPressed ? "D" : "U"), 10, 400, _scorePaint);
            canvas.drawText(MessageFormat.format("Right {0}", _isRightPressed ? "D" : "U"), 10, 460, _scorePaint);
        }

        for (Points points : _points)
        {
            points.Draw(canvas);
        }

        _healthBar.draw(canvas, _gorlornActivity.Hero.getHealthPercent());
    }

    /**
     * Draws a button
     *
     * @param canvas Canvas upon which to draw the button
     * @param sprite Image to draw inside the button
     * @param box    Border of the button
     */
    private void drawButton(Canvas canvas, Bitmap sprite, Rect box)
    {
        int x = (int) (box.left + ((box.width() - sprite.getWidth()) * 0.5f));
        int y = (int) (box.top + ((box.height() - sprite.getHeight()) * 0.5f));
        canvas.drawBitmap(sprite, x, y, null);
    }

    private class Pointer
    {
        public Pointer(int id, int x, int y)
        {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int id;
        public int x;
        public int y;
    }
}