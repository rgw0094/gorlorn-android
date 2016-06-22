package gorlorn;

/**
 * Created by Rob on 1/19/2016.
 */
public class Constants
{
    //region Sizes

    //Diameters are percentages of screen width

    //Entities
    public static float EnemyDiameter = 0.052f;
    public static float HeroDiameter = 0.055f;
    public static float BulletDiameter = 0.026f;
    public static float HeartDiameter = 0.035f;

    //UI Elements
    public static float ButtonDiameter = 0.1f;
    public static float HealthBarLength = 0.35f;
    public static float HealthBarThickness = 0.06f;

    //endregion

    //Speeds
    public static float EnemySpeed = 0.65f;             //Percent of the screen traversed per second
    public static float EnemySpeedMultiplier = 1.001f;  //Each new enemy is this much faster than the last
    public static float HeroSpeed = 0.9f;
    public static float HeroAcceleration = 2.0f;        //Percent of the screen width accelerated per second
    public static float BulletSpeed = 0.8f;
    public static float HeartSpeed = 0.2f;

    //Times
    public static int PlayerFrozenOnHitMs = 1200;
    public static int PlayerBlinksOnHitMs = 2000;
    public static int ChainBulletLifeTimeMs = 200;

    //region Misc

    public static long StartingEnemySpawnIntervalMs = 600;
    public static float EnemySpawnRateAcceleration = 0.995f;
    public static float EnemyDamage = 0.2f;
    public static float HeartHealthRestore = 0.2f;
    public static long MinShotIntervalMs = 200;
    public static int StartingChainCountToSpawnHeart = 4;

    public static float EnergyPerShot = 0.18f;   //Percent of energy consumed per shot
    public static float EnergyRegen = 0.55f;     //Percent of energy regenerated per second

    //endregion
}
