package util

object UnsafeCharFilter {
  private[this] val safeChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray

  def filterUnsafeChars(input: String): String =
    input
      .filter(c => safeChars.contains(c))
      .map(c => {
        if (c == '/' || c == '\\') '-'
        else if (c == '²') '2'
        else c
      })
}
