//package uz.techie.schedulize.ui.services
//
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import android.view.View
//import android.widget.RemoteViews
//import android.widget.RemoteViewsService
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.collect
//import uz.techie.schedulize.R
//import uz.techie.schedulize.repositories.SubjectRepository
//import java.util.*
//import javax.inject.Inject
//import javax.security.auth.Subject
//
//@AndroidEntryPoint
//class SchedulizeWidgetService : RemoteViewsService() {
//    private val TAG = SchedulizeWidgetService::class.java.canonicalName
//    @Inject
//    lateinit var repository: SubjectRepository
//
//    private val subjects = mutableListOf<Subject>()
//    private var day: Int = 6
//
//    override fun onCreate() {
//        super.onCreate()
//        val toDay = Date(System.currentTimeMillis())
//        val calendar = Calendar.getInstance()
//        calendar.time = toDay
//        day = when (calendar.get(Calendar.DAY_OF_WEEK)) {
//            1 -> 6
//            2 -> 0
//            3 -> 1
//            4 -> 2
//            5 -> 3
//            6 -> 4
//            7 -> 5
//            else -> 6
//        }
//        setData()
//    }
//
//    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
//        return SchedulizeRemoteViewFactory(applicationContext, intent, subjects)
//    }
//    private fun setData() {
//        CoroutineScope(Dispatchers.Main).launch {
//            repository.getAllSubjects().collect {
//                subjects.addAll(it)
//                Log.d(TAG, "repository list size = ${it.size}")
//                Log.d(TAG, " setData subjects size = ${subjects.size}")
//            }
//        }
//        Log.d(TAG, " setData subjects size = ${subjects.size}")
//
//    }
//}
//
//class SchedulizeRemoteViewFactory(
//    private val context: Context,
//    intent: Intent?,
//    private val items: MutableList<Subject>
//) : RemoteViewsService.RemoteViewsFactory {
//    private val TAG = SchedulizeRemoteViewFactory::class.java.canonicalName
//
//    private val subjects = mutableListOf<Subject>()
//    override fun onCreate() {
//        subjects.addAll(items)
//
//    }
//
//    override fun onDataSetChanged() {
//        Log.d(TAG,"onDataSetChanged")
//    }
//
//    override fun onDestroy() {
//        Log.d(TAG,"onDestroy")
//        subjects.clear()
//    }
//
//    override fun getCount(): Int {
//        Log.d(TAG, "getCount() = ${subjects.size}")
//        return subjects.size
//    }
//
//    override fun getViewAt(position: Int): RemoteViews {
//        Log.d(TAG, "getItemAt($position)")
//        val remoteView = RemoteViews(context.packageName, R.layout.item_card).apply {
//            val subject = subjects[position]
//            setTextViewText(R.id.subject_name, subject.subjectName)
//            setTextViewText(R.id.subject_period, subject.subjectPeriod.toString())
//
//            setInt(R.id.rootLayout, "setBackgroundResource", subject.color)
//            if (subject.subjectTeacher.isNullOrEmpty()) {
//                setViewVisibility(R.id.subject_teacher, View.INVISIBLE)
//            } else {
//                setViewVisibility(R.id.subject_teacher, View.VISIBLE)
//                setTextViewText(R.id.subject_teacher, subject.subjectTeacher)
//            }
//
//            if (subject.subjectPlace.isNullOrEmpty()) {
//                setViewVisibility(R.id.subject_place, View.INVISIBLE)
//            } else {
//                setViewVisibility(R.id.subject_place, View.VISIBLE)
//                setTextViewText(R.id.subject_place, subject.subjectPlace)
//            }
//            Log.d(TAG, "subject: $subject")
//        }
//
//        return remoteView
//    }
//
//    override fun getLoadingView(): RemoteViews? {
//        Log.d(TAG, "getLoadingView")
//        return null
//    }
//
//    override fun getViewTypeCount(): Int {
//        return 1
//    }
//
//    override fun getItemId(position: Int): Long {
//        Log.d(TAG, "getItemId")
//        return position.toLong()
//    }
//
//    override fun hasStableIds(): Boolean {
//        return true
//    }
//
//}