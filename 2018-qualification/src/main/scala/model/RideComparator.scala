package model

import java.util.Comparator

/**
  * Copyright (C) 20-Feb-19 - REstore NV
  */

class RideComparator extends Comparator[Ride] {
  //The Comparator should sort in ascending difficulty
  //Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
  override def compare(a: Ride, b: Ride) = a.distance - b.distance
}