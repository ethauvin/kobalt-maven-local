import com.beust.kobalt.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.publish.*
import net.thauvin.erik.kobalt.plugin.versioneye.*
import org.apache.maven.model.*

// ./kobaltw install

val bs = buildScript {
    repos(file("K:/maven/repository"))
    plugins("net.thauvin.erik:kobalt-versioneye:")
}

val dev by profile()
val kobaltDependency = if (dev) "kobalt" else "kobalt-plugin-api"

val p = project {
    name = "kobalt-maven-local"
    group = "net.thauvin.erik"
    artifactId = name
    version = "0.5.3"

    pom = Model().apply {
        description = "Maven Local Repository plug-in for the Kobalt build system."
        url = "https://github.com/ethauvin/kobalt-exec"
        licenses = listOf(License().apply {
            name = "BSD 3-Clause"
            url = "https://opensource.org/licenses/BSD-3-Clause"
        })
        scm = Scm().apply {
            url = "https://github.com/ethauvin/kobalt-maven-local"
            connection = "https://github.com/ethauvin/kobalt-maven-local.git"
            developerConnection = "git@github.com:ethauvin/kobalt-maven-local.git"
        }
        developers = listOf(Developer().apply {
            id = "ethauvin"
            name = "Erik C. Thauvin"
            email = "erik@thauvin.net"
        })
    }

    dependencies {
        compile("org.apache.maven:maven-settings-builder:3.3.9")
        compile("com.beust:$kobaltDependency:")
    }

    dependenciesTest {
        compile("org.testng:testng:")

    }

    install {
        target = "K:/maven/repository/net/thauvin/erik/kobalt-maven-local/$version/"
    }

    assemble {
        jar {
            fatJar = true
        }

        mavenJars {
            fatJar = true
        }
    }

    autoGitTag {
        enabled = true
        message = "Version $version"
    }

    bintray {
        publish = true
        description = "Release version $version"
        vcsTag = version
    }

    versionEye {
        org = "thauvin"
        team = "Owners"
    }
}