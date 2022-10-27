# Scene Objects
Scene Objects are used to enable user-interactions.

## Button

The button isn't really a button. It detects if the mouse is currently over the button. When the mouse is clicked, the button can be asked if the mouse is currently hovering over it, which therefore would mean, that the button is pressed.

Currently the button detects the hovering, by giving it the mouse coordinates and using its position and dimensions it calculates if the mouse is in a rectangular area around it. I tried to make advantage of JavaFX's MouseEvents, but it only worked on some instances and completely ignored others.

## Flat Layout

The flat layout is used to display a layout without any function. It basically does nothing.

## Mini Scene

The mini scene works like a scene, but is still a scene object. I did this, to make it easier for me when I have multiple Buttons which I want to move. It's planned for the time-lapse scene, if I want to make it possible to have multiple time-lapse assignment at once.
