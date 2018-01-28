# CS5011-NettleSweeper
One project I did as an assignement for CS5011 (AI practices) during my Masters at the University of St Andrews. 
 
 ## The assignement
 This assignement was written by Alice Toniolo, School of Computer Science, University of St Andrews 2017-18
 
### Introduction
The nettle sweeper game is inspired by the well-known Mineswepeer computer game1
* The nettle sweeper world is a square grid of NxN squares with M nettles scattered among them.
Any square can be probed by the agent in any order. If the agent probes a square that
contains a nettle, the game is over. Otherwise, a number appears that indicates the number
of nettles in the 8 adjacent neighbours of the probed square. While the rules of the nettle
sweeper game are the same as those of Minesweeper, the main different resides in the type of
worlds used. The nettle worlds used for this practical are a subset of those of Minesweeper
and will be guaranteed to be solved by logical inference, starting from the first top left hand
cell. In this practical, we will develop a number of algorithms that can be used by the agent
to solve the nettle sweeper game.

### The task
For this practical, the nettle worlds to be used are provided in three files according to
different levels of difficulty, easy.txt, medium.txt, hard.txt. The worlds are represented as
Java arrays filled with integers, where each integer represents a cell with the following code:
* -1 means that the cell contains a mine
* 0–8 is the number of nettles in the 8 adjacent neighbours

Each file contains 5 examples of nettle worlds to be used in this assignment with the following
characteristics:
* Easy level: in which N=5, M=5, indicating a board of 5x5 with 5 nettles
* Medium level: in which N=9, M=10, indicating a board of 9x9 with 10 nettles
* Hard level: in which N=10, M=20, indicating a board of 10x10 with 20 nettles

The rules of the game are as follows:
1. The total number of nettles is known by the agent in advance.
2. At the initial step the cells are all covered and the agent has no other information
other than the total number of nettles. The first probe is to be made at position [0,0].
3. At each step the agent probes a covered cell, and receives the information about the
content of the cell. The cell will then be marked as uncovered.
4. If the probed cell is a nettle, the game is over.
5. If the cell contains a value 0 meaning that no adjacent cells contain nettles, all the
adjacent cells will also be uncovered.
6. If the content of the cell has a value > 0, the agent should make inferences about its
view of the world and decide which cell should be probed next.
7. If all but M cells are uncovered without finding a nettle, the agent wins the game.
8. The agent may use flags to signal the presence of a nettle in covered cells.
There are a number of possible reasoning strategies that the agent can employ, the agent
goal is to employ one that limits the amount of random guessing needed to solve the game,
in order to increase the chances of winning. For more details on the strategies, please refer
to the lecture slides

## Contents
This repo contains:
1. The game code
2. logic codge to provide solutions
3. Code to randomly generate new worlds
4. Code to test all registered algorithms and print the results in various formats

