package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.transition.Visibility.Mode
import com.example.myapp.databinding.FragmentHomefragBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class homefrag : Fragment() {
    data class User(val name: String = "", val username: String = "")
    private lateinit var binding:FragmentHomefragBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance(Constants.dburl)
        binding = FragmentHomefragBinding.inflate(inflater, container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.plannerCard.setOnClickListener{
            startActivity(Intent(requireActivity(), planner::class.java))
        }
        binding.mentorshipCard.setOnClickListener{
            startActivity(Intent(requireActivity(),ChatActivity::class.java ))
        }
        val srf = requireActivity().getSharedPreferences("data_orbit", Context.MODE_PRIVATE)
        val editor = srf.edit()
        val name = srf.getString("name",null)
        if (name == null) {
            val usrid = auth.currentUser?.uid
            usrid?.let { uid ->
                db.reference.child("users").child(uid).get().addOnSuccessListener { snp ->
                    if (snp.exists()) {
                        val user = snp.getValue(User::class.java)
                        user?.let {
                            var name: String = user.name
                            binding.nametxt.text = name
                            editor.putString("name", name)
                            editor.apply()
                        }
                    }

                }.addOnFailureListener {


                }


            }
        }
        else {
            binding.nametxt.text = name
        }
        val bar = activity?.findViewById<ChipNavigationBar>(R.id.menubar)
        binding.profileIcon.setOnClickListener {
            bar?.setItemSelected(R.id.item2)
            (activity as? MainActivity )?.repl(profile())
        }
    }
}