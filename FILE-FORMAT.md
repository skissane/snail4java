# General Snail file format

This is a general file format to be used during Snail development.

It is not finalised and subject to change at any time. (I'd actually like to do something smarter in the future.)

The file format is based on the IFF chunk format. Each chunk is composed of:

* 4 byte tag, this must be US-ASCII and match the regex `^[A-Za-z0-9]{4}$`
* 4 byte length, this is signed twos-complement big-endian 32-bit value. Must be greater than 0, so valid range is 0 to 0x7FFFFFFF.
* Arbitrary bytes of data, format and interpretation depends on tag.

The compiler generates an output file in chunk format. The VM then loads that output file and executes it.

Following tags currently defined supported:
|Tag |Length   |Description                                   |
|----|---------|----------------------------------------------|
|ENDS|must be 0|End of file marker, must be last chunk in file|
|METH|> 0      |Contains the bytecode of a Snail method       |
|OBJX|> 0      |Contains a global Snail object                |
