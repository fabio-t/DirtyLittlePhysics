
public class Engine
{
    final static int MAX_NUM_OF_PARTICLES = 200000;
    
    Vect3D gravity;
    double drag;
    
    int NUM_OF_PARTICLES;
    Particle particles[];
    
    public Engine()
    {
        particles = new Particle[MAX_NUM_OF_PARTICLES];
        NUM_OF_PARTICLES = 0;
        
        gravity = new Vect3D(0, 0, -9.81d);
        drag    = 0.5;
    }
    
    /**
     * Sets the gravity. It will be directly
     * added to each component of each particles'
     * acceleration. The default is Earth's gravity
     * vector, (0, 0, -9.81)
     * 
     * @param gravity
     */
    public void setGravity(Vect3D gravity)
    {
        this.gravity = gravity;
    }
    
    /**
     * Sets the coefficient of the linear
     * drag applied to any moving particle.
     * It is applied as a force before calculating the
     * acceleration.
     * 
     * Should be a number minor than zero, as it is
     * defined as:
     * 
     * Fd = - b*V
     * 
     * where V is the velocity vector of the particle
     * at each updated.
     * 
     * @param drag
     */
    public void setDragCoefficient(double drag)
    {
        this.drag = drag;
    }
    
    /**
     * Adds a new particle to the engine.
     * O(1)
     * @param p
     */
    public void addParticle(Particle p)
    {
        if (NUM_OF_PARTICLES > MAX_NUM_OF_PARTICLES || p == null)
            return;
                
        particles[NUM_OF_PARTICLES++] = p;
    }
    
    /**
     * Removes a particle from the engine,
     * if it was there.
     * O(N)
     * @param p
     */
    public void removeParticle(Particle p)
    {
        if (p == null)
            return;
        
        for (int i = 0; i < NUM_OF_PARTICLES; i++)
        {
            if (particles[i] == p)
            {
                particles[i] = particles[--NUM_OF_PARTICLES];
                break;
            }
        }
    }
    
    private Vect3D getCumulativeForce(Vect3D velocity)
    {
        // TODO: only one for now, drag
        Vect3D force = new Vect3D();
        
        force.x = - drag*velocity.x;
        force.y = - drag*velocity.y;
        force.z = - drag*velocity.z;
        
        return force;
    }
    
    /**
     * Verlet Velocity integration
     * @param dt
     */
    public void update(double dt)
    {
        double drag = 0.5;
        
        int i;
        
        Particle p;
        Vect3D force;
        Vect3D acc;
        for (i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];
            acc = p.acc;
            
            force = getCumulativeForce(p.vel);
            
            acc.x = (force.x / p.mass) + gravity.x;
            acc.y = (force.y / p.mass) + gravity.y;
            acc.z = (force.z / p.mass) + gravity.z;
        }
 
        // half the delta t, to save
        // a few divisions
        double dt2 = dt / 2d;
        
        Vect3D pos;
        Vect3D vel;
        for (i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];
            pos = p.pos;
            vel = p.vel;
            acc = p.acc;
            
            pos.x += dt * (vel.x + (dt2 * acc.x));
            pos.y += dt * (vel.y + (dt2 * acc.y));
            pos.z += dt * (vel.z + (dt2 * acc.z));
        }
        
        // TODO: apply forces again as before,
        // this time without zeroing the acceleration
        
        for (i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];
            vel = p.vel;
            acc = p.acc;
            
            vel.x += dt2 * acc.x;
            vel.y += dt2 * acc.y;
            vel.z += dt2 * acc.z;
        }
    }
    
    public static void main(String[] args)
    {
        final double STEP = 0.01d;
        
        Engine w = new Engine();
        
        Particle p = new Particle();
        p.vel.x = 1d;
        p.vel.z = 100d;
        w.addParticle(p);

        long time1 = System.currentTimeMillis();
        System.out.print("Adding particles.. ");
        for (int i = 0; i < 100000; i++)
        {
            Particle p2 = new Particle();
            p2.vel.x = 1d;
            w.addParticle(p2);
        }
        long time2 = System.currentTimeMillis();
        System.out.println((time2 - time1) + " ms");
        
        System.out.println("Pos: " + p.pos.x + " " + p.pos.y + " " + p.pos.z);
        System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        
        time1 = System.currentTimeMillis();
        System.out.print("Moving forward the simulation.. ");
        for (double dt = 0d; dt < 10d; dt = dt + STEP)
        {
            w.update(STEP);
        }
        time2 = System.currentTimeMillis();
        System.out.println((time2 - time1) + " ms");
        
        System.out.println("Pos: " + p.pos.x + " " + p.pos.y + " " + p.pos.z);
        System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        
        time1 = System.currentTimeMillis();
        System.out.print("Moving forward the simulation.. ");
        for (double dt = 0d; dt < 10d; dt = dt + STEP)
        {
            w.update(STEP);
        }
        time2 = System.currentTimeMillis();
        System.out.println((time2 - time1) + " ms");
        
        System.out.println("Pos: " + p.pos.x + " " + p.pos.y + " " + p.pos.z);
        System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        
        time1 = System.currentTimeMillis();
        System.out.print("Moving forward the simulation.. ");
        for (double dt = 0d; dt < 10d; dt = dt + STEP)
        {
            w.update(STEP);
        }
        time2 = System.currentTimeMillis();
        System.out.println((time2 - time1) + " ms");
        
        System.out.println("Pos: " + p.pos.x + " " + p.pos.y + " " + p.pos.z);
        System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
    }
}
