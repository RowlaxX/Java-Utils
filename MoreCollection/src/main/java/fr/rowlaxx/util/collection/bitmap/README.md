# Bitmap

A bitmap (or bitset) is a set of integer.  
There are different solution to handle a bitmap.

## BitSet

The java.util.BitSet class store an array of longs.
Each long can describe up to 64 number present or absent in the
set (depending on the bits). This solution is efficient when you 
need a small amount of number, but consume a lot of memory when you 
need a lot of number

## RoaringBitmap

This library provide a really fast and memory efficient solution
to bitmap. However, this library is first built for 32 bits application.
RoaringBitmap do provide a 64 bits support, but it's limited compared
to it's 32 bits variant.

# Goals

The goals of this library are :
- built for 64 bits
- lightweigth
- ultra fast
- memory efficient

# Usage

## Creation

```java
SegmentedBitmap sb = SegmentedBitmap.create();
```

or

```java
SegmentedBitmap sb = SegmentedBitmap.create(100); //capacity of 100
```

## Adding

```java
sb.add(4); //add 4
```

```java
sb.addAll(10,14); //add 10, 11, 12, 13, 14
```

## Removing

```java
sb.remove(4); //remove 4
```

```java
sb.removeAll(10,14); //remove 10, 11, 12, 13, 14
```

## Other operation

```java
sb.flip(5); //flip the number 5 (absent -> present, present -> absent)
```

```java
sb.next(5); //get the next number starting from 5
```

```java
sb.previous(10); //get the previous number starting from 10
```

```java
sb.contains(5); //return true if 5 is in the set
```

```java
sb.containsAll(5,7); //return true if 5,6 and 7 are in the set
```

```java
sb.size(); //return the number of numbers in the set
```
