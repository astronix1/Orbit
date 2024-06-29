package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
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
        val usrid = auth.currentUser?.uid
        usrid?.let {
            uid->
            db.reference.child("users").child(uid).get().addOnSuccessListener {
                snp->
                if (snp.exists()){
                    val user = snp.getValue(User::class.java)
                    user?.let{
                        val name:String= user.name
                        binding.nametxt.text=name

                    }
                }

            }.addOnFailureListener{


            }
        }
        val bar = activity?.findViewById<ChipNavigationBar>(R.id.menubar)
        binding.profileIcon.setOnClickListener {
            bar?.setItemSelected(R.id.item2)
            (activity as? MainActivity )?.repl(profile())
        }
    }
}