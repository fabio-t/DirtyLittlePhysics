import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EngineTest
{
    protected Engine engine;
        
    @Before
    public void setUp() throws Exception
    {
        engine = new Engine();
    }
    
    @After
    public void tearDown() throws Exception
    {
        engine = null;
    }
    
    @Test
    public final void testAddParticle()
    {
        int particles = engine.NUM_OF_PARTICLES;
        
        addParticles(1);
        
        assertEquals(engine.NUM_OF_PARTICLES, particles + 1);
        
        addParticles(100000);
        
        assertEquals(engine.NUM_OF_PARTICLES, particles + 1 + 100000);
    }
    
    @Test
    public final void testRemoveParticle()
    {        
        removeAllParticles();
        
        assertEquals(engine.NUM_OF_PARTICLES, 0);
    }
    
    @Test
    /**
     * When the number of particles becomes exactly equal to
     * 1000*2^N where N is an integer, the array will double
     * in size.
     */
    public final void testExtendArray()
    {
        removeAllParticles();
        
        // the array shouldn't increase
        addParticles(999);
        
        assert(engine.particles.length == 1000);
        
        // exactly 1000 particles, it shouldn't increase yet
        addParticles(1);
        
        assert(engine.particles.length == 1000);
                
        // overflow, the array should have doubled in size
        addParticles(1);
        
        assert(engine.particles.length == 2000);
        
        removeAllParticles();
    }
    
    // TODO: tests to verify the physics is doing its job
    
    private void addParticles(int n)
    {
        Particle p;
        
        for (int i = 0; i < n; i++)
        {
            p = new Particle();
            engine.addParticle(p);
        }
    }
    
    private void removeAllParticles()
    {
        for (Particle p : engine.particles)
        {
            engine.removeParticle(p);
        }
    }
}
