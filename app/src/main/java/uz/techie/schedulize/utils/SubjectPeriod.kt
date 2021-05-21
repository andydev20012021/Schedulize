package uz.techie.schedulize.utils

import android.util.Log
import java.lang.IllegalArgumentException
import java.sql.Time

class SubjectPeriod(val start:Time, val end:Time?){

    companion object{
        private val TAG = SubjectPeriod::class.java.canonicalName
        fun valueOf(period:String):SubjectPeriod {
            var start = Time.valueOf(period.substringBefore("/"))
            var end:Time? = null

            try {
                end = Time.valueOf(period.substringAfter("/"))
            }catch (e:IllegalArgumentException){
                Log.d(TAG,period,e)
            }
             return SubjectPeriod(start!!,end)
        }
    }


    override fun toString(): String {
        val time = StringBuilder()
        time.append(start.toString().substringBeforeLast(":"))
        if (end != null){
            time.append("-")
            time.append(end.toString().substringBeforeLast(":"))
        }

        return time.toString()
    }
}