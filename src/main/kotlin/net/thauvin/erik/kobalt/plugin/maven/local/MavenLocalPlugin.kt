package net.thauvin.erik.kobalt.plugin.maven.local

import com.beust.kobalt.api.BasePlugin
import com.beust.kobalt.api.ILocalMavenRepoPathInterceptor
import com.beust.kobalt.api.KobaltContext
import com.beust.kobalt.api.Project
import com.beust.kobalt.misc.log
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest
import java.io.File

public class MavenLocalPlugin : BasePlugin(), ILocalMavenRepoPathInterceptor {
    var mavenLocalPath: String = ""

    override val name = "kobalt-maven-local"

    override fun apply(project: Project, context: KobaltContext) {
        val factory = DefaultSettingsBuilderFactory()
        val builder = factory.newInstance()
        val settings = DefaultSettingsBuildingRequest()

        settings.systemProperties = System.getProperties()
        settings.userSettingsFile = File(System.getProperty("user.home"), ".m2/settings.xml")

        val m2Home = System.getProperty("M2_HOME")
        if (m2Home != null) {
            settings.globalSettingsFile = File(m2Home, "conf/settings.xml")
        }

        val result = builder.build(settings)
        mavenLocalPath = result.effectiveSettings.localRepository
    }

    override fun repoPath(currentPath: String): String {
        if (mavenLocalPath.isNotEmpty()) {
            log(2, "Setting maven local repository path to: " + mavenLocalPath)
            return mavenLocalPath
        } else {
            return currentPath
        }
    }
}