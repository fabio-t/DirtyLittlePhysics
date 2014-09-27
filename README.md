DirtyLittlePhysics
==================

A small particle physics engine, build for speed without too much care about readability.

Will be my little dirty physics engine for games that don't even need rigid body physics.

The initial features will only be:

  * Particle system in 3D
  * Each particle has a position in space, velocity and acceleration
  * Each particle is subject to forces that change the acceleration, and in turn velocity and position
  * Each particle is isolated from the others, unless an explicit link is established.

By an update method, the simulation can be moved forward. Pretty common stuff.

"Collision" detection and nearest neighbours search are the second step.
