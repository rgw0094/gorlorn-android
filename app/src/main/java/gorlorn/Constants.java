package gorlorn;

/**
 * Created by Rob on 1/19/2016.
 */
public class Constants
{
    //Debug flags
    public static boolean IsDebugMode = false;
    public static boolean AutoClearStats = false;
    public static boolean DieInOneHit = false;

    //region Sizes

    //Diameters are percentages of screen width

    //Entities
    public static float EnemyDiameter = 0.058f;
    public static float HeroDiameter = 0.056f;
    public static float BulletDiameter = 0.026f;
    public static float HeartDiameter = 0.035f;

    //UI Elements
    public static float ButtonDiameter = 0.1f;
    public static float HealthBarLength = 0.35f;
    public static float HealthBarThickness = 0.06f;

    //endregion

    //Speeds
    public static float EnemySpeed = 0.7f;             //Percent of the screen traversed per second
    public static float HeroSpeed = 0.9f;
    public static float HeroAcceleration = 8.0f;        //Percent of the screen width accelerated per second
    public static float BulletSpeed = 0.8f;
    public static float HeartSpeed = 0.3f;

    //Times
    public static int PlayerFrozenOnHitMs = 2000;
    public static int PlayerBlinksOnHitMs = 3000;
    public static int ChainBulletLifeTimeMs = 300;

    //region Misc

    public static long StartingEnemySpawnIntervalMs = 400;
    public static float EnemySpawnRateAcceleration = 0.995f;    //The enemy spawn rate is multiplied by this every second
    public static float EnemySpeedIncrement = 0.0008f;           //This is added to the starting enemy speed each second
    public static long MaxComboSize = 20;
    public static float EnemyDamage = 20.0f;
    public static float PlayerHealth = 140.0f;
    public static float HeartHealthRestore = 40.0f;
    public static long MinShotIntervalMs = 250;
    public static int StartingChainCountToSpawnHeart = 4;

    //endregion
}
