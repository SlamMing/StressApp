#StressApp presentation
Android app for stress detection, it uses data from the accelerometer and inputs from the user and tries to predict 
the level of stress based on collected data and draws a tree based on how stressed you are.
This was the project I presented as my bachelor thesis, it uses simple machine learning to predict stress,
the input data was sampled because 3 (x, y, z) x 60 (refresh rate of on-board accelerometer) floats each second was 
stressful (pun intended) for the memory and the battery, to solve this it was decided to gather a batch of data for example
each minute and then extract from this data some statistical values like mean, median, skew..etc and just save these instead, this drastically reduced the amount of data while maintaining a fairly good representation of that minute of data.
 
Since the data gathering is being run as a service (always running even if the app is closed), only the last minute of data is kept on the RAM, and when the user adds an input through the buttons "stressed", "normal" and "chilled", then the apps saves the last minute of data.

This data is then used to update the new centroid of a stress level, which will then be used to predict the stress in the new data using simple k-means.

The tree is drawn on an applet using Processing graphics library, it is a simple particle system that leaves a trail going upwards drawing the tree, it starts with a single big particle that has a chance to split into 2 or more of the same particle, effectively branching our tree, the particle will lose radius over time and when splitting, when it reaches a certain radius it will die and leave a leaf where it stopped.

This is a really lightweight implementation, it ran smoothly on a Huawei P8 lite and a Samsung Galaxy S4, further improvements could be made like using a small neural network (which kinda seems overkill) to predict stress and have some kind of outlier detection during the raw data gathering.

This was made 3 years ago, when this repository was created, I'm only uploading it now because i kinda forgot to do so, it's been sitting on my laptop since.