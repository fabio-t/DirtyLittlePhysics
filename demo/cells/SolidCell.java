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
package cells;

import shapes.Box;
import utils.Vect3D;
import engine.Cell;
import engine.Particle;
import engine.Simulator;

/**
 * 
 * @author Fabio Ticconi
 */
public class SolidCell implements Cell, Box
{
    private final Vect3D min;
    private final Vect3D max;

    public SolidCell()
    {
        min = new Vect3D();
        max = new Vect3D();
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#canPass(engine.Particle)
     */
    @Override
    public boolean canPass(final Particle p)
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p)
    {
        return Simulator.nullVector;
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        return 1.0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMinPoint()
     */
    @Override
    public Vect3D getMinPoint()
    {
        return min;
    }

    public void setMinPoint(final Vect3D v)
    {
        min.assign(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMaxPoint()
     */
    @Override
    public Vect3D getMaxPoint()
    {
        return max;
    }

    public void setMaxPoint(final Vect3D v)
    {
        max.assign(v);
    }
}
