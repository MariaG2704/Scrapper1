WARP program for graph Example4
Scheduler Name: RateMonotonic
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #7)	wait(#7)	sleep	sleep
1	wait(#13)	if has(F0) push(F0: B -> C, #13) else pull(F0: A -> B, #13)	wait(#13)	sleep
2	wait(#14)	if has(F0) push(F0: B -> C, #14) else pull(F0: A -> B, #14)	if has(F0) push(F0: C -> D, #10) else wait(#14)	wait(#10)
3	sleep	wait(#11)	if has(F0) push(F0: C -> D, #11) else pull(F0: B -> C, #11)	wait(#11)
4	sleep	if has(F0) push(F0: B -> C, #0)	wait(#0)	sleep
5	sleep	sleep	if has(F0) push(F0: C -> D, #12)	wait(#12)
6	sleep	wait(#0)	if has(F1) push(F1: C -> B, #0)	sleep
7	wait(#5)	if has(F1) push(F1: B -> A, #5) else pull(F1: C -> B, #5)	wait(#5)	sleep
8	wait(#6)	if has(F1) push(F1: B -> A, #6) else pull(F1: C -> B, #6)	wait(#6)	sleep
9	wait(#7)	if has(F1) push(F1: B -> A, #7)	sleep	sleep
10	if has(F0) push(F0: A -> B, #10)	wait(#10)	sleep	sleep
11	wait(#1)	if has(F0) push(F0: B -> C, #1) else pull(F0: A -> B, #1)	wait(#1)	sleep
12	wait(#2)	if has(F0) push(F0: B -> C, #2) else pull(F0: A -> B, #2)	if has(F0) push(F0: C -> D, #13) else wait(#2)	wait(#13)
13	sleep	wait(#14)	if has(F0) push(F0: C -> D, #14) else pull(F0: B -> C, #14)	wait(#14)
14	sleep	if has(F0) push(F0: B -> C, #4)	wait(#4)	sleep
15	sleep	sleep	if has(F0) push(F0: C -> D, #15)	wait(#15)
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
