package com.example.myapp

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myapp.databinding.FragmentExamfragBinding
import com.example.myapp.databinding.FragmentHomefragBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class examfrag : Fragment() {
    var Y = -1
    var M = -1
    var DAY = -1
    var x:String=""
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    val monthsList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    data class exam(val i:Int, val d:Int, val m:Int, val y:Int)

    private lateinit var binding: FragmentExamfragBinding
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance(Constants.dburl)
        binding = FragmentExamfragBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner_list: List<String> = listOf("JEE Main", "JEE Adv","NEET")
        var n = -1
        binding.spinkro.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                n = position
                x = parent?.getItemAtPosition(position).toString()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,spinner_list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinkro.adapter = arrayAdapter
        binding.datep.setOnClickListener{
            openD()
        }
        val srf = requireActivity().getSharedPreferences("examdata", Context.MODE_PRIVATE)
        val editor = srf.edit()
        editor.putInt("d",0)
        editor.putInt("m",-1)
        editor.putInt("y",0)
        editor.putInt("i",-1)
        editor.apply()
        var selectedint = srf.getInt("i", -1)
        var dd = srf.getInt("d", 0)
        var mm = srf.getInt("m", -1)
        var yy = srf.getInt("y", 0)
        if (yy == 0 || mm == -1 || dd == 0 || selectedint == -1){
            val usrid = auth.currentUser?.uid
            usrid?.let { uid->
                db.reference.child("users").child(uid).get().addOnSuccessListener { snp->
                    if(snp.exists()){
                        val D = snp.getValue(exam::class.java)
                        D?.let {
                            dd=D.d
                            mm=D.m
                            yy=D.y
                            selectedint=D.i

                        }
                    }
                }.addOnFailureListener{
                    selectedint = n
                    dd = DAY
                    mm = M
                    yy = Y
                    editor.putInt("d",dd)
                    editor.putInt("m",mm)
                    editor.putInt("y",yy)
                    editor.putInt("i",selectedint)
                    editor.apply()

                }
            }
            dowork(selectedint,dd,mm,yy)
        }
        else{
            dowork(selectedint,dd,mm,yy)
        }





    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dowork(selectedint: Int, dd: Int, mm: Int, yy: Int) {
            if (dd!=-1 && mm!=-1 && yy!=-1){
                binding.textView17.text=(" $yy")
                binding.textView16.text=("$dd "+monthsList[mm]+" $yy")
                val dl =(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.of(yy,mm+1, dd)))
                binding.textView65.text=dl.toString()
            }
            binding.spinkro.setSelection(selectedint)
            binding.examtxt.text = x
                if (selectedint==0 || selectedint==2){
                    binding.imageView13.setImageResource(R.drawable.ntta)
                }
                else{
                    binding.imageView13.setImageResource(R.drawable.adv)
                }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openD() {
        val datepickD = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{DatePicker,year, month, day ->
            Y = year
            M = month
            DAY = day




        },LocalDate.now().year,LocalDate.now().monthValue - 1,LocalDate.now().dayOfMonth)
        datepickD.show()
    }


}
