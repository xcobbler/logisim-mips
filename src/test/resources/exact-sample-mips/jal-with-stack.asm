.text
	j main
	test:
		addi $sp, $sp, -4
		sw $s0, 0($sp)
		addi $s0, $s0, 1
		
		
		lw $s0, 0($sp)
		addi $sp, $sp, 4
		
		jr $ra
	main:
		jal test
		jal test
		
		addi $s0, $s0, 1