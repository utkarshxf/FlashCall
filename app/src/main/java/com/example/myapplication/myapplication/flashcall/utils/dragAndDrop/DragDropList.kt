package com.jetpack.draganddroplist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Screens.AddedLinkLayout
import com.example.myapplication.myapplication.flashcall.ViewModel.RegistrationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun DragDropList(
    items: List<LinkData>,
    onMove: (Int, Int) -> Unit,
    viewModel: RegistrationViewModel
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
                    onDragEnd = { dragDropListState.onDragInterrupted()
                                viewModel.linksPositionReordered()},
                    onDragCancel = { dragDropListState.onDragInterrupted() }
                )
            }
            .fillMaxWidth()
            .height(height.dp),
        state = dragDropListState.lazyListState
    ) {
        itemsIndexed(items) { index, item ->
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
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            ) {
                AddedLinkLayout(item = item, isActive = {
                    viewModel.isActiveAdditionalLink(index)
                }, edit = {
                    viewModel.showEditingAdditionalLayout(isShow = true, index = index)
                }, delete = {
                    viewModel.deleteAdditionalLinks(item = item)
                } )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}





















