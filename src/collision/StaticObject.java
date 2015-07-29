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
package collision;

import shapes.Box;
import utils.Vect3D;

/**
 * Represents a static object, not participating in the
 * simulation but still placed on the world and included
 * in the collision checks.
 * 
 * <br/>
 * <br/>
 * 
 * Static objects are stored as AABB, axis-aligned
 * bounding boxes.
 * 
 * @author Fabio Ticconi
 */
public class StaticObject extends Box
{
    final Vect3D center;
    final Vect3D extent;

    public StaticObject(final Vect3D min, final Vect3D max)
    {
        super(new Vect3D(min), new Vect3D(max));

        center = new Vect3D();
        extent = new Vect3D();

        recalculateCenterExtent();
    }

    public void setCenterExtent(final Vect3D center, final Vect3D extent)
    {
        this.center.set(center);
        this.extent.set(extent);

        recalculateMinMax();
    }

    public void setCenter(final Vect3D center)
    {
        this.center.set(center);

        recalculateMinMax();
    }

    public void setExtent(final Vect3D extent)
    {
        this.extent.set(extent);

        recalculateMinMax();
    }

    @Override
    public void setMinPoint(final Vect3D min)
    {
        super.setMinPoint(min);

        recalculateCenterExtent();
    }

    @Override
    public void setMaxPoint(final Vect3D max)
    {
        super.setMaxPoint(max);

        recalculateCenterExtent();
    }

    @Override
    public void setMinMax(final Vect3D min, final Vect3D max)
    {
        super.setMinMax(min, max);

        recalculateCenterExtent();
    }

    @Override
    public Vect3D getCenter()
    {
        return center;
    }

    public Vect3D getExtent()
    {
        return extent;
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

    @Override
    public String toString()
    {
        return String.format("min: %s | max: %s | center: %s | extent: %s", min, max, center, extent);
    }
}
