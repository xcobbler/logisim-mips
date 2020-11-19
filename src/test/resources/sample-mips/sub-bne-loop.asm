addi $t0, $zero, 0
addi $t1, $zero, 5
addi $t2, $zero, 1

do:
 addi $t0, $t0, 1
 sub $t1, $t1, $t2
bne $t1, $zero, do
