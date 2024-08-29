WARP program for graph Example4
Scheduler Name: DeadlineMonotonic
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #13)	wait(#13)	sleep	sleep
1	wait(#8)	if has(F0) push(F0: B -> C, #8) else pull(F0: A -> B, #8)	wait(#8)	sleep
2	wait(#9)	if has(F0) push(F0: B -> C, #9) else pull(F0: A -> B, #9)	if has(F0) push(F0: C -> D, #3) else wait(#9)	wait(#3)
3	sleep	wait(#4)	if has(F0) push(F0: C -> D, #4) else pull(F0: B -> C, #4)	wait(#4)
4	sleep	if has(F0) push(F0: B -> C, #11)	wait(#11)	sleep
5	sleep	sleep	if has(F0) push(F0: C -> D, #5)	wait(#5)
6	sleep	wait(#9)	if has(F1) push(F1: C -> B, #9)	sleep
7	wait(#0)	if has(F1) push(F1: B -> A, #0) else pull(F1: C -> B, #0)	wait(#0)	sleep
8	wait(#1)	if has(F1) push(F1: B -> A, #1) else pull(F1: C -> B, #1)	wait(#1)	sleep
9	wait(#2)	if has(F1) push(F1: B -> A, #2)	sleep	sleep
10	if has(F0) push(F0: A -> B, #0)	wait(#0)	sleep	sleep
11	wait(#12)	if has(F0) push(F0: B -> C, #12) else pull(F0: A -> B, #12)	wait(#12)	sleep
12	wait(#13)	if has(F0) push(F0: B -> C, #13) else pull(F0: A -> B, #13)	if has(F0) push(F0: C -> D, #6) else wait(#13)	wait(#6)
13	sleep	wait(#7)	if has(F0) push(F0: C -> D, #7) else pull(F0: B -> C, #7)	wait(#7)
14	sleep	if has(F0) push(F0: B -> C, #15)	wait(#15)	sleep
15	sleep	sleep	if has(F0) push(F0: C -> D, #8)	wait(#8)
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
