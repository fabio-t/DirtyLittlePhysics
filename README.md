DirtyLittlePhysics
==================

A small particle physics engine, built to be easy to use, efficient but up to a point (so that code stays readable and good for learning).

Will be my little dirty physics engine for games that don't need rigid body physics and most of the stuff that the real physics engines provide :)

##Features

* 3D Particle physics simulator, simple and efficient (public fields, compact array automatically resizing if necessary)
* Verlet Velocity integrator for stable simulations even with complex position- and velocity-specific forces
* Map interface, made of Cells. A SolidCell and a FluidCell are provided but not obligatory
** Within FluidCell, you can see how to apply drag & buoyancy
* Gravity (simulator-wide for now)
* Efficient collision test primitives for Point, Sphere and Box shapes
* Graphical demo to play with particles and immediately see how this works (also good to see a fixed-timestep, variable rendering loop)
* Text based demo to see the FPS/particles number limits on your platform

## TODO

* Constraints (user-specified forces that act on conditions like simulation time, particle position/velocity/acceleration, etc)
* Static objects collision detection and resolution
* Terrain management, for now postponed to the user (through the Map and Cell interfaces that need to be implemented)
* More interesting Shapes, like Capsules

## Nice to have

* A tutorial, as soon as the API gets stable
* Tests for the update function to verify correctness
