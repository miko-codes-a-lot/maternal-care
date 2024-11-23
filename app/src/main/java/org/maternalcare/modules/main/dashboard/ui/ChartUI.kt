package org.maternalcare.modules.main.dashboard.ui

import android.graphics.Typeface
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieEntry
import org.maternalcare.R
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.residence.viewmodel.ResidenceViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.toObjectId

@Composable
fun ChartUI(
    userDto: UserDto,
    addressDto: AddressDto?,
    isArchive: Boolean = false,
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize()
    ) {
        var expanded by remember { mutableStateOf(false) }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        var selectedText by remember { mutableStateOf("Total Pregnant Records") }
        var pieChartData by remember { mutableStateOf(emptyList<ChartData>()) }
        val residenceViewModel: ResidenceViewModel = hiltViewModel()

        val selectedChartType = listOf(
            "Total Pregnant Records",
            "Normal List",
            "Critical List",
            "Complete",
            "In-Complete"
        )
        val chartTypes = mapOf(
            "Total Pregnant Records" to CheckupStatus.PREGNANT,
            "Normal List" to CheckupStatus.NORMAL,
            "Critical List" to CheckupStatus.CRITICAL,
            "Complete" to CheckupStatus.COMPLETE,
            "In-Complete" to CheckupStatus.INCOMPLETE
        )

        LaunchedEffect(selectedText) {

            val checkupStatus = chartTypes[selectedText] ?: CheckupStatus.ALL
            val residences = when (checkupStatus) {
                    CheckupStatus.PREGNANT -> residenceViewModel.fetchAllUsersByCheckup(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    checkup = 1,
                    isArchive = isArchive
                )

                CheckupStatus.NORMAL -> residenceViewModel.fetchAllUsersWithNormalCondition(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    isNormal = true,
                    isArchive = isArchive
                )

                CheckupStatus.CRITICAL -> residenceViewModel.fetchAllUsersWithCriticalCondition(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    isCritical = true,
                    isArchive = isArchive
                )

                CheckupStatus.COMPLETE -> residenceViewModel.fetchUsers(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    addressName = addressDto?.name,
                    isArchive = isArchive,
                    isCompleted = true
                )

                CheckupStatus.INCOMPLETE -> residenceViewModel.fetchUsers(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    addressName = addressDto?.name,
                    isArchive = isArchive,
                    isCompleted = false
                )

                else -> residenceViewModel.fetchUsers(
                    userId = userDto.id.toObjectId(),
                    isSuperAdmin = userDto.isSuperAdmin,
                    addressName = addressDto?.name,
                    isArchive = isArchive
                )
            }
            Log.d("LaunchedEffect", "Selected chart type: $selectedText, Residence size: ${residences.size}")

            val totalResidences = residenceViewModel.fetchTotalUserCount(
                userId = userDto.id.toObjectId(),
                isSuperAdmin = userDto.isSuperAdmin,
                isArchive = isArchive
            )
            val percentage = if (totalResidences > 0) (residences.size.toFloat() / totalResidences) * 100 else 0f

            pieChartData = listOf(
                ChartData(selectedText, percentage),
                ChartData("Remaining", 100 - percentage)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 2.dp, color = Color(0xFF6650a4), shape = RectangleShape)
                        .height(55.dp)
                        .background(Color.White)
                        .clickable { expanded = !expanded }
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6650a4),
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = icon,
                            tint = Color(0xFF6650a4),
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                            .background(Color.White)
                            .border(width = 1.dp, color = Color(0xFF6650a4))
                    ) {
                        selectedChartType.forEach { chartType ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedText = chartType
                                    expanded = false
                                },
                                text = {
                                    Box(
                                        modifier = Modifier
                                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                            .background(Color(0xFF6650a4))
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = chartType,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFFFFF),
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .size(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Crossfade(targetState = pieChartData, label = "") { data ->
                    AndroidView(factory = { context ->
                        PieChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            this.description.isEnabled = false
                            this.isDrawHoleEnabled = false
                            this.legend.isEnabled = true
                            this.legend.textSize = 14F
                            this.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.CENTER
                            ContextCompat.getColor(context, R.color.white)
                        }
                    },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp), update = {
                            updatePieChartWithData(it, data)
                        })
                }
            }
        }
    }
}

fun updatePieChartWithData(chart: PieChart, data: List<ChartData>) {
    val entries = data.map { PieEntry(it.value ?: 0f, it.browserName ?: "") }
    val typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)

    val dataSet = PieDataSet(entries, "").apply {
        colors = listOf(Color(0xFF6650a4).toArgb(), Color(0xFF41a3a4).toArgb())
        yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        sliceSpace = 2f
        valueTextSize = 15f
        valueTextColor = Color.White.toArgb()
        this.valueTypeface = typeface
    }
    chart.data = PieData(dataSet)
    chart.notifyDataSetChanged()
    chart.invalidate()
}