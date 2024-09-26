package com.example.myapplication.myapplication.flashcall.Screens.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.myapplication.flashcall.ui.theme.MainColor
import com.example.myapplication.myapplication.flashcall.ui.theme.SwitchColor

@Composable
fun WideSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(checked, label = "switch_transition")

    val thumbOffset by transition.animateDp(
        label = "thumb_offset",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { if (it) 34.dp else 2.dp }

    val trackColor by transition.animateColor(
        label = "track_color",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { if (it) MainColor else SwitchColor }

    val thumbColor by transition.animateColor(
        label = "thumb_color",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { if (it) Color.White else Color.White.copy(alpha = 0.9f) }

    val thumbScale by transition.animateFloat(
        label = "thumb_scale",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { if (it) 1f else 0.9f }

    Box(
        modifier = modifier
            .width(60.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Surface(
            modifier = Modifier
                .size(24.dp)
                .offset(x = thumbOffset)
                .scale(thumbScale)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    clip = false
                ),
            shape = CircleShape,
            color = thumbColor
        ){}
    }
}