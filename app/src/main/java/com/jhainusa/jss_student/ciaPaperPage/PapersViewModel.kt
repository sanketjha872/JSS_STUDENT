package com.jhainusa.jss_student.ciaPaperPage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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
                Log.e("PapersViewModel","Failed to load years", it)
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
                Log.e("PapersViewModel","Failed to load semesters for $yearId", it)
            }
    }
    private val _papers = MutableStateFlow<List<String>?>(null)
    val papers : StateFlow<List<String>?> = _papers
    fun loadPapers(yearId: String, semesterId: String) {
        val collectionName = "$semesterId Papers"
        Firebase.firestore
            .collection("papers")
            .document(yearId)
            .collection("SessionPapers")
            .document(semesterId)
            .collection(collectionName)
            .get()
            .addOnSuccessListener { snapshots ->
                val paperIds = snapshots.documents.map { it.id }
                _papers.value = paperIds
            }
            .addOnFailureListener {
                Log.e("PapersViewModel","Failed to load papers for $yearId / $semesterId", it)
            }
    }

    private val _pdfs = MutableStateFlow<String>("")
    val pdf_Url : StateFlow<String> = _pdfs

    fun loadpdfs(yearId: String, semesterId: String, papertype : String) {
        _pdfs.value = "" // Reset to show loading
        val collectionName = "$semesterId Papers"
        Firebase.firestore
            .collection("papers")
            .document(yearId)
            .collection("SessionPapers")
            .document(semesterId)
            .collection(collectionName)
            .document(papertype)
            .get()
            .addOnSuccessListener { snapshot ->
                val pdfUrl = snapshot.getString("pdfUrl")
                Log.d("PapersViewModel", "Loaded pdfUrl: $pdfUrl for $papertype")
                if(pdfUrl != null){
                    _pdfs.value = pdfUrl
                } else {
                    Log.e("PapersViewModel", "pdfUrl field is missing in document $papertype")
                }
            }
            .addOnFailureListener {
                Log.e("PapersViewModel", "Failed to load PDF for $papertype", it)
            }
   }
}
