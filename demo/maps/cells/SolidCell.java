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
package maps.cells;

import collision.Static;
import engine.Particle;
import maps.Cell;
import utils.ImmutableVect3D;
import utils.Vect3D;

/**
 * @author Fabio Ticconi
 */
public class SolidCell extends Static implements Cell
{
    public SolidCell()
    {
        super(new Vect3D(), new Vect3D());

        friction = 0.4;
    }

    /*
     * (non-Javadoc)
     *
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p, final double dt)
    {
        // FIXME: this is becoming useless, the cell division could probably
        // all go inside SimpleMap

        return new Vect3D(ImmutableVect3D.zero);
    }

    /*
     * (non-Javadoc)
     *
     * @see environment.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        return 1.0;
    }
}
