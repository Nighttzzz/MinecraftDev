/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2016 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.platform.mixin.debug

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder

internal val MIXIN_DEBUG_KEY = Key.create<Boolean>("mixin.debug")

internal fun UserDataHolder.hasMixinDebugKey(): Boolean = getUserData(MIXIN_DEBUG_KEY) == true
