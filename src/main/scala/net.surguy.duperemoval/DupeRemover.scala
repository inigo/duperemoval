package net.surguy.duperemoval

import scala.collection.mutable

/**
  * Remove duplicate and almost-duplicate lines from text.
  */
class DupeRemover() {

  /**
    * Remove duplicate and almost-duplicate lines.
    *
    * @param lines - this must be an iterable, not an iterator, because we iterate across it twice
    * @return the lines with duplicates removed
    */
  def removeCommonDupes(lines: Iterable[String]): Iterable[String] = {
    val existingLines = new mutable.HashMap[String, Int]().withDefaultValue(0)

    def buildDupesList(line: String): Unit = existingLines(line) += 1
    def inDupesList(line: String): Boolean = existingLines(line) > 1

    lines.foreach(buildDupesList)
    lines.filterNot(inDupesList)
  }

}
