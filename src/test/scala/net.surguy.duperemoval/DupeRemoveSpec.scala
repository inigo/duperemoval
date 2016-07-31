package net.surguy.duperemoval

import org.specs2.mutable.Specification

class DupeRemoveSpec extends Specification {

  val dupeRemover = new DupeRemover()

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

}
