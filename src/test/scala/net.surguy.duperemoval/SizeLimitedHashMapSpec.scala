package net.surguy.duperemoval

import org.specs2.mutable.Specification

class SizeLimitedHashMapSpec extends Specification {
  isolated
  val map = new SizeLimitedHashMap[String](3).withDefaultValue(0)

  "adding new entries" should {
    "work as normal when the map is not at its limit" in {
      map("one") += 1
      map.keySet mustEqual Set("one")
      map("two") += 1
      map.keySet mustEqual Set("one", "two")
    }
    "remove the lowest values when adding items at the limit" in {
      map("five") += 5
      map("three") += 3
      map("one") += 1
      map.keySet mustEqual Set("five", "three", "one")
      map("anotherone") += 1
      map.keySet mustEqual Set("five", "three", "anotherone")
    }
    "remove the oldest entry when adding items at the limit when there are multiple options to remove" in {
      // Doesn't appear to need any code to make this work - perhaps a characteristic of the LinkedHashMap already?
      map("one") += 1
      map("anotherone") += 1
      map("thirdone") += 1
      map.keySet mustEqual Set("one", "anotherone", "thirdone")
      map("yetanotherone") += 1
      map.keySet mustEqual Set("anotherone", "thirdone", "yetanotherone")
    }
  }

}
