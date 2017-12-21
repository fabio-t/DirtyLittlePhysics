/*
 * Copyright (c) 2014-2017 Fabio Ticconi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package shapes;

import collision.Collider;
import utils.Vect3D;

/**
 * @author Fabio Ticconi
 */
public class Box implements Shape
{
    private final Vect3D min;
    private final Vect3D max;

    private final Vect3D center;
    private final Vect3D extent;

    protected Box(final Vect3D min, final Vect3D max)
    {
        this.min = new Vect3D(min);
        this.max = new Vect3D(max);

        center = new Vect3D();
        extent = new Vect3D();

        recalculateCenterExtent();
    }

    public Vect3D getMinPoint()
    {
        return min;
    }

    public void setMinPoint(final Vect3D min)
    {
        this.min.set(min);

        recalculateCenterExtent();
    }

    public Vect3D getMaxPoint()
    {
        return max;
    }

    public void setMaxPoint(final Vect3D max)
    {
        this.max.set(max);

        recalculateCenterExtent();
    }

    public void setMinMax(final Vect3D min, final Vect3D max)
    {
        this.min.set(min);
        this.max.set(max);

        recalculateCenterExtent();
    }

    public void setCenterExtent(final Vect3D center, final Vect3D extent)
    {
        this.center.set(center);
        this.extent.set(extent);

        recalculateMinMax();
    }

    /*
     * (non-Javadoc)
     *
     * @see shapes.Shape#getCenter()
     */
    @Override
    public Vect3D getCenter()
    {
        return center;
    }

    public void setCenter(final Vect3D center)
    {
        this.center.set(center);

        recalculateMinMax();
    }

    public Vect3D getExtent()
    {
        return extent;
    }

    public void setExtent(final Vect3D extent)
    {
        this.extent.set(extent);

        recalculateMinMax();
    }

    private void recalculateMinMax()
    {
        min.set(center).sub(extent);
        max.set(center).add(extent);
    }

    private void recalculateCenterExtent()
    {
        center.set(min).add(max).div(2.0);
        extent.set(max).sub(min).div(2.0);
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

    @Override
    public String toString()
    {
        return String.format("min: %s | max: %s | center: %s | extent: %s", min, max, center, extent);
    }

    /*
     * (non-Javadoc)
     *
     * @see shapes.Shape#intersects(shapes.Shape)
     */
    @Override
    public boolean intersects(final Shape s)
    {
        return s.intersects(this);
    }
}
