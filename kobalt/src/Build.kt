import com.beust.kobalt.buildScript
import com.beust.kobalt.file
import com.beust.kobalt.localMaven
import com.beust.kobalt.misc.kobaltLog
import com.beust.kobalt.plugin.application.application
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.bintray
import com.beust.kobalt.project
import net.thauvin.erik.kobalt.plugin.versioneye.versionEye
import org.apache.maven.model.Developer
import org.apache.maven.model.License
import org.apache.maven.model.Model
import org.apache.maven.model.Scm

val semver = "0.5.2"

val bs = buildScript {
    val f = java.io.File("kobaltBuild/libs/kobalt-maven-local-$semver.jar")
    val p = if (f.exists()) {
        kobaltLog(1, "  >>> Using: ${f.path}")
        file(f.path)
    } else {
        "net.thauvin.erik:kobalt-maven-local:"
    }
    plugins("net.thauvin.erik:kobalt-versioneye:", p)
    repos(localMaven())
}

val dev = false
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

    sourceDirectories {
        path("src/main/kotlin")
    }

    sourceDirectoriesTest {
        path("src/test/kotlin")
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

    bintray {
        publish = true
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

    sourceDirectories {
        path("src/main/java")
    }

    sourceDirectoriesTest {
        path("src/test/java")
    }

    dependencies {
    }

    dependenciesTest {
    }

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.Main"
    }
}