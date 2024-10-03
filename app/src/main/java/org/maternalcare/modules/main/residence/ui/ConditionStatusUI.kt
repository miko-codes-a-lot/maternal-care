package org.maternalcare.modules.main.residence.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Preview(showSystemUi = true)
@Composable
fun ConditionStatusPreview() {
    ConditionStatusUI(
        navController = rememberNavController(),
        userDto = UserDto(),
        userCondition = UserConditionDto(),
        currentUser = UserDto()
    )
}

@Composable
fun ConditionStatusUI(
    navController: NavController,
    userDto: UserDto,
    userCondition: UserConditionDto,
    currentUser: UserDto
) {
    val conditionStates = mapOf(
        "Tuberculosis" to Pair(
            remember { mutableStateOf(userCondition.tuberculosisPersonal) },
            remember { mutableStateOf(userCondition.tuberculosisFamily) }
        ),
        "Heart Diseases" to Pair(
            remember { mutableStateOf(userCondition.heartDiseasesPersonal) },
            remember { mutableStateOf(userCondition.heartDiseasesFamily) }
        ),
        "Diabetes" to Pair(
            remember { mutableStateOf(userCondition.diabetesPersonal) },
            remember { mutableStateOf(userCondition.diabetesFamily) }
        ),
        "Hypertension" to Pair(
            remember { mutableStateOf(userCondition.hypertensionPersonal) },
            remember { mutableStateOf(userCondition.hypertensionFamily) }
        ),
        "Branchial Asthma" to Pair(
            remember { mutableStateOf(userCondition.branchialAsthmaPersonal) },
            remember { mutableStateOf(userCondition.branchialAsthmaFamily) }
        ),
        "Urinary Tract Infection" to Pair(
            remember { mutableStateOf(userCondition.urinaryTractInfectionPersonal) },
            remember { mutableStateOf(userCondition.urinaryTractInfectionFamily) }
        ),
        "Parasitism" to Pair(
            remember { mutableStateOf(userCondition.parasitismPersonal) },
            remember { mutableStateOf(userCondition.parasitismFamily) }
        ),
        "Goiter" to Pair(
            remember { mutableStateOf(userCondition.goitersPersonal) },
            remember { mutableStateOf(userCondition.goitersFamily) }
        ),
        "Anemia" to Pair(
            remember { mutableStateOf(userCondition.anemiaPersonal) },
            remember { mutableStateOf(userCondition.anemiaFamily) }
        )
    )

    val genitalTractInfection = remember { mutableStateOf(userCondition.genitalTractInfection ?: "") }
    val otherInfectionsDiseases = remember { mutableStateOf(userCondition.otherInfectionsDiseases ?: "") }
    val isUnderWeight = remember { mutableStateOf(userCondition.isUnderWeight) }
    val isOverWeight = remember { mutableStateOf(userCondition.isOverWeight) }
    val textFields = listOf(
        "Genital Tract Infection" to genitalTractInfection,
        "Other Infections or Diseases" to otherInfectionsDiseases
    )
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Illness History",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 19.sp,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Past & Present Illness",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            )
            {
                Text(
                    text = "Personal",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(35.dp))
                Text(
                    text = "Family",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(422.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conditionStates.toList()) { (label, states) ->
                LabelWithCheckBoxes(
                    label = label,
                    personalChecked = states.first.value,
                    familyChecked = states.second.value,
                    onPersonalCheckedChange = { checked -> states.first.value = checked },
                    onFamilyCheckedChange = { checked -> states.second.value = checked }
                )
            }
        }
        textFields.forEach { (label, state) ->
            TextFieldStatusContainer(
                label = label,
                value = state.value,
                onValueChange = { state.value = it }
            )
        }
        NutritionalStatus(
            isUnderWeight = isUnderWeight.value,
            isOverWeight = isOverWeight.value,
            onSelect = { selectedOption ->
                isUnderWeight.value = selectedOption == "UnderWeight"
                isOverWeight.value = selectedOption == "OverWeight"
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        ButtonSaveStatus(
            userId = userDto.id!!,
            navController = navController,
            conditionStates = conditionStates,
            genitalTractInfection = genitalTractInfection,
            otherInfectionsDiseases = otherInfectionsDiseases,
            isUnderWeight = isUnderWeight.value,
            isOverWeight = isOverWeight.value,
            currentUser = currentUser,
            userCondition = userCondition
        )
    }
}

@Composable
fun LabelWithCheckBoxes(
    label: String,
    personalChecked: Boolean,
    familyChecked: Boolean,
    onPersonalCheckedChange: (Boolean) -> Unit,
    onFamilyCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Checkbox(
                    checked = personalChecked,
                    onCheckedChange = onPersonalCheckedChange,
                    colors = CheckboxDefaults.colors(Color(0xFF6650a4))
                )
                Spacer(modifier = Modifier.width(45.dp))
                Checkbox(
                    checked = familyChecked,
                    onCheckedChange = onFamilyCheckedChange,
                    colors = CheckboxDefaults.colors(Color(0xFF6650a4))
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Black
        )
    }
}

@Composable
fun TextFieldStatusContainer(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = " : ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFFFFF),
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.Black
    )
}

@Composable
fun NutritionalStatus(
    isUnderWeight: Boolean,
    isOverWeight: Boolean,
    onSelect: (String) -> Unit
) {
    val selectionOption = listOf("UnderWeight", "OverWeight")
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            when {
                isUnderWeight -> "UnderWeight"
                isOverWeight -> "OverWeight"
                else -> "UnderWeight"
            }
        )
    }
    Row {
        selectionOption.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            onSelect(text)
                        }
                    )
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                        onSelect(text)
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue,
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
fun ButtonSaveStatus(
    userId: String,
    currentUser: UserDto,
    conditionStates: Map<String, Pair<MutableState<Boolean>, MutableState<Boolean>>>,
    genitalTractInfection: MutableState<String>,
    otherInfectionsDiseases: MutableState<String>,
    navController: NavController,
    userCondition: UserConditionDto,
    isUnderWeight: Boolean,
    isOverWeight: Boolean,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            val userConditionStatus  = UserConditionDto(
                id = userCondition.id,
                userId = userId,
                tuberculosisPersonal = conditionStates["Tuberculosis"]?.first?.value ?: false,
                tuberculosisFamily = conditionStates["Tuberculosis"]?.second?.value ?: false,
                heartDiseasesPersonal = conditionStates["Heart Diseases"]?.first?.value ?: false,
                heartDiseasesFamily = conditionStates["Heart Diseases"]?.second?.value ?: false,
                diabetesPersonal = conditionStates["Diabetes"]?.first?.value ?: false,
                diabetesFamily = conditionStates["Diabetes"]?.second?.value ?: false,
                hypertensionPersonal = conditionStates["Hypertension"]?.first?.value ?: false,
                hypertensionFamily = conditionStates["Hypertension"]?.second?.value ?: false,
                branchialAsthmaPersonal = conditionStates["Branchial Asthma"]?.first?.value ?: false,
                branchialAsthmaFamily = conditionStates["Branchial Asthma"]?.second?.value ?: false,
                urinaryTractInfectionPersonal = conditionStates["Urinary Tract Infection"]?.first?.value ?: false,
                urinaryTractInfectionFamily = conditionStates["Urinary Tract Infection"]?.second?.value ?: false,
                parasitismPersonal = conditionStates["Parasitism"]?.first?.value ?: false,
                parasitismFamily = conditionStates["Parasitism"]?.second?.value ?: false,
                goitersPersonal = conditionStates["Goiter"]?.first?.value ?: false,
                goitersFamily = conditionStates["Goiter"]?.second?.value ?: false,
                anemiaPersonal = conditionStates["Anemia"]?.first?.value ?: false,
                anemiaFamily = conditionStates["Anemia"]?.second?.value ?: false,
                genitalTractInfection = genitalTractInfection.value,
                otherInfectionsDiseases = otherInfectionsDiseases.value,
                isUnderWeight = isUnderWeight,
                isOverWeight = isOverWeight,
                createdById = userCondition.createdById ?: currentUser.id
            )

            scope.launch {
                try {
                    val result = userViewModel.upsertCondition(userCondition)
                    if (result.isSuccess) {
                        Log.d("CheckUpSave", "Saving CheckUpDto: $userConditionStatus")

                        navController.navigate(MainNav.ConditionStatus(userId)) {
                            popUpTo(MainNav.ConditionStatus(userId)) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.e("saving", "Error: ${result.exceptionOrNull()}")
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error updating data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        },
        modifier = Modifier
            .width(360.dp)
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color.White
        )
    ) {
        Text(text = "Confirm", fontSize = 17.sp)
    }
}