package gorlorn.Screens;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gorlorn.Framework.RenderLoopBase;
import gorlorn.Gorlorn;
import gorlorn.GorlornStats;
import gorlorn.UI.Background;

/**
 * Created by Rob on 6/27/2016.
 */
public class StatisticsScreen extends ScreenBase
{
    private GorlornStats _stats;
    private Phase _phase;
    private Paint _statsPaint;
    private float _rowHeight;
    private long _timeShownMs;
    private float _topY;
    private float _targetTopY;

    private enum Phase
    {
        RetractingBackground,
        DescendingStats,
        ShowingStats,
        AscendingStats,
        ExtendingBackground
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
        _statsPaint.setTextSize(gorlorn.getYFromPercent(0.05f));

        _targetTopY = gorlorn.getYFromPercent(0.4f);
        _rowHeight = gorlorn.getYFromPercent(0.6f) / 3.0f;
    }

    public void show()
    {
        _phase = Phase.RetractingBackground;
        _timeShownMs = new Date().getTime();
        _gorlorn.getBackground().retractGrid(500);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {

    }

    @Override
    public boolean leave()
    {
        //Don't leave immediately - we have a transition effect!
        return false;
    }

    public boolean update(float dt)
    {
        switch (_phase)
        {
            case RetractingBackground:
                if (new Date().getTime() - _timeShownMs > 500)
                    _phase = Phase.DescendingStats;
                break;

            case DescendingStats:
                _topY += _gorlorn.getYFromPercent(1.0f) * dt;
                if (_topY >= _targetTopY)
                {
                    _topY = _targetTopY;
                    _phase = Phase.ShowingStats;
                }
                break;
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        if (_phase == Phase.AscendingStats || _phase == Phase.DescendingStats || _phase == Phase.ShowingStats)
        {
            canvas.drawText("Hello world: ", _gorlorn.getXFromPercent(0.2f), _topY, _statsPaint);
        }
    }

    public String getHighScore()
    {
        return NumberFormat.getNumberInstance().format(_stats.highScore);
    }

    public String getScore()
    {
        return NumberFormat.getNumberInstance().format(_stats.score);
    }

    public String getGamesPlayed()
    {
        return NumberFormat.getNumberInstance().format(_stats.gamesPlayed);
    }

    public String getShotsFired()
    {
        return NumberFormat.getNumberInstance().format(_stats.shotsFired);
    }

    public String getEnemiesKilled()
    {
        return NumberFormat.getNumberInstance().format(_stats.enemiesVanquished);
    }

    public String getHighestCombo()
    {
        return NumberFormat.getNumberInstance().format(_stats.highestCombo);
    }

    public String getTimePlayed()
    {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(_stats.timePlayedMs));
    }

    public String getHeartsSpawned()
    {
        return NumberFormat.getNumberInstance().format(_stats.heartsSpawned);
    }

    public String getHeartsCollected()
    {
        return NumberFormat.getNumberInstance().format(_stats.heartsCollected);
    }
}
