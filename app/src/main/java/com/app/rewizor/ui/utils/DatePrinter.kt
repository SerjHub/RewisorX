package com.app.rewizor.ui.utils

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*

object DatePrinter {


    fun getDateForAdapter(start: String?, end: String?): String =
        when {
            start != null && end != null -> getForPeriod(start, end)
            start != null -> getSingleDate(start)
            end != null -> getSingleDate(end)
            else -> ""
        }

    fun getForPeriod(start: String, end: String): String {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
        val startDate: DateTime = formatter.parseDateTime(start)
        val endDate: DateTime = formatter.parseDateTime(end)

        when {
            startDate.isBeforeNow && endDate.isAfterNow -> parseForPrint(endDate)
            checkIfTodayDate(endDate) -> return "Сегодня"
            endDate.isBeforeNow -> return "Закончено"
            startDate.isAfterNow -> return parseForPrint(startDate)
            else -> ""
        }
            .also {
                return it
            }

    }


    fun getSingleDate(inputIsoDate: String): String {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime: DateTime = formatter.parseDateTime(inputIsoDate)
        if (checkIfTodayDate(dateTime)) {
            return "Сегодня"
        } else if (dateTime.isBeforeNow)
            return "Завершено"


        return parseForPrint(dateTime)
    }

    fun parseForPrint(dateTime: DateTime) =
        (if (dateTime.millisOfDay().get() == 0) NO_TIME
        else WITH_TIME)
            .let {
                DateTimeFormat
                    .forStyle(it)
                    .withLocale(Locale("ru"))
            }
            .let {
                val dateTxt = it.print(dateTime)
                dateTxt.replace(" г.", "")
            }


    fun checkIfTodayDate(dateTime: DateTime) = (dateTime.toLocalDate() == LocalDate())


    const val NO_TIME = "L-"
    const val WITH_TIME = "LS"
}