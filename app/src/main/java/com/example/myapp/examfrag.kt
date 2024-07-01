package com.example.myapp

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.example.myapp.databinding.FragmentExamfragBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class examfrag : Fragment() {
    data class exdate(val d: Int?, val m: Int?, val y: Int?, val dl: Long?)
    data class examname(val i: Int?, val name_of_exam: String?)
    data class exn(val i: String?, val name_of_exam: String?)
    data class exd(val d: String?, val m: String?, val y: String?, val dl: String?)

    val monthsList =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private lateinit var binding: FragmentExamfragBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance(Constants.dburl)
//        db.setPersistenceEnabled(true)
        binding = FragmentExamfragBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner_list: List<String> = listOf("JEE Main", "JEE Adv", "NEET")
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinner_list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinkro.adapter = arrayAdapter

        binding.datep.setOnClickListener {
            Log.d("patt","opend ke uppr")
            openD()
        }
        val usrid = auth.currentUser?.uid
        if (usrid!=null){
            Log.d("patt","user not null")
            db.reference.child("users").child(usrid).get().addOnSuccessListener { snp->
                if (snp.exists()){
                    Log.d("patt","snap exists")
                    val userData = snp.value as? Map<String, Any>
                    userData?.let {
                        val examName = exn(
                            i = userData["i"] as? String?,
                            name_of_exam = userData["name_of_exam"] as? String ?

                        )

                        val examDate = exd(
                            d = userData["d"] as? String ?,
                            m = userData["m"] as? String ?,
                            y = userData["y"] as? String ?,
                            dl = userData["dl"] as? String ?
                        )
                        Log.d("patt","data succesfully fetched ${examDate.y}  ${examDate.d} ${examDate.m} ")
                        if (examDate.d!=null && examDate.y!=null && examDate.m!=null && examDate.dl!=null && examName.i!=null && examName.name_of_exam!=null){
                            Log.d("patt","data on server not null")
                            binding.textView65.text = examDate.dl.toString()
                            binding.textView17.text = examDate.y.toString()
                            binding.textView16.text = "${examDate.d} ${monthsList[(examDate.m).toInt()]} ${examDate.y}"
                            if ((examName.i).toInt() == 0 || (examName.i).toInt() == 2) {
                                binding.imageView13.setImageResource(R.drawable.ntta)
                            } else {
                                binding.imageView13.setImageResource(R.drawable.adv)
                            }
                            binding.examtxt.text = examName.name_of_exam
                            binding.spinkro.setSelection((examName.i).toInt())
                        }


                    }
                }
            }
        }
//        binding.spinkro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val nm = parent?.getItemAtPosition(position).toString()
//                binding.examtxt.text = nm
//                Log.d("patt","item selected listener chl gya")
//                val usrid = auth.currentUser?.uid
//                usrid?.let { uid ->
//                    db.reference.child("users").child(uid).updateChildren(
//                        mapOf("i" to position, "name_of_exam" to nm)
//                    )
//
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Handle case when nothing is selected
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openD() {
        val datepickD = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                val dl = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    LocalDate.of(year, month + 1, day)
                )

                val usrid = auth.currentUser?.uid
                usrid?.let { uid ->
                    db.reference.child("users").child(uid).updateChildren(
                        mapOf("d" to day, "m" to month, "y" to year, "dl" to dl)
                    )

                }
            },
            LocalDate.now().year,
            LocalDate.now().monthValue - 1,
            LocalDate.now().dayOfMonth
        )
        datepickD.show()
    }
}
