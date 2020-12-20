.text
	j main
	test:
		addi $s0, $s0, 1
		
		jr $ra
	main:
		jal test
		jal test
		
		addi $s0, $s0, 1