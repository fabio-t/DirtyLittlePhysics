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
package collision;

import shapes.Box;
import utils.Vect3D;

/**
 * @author Fabio Ticconi
 */
public class Static extends Box
{
    protected double friction;

    /**
     * @param min
     * @param max
     */
    protected Static(final Vect3D min, final Vect3D max)
    {
        super(min, max);
    }

    /**
     * @param min
     * @param max
     * @param friction
     */
    public Static(final Vect3D min, final Vect3D max, final double friction)
    {
        super(min, max);

        this.friction = friction;
    }

    public double getFriction()
    {
        return friction;
    }

    public void setFriction(final double friction)
    {
        this.friction = friction;
    }
}
