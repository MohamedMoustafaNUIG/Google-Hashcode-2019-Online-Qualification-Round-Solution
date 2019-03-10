# Google HashCode 2019 Online Qualification Round Solution
## Score
| File  | Score  |
|---|---|
| a  |  2 |
| b  | 67,572  |
| c  |  1,394 |
| d  | 408,122  |
| e  | 375,416  |
| Total  | 852,506  |

Extended Round Position: #783 (Global), #7 (Ireland-Local)

## Usage

Most straightforward way to run this code on your machine would be :
* Clone repo into a folder
* Go to your prefered Java IDE
* Open the folder from the IDE

## Optimisation

A Dynamic Programming approach was taken to try and get the best possible order for slides.
* Photos were sorted by orientation, then were put into slides by order (orientation sorting is to limit number of V photos discarded to one)
* The slides were then sorted by number of tags
* The slides are divided into "buckets" (Array Lists) of size<= 10,000 and each bucket is optimised using the following aproach:

1)Create a 2d array containing scores of transitions between each slides in the Array List

2)Find the largest score, and let those two slides be the first two in the "optimised" slideshow

3)Then, until all slides are used, find the slide with the highest transition score (between it and the last pushed slide).

4)Go over all the "buckets" while pushing the optimised order into a large "sorted" Array List

## Note
Other approaches (using simulated annealing and ordering the photos first) are being worked on.
