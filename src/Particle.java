
/**
 * Particle class, including movement vectors and size/weight properties.
 * 
 * Includes some helper functions to set the mass and radius.
 * 
 * Note that they automatically set the density assuming the Particle
 * is a sphere.
 */
public class Particle
{
    double mass;
    double radius;
    double density;
    
    Vect3D pos;
    Vect3D vel;
    Vect3D acc;
    
    public Particle()
    {
        this.mass    = 1d;
        this.radius  = 1d;
        this.density = sphereDensity(mass, radius);
        
        this.pos = new Vect3D();
        this.vel = new Vect3D();
        this.acc = new Vect3D();
    }
    
    public Particle(double mass, double radius, Vect3D pos, Vect3D vel, Vect3D acc)
    {
        this.mass    = mass;
        this.radius  = radius;
        this.density = sphereDensity(mass, radius);
        
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
    }
    
    public void setMass(double mass)
    {
        this.mass = mass;
        this.density = sphereDensity(mass, radius);
    }
    
    public void setRadius(double radius)
    {
        this.radius = radius;
        this.density = sphereDensity(mass, radius);
    }
    
    private double sphereDensity(double mass, double radius)
    {
        return (mass * 3d) / (4d * Math.PI * Math.pow(radius, 3d));
    }
}
