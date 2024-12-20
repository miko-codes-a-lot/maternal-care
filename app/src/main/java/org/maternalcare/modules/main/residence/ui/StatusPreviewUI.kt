package org.maternalcare.modules.main.residence.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.maternalcare.modules.main.user.model.dto.UserConditionDto

@Preview(showSystemUi = true)
@Composable
fun PregnantStatusPreview(){
    StatusPreviewUI(
        userCondition = UserConditionDto()
    )
}

@Composable
fun StatusPreviewUI (
    userCondition: UserConditionDto,
){
    Log.d("StatusPreviewUI", "UserCondition value: $userCondition")
    val statesValue = remember(userCondition) {
        listOf(
            "Tuberculosis" to getConditionStatus(userCondition.tuberculosisFamily, userCondition.tuberculosisPersonal),
            "Heart Diseases" to getConditionStatus(userCondition.heartDiseasesFamily, userCondition.heartDiseasesPersonal),
            "Diabetes" to getConditionStatus(userCondition.diabetesFamily, userCondition.diabetesPersonal),
            "Hypertension" to getConditionStatus(userCondition.hypertensionFamily, userCondition.hypertensionPersonal),
            "Branchial Asthma" to getConditionStatus(userCondition.branchialAsthmaFamily, userCondition.branchialAsthmaPersonal),
            "Urinary Tract Infection" to getConditionStatus(userCondition.urinaryTractInfectionFamily, userCondition.urinaryTractInfectionPersonal),
            "Parasitism" to getConditionStatus(userCondition.parasitismFamily, userCondition.parasitismPersonal),
            "Goiter" to getConditionStatus(userCondition.goitersFamily, userCondition.goitersPersonal),
            "Anemia" to getConditionStatus(userCondition.anemiaFamily, userCondition.anemiaPersonal),
            "Genital Tract Infection" to userCondition.genitalTractInfection,
            "Other Infections" to userCondition.otherInfectionsDiseases,
            "Notes" to userCondition.notes,
            "Is Normal" to if (userCondition.isNormal) "Yes" else "No",
            "Is Critical" to if (userCondition.isCritical) "Yes" else "No"
        ).associate { (label, value) -> label to mutableStateOf(value) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pregnant Status History",
            fontFamily = FontFamily.SansSerif,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 3.dp, top = 7.dp)
        )
        ViewStatusData(statesValue)
        Spacer(modifier = Modifier.padding(top = 9.dp))
    }
}

fun getConditionStatus(family: Boolean, personal: Boolean): String {
    return when {
        personal -> "Personal"
        family -> "Family"
        else -> "None"
    }
}

@Composable
fun ViewStatusData(stateValues: Map<String, MutableState<String>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(620.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stateValues.toList()) { (label, states) ->
            TextStatusContainer(textLabel = label, textValue = states.value)
        }
    }
}


@Composable
fun TextStatusContainer(textLabel: String, textValue: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = textLabel,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(" : ",fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = textValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        fontSize = 17.sp,
                        fontFamily = FontFamily.SansSerif,
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}