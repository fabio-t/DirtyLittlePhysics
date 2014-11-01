DirtyLittlePhysics
==================

A small particle physics engine, build for speed without too much care about readability and code reuse (although I *do* comment the code).

Will be my little dirty physics engine for games that don't need rigid body physics and most of the stuff that the real physics engines provide :)

##Features

* 3D Particle physics simulator, simple and efficient (public fields, compact array automatically resizing if necessary)
* Position and velocity updated using a modified Verlet Velocity integrator, that is suitable for forces dependent on velocity, too
* Linked to the above, implemented a semi-realistic quadratic drag force (see comments in code for details)
* Buoyancy is taken into consideration for any fluid

## TODO

* A Terrain structure to be passed to the Engine, so that each particle will be simulated in its own "environment" (fluid viscosity and density for example)
* A better storage for the particles, eg a KDtree, to make easier collision detection and neareast neighbours searches. (*not sure to put this inside the engine, but might provide helper classes instead*)

## Nice to have
* Unit tests for the update function
* A bit of documentation
* A graphical demo
