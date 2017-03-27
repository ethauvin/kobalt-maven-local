import com.beust.kobalt.*
import com.beust.kobalt.misc.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.publish.*
import net.thauvin.erik.kobalt.plugin.versioneye.*
import org.apache.maven.model.*
import java.io.*

val semver = "0.5.2"

val bs = buildScript {
    val p = with(File("kobaltBuild/libs/kobalt-maven-local-$semver.jar")) {
        if (exists()) {
            kobaltLog(1, "  >>> Using: $path")
            file(path)
        } else {
            "net.thauvin.erik:kobalt-maven-local:"
        }
    }
    plugins("net.thauvin.erik:kobalt-versioneye:", p)
    repos(localMaven())
}

val dev by profile()
val kobaltDependency = if (dev) "kobalt" else "kobalt-plugin-api"

val p = project {
    name = "kobalt-maven-local"
    group = "net.thauvin.erik"
    artifactId = name
    version = semver

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
        compile("com.beust:$kobaltDependency:")
        compile("org.apache.maven:maven-settings-builder:3.3.9")
    }

    dependenciesTest {
        compile("org.testng:testng:")

    }

    assemble {
        jar {
            fatJar = true
        }

        mavenJars {}
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

val example = project(p) {

    println("  >>> LOCAL MAVEN REPO: " + localMaven())

    name = "example"
    group = "com.example"
    artifactId = name
    version = "0.1"
    directory = "example"

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.Main"
    }
}