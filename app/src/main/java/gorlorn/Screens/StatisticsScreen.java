package gorlorn.Screens;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gorlorn.Gorlorn;
import gorlorn.GorlornStats;

/**
 * Created by Rob on 6/27/2016.
 */
public class StatisticsScreen extends ScreenBase
{
    private static int BackgroundRetractMs = 300;

    private GorlornStats _stats;
    private Phase _phase;
    private Paint _statsPaint;
    private long _timeEnteredPhaseMs;
    private float _statsSizePercent = 0.0f;

    private enum Phase
    {
        Entering,
        ShowingStats,
        Leaving,
    }

    /**
     * Constructs a new StatisticsScreen.
     *
     * @param gorlorn
     * @param stats
     */
    public StatisticsScreen(Gorlorn gorlorn, GorlornStats stats)
    {
        super(gorlorn);

        _stats = stats;

        _statsPaint = new Paint();
        _statsPaint.setARGB(255, 255, 255, 255);
        _statsPaint.setStyle(Paint.Style.FILL);
        _statsPaint.setAntiAlias(true);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
        enterPhase(Phase.Entering);
        _gorlorn.getBackground().retractGrid(BackgroundRetractMs);
    }

    @Override
    public boolean leave()
    {
        //Don't leave immediately - we have a transition effect!
        _gorlorn.hideAd();
        _gorlorn.getBackground().extendGrid(BackgroundRetractMs);
        enterPhase(Phase.Leaving);
        return false;
    }

    public boolean update(float dt)
    {
        switch (_phase)
        {
            case Entering:
                _statsSizePercent = Math.min(1.0f, _statsSizePercent + (1000.0f / (float) BackgroundRetractMs) * dt * 1.05f);
                if (timeInPhase() > BackgroundRetractMs)
                {
                    _statsSizePercent = 1.0f;
                    _gorlorn.showAd();
                    enterPhase(Phase.ShowingStats);
                }
                break;

            case Leaving:
                _statsSizePercent -= (1000.0f / (float) BackgroundRetractMs) * dt;
                if (timeInPhase() > BackgroundRetractMs)
                {
                    _statsSizePercent = 0.0f;
                    return true;
                }
                break;
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);

        float height = _gorlorn.getYFromPercent(0.6f) * _statsSizePercent;
        float width = height * 2.4f;
        int x = (int) ((_gorlorn.ScreenWidth - width) * 0.5f);
        int y = (int) ((_gorlorn.ScreenHeight - height) * 0.5f) + _gorlorn.getYFromPercent(0.05f);

        Rect statsBounds = new Rect(x, y, x + (int) width, y + (int) height);
        drawStatsInGrid(canvas, statsBounds);
    }

    //region Private Methods

    private void drawStatsInGrid(Canvas canvas, Rect bounds)
    {
        float[] columnWidthPercents = new float[]{.32f, .26f, .33f, .15f};
        int numRows = 5;
        float rowHeight = (float) bounds.height() / (float) numRows;

        String[] strings = new String[]
                {
                        "High Score: ", getHighScore(), "", "",
                        "Career Score: ", getScore(), "", "",
                        "Highest Combo: ", getHighestCombo(), "Shots Fired: ", getShotsFired(),
                        "Time Played: ", getTimePlayed(), "Enemies Killed: ", getEnemiesKilled(),
                        "Games Played: ", getGamesPlayed(), "Hearts Collected: ", getHeartsCollected(),
                };

        _statsPaint.setTextSize(_gorlorn.getYFromPercent(0.06f * _statsSizePercent));

        float y = bounds.top;
        for (int row = 0; row < numRows; row++)
        {
            float x = bounds.left;
            for (int col = 0; col < columnWidthPercents.length; col++)
            {
                int index = row * columnWidthPercents.length + col;
                canvas.drawText(strings[index], x, y, _statsPaint);
                x += (bounds.width() * columnWidthPercents[col]);
            }
            y += rowHeight;
        }

        //canvas.drawRect(bounds, _statsPaint);
    }

    private long timeInPhase()
    {
        return new Date().getTime() - _timeEnteredPhaseMs;
    }

    private void enterPhase(Phase phase)
    {
        _phase = phase;
        _timeEnteredPhaseMs = new Date().getTime();
    }

    private String getHighScore()
    {
        return NumberFormat.getNumberInstance().format(_stats.highScore);
    }

    private String getScore()
    {
        return NumberFormat.getNumberInstance().format(_stats.score);
    }

    private String getGamesPlayed()
    {
        return NumberFormat.getNumberInstance().format(_stats.gamesPlayed);
    }

    private String getShotsFired()
    {
        return NumberFormat.getNumberInstance().format(_stats.shotsFired);
    }

    private String getEnemiesKilled()
    {
        return NumberFormat.getNumberInstance().format(_stats.enemiesVanquished);
    }

    private String getHighestCombo()
    {
        return NumberFormat.getNumberInstance().format(_stats.highestCombo);
    }

    private String getTimePlayed()
    {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(_stats.timePlayedMs));
    }

    private String getHeartsCollected()
    {
        return NumberFormat.getNumberInstance().format(_stats.heartsCollected);
    }

    //endregion
}
