package org.maternalcare.modules.main.menu.model

data class MenuItem(
    val text: String,
    val action: () -> Unit,
    val iconResId: Int
)
