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
package utils;

/**
 * 
 * Vect3D
 * 
 * @author Fabio Ticconi
 */
public class Vect3D
{
    public double x;
    public double y;
    public double z;
    
    public Vect3D()
    {
        this.x = 0d;
        this.y = 0d;
        this.z = 0d;
    }
    
    public Vect3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vect3D(final Vect3D v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public Vect3D assign(final Vect3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
        
        return this;
    }
    
    public Vect3D assign(double d)
    {
        x = d;
        y = d;
        z = d;
        
        return this;
    }
    
    public Vect3D add(final Vect3D v)
    {
        x += v.x;
        y += v.y;
        z += v.z;
        
        return this;
    }
    
    public Vect3D add(double d)
    {
        x += d;
        y += d;
        z += d;
        
        return this;
    }
    
    public Vect3D sub(final Vect3D v)
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        
        return this;
    }
    
    public Vect3D sub(double d)
    {
        x -= d;
        y -= d;
        z -= d;
        
        return this;
    }
    
    public Vect3D mul(final Vect3D v)
    {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        
        return this;
    }
    
    public Vect3D mul(double d)
    {
        x *= d;
        y *= d;
        z *= d;
        
        return this;
    }
    
    public Vect3D div(final Vect3D v)
    {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        
        return this;
    }
    
    public Vect3D div(double d)
    {
        x /= d;
        y /= d;
        z /= d;
        
        return this;
    }
    
    public static Vect3D add(final Vect3D v1, final Vect3D v2)
    {
        Vect3D v = new Vect3D();
        
        v.assign(v1);
        v.add(v2);
        
        return v;
    }
    
    public static Vect3D sub(final Vect3D v1, final Vect3D v2)
    {
        Vect3D v = new Vect3D();
        
        v.assign(v1);
        v.sub(v2);
        
        return v;
    }
    
    public static Vect3D mul(final Vect3D v1, final Vect3D v2)
    {
        Vect3D v = new Vect3D();
        
        v.assign(v1);
        v.mul(v2);
        
        return v;
    }
    
    public static Vect3D div(final Vect3D v1, final Vect3D v2)
    {
        Vect3D v = new Vect3D();
        
        v.assign(v1);
        v.div(v2);
        
        return v;
    }
}
