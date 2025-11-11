package utils

import java.util.Locale

object KStrings {
    @JvmStatic
    fun uppercase(s: String): String = s.uppercase()

    @JvmStatic
    fun lowercase(s: String): String = s.lowercase()

    @JvmStatic
    fun reversed(s: String): String = s.reversed()

    @JvmStatic
    fun capitalize(s: String): String = s.replaceFirstChar { it.titlecase() }

    @JvmStatic
    fun decapitalize(s: String): String = s.replaceFirstChar { it.lowercase() }

    @JvmStatic
    fun trim(s: String): String = s.trim()

    @JvmStatic
    fun isBlank(s: String): Boolean = s.isBlank()

    @JvmStatic
    fun repeat(s: String, times: Int): String = s.repeat(times)

    @JvmStatic
    fun removePrefix(s: String, prefix: String): String = s.removePrefix(prefix)

    @JvmStatic
    fun removeSuffix(s: String, suffix: String): String = s.removeSuffix(suffix)

    @JvmStatic
    fun replace(s: String, old: String, new: String): String = s.replace(old, new)
}
