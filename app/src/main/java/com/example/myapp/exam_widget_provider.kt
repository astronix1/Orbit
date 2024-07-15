import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.myapp.Constants
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ExamWidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetIds)
        }
    }

    companion object {
        private val monthsList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        @RequiresApi(Build.VERSION_CODES.O)
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

            val auth = FirebaseAuth.getInstance()
            val db = FirebaseDatabase.getInstance(Constants.dburl)
            val srf = context.getSharedPreferences("examData", Context.MODE_PRIVATE)
            val remoteViews = RemoteViews(context.packageName, R.layout.exam_widget)
            val i = srf.getInt("i", -1)
            val name_of_exam = srf.getString("name_of_exam", "")
            val d = srf.getInt("d", -1)
            val m = srf.getInt("m", -1)
            val y = srf.getInt("y", -1)
            remoteViews.setTextViewText(R.id.textView17, " " + y.toString())
            val mm = m.toString()
            val yy = y.toString()
            val dd = d.toString()
            val dl = ChronoUnit.DAYS.between(
                LocalDate.now(),
                LocalDate.of(yy.toInt(), mm.toInt() + 1, dd.toInt())
            )
            remoteViews.setTextViewText(R.id.textView65, dl.toString())
            if ((i).toString() == "0" || (i).toString() == "2") {
                remoteViews.setImageViewResource(R.id.imageView13, R.drawable.ntta)
            } else {
                remoteViews.setImageViewResource(R.id.imageView13, R.drawable.adv)
            }
            remoteViews.setTextViewText(R.id.examtxt,name_of_exam.toString())

            remoteViews.setTextViewText(R.id.textView16, "${d} ${monthsList[m]} ${y}")



            for (appWidgetId in appWidgetIds) {
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}
