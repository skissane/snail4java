# Snail Bytecode Design

* Register machine, there are three sets of registers:
  * **W** registers: these are work registers, used for temporary
    values.
    * **Y** registers: These are simply another way of referring to
      the W registers. If there are 10 W registers, W0-W9, then Y0 is
      an alias for W9, Y1 is an alias for W8, etc. Y registers are
      used for outbound parameters to methods this method calls. Y0 is
      the first parameter, Y1 the second, etc. This is because our
      method calling convention is right-to-left.
  * **X** registers: these are parameters to this method.
  * **Z** registers: these store local variables.
* Each method declares how many registers of each type it requires. There
  are a maximum of 256 registers available to a method.
* The registers are arranged in order X, Z, W.
* Each method has a constant pool containing constants. The constant pool
  is actually a Snail list, and so can contain any value a Snail list can
  contain (other than resources).
* XZ registers are typed, W registers are untyped.
  * It makes little sense to type W registers used as outbound parameters
    (Y registers), since the method call will type-check. We could do it
    for W registers not used as Y registers, but the complexity is
    probably not worth it.
* On return from a CALL, the Y registers used to pass the method arguments
  are set to null.

# Implementation notes

* Not strictly part of the specification, but the WXZ registers form
  a stack, in the order XZW within each frame. The Y registers of caller
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
# Register counts: X=0, Z=0, W=3; total=3
0  CONSTANT c0, y0     # c0=1; y0=w2
3  CONSTANT c0, y1     # c0=1; y1=w1
6  CALL     c1, 2, w0  # c1=(add method)
10 RET      w0
```
