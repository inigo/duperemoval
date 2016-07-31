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
    val existingLines = new SizeLimitedHashMap[String](maxLinesBetweenDuplication).withDefaultValue(0)

    def buildDupesList(line: String): Unit = existingLines(line) += 1
    def inDupesList(line: String): Boolean = existingLines(line) > 1

    lines.foreach(buildDupesList)
    lines.filterNot(inDupesList)
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