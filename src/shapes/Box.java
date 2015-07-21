/**
 * Copyright 2015 Fabio Ticconi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shapes;

import utils.Vect3D;
import collision.Collider;

/**
 * 
 * @author Fabio Ticconi
 */
public class Box implements Shape
{
    protected final Vect3D min;
    protected final Vect3D max;

    public Box(final Vect3D min, final Vect3D max)
    {
        this.min = min;
        this.max = max;
    }

    public Vect3D getMinPoint()
    {
        return min;
    }

    public void setMinPoint(final Vect3D min)
    {
        this.min.set(min);
    }

    public Vect3D getMaxPoint()
    {
        return max;
    }

    public void setMaxPoint(final Vect3D max)
    {
        this.max.set(max);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(shapes.Box)
     */
    @Override
    public boolean intersects(final Box b)
    {
        return Collider.test(this, b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(shapes.Sphere)
     */
    @Override
    public boolean intersects(final Sphere s)
    {
        return Collider.test(s, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(utils.Vect3D)
     */
    @Override
    public boolean intersects(final Vect3D p)
    {
        return Collider.test(p, this);
    }
}
