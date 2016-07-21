/*
 * MavenLocalPlugin.kt
 *
 * Copyright (c) 2016, Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of this project nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.thauvin.erik.kobalt.plugin.maven.local

import com.beust.kobalt.api.BasePlugin
import com.beust.kobalt.api.ILocalMavenRepoPathInterceptor
import com.beust.kobalt.misc.log
import com.beust.kobalt.misc.warn
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest
import java.io.File
import java.io.FileInputStream
import java.util.*

public class MavenLocalPlugin : BasePlugin(), ILocalMavenRepoPathInterceptor {
    val MAVEN_LOCAL_REPO_PROPERTY = "maven.repo.local"

    var mvnLocalPath: String? = null

    override val name = "kobalt-maven-local"

    init {
        val factory = DefaultSettingsBuilderFactory()
        val builder = factory.newInstance()
        val settings = DefaultSettingsBuildingRequest()

        val localProps = Properties().apply {
            FileInputStream("local.properties").use { fis -> load(fis) }
        }

        val sysProps = System.getProperties()

        if (localProps.containsKey(MAVEN_LOCAL_REPO_PROPERTY)) {
            val mvnLocalProp = localProps.getProperty(MAVEN_LOCAL_REPO_PROPERTY, "")
            if (mvnLocalProp.length > 0) {
                mvnLocalPath = mvnLocalProp
            }
        } else if (sysProps.containsKey(MAVEN_LOCAL_REPO_PROPERTY)) {
            val mvnLocalProp = sysProps.getProperty(MAVEN_LOCAL_REPO_PROPERTY, "")
            if (mvnLocalProp.length > 0) {
                mvnLocalPath = mvnLocalProp
            }
        } else {
            settings.systemProperties = sysProps
            if (localProps.isNotEmpty()) {
                if (localProps.containsKey(MAVEN_LOCAL_REPO_PROPERTY)) {
                    sysProps.put(MAVEN_LOCAL_REPO_PROPERTY, localProps.getProperty(MAVEN_LOCAL_REPO_PROPERTY))
                }
            }
            settings.userSettingsFile = File(System.getProperty("user.home"), ".m2/settings.xml")

            val m2Home = System.getProperty("M2_HOME")
            if (m2Home != null) {
                settings.globalSettingsFile = File(m2Home, "conf/settings.xml")
            }

            val result = builder.build(settings)
            mvnLocalPath = result.effectiveSettings.localRepository

            if (mvnLocalPath == null) {
                warn("Unable to parse local maven settings.")
            }
        }
    }

    override fun repoPath(currentPath: String): String {
        if (mvnLocalPath != null) {
            log(2, "Setting Maven Local Repository path to: " + mvnLocalPath)
            return mvnLocalPath.toString()
        } else {
            return currentPath
        }
    }
}