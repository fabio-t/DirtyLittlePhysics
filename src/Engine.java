
public class Engine
{
    final static int MAX_NUM_OF_PARTICLES = 200000;
    
    int NUM_OF_PARTICLES;
    Particle particles[];
    
    public Engine()
    {
        particles = new Particle[MAX_NUM_OF_PARTICLES];
        NUM_OF_PARTICLES = 0;
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
    
    /**
     * Verlet Velocity integration
     * @param dt
     */
    public void update(double dt)
    {
        int i;
        
        Particle p;
        Vect3D acc;
        for (i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];
            acc = p.acc;
            
            acc.x = 0d;
            acc.y = 0d;
            acc.z = -9.81d; // gravity
        }
        
        // TODO: apply forces to
        // each particle.
        // Inside, it should
        // do something like:
        // acc.x += (force.x / mass)
        // acc.y += (force.y / mass)
        // acc.z += (force.z / mass)
 
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
