/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2017 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.util.PathUtil
import org.intellij.lang.annotations.Language

/**
 * Like most things in this project at this point, taken from the intellij-rust folks
 * https://github.com/intellij-rust/intellij-rust/blob/master/src/test/kotlin/org/rust/TestProjectBuilder.kt
 */
class TestProjectBuilder(
    private val fixture: JavaCodeInsightTestFixture,
    private val project: Project = fixture.project,
    private val root: VirtualFile = project.baseDir
) {
    private val fileList = mutableListOf<PsiFile>()

    fun java(path: String, @Language("JAVA") code: String) = file(path, code, ".java")

    fun at(path: String, @Language("Access Transformers") code: String) = file(path, code, "_at.cfg")

    private fun file(path: String, code: String, ext: String): PsiFile {
        check(path.endsWith(ext))
        val dir = PathUtil.getParentPath(path)
        val vDir = VfsUtil.createDirectoryIfMissing(root, dir)
        val vFile = vDir.createChildData(this, PathUtil.getFileName(path))
        VfsUtil.saveText(vFile, code.trimIndent())

        val psiFile = fixture.configureByFile(vFile.path)!!
        fileList.add(psiFile)

        return psiFile
    }

    fun build(builder: TestProjectBuilder.() -> Unit): TestProject {
        runWriteAction {
            VfsUtil.markDirtyAndRefresh(false, true, true, root)
            builder()
            VfsUtil.markDirtyAndRefresh(false, true, true, root)
        }
        return TestProject(project, root, fileList)
    }
}

class TestProject(val project: Project, val root: VirtualFile, val files: List<PsiFile>)