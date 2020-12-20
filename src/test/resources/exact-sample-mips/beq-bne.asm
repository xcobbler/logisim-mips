addi $t1, $zero, 0
beq $t1, $zero, b1
addi $t1, $t1, 1
b1:
 addi $t1, $t1, 2
bne $zero, $t1, b2
addi $t1, $t1, 1
b2:
addi $t1, $t1, 2
