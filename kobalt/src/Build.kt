import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.bintray
import com.beust.kobalt.project
import com.beust.kobalt.repos
import org.apache.maven.model.Developer
import org.apache.maven.model.License
import org.apache.maven.model.Model
import org.apache.maven.model.Scm

val repos = repos()

val dev = false
val kobaltDependency = if (dev) "kobalt" else "kobalt-plugin-api"

val p = project {

    name = "kobalt-maven-local"
    group = "net.thauvin.erik"
    artifactId = name
    version = "0.4.0-beta"

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
        compile("com.beust:$kobaltDependency:0.861")
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
        publish = false
    }
}