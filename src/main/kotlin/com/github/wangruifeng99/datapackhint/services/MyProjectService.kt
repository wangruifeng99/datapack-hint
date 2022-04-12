package com.github.wangruifeng99.datapackhint.services

import com.intellij.openapi.project.Project
import com.github.wangruifeng99.datapackhint.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
