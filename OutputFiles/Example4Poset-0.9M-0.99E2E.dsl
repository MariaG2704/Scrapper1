WARP program for graph Example4
Scheduler Name: Poset
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0)	sleep	sleep
1	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
2	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
3	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
4	sleep	if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
5	sleep	sleep	wait(#0)	if !has(F0: C -> D) pull(F0: C -> D, #0)
6	sleep	sleep	wait(#0)	if !has(F0: C -> D) pull(F0: C -> D, #0)
7	sleep	sleep	wait(#0)	if !has(F0: C -> D) pull(F0: C -> D, #0)
8	sleep	sleep	sleep	sleep
9	sleep	sleep	sleep	sleep
10	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0)	sleep	sleep
11	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
12	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
13	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
14	sleep	if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)	sleep
15	sleep	sleep	wait(#0)	if !has(F0: C -> D) pull(F0: C -> D, #0)
16	sleep	if !has(F1: C -> B) pull(F1: C -> B, #1)	wait(#1)	if !has(F0: C -> D) pull(F0: C -> D, #0)
17	wait(#1)	if !has(F1: C -> B) pull(F1: C -> B, #1) else if has(F1: B -> A) push(F1: B -> A, #1)	wait(#1)	if !has(F0: C -> D) pull(F0: C -> D, #0)
18	wait(#1)	if !has(F1: C -> B) pull(F1: C -> B, #1) else if has(F1: B -> A) push(F1: B -> A, #1)	wait(#1)	sleep
19	wait(#1)	if has(F1: B -> A) push(F1: B -> A, #1)	sleep	sleep
// All flows meet their deadlines
