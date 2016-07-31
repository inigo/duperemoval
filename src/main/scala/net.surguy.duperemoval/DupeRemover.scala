package net.surguy.duperemoval

import org.apache.commons.lang3.StringUtils

import scala.collection.mutable

/**
  * Remove duplicate and almost-duplicate lines from text.
  *
  * @param maxLinesBetweenDuplication - the number of lines in the rolling dupe-counting buffer. Increasing this will find dupes that
  *   are further apart from each other, at the cost of increasing the memory usage.
  * @param isSimilar - a function that checks if two strings are similar, for example def similar(a: String)(b: String) = { a==b }
  */
class DupeRemover(maxLinesBetweenDuplication: Int = 100, isSimilar : (String) => (String) => Boolean = DupeRemover.levenshteinIsSimilar) {

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

}

object DupeRemover {

  def levenshteinIsSimilar(line: String)(anotherLine: String): Boolean = {
    val distance = StringUtils.getLevenshteinDistance(line.toLowerCase, anotherLine.toLowerCase, 2)
    distance >= 0 // Will return -1 if greater than the threshold
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