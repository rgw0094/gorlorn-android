package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;

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
    private Rect _fireButtonHitBox;
    private Bitmap _leftButtonSprite;
    private Bitmap _rightButtonSprite;
    private HealthBar _healthBar;
    private HealthBar _energyBar;
    private LinkedList<Points> _points = new LinkedList<>();
    private Paint _scorePaint;
    private int _numPointersDown = 0;

    private Paint _buttonBorderPaint;
    private Paint _buttonBackgroundPaint;
    private boolean _isMoveButtonDown;
    private int _moveButtonIndex = -1;
    private int _fireButtonIndex = -1;

    private boolean _isLeftPressed;
    private boolean _isRightPressed;
    private boolean _isFirePressed;
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

        int buttonHitBoxDiameter = gorlornActivity.getXFromPercent(Constants.ButtonDiameter);
        _leftButtonHitBox = new Rect(0, gorlornActivity.ScreenHeight - buttonHitBoxDiameter, buttonHitBoxDiameter, gorlornActivity.ScreenHeight);
        _rightButtonHitBox = new Rect(buttonHitBoxDiameter, gorlornActivity.ScreenHeight - buttonHitBoxDiameter, buttonHitBoxDiameter * 2, gorlornActivity.ScreenHeight);
        _fireButtonHitBox = new Rect(gorlornActivity.ScreenWidth - buttonHitBoxDiameter, gorlornActivity.ScreenHeight - buttonHitBoxDiameter, gorlornActivity.ScreenWidth, gorlornActivity.ScreenHeight);

        float buttonSpriteDiameterPercent = Constants.ButtonDiameter * 0.6f;
        _leftButtonSprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.left_button, buttonSpriteDiameterPercent);
        _rightButtonSprite = gorlornActivity.createBitmapByWidthPercent(R.drawable.right_button, buttonSpriteDiameterPercent);

        _buttonBorderPaint = new Paint();
        _buttonBorderPaint.setAntiAlias(true);
        _buttonBorderPaint.setARGB(255, 55, 55, 55);

        _buttonBackgroundPaint = new Paint();
        _buttonBackgroundPaint.setAntiAlias(true);
        _buttonBackgroundPaint.setARGB(255, 22, 22, 22);

        _scorePaint = new Paint();
        _scorePaint.setColor(Color.WHITE);
        _scorePaint.setStyle(Paint.Style.FILL);
        _scorePaint.setAntiAlias(true);
        _scorePaint.setTextSize(50);

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

        _energyBar = new HealthBar(
                Orientation.Horizontal,
                _gorlornActivity.getXFromPercent(0.99f) - healthBarLength,
                _gorlornActivity.getYFromPercent(0.02f) + healthBarThickness,
                healthBarThickness,
                healthBarLength,
                energyPaint);
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

    /**
     * Gets whether or not the fire button is currently pressed;
     */
    public boolean isFirePressed()
    {
        return _isFirePressed;
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

        int index = me.getActionIndex();
        int x = (int) me.getX(index);
        int y = (int) me.getY(index);

        switch (me.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (_leftButtonHitBox.contains(x, y) || _rightButtonHitBox.contains(x, y))
                {
                    _moveButtonIndex = index;
                    _isMoveButtonDown = true;
                }
                else if (_fireButtonHitBox.contains(x, y))
                {
                    _fireButtonIndex = index;
                    _isFirePressed = true;
                }
                _numPointersDown++;

                if (_numPointersDown == 1 && !_isPointerDown)
                {
                    _isClicked = true;
                    _clickX = x;
                    _clickY = y;
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (index == _fireButtonIndex)
                {
                    _isFirePressed = false;
                    _fireButtonIndex = -1;
                }
                else if (index == _moveButtonIndex)
                {
                    _isMoveButtonDown = false;
                    _isLeftPressed = false;
                    _isRightPressed = false;
                    _moveButtonIndex = -1;
                }
                _numPointersDown--;
                break;
        }

        if (_isMoveButtonDown && index == _moveButtonIndex)
        {
            if (_leftButtonHitBox.contains(x, y))
            {
                _isLeftPressed = true;
                _isRightPressed = false;
            }
            else if (_rightButtonHitBox.contains(x, y))
            {
                _isLeftPressed = false;
                _isRightPressed = true;
            }
            else
            {
                _isRightPressed = false;
                _isLeftPressed = false;
            }
        }

        _isPointerDown = _numPointersDown == 1;

        //Extra check for multi-touch wackiness
        if (_numPointersDown == 0)
        {
            _isMoveButtonDown = false;
            _isFirePressed = false;
            _isLeftPressed = false;
            _isRightPressed = false;
            _fireButtonIndex = -1;
            _moveButtonIndex = -1;
        }
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
        canvas.drawText(MessageFormat.format("{0}", _gorlornActivity.Score), 10, 50, _scorePaint);

        drawButton(canvas, _leftButtonSprite, _leftButtonHitBox);
        drawButton(canvas, _rightButtonSprite, _rightButtonHitBox);
        drawButton(canvas, _leftButtonSprite, _fireButtonHitBox);

        if (_gorlornActivity.IsDebugMode)
        {
            //Show button states
            canvas.drawText(MessageFormat.format("Left  {0} {1}", _isLeftPressed ? "D" : "U", _moveButtonIndex), 10, 100, _scorePaint);
            canvas.drawText(MessageFormat.format("Right {0} {1}", _isRightPressed ? "D" : "U", _moveButtonIndex), 10, 150, _scorePaint);
            canvas.drawText(MessageFormat.format("Fire  {0} {1}", _isFirePressed ? "D" : "U", _fireButtonIndex), 10, 200, _scorePaint);
        }

        for (Points points : _points)
        {
            points.Draw(canvas);
        }

        _healthBar.draw(canvas, _gorlornActivity.Hero.getHealthPercent());
        _energyBar.draw(canvas, _gorlornActivity.Hero.getEnergyPercent());
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
        canvas.drawRect(box, _buttonBorderPaint);
        canvas.drawLine(box.left, box.top, box.right, box.top, _buttonBorderPaint);
        canvas.drawLine(box.right, box.top, box.right, box.bottom, _buttonBorderPaint);
        canvas.drawLine(box.right, box.bottom, box.left, box.bottom, _buttonBorderPaint);
        canvas.drawLine(box.left, box.bottom, box.left, box.top, _buttonBorderPaint);

        int x = (int) (box.left + ((box.width() - sprite.getWidth()) * 0.5f));
        int y = (int) (box.top + ((box.height() - sprite.getHeight()) * 0.5f));
        canvas.drawBitmap(sprite, x, y, null);
    }
}