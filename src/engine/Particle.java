/**
 * Copyright 2014 Fabio Ticconi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package engine;

import utils.HashGrid.Point;
import utils.Maths;
import utils.Vect3D;


/**
 * Particle class, including movement vectors and size/weight properties.
 * <br /><br />
 * 
 * Includes some helper functions to set the mass and radius, and
 * automatically set the density assuming the Particle
 * is a sphere.
 * 
 * @author Fabio Ticconi
 */
public class Particle implements Point
{
    double mass;
    double radius;
    double density;
    
    Vect3D pos;
    Vect3D vel;
    Vect3D acc;
    
    public Particle()
    {
        this.mass    = 1d;
        this.radius  = 1d;
        this.density = Maths.sphereDensity(mass, radius);
        
        this.pos = new Vect3D();
        this.vel = new Vect3D();
        this.acc = new Vect3D();
    }
    
    public Particle(double mass, double radius, Vect3D pos, Vect3D vel, Vect3D acc)
    {
        this.mass    = mass;
        this.radius  = radius;
        this.density = Maths.sphereDensity(mass, radius);
        
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
    }
    
    public void setMass(double mass)
    {
        this.mass = mass;
        this.density = Maths.sphereDensity(mass, radius);
    }
    
    public void setRadius(double radius)
    {
        this.radius = radius;
        this.density = Maths.sphereDensity(mass, radius);
    }

    @Override
    public double getX()
    {
        return pos.x;
    }

    @Override
    public double getY()
    {
        return pos.y;
    }

    @Override
    public double getZ()
    {
        return pos.z;
    }
}
