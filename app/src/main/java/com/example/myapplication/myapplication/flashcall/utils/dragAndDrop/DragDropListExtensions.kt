package com.jetpack.draganddroplist

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.getVisibleItemInfoFor(absolute: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(absolute - this.layoutInfo.visibleItemsInfo.first().index)
}

val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to)
        return

    val element = this.removeAt(from) ?: return
    this.add(to, element)
}

//fun <E> List<E>.move(fromIndex: Int, toIndex: Int) {
//    if (fromIndex == toIndex)
//        return
//
//    val element = this.removeAt(from) ?: return
//    val element = this.re
//    this.add(to, element)
//}