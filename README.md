<h1><b>BenchWizard – a gaming benchmark</b></h1>

<img src="http://puu.sh/jrLud/560b5499f2.png"/>

<p><b>1. Introduction</b></p>

<p>This benchmark is meant to test the “gaming” capabilities of a computer, with an emphasis on the ability to run 2D games. As such, the requirements for a high score from this benchmark are: a good CPU, good OS and drivers memory management, a fair amount of RAM and the presence of a dedicated video card – the more powerful, the better, but not by very much. Also, the best case scenario would happen when running BenchWizard on a low load of the previously mentioned components individually – that means that in the absence of the best possible performance of one component, the others will not pick up the slack, each having their specialized contribution to the score.</p>

<p>I chose to make this as my project because of my interest in game development. I felt that developing a game-like benchmark would help me learn the best way to develop different game functionalities and their impact on the hardware. Also, I think I will reuse some of the systems I implemented in the development of this benchmark for my future projects, at least to some extent (the system that deals with sprites for example). Finally, I think that the most important attribute of a software application is its ability to capture its users’ attention and make them keep using it. Games do that by enforcing positive tactics like high entertainment value and low knowledge and experience requirements for their users.</p>

<p><b>2. Design and implementation</b></p>

<p>Having to deal with different architectures, the best way to measure performance of a system for a specific task is to empirically test that task on the system. What’s a better way to test 2D gaming capabilities of a computer than to run a generic 2D game on it and measure the performance? This is the idea behind my benchmark.</p>

<p>BenchWizard is, for all intents and purposes, a 2D game, albeit not a very fun one. Player inputs will take a tiny wizard through a maze at the end of which a score will be output. The score is represented by the average FPS (frames per second) or the average number of times per second the image is drawn on screen throughout the current run.</p>

<p>The game is displayed within a window frame (java JFrame object) as a runnable (implementing the java Runnable interface) canvas (java Canvas object). The image is drawn (on a java Graphics object background) on a screen (java Screen object). The screen information is stored in a buffer (with the help of java BufferStrategy) and is being updated several times a second. Individual objects found within the frame are taken from .png image files and stored in integer arrays as the ARGB (alpha, red, green, blue) values for their individual pixels. Another aspect that influences performance other than the constant redrawing of the visuals is the background music, which runs in a separate thread from a stream input of a .wav audio file. Most other performance draining processes I’ve tried to lessen the impact of, such as arithmetic calculations. One that stands out is division by 16 (it happens a lot because of the nature of the graphics that are 16x16) which I’ve replaced with right shift by 4.</p>

<p>The way graphics are managed is by defining objects of a fixed size (16 x 16) which hold specific visual information, called tiles. The player character is the size of one tile, but the tile is different depending on the direction of movement and the instance of movement (right step, left step, standing). The player tiles are imported from a single image (shown below) based on their position within the image. Pink is ignored to account for transparent pixels easier.</p>

<img src="http://puu.sh/jrLxK/f2ef0a03ac.png" />

<p>The tile information is saved pixel by pixel. Dealing with surface tiles (wood, brick, grass and so on) happens the same way. Surface tiles also hold collision information, more specifically whether the tile can be walked on or perhaps if the tile is breakable (didn’t use breakable tiles but they are possible with this implementation). Levels are constructed by placing specific tiles in a predefined manner or randomly. The zoomed in input image file for the design of a predefined level is shown below:</p>

<img src="http://puu.sh/jrLHR/11544031ec.png" />

<p>Each pixel translates into a specific tile based on its color. The size of the level is also given by the size of the input image file (as many 16x16 tiles as there are pixels). Each tile is rendered on the screen individually, regardless of whether it’s the player tile or part of the level.</p>

<p>Key input is dealt with by means of an object whose class implements KeyListener. It is also the input that decides the direction of the player and as such the tile which will be rendered on the screen. This variable input and the fact that only the visible part of the level is drawn makes it certain that no information that needs to be rendered can be estimated and optimized making it so that the FPS is constant throughout the run.</p>

<p><b>3. Usage</b></p>

<p>BenchWizard is very user friendly and will seem familiar to most people that would be interested in a gaming benchmark. The steps for obtaining your computer’s score are:</p>

<p>- Start the game and you’re presented with a JOptionPane message that read “Use WASD or the arrow keys to find the exit!”.</p>

<p>- Once you confirm reading the directions follow them! The maze that follows is not very complicated – the aim was to entertain while achieving the goal in less than a minute, not to frustrate the user.</p>

<p>- At the end of the maze there’s a boat waiting for you. Get on the boat.</p>

<p>- You’re confronted with another JOptionPane which displays your score as well as a customized message based on your score.</p>

<p>The customized message is displayed based on the score as follows:</p>

<p>· 0-50 - How did you make this run on a toaster?</p>

<p>· 50-100 - 2004 was a great year to buy a PC.</p>

<p>· 100-300 - Your netbook runs within normal parameters.</p>

<p>· 300-700 - You can probably run most indie games.</p>

<p>· 700-1000 - Not bad. You can run most games.</p>

<p>· 1000-1800 - You've got a decent gaming rig.</p>

<p>· 1800-2400 - Ultra settings all the way.</p>

<p>· 2400-3000 - Your PC could feed a small African country for 3.78 weeks.</p>

<p>· &gt;3000 - I hope your trip from Alpha Centauri was pleasant.</p>

<p>The FPS count is computed and saved once every second. Average fps is computed as a division between the sum of frames for each second and the number of counted seconds. The average FPS displayed as a score is computed starting at the 5<sup>th</sup> second, where I found that the FPS count stabilizes even on weaker systems. I decided to only display the average FPS count because it holds the most value to the user and for the sake of simplicity. The current FPS count can always be seen in the title of the application frame.</p>

<p><b>BenchWizard was developed as a benchmarking laboratory project.</b></p>
<h2>To see it in action run BenchWizard.jar</h2>
