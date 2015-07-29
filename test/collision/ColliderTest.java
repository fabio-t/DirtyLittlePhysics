package collision;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import shapes.Box;
import utils.Vect3D;

public class ColliderTest
{
    static Box b1;
    static Box b2;
    static Box b3;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        b1 = new StaticObject(new Vect3D(112.0, -50.0, -26.0), new Vect3D(267.0, 50.0, 88.0));
        b2 = new StaticObject(new Vect3D(200.0, 0.0, 51.0), new Vect3D(500.0, 1000.0, 250.0));
        b3 = new StaticObject(new Vect3D(600.0, 1300.0, 600.0), new Vect3D(1000.0, 2000.0, 900.0));
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
}
