# javautils
grabbag of Java stuff to make life less miserable.

## bench

Utility to compare execution speed of code variants. More or less a straightforward port of:
Go's [benchmark](https://golang.org/pkg/testing/) utility 

## bytes

Code to deal with bytes & byte arrays, e.g. `h2b` and `b2h`. Base64 (so you're not stuck using `java.sun.dontuse` packages or forced to download 2GB of apache.org) as well as hex dumps.C

## crypto

Crypto code to avoid the String-constant-and-Exception-Boilerplate tedium of the JDK.

## csv

Suprisingly, code to parse and generate CSV files.

## flags

Command line flag parsing.

## io

Utilities to deal with (classic) IO, `readAll(stream)`, `closeStream()`, etc.

## json

Fast and simple JSON parsing and serialization.
