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
        val startDate: DateTime = ISO_FORMATTER.parseDateTime(start)
        val endDate: DateTime = ISO_FORMATTER.parseDateTime(end)

        when {
            startDate.isBeforeNow && endDate.isAfterNow -> "до ${parseForPrint(endDate)}"
            checkIfTodayDate(endDate) -> return "Сегодня"
            endDate.isBeforeNow -> return "Завершено"
            startDate.isAfterNow -> return parseForPrint(startDate)
            else -> ""
        }
            .also {
                return it
            }
    }

    //19
    fun printIsoPeriod(period: String) =
        period.run {
            when (period.length) {
                39 -> {
                    toMutableList()
                        .let {
                            "${dateNoYear(
                                String(it.subList(0, 18).toCharArray())
                            )} - ${dateNoYear(
                                String(it.subList(20, 39).toCharArray())
                            )}"
                        }
                }
                else -> ""
            }
        }


    fun simpleDate(str: String, withTime: Boolean = true): String =
        parseForPrint(
            ISO_FORMATTER.parseDateTime(str),
            withTime
        )

    fun dateNoYear(str: String): String =
        parseForPrint(
            ISO_FORMATTER.parseDateTime(str),
            printTime = false
        ).let {
            checkForYear(it)
        }

    fun dateToIso(d: DateTime): String =
        d.toString("yyyy-MM-dd'T'HH:mm:ss")


    fun getSingleDate(inputIsoDate: String): String {
        val dateTime: DateTime = ISO_FORMATTER.parseDateTime(inputIsoDate)
        if (checkIfTodayDate(dateTime)) {
            return "Сегодня"
        } else if (dateTime.isBeforeNow)
            return "Завершено"

        return parseForPrint(dateTime)
    }

    fun parseForPrint(
        dateTime: DateTime,
        printTime: Boolean = true
    ) =
        (when {
            dateTime.millisOfDay().get() == 0 || !printTime -> NO_TIME
            else -> WITH_TIME
        })
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

    fun checkForYear(it: String) =
        if (it.contains("$CURRENT_YEAR")) it.removeSuffix("$CURRENT_YEAR") else it


    const val NO_TIME = "L-"
    const val WITH_TIME = "LS"
    val CURRENT_YEAR = DateTime.now().year

    val ISO_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
}