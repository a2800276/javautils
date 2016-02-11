# javautils
grabbag of Java stuff to make life less miserable.

##m Repo & Dev

This is currently a bunch of IntelliJ projects. No Maven, Ant, etc. Sorry.

## [bench](https://github.com/a2800276/javautils/blob/master/bench/src/util/benchmark/Benchmark.java)

Utility to compare execution speed of code variants. More or less a straightforward port of:
Go's [benchmark](https://golang.org/pkg/testing/) utility 

## bytes

Code to deal with bytes & byte arrays, e.g. `h2b` and `b2h`. Simple [Base64](https://github.com/a2800276/javautils/blob/master/bytes/src/util/bytes/base64/Base64.java) (so you're not stuck using `java.sun.dontuse` packages or forced to download 2GB of apache.org) as well as [hex](https://github.com/a2800276/javautils/blob/master/bytes/src/util/bytes/hexy/Hexy.java) dumps.

## [crypto](https://github.com/a2800276/javautils/blob/master/crypto/src/util/crypto/package-info.java)

Crypto code to avoid the String-constant-and-Exception-Boilerplate tedium of the JDK.

## [csv](https://github.com/a2800276/javautils/blob/master/csv/src/util/csv/CSVParser.java)

Suprisingly, code to parse and generate CSV files.

## [flags](https://github.com/a2800276/javautils/blob/master/flags/src/util/flags/CmdLine.java)

Command line flag parsing.

## [io](https://github.com/a2800276/javautils/blob/master/io/src/util/io/IO.java)

Utilities to deal with (classic) IO, `readAll(stream)`, `closeStream()`, etc.

## [json](https://github.com/a2800276/javautils/tree/master/json)

Fast and simple JSON parsing and serialization.

## License (MIT)

Copyright (c) - 2016 Tim Becker (tim@kuriositaet.de)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
