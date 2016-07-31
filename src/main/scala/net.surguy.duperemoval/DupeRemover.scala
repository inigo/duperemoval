package net.surguy.duperemoval

import scala.collection.mutable

/**
  * Remove duplicate and almost-duplicate lines from text.
  */
class DupeRemover(maxLinesBetweenDuplication: Int = 100) {

  /**
    * Remove duplicate and almost-duplicate lines.
    *
    * @param lines - this must be an iterable, not an iterator, because we iterate across it twice
    * @return the lines with duplicates removed
    */
  def removeCommonDupes(lines: Iterable[String]): Iterable[String] = {
    val existingLines = buildDupesList(lines)
    def inDupesList(line: String): Boolean = existingLines.exists(isSimilar(line))

    lines.filterNot(inDupesList)
  }

  private[duperemoval] def buildDupesList(lines: Iterable[String]): List[String] = {
    val existingLines = new SizeLimitedHashMap[String](maxLinesBetweenDuplication).withDefaultValue(0)

    def countLines(line: String): Unit = {
      existingLines.keys.find(isSimilar(line)) match {
        case Some(existingLine) =>
          existingLines(existingLine) +=1
        case None => existingLines(line) += 1
      }
    }
    lines.foreach(countLines)

    // Removing lines that aren't actually dupes
    existingLines.filterNot(_._2 <= 1).keys.toList
  }

  private[duperemoval] def isSimilar(line: String)(anotherLine: String): Boolean = {
    line == anotherLine
  }

}

/** A size-limited map of key/values, in which the lowest-value entry is removed when the map gets full. */
class SizeLimitedHashMap[K](maxEntries: Int) extends mutable.LinkedHashMap[K, Int] {
  override def put(key: K, value: Int): Option[Int] = {
    if ((keySet.size >= maxEntries) && !contains(key)) {
      removeLowestValueEntry()
    }
    super.put(key, value)
  }

  def removeLowestValueEntry() = {
    val lowestEntry = entriesIterator.toList.sortBy(_.value).head
    remove(lowestEntry.key)
  }

}