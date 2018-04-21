# Snail Bytecode Design

* Register machine, there are four sets of registers:
  * **W** registers: these are work registers, used for temporary
    values.
  * **X** registers: these are parameters to this method.
  * **Y** registers: these are parameters to methods this method calls.
  * **Z** registers: these store local variables.
* Each method declares how many registers of each type it requires. There
  are a maximum of 256 registers available to a method.
* The registers are arranged in order X, Z, W, Y.
* Each method has a constant pool containing constants. The constant pool
  is actually a Snail list, and so can contain any value a Snail list can
  contain (other than resources).
* XZ registers are typed, WY registers are untyped. (We could type W
  registers, potentially. Typing Y registers is somewhat pointless, since
* X registers are read-only, Y registers are write-only. Constants are
  read-only too.
* If caller has 5 Y registers but wants to call a one argument method, then
  it fills its last Y register with the argument, which becomes the only X
  register of the callee. Because of this, Y registers are numbered backward
  in instruction listings. So Y0 is the last Y register, Y1 is the second
  last, etc. Hence arguments are passed right-to-left. But this reverse
  numbering is only used in listings, in the actual bytecode they are
  numbered forward.
* Y registers are cleared upon return from each CALL opcode.

# Implementation notes

* Not strictly part of the specification, but the WXYZ registers form
  a stack, in the order XZWY within each frame. The Y registers of caller
  become the X registers of callee.
* The register indices start at the stack location of the first X register.

# Argument mnenomics

|Name   |# Bytes|Description                              |
|-------|-------|-----------------------------------------|
|c      |1      |Constant index                           |
|n      |1      |Argument count                           |
|o      |1      |Signed relative jump offset              |
|r      |1      |Register index                           |

# Opcodes

|Mnenomic |Format |Description                                |
|---------|-------|-------------------------------------------|
|CALL     |c,n,r  |Call a method                              |
|CONSTANT |c,r    |Move constant to register                  |
|JUMP     |o      |Jump always to offset                      |
|JUMPIF   |c,o    |Jump if register true                      |
|LISTADD  |r,r    |Adds register to list in register          |
|LISTDEL  |r,r    |Removes nth element of list                |
|LISTGET  |r,r    |Gets nth element of list                   |
|LISTNEW  |r      |Store a new list into register             |
|LISTSET  |r,r,r  |Sets nth element of list                   |
|MOVE     |r,r    |Move from source to destination register   |
|OBJDEL   |r,r    |Deletes value of object attribute          |
|OBJGET   |r,r    |Gets value of object attribute             |
|OBJNEW   |r,r    |Store new object of class into register    |
|OBJSET   |r,r,r  |Sets value of object attribute             |
|RET      |r      |Return using value of register             |

# Example: return 1+1

```
# Register counts: X=0, Z=0, W=1, Y=2; total=3
0	CONSTANT	c0, y0		# c0=1
3	CONSTANT	c0, y1
6	CALL		c1, 2, w0	# c1=(add method)
10	RET		w0
```
