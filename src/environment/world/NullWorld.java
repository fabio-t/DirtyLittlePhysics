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
package environment.world;

import engine.Particle;
import engine.Simulator;
import environment.World;
import utils.ImmutableVect3D;
import utils.Vect3D;

/**
 * If a {@link World} is not set, {@link Simulator} will
 * default to this. Gravity is zero and no force is ever
 * returned.
 *
 * @author Fabio Ticconi
 */
public class NullWorld implements World
{
    /*
     * (non-Javadoc)
     *
     * @see environment.World#process(engine.Particle, double)
     */
    @Override
    public void process(final Particle p, final double dt)
    {
        // do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see environment.World#getForces(engine.Particle, double)
     */
    @Override
    public Vect3D getForces(final Particle p, final double dt)
    {
        return new Vect3D(0.0, 0.0, 0.0);
    }

    /*
     * (non-Javadoc)
     *
     * @see environment.World#getGravity()
     */
    @Override
    public ImmutableVect3D getGravity()
    {
        return ImmutableVect3D.zero;
    }

    /*
     * (non-Javadoc)
     *
     * @see environment.World#setGravity(utils.ImmutableVect3D)
     */
    @Override
    public void setGravity(final ImmutableVect3D gravity)
    {
        // do nothing
    }

}
