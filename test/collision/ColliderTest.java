package collision;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import shapes.Box;
import utils.Vect3D;

public class ColliderTest
{
    static Box    b1;
    static Box    b2;
    static Box    b3;

    static Vect3D p1;
    static Vect3D p2;
    static Vect3D p3;
    static Vect3D p4;
    static Vect3D p5;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        b1 = new Box(new Vect3D(112.0, -50.0, -26.0), new Vect3D(267.0, 50.0, 88.0));
        b2 = new Box(new Vect3D(200.0, 0.0, 51.0), new Vect3D(500.0, 1000.0, 250.0));
        b3 = new Box(new Vect3D(600.0, 1300.0, 600.0), new Vect3D(1000.0, 2000.0, 900.0));

        p1 = new Vect3D(50.0, 100.0, 200.0);
        p2 = new Vect3D(170.0, -10.0, 48.0);
        p3 = new Vect3D(200.0, 0.0, 80.0);
        p4 = new Vect3D(800.0, 1800.0, 800.0);
        p5 = new Vect3D(1100.0, 1800.0, 800.0);
    }

    @Test
    public void testTestBoxBox() throws Exception
    {
        assertTrue(Collider.test(b1, b1));
        assertTrue(Collider.test(b1, b2));
        assertTrue(Collider.test(b2, b1));
        assertTrue(Collider.test(b2, b2));
        assertTrue(Collider.test(b3, b3));

        assertFalse(Collider.test(b1, b3));
        assertFalse(Collider.test(b2, b3));
    }

    @Test
    public void testTestSphereSphere() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testTestSphereBox() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testTestVect3DSphere() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testTestVect3DBox() throws Exception
    {
        assertFalse(Collider.test(p1, b1));
        assertFalse(Collider.test(p1, b2));
        assertFalse(Collider.test(p1, b3));

        assertTrue(Collider.test(p2, b1));
        assertFalse(Collider.test(p2, b2));
        assertFalse(Collider.test(p2, b3));

        assertTrue(Collider.test(p3, b1));
        assertTrue(Collider.test(p3, b2));
        assertFalse(Collider.test(p3, b3));

        assertFalse(Collider.test(p4, b1));
        assertFalse(Collider.test(p4, b2));
        assertTrue(Collider.test(p4, b3));

        assertFalse(Collider.test(p5, b1));
        assertFalse(Collider.test(p5, b2));
        assertFalse(Collider.test(p5, b3));
    }

    @Test
    public void testClosestPointToSegment() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testSqDistPointSegment() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testIntersectRayBox() throws Exception
    {
        throw new RuntimeException("not yet implemented");
    }
}
