package ch.leadrian.gradle.plugin.propertykeygenerator

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import java.io.File

internal object ResourceDirectoriesResolver {

    fun resolve(project: Project): Set<File> {
        return project.convention
                .findPlugin(JavaPluginConvention::class.java)
                ?.sourceSets
                ?.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                ?.resources
                ?.sourceDirectories
                ?.files
                .orEmpty()
    }

}