package uz.techie.schedulize.ui.screens.setting

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uz.techie.schedulize.db.models.DBSubjectModel
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.utils.utilModel.Subject
import uz.techie.schedulize.utils.utilModel.mapToDBSubjectModel
import uz.techie.schedulize.utils.utilModel.mapToUtilSubjectModel
import java.io.*
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private @ApplicationContext val context: Context,
    private val repository: SubjectRepository
) : ViewModel() {
    private val TAG = "SettingViewModel"
    private val contentResolver = context.contentResolver

    private val _progressData = MutableStateFlow<FileResult>(FileResult.IDLE)
    val processData = _progressData.asStateFlow()

    fun exportData(uri: Uri?) {
        tryToOpenFile {
            if (uri == null) throw FileNotFoundException("file not founded")
            viewModelScope.launch(Dispatchers.IO) {
                _progressData.value = FileResult.Loading
                repository.getAllSubjects().collect { it ->
                    val subjects = it.map { it.mapToUtilSubjectModel() }
                    val jsonString = Json.encodeToString(subjects)
                    writeTextToUri(uri, jsonString)

                    Firebase.analytics.logEvent("export") {
                        param("item_count", subjects.size.toLong())
                    }
                    _progressData.value = FileResult.Success
                }
            }
        }
    }

    fun importData(uri: Uri?) {
        tryToOpenFile {
            _progressData.value = FileResult.Loading
            if (uri == null) throw FileNotFoundException("file not founded")
            viewModelScope.launch(Dispatchers.IO) {
                val str = readTextFromUri(uri)
                val subjects =
                    Json.decodeFromString<List<Subject>>(str).map { it.mapToDBSubjectModel() }
                repository.insertAllSubjects(subjects)

                Firebase.analytics.logEvent("import") {
                    param("item_count", subjects.size.toLong())
                }
                _progressData.value = FileResult.Success
            }
        }
    }

    @Throws(IOException::class)
    private fun writeTextToUri(uri: Uri, text: String) {
        contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use {
                it.write(text.toByteArray())
            }
        }
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun tryToOpenFile(body: () -> Unit) {
        try {
            body()
        } catch (e: FileNotFoundException) {
            _progressData.value = FileResult.Error(e.message)
        } catch (e: IOException) {
            _progressData.value = FileResult.Error(e.message)
        } catch (e: Exception) {
            _progressData.value = FileResult.Error(e.message)
        }
    }

    sealed class FileResult {
        object IDLE : FileResult()
        data class Error(val msg: String?) : FileResult()
        object Success : FileResult()
        object Loading : FileResult()
    }
}