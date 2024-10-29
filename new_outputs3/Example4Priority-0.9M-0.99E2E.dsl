WARP program for graph Example4
Scheduler Name: Priority
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #1)	wait(#1)	sleep	sleep
1	wait(#2)	if has(F0) push(F0: B -> C, #2) else pull(F0: A -> B, #2)	wait(#2)	sleep
2	wait(#3)	if has(F0) push(F0: B -> C, #3) else pull(F0: A -> B, #3)	if has(F0) push(F0: C -> D, #1) else wait(#3)	wait(#1)
3	sleep	wait(#2)	if has(F0) push(F0: C -> D, #2) else pull(F0: B -> C, #2)	wait(#2)
4	sleep	if has(F0) push(F0: B -> C, #5)	wait(#5)	sleep
5	sleep	sleep	if has(F0) push(F0: C -> D, #3)	wait(#3)
6	sleep	wait(#7)	if has(F1) push(F1: C -> B, #7)	sleep
7	wait(#10)	if has(F1) push(F1: B -> A, #10) else pull(F1: C -> B, #10)	wait(#10)	sleep
8	wait(#11)	if has(F1) push(F1: B -> A, #11) else pull(F1: C -> B, #11)	wait(#11)	sleep
9	wait(#12)	if has(F1) push(F1: B -> A, #12)	sleep	sleep
10	if has(F0) push(F0: A -> B, #4)	wait(#4)	sleep	sleep
11	wait(#6)	if has(F0) push(F0: B -> C, #6) else pull(F0: A -> B, #6)	wait(#6)	sleep
12	wait(#7)	if has(F0) push(F0: B -> C, #7) else pull(F0: A -> B, #7)	if has(F0) push(F0: C -> D, #4) else wait(#7)	wait(#4)
13	sleep	wait(#5)	if has(F0) push(F0: C -> D, #5) else pull(F0: B -> C, #5)	wait(#5)
14	sleep	if has(F0) push(F0: B -> C, #9)	wait(#9)	sleep
15	sleep	sleep	if has(F0) push(F0: C -> D, #6)	wait(#6)
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
