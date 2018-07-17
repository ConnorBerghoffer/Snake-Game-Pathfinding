# Introduction
Hi there! this project is a simple snake game I made in my spare time. <br>
One day, I decided that just playing the game wasn't enough, so I decided to make some **Super Advanced Machine Learning Artificial Intellegence** to play the game for me. <br> <br>
So I got to work make this **Super Advanced Machine Learning Artificial Intellegence**. but it took too long so I made a simple pathfinding AI that does the job just fine.

## The Code...
All the controls is in there, so if you want the game to play normally, just get rid of the pathfinding part and you are good to go! 
<br><br>
### Pathfinding:
```
//Find And Follow The Apple
		if (!triggered) {
			if (apple.getX() > head.getX()) {
				left = false;
				right = true;
			} else if (apple.getX() < head.getX()) {
				left = true;
				right = false;
			} else if (apple.getX() == head.getX()) {
				triggered = true;
				left = false;
				right = false;

				if (apple.getY() > head.getY()) {
					up = false;
					down = true;
				} else if (apple.getY() < head.getY()) {
					up = true;
					down = false;
				}
			}
		}
```
You can find this on line **166** in the **GamePanel.java**, delete it if you want to control the game.

## There Is One Problem...
The snake has problems running into its own tail because I have not implemented any means of avoiding it. <br>
If you have a solution to this, feel free to contact me.
