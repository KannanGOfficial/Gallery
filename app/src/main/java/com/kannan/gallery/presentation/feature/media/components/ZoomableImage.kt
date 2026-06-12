package com.kannan.gallery.presentation.feature.media.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.kannan.gallery.R
import com.kannan.gallery.utils.MediaEqualityDelegate
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ZoomableImage(
    uri: String,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    isTransitionComplete: Boolean = false,
    onScaleChanged: (Float) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    val coroutineScope = rememberCoroutineScope()

    val scaleAnimatable = remember { Animatable(1f) }
    val offsetXAnimatable = remember { Animatable(0f) }
    val offsetYAnimatable = remember { Animatable(0f) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .placeholder(R.color.eerie_black)
            .placeholderMemoryCacheKey(uri)
            .build(),
        modelEqualityDelegate = MediaEqualityDelegate(),
        contentScale = ContentScale.FillWidth,
        filterQuality = FilterQuality.None
    )

    fun calculateBoundedOffset(
        currentOffset: Offset,
        currentScale: Float,
        size: IntSize
    ): Offset {
        val maxX = (size.width * (currentScale - 1)) / 2f
        val maxY = (size.height * (currentScale - 1)) / 2f
        return Offset(
            x = currentOffset.x.coerceIn(-maxX, maxX),
            y = currentOffset.y.coerceIn(-maxY, maxY)
        )
    }

    fun animateBackToNormal() {
        coroutineScope.launch {
            launch {
                scaleAnimatable.snapTo(scale)
                scaleAnimatable.animateTo(minScale, tween(300)) {
                    scale = value
                }
            }
            launch {
                offsetXAnimatable.snapTo(offset.x)
                offsetXAnimatable.animateTo(0f, tween(300)) {
                    offset = offset.copy(x = value)
                }
            }
            launch {
                offsetYAnimatable.snapTo(offset.y)
                offsetYAnimatable.animateTo(0f, tween(300)) {
                    offset = offset.copy(y = value)
                }
            }
            onScaleChanged(minScale)
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { layoutSize = it }
            .clipToBounds()
            .pointerInput(Unit) {
                // ── Pan & Zoom ──────────────────────────────────────────────
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)

                    var zoom = 1f
                    var pan = Offset.Zero
                    var pastTouchSlop = false
                    val touchSlop = viewConfiguration.touchSlop

                    do {
                        val event = awaitPointerEvent()
                        val canceled = event.changes.any { it.isConsumed }
                        if (canceled) break

                        val zoomChange = event.calculateZoom()
                        val panChange = event.calculatePan()

                        if (!pastTouchSlop) {
                            zoom *= zoomChange
                            pan += panChange
                            val centroidSize =
                                event.calculateCentroidSize(useCurrent = false)
                            val zoomMotion = abs(1 - zoom) * centroidSize
                            val panMotion = pan.getDistance()

                            if (zoomMotion > touchSlop || panMotion > touchSlop) {
                                pastTouchSlop = true
                            }
                        }

                        if (pastTouchSlop) {
                            val isMultiTouch =
                                event.changes.count { it.pressed } > 1

                            // Consume only when pinching or already zoomed in.
                            // At scale == 1f with a single finger, let the
                            // HorizontalPager handle the horizontal swipe.
                            if (isMultiTouch || scale > 1f) {
                                val newScale =
                                    (scale * zoomChange).coerceIn(minScale, maxScale)
                                val newOffset = calculateBoundedOffset(
                                    offset + panChange,
                                    newScale,
                                    layoutSize
                                )
                                scale = newScale
                                offset = newOffset
                                onScaleChanged(scale)
                                event.changes.forEach { it.consume() }
                            }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
            .pointerInput(Unit) {
                // ── Tap & Double-tap ────────────────────────────────────────
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        coroutineScope.launch {
                            val targetScale = if (scale > 1f) minScale else 2.5f
                            val targetOffset = if (targetScale == minScale) {
                                Offset.Zero
                            } else {
                                val centeredOffset = Offset(
                                    x = (layoutSize.width / 2f - tapOffset.x) * (targetScale - 1f),
                                    y = (layoutSize.height / 2f - tapOffset.y) * (targetScale - 1f)
                                )
                                calculateBoundedOffset(
                                    centeredOffset,
                                    targetScale,
                                    layoutSize
                                )
                            }

                            launch {
                                scaleAnimatable.snapTo(scale)
                                scaleAnimatable.animateTo(targetScale, tween(300)) {
                                    scale = value
                                }
                            }
                            launch {
                                offsetXAnimatable.snapTo(offset.x)
                                offsetXAnimatable.animateTo(
                                    targetOffset.x,
                                    tween(300)
                                ) { offset = offset.copy(x = value) }
                            }
                            launch {
                                offsetYAnimatable.snapTo(offset.y)
                                offsetYAnimatable.animateTo(
                                    targetOffset.y,
                                    tween(300)
                                ) { offset = offset.copy(y = value) }
                            }
                            onScaleChanged(targetScale)
                        }
                    },
                    onTap = {
                        if (scale > 1f) animateBackToNormal() else onDismiss()
                    }
                )
            }
    ) {
        Image(
            painter = painter,
            contentDescription = uri,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxSize()
                .then(
                    // Only apply zoom transforms after shared transition is done
                    if (isTransitionComplete) {
                        Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                    } else Modifier
                )
        )
    }
}