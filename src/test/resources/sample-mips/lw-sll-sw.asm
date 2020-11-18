.text
#note: since we saying data starts at 0, we can use $0 as starting point
lw $t0, a
sll $t1, $t0, 1
sw $t1, c
lw $t2, c
.data
a: 4
c: 0
