package utils;

public class Maths
{    
    public static double sphereDensity(double mass, double radius)
    {
        return mass / sphereVolume(radius);
    }
    
    public static double sphereVolume(double radius)
    {
        return (4d / 3d) * Math.PI * radius * radius * radius;
    }
}
