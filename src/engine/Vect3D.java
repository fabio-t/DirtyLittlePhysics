package engine;

public class Vect3D
{
    double x;
    double y;
    double z;
    
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
