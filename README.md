# Maven Local Repository plug-in for [Kobalt](http://beust.com/kobalt/home/index.html)

[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause) [![Build Status](https://travis-ci.org/ethauvin/kobalt-maven-local.svg?branch=master)](https://travis-ci.org/ethauvin/kobalt-maven-local)

The plug-in will detect the Maven Local Repository location, similarly to Gradle's [mavenLocal()](https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.dsl.RepositoryHandler.html#org.gradle.api.artifacts.dsl.RepositoryHandler:mavenLocal()) repository handler.

To use the plug-in include the following in your `Build.kt` file:

```kotlin
import net.thauvin.erik.kobalt.plugin.maven.local.*

val repos = repos(localMaven())

val pl = plugins("net.thauvin.erik:kobalt-maven-local:")

val p = project {
    name = "example"
}
```
[Example](https://github.com/ethauvin/kobalt-maven-local/blob/master/example/kobalt/src/Build.kt)

The `localMaven()` directive will add the location of the Maven Local Repository to Kobalt's repositories list.

To publish to the Maven Local Repository use the `publishToMavenLocal` task:

```
./kobaltw publishToMavenLocal
```

### Locations

The plug-in looks for the Maven Local Repository in the following locations:

## `maven.repo.local`

Either as a system property or in the project's `local.properties` file.

To set the system property use:

```
./kobaltw -Dmaven.repo.local=~/foo/repository ...
```

or in `local.properties` add:

```
maven.repo.local=~/foo/repository 
```

## `~/.m2/settings.xml`

If `<localRepository>` is set:

```xml
<localRepository>~/foo/repository</localRepository>
```

## `$M2_HOME/conf/settings.xml`

If the `$M2_HOME` environment variable and `<localRepository>` are set.

## `~/.m2/repository`

If the directory exists.

