package gorlorn.Screens;

import android.graphics.Canvas;
import gorlorn.Gorlorn;

/**
 * Created by Rob on 6/28/2016.
 */
public abstract class ScreenBase
{
    protected Gorlorn _gorlorn;

    /**
     * Constructs a new ScreenBase
     * @param gorlorn    The gorlorn game displaying this screen.
     */
    public ScreenBase(Gorlorn gorlorn)
    {
        _gorlorn = gorlorn;
    }

    /**
     * Shows the screen, initiating any transition effects.
     *
     * @param previousScreen    The screen from which we are coming.
     */
    public abstract void show(ScreenBase previousScreen);

    /**
     * Begins the process of leaving the screen. When it is complete, the update method will return true.
     * @returns True if it the screen can leave immediately, or false if it needs to transition. If returning
     *          false, the screen won't switch to the new one until this one signals its ok by returning true
     *          from update.
     */
    public abstract boolean leave();

    /**
     * Updates the screen.
     * @param dt        The time since the last call to update
     * @return
     */
    public abstract boolean update(float dt);

    /**
     * draws the screen.
     * @param canvas    The canvas upon which to draw the screen.
     */
    public abstract void draw(Canvas canvas);
}
