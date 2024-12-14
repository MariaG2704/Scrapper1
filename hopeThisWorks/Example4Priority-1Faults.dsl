WARP program for graph Example4
Scheduler Name: Priority
numFaults: 1
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #1)	wait(#1)	sleep	sleep
1	wait(#2)	if has(F0) push(F0: B -> C, #2) else pull(F0: A -> B, #2)	wait(#2)	sleep
2	sleep	wait(#1)	if has(F0) push(F0: C -> D, #1) else pull(F0: B -> C, #1)	wait(#1)
3	sleep	sleep	if has(F0) push(F0: C -> D, #2)	wait(#2)
4	sleep	wait(#5)	if has(F1) push(F1: C -> B, #5)	sleep
5	wait(#6)	if has(F1) push(F1: B -> A, #6) else pull(F1: C -> B, #6)	wait(#6)	sleep
6	wait(#7)	if has(F1) push(F1: B -> A, #7)	sleep	sleep
7	sleep	sleep	sleep	sleep
8	sleep	sleep	sleep	sleep
9	sleep	sleep	sleep	sleep
10	if has(F0) push(F0: A -> B, #3)	wait(#3)	sleep	sleep
11	wait(#4)	if has(F0) push(F0: B -> C, #4) else pull(F0: A -> B, #4)	wait(#4)	sleep
12	sleep	wait(#3)	if has(F0) push(F0: C -> D, #3) else pull(F0: B -> C, #3)	wait(#3)
13	sleep	sleep	if has(F0) push(F0: C -> D, #4)	wait(#4)
14	sleep	sleep	sleep	sleep
15	sleep	sleep	sleep	sleep
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
