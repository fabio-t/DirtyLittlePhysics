
public class Particle
{
    double mass;
    
    Vect3D pos;
    Vect3D vel;
    Vect3D acc;
    
    public Particle()
    {
        mass = 0d;
        
        pos = new Vect3D();
        vel = new Vect3D();
        acc = new Vect3D();
    }
}
