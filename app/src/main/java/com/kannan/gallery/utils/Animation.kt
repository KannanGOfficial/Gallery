package com.kannan.gallery.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

object Animation {

    val combinedAnimation = (fadeIn(animationSpec = tween(0, delayMillis = 0)) +
            scaleIn(initialScale = 0.92f, animationSpec = tween(0, delayMillis = 0)))
        .togetherWith(fadeOut(animationSpec = tween(0)))
}