WARP program for graph Example4
Scheduler Name: Priority
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	B	C	D	pullA
0	wait(#1)	sleep	sleep	if has(F0) push(F0: pullA -> B, #1)
1	if has(F0) push(F0: B -> C, #2) else pull(F0: pullA -> B, #2)	wait(#2)	sleep	wait(#2)
2	if has(F0) push(F0: B -> C, #3) else pull(F0: pullA -> B, #3)	if has(F0) push(F0: C -> D, #1) else wait(#3)	wait(#1)	wait(#3)
3	wait(#2)	if has(F0) push(F0: C -> D, #2) else pull(F0: B -> C, #2)	wait(#2)	sleep
4	if has(F0) push(F0: B -> C, #5)	wait(#5)	sleep	sleep
5	sleep	if has(F0) push(F0: C -> D, #3)	wait(#3)	sleep
6	wait(#7)	if has(F1) push(F1: C -> B, #7)	sleep	sleep
7	if has(F1) push(F1: B -> pullA, #10) else pull(F1: C -> B, #10)	wait(#10)	sleep	wait(#10)
8	if has(F1) push(F1: B -> pullA, #11) else pull(F1: C -> B, #11)	wait(#11)	sleep	wait(#11)
9	if has(F1) push(F1: B -> pullA, #12)	sleep	sleep	wait(#12)
10	wait(#4)	sleep	sleep	if has(F0) push(F0: pullA -> B, #4)
11	if has(F0) push(F0: B -> C, #6) else pull(F0: pullA -> B, #6)	wait(#6)	sleep	wait(#6)
12	if has(F0) push(F0: B -> C, #7) else pull(F0: pullA -> B, #7)	if has(F0) push(F0: C -> D, #4) else wait(#7)	wait(#4)	wait(#7)
13	wait(#5)	if has(F0) push(F0: C -> D, #5) else pull(F0: B -> C, #5)	wait(#5)	sleep
14	if has(F0) push(F0: B -> C, #9)	wait(#9)	sleep	sleep
15	sleep	if has(F0) push(F0: C -> D, #6)	wait(#6)	sleep
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
