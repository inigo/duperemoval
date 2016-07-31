package net.surguy.duperemoval

import org.specs2.mutable.Specification

class DupeRemoveSpec extends Specification {

  val dupeRemover = new DupeRemover()

  "building the list of duplicates" should {
    "create an empty list if there are no duplicates" in {
      val list = List("one fish", "two fish", "red fish", "blue fish")
      dupeRemover.buildDupesList(list) must beEmpty
    }
    "create a list containing dupes" in {
      val list = List("one fish", "two fish", "one fish", "blue fish", "blue fish")
      dupeRemover.buildDupesList(list) mustEqual List("one fish", "blue fish")
    }
  }

  "removing duplicates" should {
    "leave a simple list unchanged" in {
      val list = List("one fish", "two fish", "red fish", "blue fish")
      dupeRemover.removeCommonDupes(list).toList mustEqual list
    }
    "remove at least one duplicate line" in {
      val list = List("one fish", "two fish", "one fish", "blue fish")
      dupeRemover.removeCommonDupes(list).toList.length must beLessThan(list.length)
    }
    "remove all duplicate lines" in {
      val list = List("one fish", "two fish", "one fish", "blue fish")
      dupeRemover.removeCommonDupes(list).toList mustEqual List("two fish", "blue fish")
    }
  }

  "removing similar lines" should {
    "remove lines that differ only by case" in {
      val list = List("one fish", "two fish", "ONE fish", "blue fish")
      dupeRemover.removeCommonDupes(list).toList mustEqual List("two fish", "blue fish")
    }
  }


}
