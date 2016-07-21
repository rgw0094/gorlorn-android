package gorlorn.UI;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import gorlorn.Framework.RenderLoopBase;

/**
 * Draws a number where each digit floats in an offset parabola, creating a wave effect
 * <p/>
 * Created by Rob on 7/1/2016.
 */
public class FloatingNumbers
{
    private RenderLoopBase _renderLoop;
    private List<Letter> _letters = new LinkedList<Letter>();
    private Paint _letterPaint;
    private float _age = 0.0f;

    /**
     * Constructs a new FloatingNumbers.
     *
     * @param renderLoop
     * @param number
     * @param y
     * @param letterWidthXPercent
     * @param letterHeightYPercent
     */
    public FloatingNumbers(RenderLoopBase renderLoop, long number, float y, float letterWidthXPercent, float letterHeightYPercent, int r, int g, int b)
    {
        _renderLoop = renderLoop;
        _letterPaint = renderLoop.createTextPaint(letterHeightYPercent);
        _letterPaint.setARGB(255, r, g, b);

        String s = NumberFormat.getNumberInstance().format(number);
        float letterWidth = renderLoop.getXFromPercent(letterWidthXPercent);
        float totalWidth = letterWidth * (float) s.length();
        float x = ((float) renderLoop.ScreenWidth - totalWidth) * 0.5f;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            _letters.add(new Letter(x, y, i, String.valueOf(c), _letterPaint));

            //Make commas less wide than digits
            if (c == ',')
                x += letterWidth * 0.3333f;
            else
                x += letterWidth;
        }
    }

    public void update(float dt)
    {
        _age += dt;
        for (Letter l : _letters)
        {
            l.update(_age);
        }
    }

    public void draw(Canvas canvas)
    {
        for (Letter l : _letters)
        {
            l.draw(canvas);
        }
    }

    private class Letter
    {
        private String _letter;
        private int _index;
        private float _baseY;
        private float _x;
        private float _y;
        Paint _paint;

        public Letter(float x, float y, int index, String letter, Paint paint)
        {
            _x = x;
            _baseY = _y = y;
            _index = index;
            _letter = letter;
            _paint = paint;
        }

        /**
         * Updates the letter
         *
         * @param age The letter's age in milliseconds
         */
        public void update(float age)
        {
            float adjustedAge = age - 50.0f * (float) _index;
            _y = _baseY + _renderLoop.getYFromPercent(0.015f) * (float) Math.cos(adjustedAge * 3.0f);
        }

        /**
         * Draws the letter.
         *
         * @param canvas
         */
        public void draw(Canvas canvas)
        {
            canvas.drawText(_letter, _x, _y, _paint);
        }
    }
}
