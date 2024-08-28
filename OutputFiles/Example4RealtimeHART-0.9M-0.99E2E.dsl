WARP program for graph Example4
Scheduler Name: RealtimeHART
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	push(F0: A -> B, #3)	wait(#3)	sleep	sleep
1	push(F0: A -> B, #4)	wait(#4)	sleep	sleep
2	push(F0: A -> B, #5)	wait(#5)	sleep	sleep
3	sleep	push(F0: B -> C, #3)	wait(#3)	sleep
4	sleep	push(F0: B -> C, #4)	wait(#4)	sleep
5	sleep	push(F0: B -> C, #5)	wait(#5)	sleep
6	sleep	push(F0: B -> C, #6)	wait(#6)	sleep
7	sleep	sleep	push(F0: C -> D, #12)	wait(#12)
8	sleep	sleep	push(F0: C -> D, #13)	wait(#13)
9	sleep	sleep	push(F0: C -> D, #14)	wait(#14)
10	push(F0: A -> B, #6)	wait(#6)	sleep	sleep
11	push(F0: A -> B, #7)	wait(#7)	sleep	sleep
12	push(F0: A -> B, #8)	wait(#8)	sleep	sleep
13	sleep	push(F0: B -> C, #7)	wait(#7)	sleep
14	sleep	push(F0: B -> C, #8)	wait(#8)	sleep
15	sleep	push(F0: B -> C, #9)	wait(#9)	sleep
16	sleep	push(F0: B -> C, #10)	wait(#10)	sleep
17	sleep	sleep	push(F0: C -> D, #15)	wait(#15)
18	sleep	sleep	push(F0: C -> D, #0)	wait(#0)
19	sleep	sleep	push(F0: C -> D, #1)	wait(#1)
20	sleep	wait(#2)	push(F1: C -> B, #2)	sleep
21	sleep	wait(#3)	push(F1: C -> B, #3)	sleep
22	sleep	wait(#4)	push(F1: C -> B, #4)	sleep
23	wait(#11)	push(F1: B -> A, #11)	sleep	sleep
24	wait(#12)	push(F1: B -> A, #12)	sleep	sleep
25	wait(#13)	push(F1: B -> A, #13)	sleep	sleep
// WARNING: NOT all flows meet their deadlines. See deadline analysis report.
