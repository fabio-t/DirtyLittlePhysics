package engine;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shapes.Shape;
import utils.ImmutableVect3D;
import utils.Vect3D;
import collision.BroadPhase;
import environment.World;

public class EngineTest
{
    protected Simulator simulator;

    @Before
    public void setUp() throws Exception
    {
        final World w = new World()
        {

            @Override
            public void process(final Particle p, final double dt)
            {
            }

            @Override
            public ImmutableVect3D getGravity()
            {
                return null;
            }

            @Override
            public void setGravity(final ImmutableVect3D gravity)
            {
            }
        };

        final BroadPhase b = new BroadPhase()
        {
            @Override
            public void add(final Shape s)
            {
            }

            @Override
            public void remove(final Shape s)
            {
            }

            @Override
            public List<Shape> getPossibleCollisions(final Vect3D p)
            {
                return null;
            }

            @Override
            public List<Shape> getCollisions(final Vect3D p)
            {
                return null;
            }
        };

        simulator = new Simulator(w, b);
    }

    @After
    public void tearDown() throws Exception
    {
        simulator = null;
    }

    @Test
    public final void testAddParticle()
    {
        final int particles = simulator.NUM_OF_PARTICLES;

        addParticles(1);

        assertEquals(simulator.NUM_OF_PARTICLES, particles + 1);

        addParticles(100000);

        assertEquals(simulator.NUM_OF_PARTICLES, particles + 1 + 100000);
    }

    @Test
    public final void testRemoveParticle()
    {
        removeAllParticles();

        assertEquals(simulator.NUM_OF_PARTICLES, 0);
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

        assert (simulator.particles.length == 1000);

        // exactly 1000 particles, it shouldn't increase yet
        addParticles(1);

        assert (simulator.particles.length == 1000);

        // overflow, the array should have doubled in size
        addParticles(1);

        assert (simulator.particles.length == 2000);

        removeAllParticles();
    }

    // TODO: tests to verify the physics is doing its job

    private void addParticles(final int n)
    {
        Particle p;

        for (int i = 0; i < n; i++)
        {
            p = new Particle(new Vect3D(10, 10, 10));
            simulator.addParticle(p);
        }
    }

    private void removeAllParticles()
    {
        for (final Particle p : simulator.particles)
            simulator.removeParticle(p);
    }
}
