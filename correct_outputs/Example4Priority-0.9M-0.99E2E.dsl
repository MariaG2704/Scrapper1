WARP program for graph Example4
Scheduler Name: Priority
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #1)	wait(#1)	sleep	sleep
1	sleep	if has(F0) push(F0: B -> C, #2)	wait(#2)	sleep
2	sleep	sleep	if has(F0) push(F0: C -> D, #1)	wait(#1)
3	sleep	wait(#3)	if has(F1) push(F1: C -> B, #3)	sleep
4	wait(#4)	if has(F1) push(F1: B -> A, #4)	sleep	sleep
5	sleep	sleep	sleep	sleep
6	sleep	sleep	sleep	sleep
7	sleep	sleep	sleep	sleep
8	sleep	sleep	sleep	sleep
9	sleep	sleep	sleep	sleep
10	if has(F0) push(F0: A -> B, #2)	wait(#2)	sleep	sleep
11	sleep	if has(F0) push(F0: B -> C, #3)	wait(#3)	sleep
12	sleep	sleep	if has(F0) push(F0: C -> D, #2)	wait(#2)
13	sleep	sleep	sleep	sleep
14	sleep	sleep	sleep	sleep
15	sleep	sleep	sleep	sleep
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
