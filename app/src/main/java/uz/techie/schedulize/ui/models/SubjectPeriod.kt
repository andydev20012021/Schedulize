package uz.techie.schedulize.ui.models

import android.util.Log
import java.lang.IllegalArgumentException
import java.sql.Time

class SubjectPeriod(val start: Time, val end: Time) {

    companion object {
        private val TAG = SubjectPeriod::class.java.canonicalName
        fun valueOf(period: String): SubjectPeriod {
            var start = Time.valueOf(period.substringBefore("-") + ":00")

            var end = try {
                if (!period.contains("-")) throw IllegalArgumentException("cant find '-'")
                Time.valueOf(period.substringAfter("-") + ":00")
            } catch (e: IllegalArgumentException) {
                Time(0, 0, 0)
            }

            return SubjectPeriod(start, end)
        }
    }

    fun start(): String {
        return start.toString().substringBeforeLast("-").substringBeforeLast(":")
    }

    fun end(): String {
        return end.toString().substringAfterLast("-").substringBeforeLast(":")
    }

    override fun toString(): String {
        val time = StringBuilder()
        time.append(start())

        if (end() != "00:00") {
            time.append("-")
            time.append(end())
        }

        return time.toString()
    }
}