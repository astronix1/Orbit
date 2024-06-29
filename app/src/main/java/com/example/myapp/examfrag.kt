package com.example.myapp

import android.app.DatePickerDialog
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
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class examfrag : Fragment() {
    val monthsList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private lateinit var binding: FragmentExamfragBinding
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExamfragBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner_list: List<String> = listOf("JEE Main", "JEE Adv","NEET")
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,spinner_list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinkro.adapter = arrayAdapter
        binding.datep.setOnClickListener{
            openD()
        }

        binding.spinkro.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var x = parent?.getItemAtPosition(position).toString()
                binding.examtxt.text = x
                if (position==0 || position==2){
                    binding.imageView13.setImageResource(R.drawable.ntta)
                }
                else{
                    binding.imageView13.setImageResource(R.drawable.adv)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openD() {
        val datepickD = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{DatePicker,year, month, day ->
//            binding..text = "$year.${month+1}.$day"
            binding.textView17.text=(" $year")
            binding.textView16.text=("$day "+monthsList[month]+" $year")
            val dl =(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.of(year,month+1, day)))
            binding.textView65.text=dl.toString()

        },LocalDate.now().year,LocalDate.now().monthValue - 1,LocalDate.now().dayOfMonth)
        datepickD.show()
    }


}
