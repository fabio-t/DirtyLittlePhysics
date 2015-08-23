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
package collision.broadphase;

import java.util.ArrayList;
import java.util.List;

import utils.Vect3D;
import collision.BroadPhase;
import collision.Static;
import engine.Simulator;

/**
 * If no BroadPhase manager is set, {@link Simulator} will
 * default to this. No collision will ever be returned.
 * 
 * @author Fabio Ticconi
 */
public class NullBroadPhase implements BroadPhase
{
    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#add(shapes.Static)
     */
    @Override
    public void add(final Static s)
    {
        // does nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#remove(shapes.Static)
     */
    @Override
    public void remove(final Static s)
    {
        // does nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#getPossibleCollisions(utils.Vect3D)
     */
    @Override
    public List<Static> getPossibleCollisions(final Vect3D p)
    {
        return new ArrayList<Static>(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#getCollisions(utils.Vect3D)
     */
    @Override
    public List<Static> getCollisions(final Vect3D p)
    {
        return new ArrayList<Static>(0);
    }
}
