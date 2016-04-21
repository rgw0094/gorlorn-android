package gorlorn;

/**
 * Created by Rob on 1/19/2016.
 */
public class Constants
{
    //region Sizes

    //Diameters are percentages of screen width

    //Entities
    public static float EnemyDiameter = 0.048f;
    public static float HeroDiameter = 0.055f;
    public static float BulletDiameter = 0.022f;
    public static float HeartDiameter = 0.035f;

    //UI Elements
    public static float ButtonDiameter = 0.1f;
    public static float HealthBarLength = 0.2f;
    public static float HealthBarThickness = 0.04f;

    //endregion

    //region Speeds

    public static float EnemySpeed = 0.45f;
    public static float HeroSpeed = 0.55f;
    public static float BulletSpeed = 0.8f;
    public static float HeartSpeed = 0.2f;

    //endregion

    //region Misc

    public static long StartingEnemySpawnIntervalMs = 650;
    public static float EnemySpawnRateAcceleration = 0.995f;
    public static float EnemyDamage = 0.1f;
    public static float HeartHealthRestore = 0.2f;
    public static long MinShotIntervalMs = 225;
    public static int StartingChainCountToSpawnHeart = 4;

    //endregion
}
