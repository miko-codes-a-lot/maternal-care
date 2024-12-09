package org.maternalcare.shared.ext

import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun UserCheckupDto.assessBloodPressure(): String = when {
    bloodPressure < 120 -> "Normal"
    bloodPressure in 120.0..129.9 -> "Elevated"
    bloodPressure >= 130 -> "High"
    else -> "Unknown"
}

fun UserCheckupDto.calculateBMI(): Double {
    val heightInMeters = height / 100.0
    return if (heightInMeters > 0) weight / (heightInMeters * heightInMeters) else 0.0
}

fun UserCheckupDto.determineBMICategory(): String {
    val bmi = calculateBMI()

    return when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9  -> "Normal"
        bmi in 25.0..29.9 -> "Overweight"
        else -> "Obesity"
    }
}

fun UserCheckupDto.calculateAgeOfGestation(): Long {
    val lmpDate = lastMenstrualPeriod.toInstant()!!.atZone(ZoneId.systemDefault()).toLocalDate()
    val currentDate = LocalDate.now()
    val daysBetween = ChronoUnit.DAYS.between(lmpDate, currentDate)
    return if (daysBetween >= 0) (daysBetween / 7) else 0L
}

fun UserCheckupDto.expectedDueDate(): String {
    val lmpDate = lastMenstrualPeriod.toInstant()!!.atZone(ZoneId.systemDefault()).toLocalDate()
    val edd = lmpDate.plusDays(280)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return edd.format(formatter)
}