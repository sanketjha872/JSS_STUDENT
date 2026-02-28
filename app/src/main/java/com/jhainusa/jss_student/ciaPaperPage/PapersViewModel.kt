package com.jhainusa.jss_student.ciaPaperPage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.jhainusa.jss_student.PdfDownloaderAndOpener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PapersViewModel : ViewModel() {

    private val _years = MutableStateFlow<List<String>>(emptyList())
    val years : StateFlow<List<String>> = _years

    fun loadyears(){
        Firebase.firestore
            .collection("papers")
            .get()
            .addOnSuccessListener { snapshots ->
                val yearIds = snapshots.documents.map { it.id }
                _years.value = yearIds
            }
            .addOnFailureListener {
                Log.e("YearsViewModel","Failed to load papers", it)
            }
    }

    private val _semesters = MutableStateFlow<List<String>?>(null)
    val semester : StateFlow<List<String>?> = _semesters

    fun loadsemesters(yearId : String){
        Firebase.firestore
            .collection("papers")
            .document(yearId)
            .collection("SessionPapers")
            .get()
            .addOnSuccessListener { snapshots ->
                val semIds = snapshots.documents.map { it.id }
                _semesters.value = semIds
            }
            .addOnFailureListener {
                Log.e("YearsViewModel","Failed to load papers", it)
            }
    }
    private val _papers = MutableStateFlow<List<String>?>(null)
    val papers : StateFlow<List<String>?> = _papers
    fun loadPapers(yearId: String, semesterId: String) {
        Firebase.firestore
            .collection("papers")
            .document(yearId)
            .collection("SessionPapers")
            .document(semesterId)
            .collection("EVS Papers")
            .get()
            .addOnSuccessListener { snapshots ->
                val paperIds = snapshots.documents.map { it.id }
                _papers.value = paperIds
            }
    }

   val _pdfs = MutableStateFlow<String>("")
    val pdf_Url : StateFlow<String> = _pdfs
    fun loadpdfs(yearId: String, semesterId: String,papertype : String) {
       Firebase.firestore
            .collection("papers")
            .document(yearId)
            .collection("SessionPapers")
            .document(semesterId)
            .collection("EVS Papers")
            .document(papertype)
            .get()
            .addOnSuccessListener { snapshot ->
                val pdfUrl = snapshot.getString("pdfUrl")
                if(pdfUrl!=null){
                    _pdfs.value = pdfUrl
                }
            }
   }

}