# logisim-mips
An Integration of MIPS, logisim, and testing.

# Implemented Operations

| Name                        | Mnemonic | Implemented |
|-----------------------------|----------|-------------|
| Add                         | add      | ✔️           |
| Add Immediate               | addi     | ✔️           |
| Add Imm. Unsigned           | addiu    | ❌           |
| Add Unsigned                | addu     | ❌           |
| And                         | and      | ✔️           |
| And Immediate               | andi     | ✔️           |
| Branch On Equal             | beq      | ✔️           |
| Branch On Not Equal         | bne      | ✔️           |
| Jump                        | j        | ✔️           |
| Jump And Link               | jal      | ✔️           |
| Jump Register               | jr       | ✔️           |
| Load Byte Unsigned          | lbu      | ❌           |
| Load Halfword Unsigned      | lhu      | ❌           |
| Load Linked                 | ll       | ❌           |
| Load Upper Imm              | lui      | ❌           |
| Load Word                   | lw       | ✔️           |
| Nor                         | nor      | ✔️           |
| Or                          | or       | ✔️           |
| Or Immediate                | ori      | ✔️           |
| Set Less Than               | slt      | ✔️           |
| Set Less Than Imm.          | slti     | ✔️           |
| Set Less Than Imm. Unsigned | sltiu    | ❌           |
| Set Less Than Unsigned      | sltu     | ❌           |
| Shift Left Logical          | sll      | ✔️           |
| Shift Right Logical         | srl      | ✔️           |
| Store Byte                  | sb       | ❌           |
| Store Conditional           | sc       | ❌           |
| Store Halfword              | sh       | ❌           |
| Store Word                  | sw       | ✔️           |
| Subtract                    | sub      | ✔️           |
| Subtract Unsigned           | subu     | ❌           |


# Limitations
1. logisim has a 24 bit limit for address for RAM components
1. logisim doesn't support byte addressing only word addressing, so pc is incremented by one.

# About this library
1. The RAM closest to the clock will be used to hold the instructions
1. the other RAM will hold the static data, dynamic data, and stack
1. program starts at address 0
1. stack pointer has initial value: 0x100 0000 (2^24)
1. static data starts at 0 (uses the other RAM component)
1. The program can only end by falling off the end of the program (no support for syscalls)
1. jump addresses are restricted to 24 bits because of the logisim RAM address size limitation

# Testing
1. CircTest - tests machine code vs a logisim circuit execution
1. MipsParserTest - tests parsing mips code into machine code
1. MipsRegressionTest - tests mips parser as well as logisim circuit execution

# Sources
- course slides (chapter 4 the processor)
- http://www.cburch.com/logisim/docs/2.7/en/html/guide/
- https://chortle.ccsu.edu/AssemblyTutorial/Chapter-18/ass18_3.html
- https://www.youtube.com/watch?v=u5Foo6mmW0I&list=PL5b07qlmA3P6zUdDf-o97ddfpvPFuNa5A&ab_channel=AmellPeralta



