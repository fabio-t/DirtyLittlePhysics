
public class Particle
{
    double mass;
    double radius;
    
    Vect3D pos;
    Vect3D vel;
    Vect3D acc;
    
    public Particle()
    {
        mass = 1d;
        radius = 1d;
        
        pos = new Vect3D();
        vel = new Vect3D();
        acc = new Vect3D();
    }
    
    public Particle(double mass, double radius, Vect3D pos, Vect3D vel, Vect3D acc)
    {
        this.mass   = mass;
        this.radius = radius;
        
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
    }
}
