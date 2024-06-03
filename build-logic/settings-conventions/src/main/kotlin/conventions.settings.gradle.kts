/**
 * Searches for subprojects' `build.gradle` files and prints their Gradle project path.
 */
private val discoverSubprojects: () -> Array<String> = {
    val excludePaths = listOf(
        rootDir.path,
        "${rootDir.path}${File.separator}buildSrc",
        "${rootDir.path}${File.separator}build-logic",
        "${rootDir.path}${File.separator}build-logic${File.separator}build-conventions",
        "${rootDir.path}${File.separator}build-logic${File.separator}settings-conventions"
    )

    rootDir.walkBottomUp().filter { file ->
        file.name == "build.gradle.kts" && file.parent !in excludePaths
    }.map { file ->
        file.path.removePrefix("${rootDir.path}${File.separator}")
            .removeSuffix("${File.separator}build.gradle.kts")
            .replace(File.separator, ":")
    }.toList().toTypedArray()
}

include(*discoverSubprojects())
