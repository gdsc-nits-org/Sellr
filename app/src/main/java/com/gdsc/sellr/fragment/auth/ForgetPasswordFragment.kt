package com.gdsc.sellr.fragment.auth

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gdsc.sellr.R
import com.google.firebase.auth.FirebaseAuth


class ForgetPasswordFragment : Fragment() {

    private lateinit var email:EditText
    private lateinit var button: Button
  private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_forgotpass,container,false)


        auth = FirebaseAuth.getInstance()
        email = view.findViewById(R.id.edittextforgotemail)
        button= view.findViewById(R.id.submit)

        button.setOnClickListener {
            val pd = ProgressDialog(context);
            pd.setMessage("Sit back and relax, we are processing")

            val emailtxt = email.text.toString().trim()
            if(emailtxt.isBlank() || !emailtxt.contains("nits"))
                email.setError("Please Enter Valid Email")
            else {
                pd.show()
                auth.sendPasswordResetEmail(emailtxt)
                Toast.makeText(
                    requireContext(),
                    "Password reset email link sent",
                    Toast.LENGTH_SHORT
                ).show()
                pd.hide()
                fragmentload(LoginFragment())
                //startActivity(Intent(requireContext(), AuthActivity::class.java))
            }

        }
        return view

    }
    private fun fragmentload(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }


}