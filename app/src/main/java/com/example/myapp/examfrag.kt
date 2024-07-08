package com.example.myapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myapp.databinding.FragmentExamfragBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class examfrag : Fragment() {

    var nnn:Int = -1
    var nmnm:String =""
    val monthsList =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private lateinit var binding: FragmentExamfragBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var srf: SharedPreferences
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance(Constants.dburl)
        srf = requireContext().getSharedPreferences("examData", Context.MODE_PRIVATE)
        binding = FragmentExamfragBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner_list: List<String> = listOf("JEE Main", "JEE Adv", "NEET")
        val arrayAdapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, spinner_list) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                view.typeface = resources.getFont(R.font.raleway_semibold)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.typeface = resources.getFont(R.font.raleway_semibold)
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                return view
            }
        }

        binding.spinkro.adapter = arrayAdapter


        binding.datep.setOnClickListener {
            Log.d("patt","opend ke uppr")
            openD()
        }
        val x = srf.getInt("y", -1)

        if (x==-1){
            val usrid = auth.currentUser?.uid
            if (usrid!=null){
                Log.d("patt","user not null")
                db.reference.child("users").child(usrid).get().addOnSuccessListener { snp->
                    if (snp.exists()){
                        Log.d("patt","snap exists")
                        val d = snp.child("d").value
                        val m = snp.child("m").value
                        val y = snp.child("y").value
                        val i: Any? = snp.child("i").value
                        val name_of_exam = snp.child("name_of_exam").value
                        Log.d("patt", "data on server : $d $m $y  $i $name_of_exam")
                        if (d!=null && y!=null && m!=null && i!=null && name_of_exam!=null){
                            Log.d("patt","data on server not null")


                            binding.textView17.text = " " + y.toString()
                            val mm = m.toString()
                            val yy = y.toString()
                            val dd = d.toString()
                            val ii = i.toString()
                            val dl = ChronoUnit.DAYS.between(
                                LocalDate.now(),
                                LocalDate.of(yy.toInt(), mm.toInt() + 1, dd.toInt())
                            )
                            binding.textView65.text = dl.toString()

                            Log.d("patt","mm $mm")
                            if (mm!=null){
                                binding.textView16.text = "${d} ${monthsList[(mm.toInt())]} ${y}"
                            }
                            if ((i).toString() == "0" || (i).toString() == "2") {
                                binding.imageView13.setImageResource(R.drawable.ntta)
                            } else {
                                binding.imageView13.setImageResource(R.drawable.adv)
                            }
                            binding.examtxt.text =name_of_exam.toString()
                            if (ii!=null){
                                binding.spinkro.setSelection((ii.toInt()))
                            }
                            val editor = srf.edit()
                            editor.putInt("d", dd.toInt())
                            editor.putInt("m", mm.toInt())
                            editor.putInt("y", yy.toInt())
                            editor.putInt("i", ii.toInt())
                            editor.putString("name_of_exam", name_of_exam.toString())
                            editor.apply()
                        }
                    }
                }
            }
        }
        else {
            val i = srf.getInt("i", -1)
            val name_of_exam = srf.getString("name_of_exam", "")
            val d = srf.getInt("d", -1)
            val m = srf.getInt("m", -1)
            val y = srf.getInt("y", -1)
            binding.spinkro.setSelection(i)
            binding.textView17.text = " " + y.toString()
            val mm = m.toString()
            val yy = y.toString()
            val dd = d.toString()
            val dl = ChronoUnit.DAYS.between(
                LocalDate.now(),
                LocalDate.of(yy.toInt(), mm.toInt() + 1, dd.toInt())
            )
            binding.textView65.text = dl.toString()
            if ((i).toString() == "0" || (i).toString() == "2") {
                binding.imageView13.setImageResource(R.drawable.ntta)
            } else {
                binding.imageView13.setImageResource(R.drawable.adv)
            }
            binding.examtxt.text =name_of_exam.toString()
            binding.textView16.text = "${d} ${monthsList[m]} ${y}"


        }

        binding.spinkro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                nmnm = parent?.getItemAtPosition(position).toString()
                nnn= position
                Log.d("patt","item selected listener chl gya")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
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
                        mapOf("d" to day, "m" to month, "y" to year, "i" to nnn, "name_of_exam" to nmnm)
                    )

                }
                val editor = srf.edit()
                editor.putInt("d", day)
                editor.putInt("m", month)
                editor.putInt("y", year)
                editor.putInt("i", nnn)
                editor.putString("name_of_exam", nmnm)
                editor.apply()
                binding.textView65.text = dl.toString()
                binding.textView17.text = " " + year.toString()
                binding.textView16.text = "${day} ${monthsList[month]} ${year}"
                if (nnn == 0 || nnn == 2) {
                    binding.imageView13.setImageResource(R.drawable.ntta)
                } else {
                    binding.imageView13.setImageResource(R.drawable.adv)
                }
                binding.examtxt.text =nmnm.toString()
                binding.spinkro.setSelection((nnn.toInt()))
            },
            LocalDate.now().year,
            LocalDate.now().monthValue - 1,
            LocalDate.now().dayOfMonth
        )
        datepickD.show()
    }
}
