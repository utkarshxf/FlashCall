package com.example.myapplication.myapplication.flashcall.utils.dragAndDrop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.Feedback
import com.example.myapplication.myapplication.flashcall.Screens.AddedLinkLayout
import com.example.myapplication.myapplication.flashcall.Screens.feedback.FeedbackListUtil
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import com.example.myapplication.myapplication.flashcall.ViewModel.feedback.FeedbackViewModel
import com.example.myapplication.myapplication.flashcall.ui.theme.Background
import com.jetpack.draganddroplist.rememberDragDropListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun FeedbackDragDropList(
    items: List<Feedback>,
    onMove: (Int, Int) -> Unit,
    viewModel: FeedbackViewModel
) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove)
    val height = items.size * 66

    LazyColumn(
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, offset ->
                        change.consumeAllChanges()
                        dragDropListState.onDrag(offset = offset)

                        if (overScrollJob?.isActive == true)
                            return@detectDragGesturesAfterLongPress

                        dragDropListState
                            .checkForOverScroll()
                            .takeIf { it != 0f }
                            ?.let {
                                overScrollJob = scope.launch {
                                    dragDropListState.lazyListState.scrollBy(it)
                                }
                            } ?: kotlin.run { overScrollJob?.cancel() }
                    },
                    onDragStart = { offset -> dragDropListState.onDragStart(offset) },
                    onDragEnd = { dragDropListState.onDragInterrupted() },
                    onDragCancel = { dragDropListState.onDragInterrupted() }
                )
            }
            .fillMaxWidth()
            .height(height.dp),
        state = dragDropListState.lazyListState
    ) {
        itemsIndexed(items) { index, feedbackResponse ->
            Column(
                modifier = Modifier
                    .composed {
                        val offsetOrNull = dragDropListState.elementDisplacement.takeIf {
                            index == dragDropListState.currentIndexOfDraggedItem
                        }
                        Modifier.graphicsLayer {
                            translationY = offsetOrNull ?: 0f
                        }
                    }
                    .fillMaxWidth()
            ) {

                feedbackResponse.feedbacks?.forEach { feedback ->
                    FeedbackListUtil(feedback) {
                        viewModel.updateFeedback(
                            UpdateFeedback(
                                clientId = feedback.clientId?.id,
                                createdAt = feedback.createdAt,
                                creatorId = feedbackResponse.creatorId,
                                feedbackText = feedback.feedback,
                                rating = feedback.rating,
                                showFeedback = it
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
