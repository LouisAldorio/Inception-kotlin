package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.auth0.android.jwt.JWT
import com.example.inception.R
import com.example.inception.RegisterMutation
import com.example.inception.activity.LandingPage
import com.example.inception.activity.hideKeyboard
import com.example.inception.api.apolloClient
import com.example.inception.constant.*
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


class register : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = User.getToken(requireContext())
        if (token != null){
            var LandingPageIntent = Intent(activity,LandingPage::class.java)
            startActivity(LandingPageIntent)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = activity?.let { ArrayAdapter.createFromResource(it,R.array.spinner_role,android.R.layout.simple_spinner_dropdown_item) }
        role_spinner.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var objectView = inflater.inflate(R.layout.fragment_register, container, false)
        return objectView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState != null) {
            view.username.setText(savedInstanceState.getString(USERNAME))
            view.password.setText(savedInstanceState.getString(PASSWORD))
            view.confirm_password.setText(savedInstanceState.getString(CONFIRM_PASSWORD))
            view.whatsapp.setText(savedInstanceState.getString(WHATSAPP))
            view.email.setText(savedInstanceState.getString(EMAIL))
        }

        view.findViewById<RelativeLayout>(R.id.register).setOnClickListener {
            it.hideKeyboard()

            //collect all data and mutate
            val username = view.username.text.toString()
            if(username.trim() == ""){
                ToastInvalidInput("Username must not be empty")
                return@setOnClickListener
            }

            val email = view.email.text.toString()
            if(email.trim() == ""){
                ToastInvalidInput("Email must not be empty!")
                return@setOnClickListener
            }else{
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    ToastInvalidInput("Email Seems to be Invalid!")
                    return@setOnClickListener
                }
            }

            val whatsapp_number = view.whatsapp.text.toString()
            if(whatsapp_number.trim() == ""){
                ToastInvalidInput("Whatsapp Number must not be empty!")
                return@setOnClickListener
            }

            val password = view.password.text.toString()
            if(password.trim() == ""){
                ToastInvalidInput("Password must not be empty!")
                return@setOnClickListener
            }

            val confirm_password = view.confirm_password.text.toString()
            if(confirm_password.trim() == ""){
                ToastInvalidInput("Confirm Password must not be Empty!")
                return@setOnClickListener
            }else {
                if(confirm_password != password) {
                    ToastInvalidInput("Password does not match!")
                    return@setOnClickListener
                }
            }

            val role = view.role_spinner.selectedItem.toString()

            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            it.visibility = View.GONE

            register(username,email,whatsapp_number,password,confirm_password,role)
        }
    }

    fun ToastInvalidInput(text: String){

        var toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun register(username: String, email: String, whatsapp_number : String, password : String, confirm_password: String, role: String){
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient(requireContext()).mutate(RegisterMutation(
                    username = username,
                    role = role,
                    password = password,
                    confirm_password = confirm_password,
                    email = email,
                    wa_number = whatsapp_number
                )).await()
            } catch (e: ApolloException) {
                Log.i("error",e.toString())
                null
            }

            val registerResponse = response?.data?.user?.register
            if (registerResponse == null || response.hasErrors()) {
                view?.progressBar?.visibility = View.GONE
                view?.register?.visibility = View.VISIBLE

                ToastInvalidInput(response?.errors?.get(0)?.message.toString())
                return@launchWhenResumed
            }


            val access_token = registerResponse?.access_token
            User.setToken(requireContext(),access_token)
            User.setUsername(requireContext(),registerResponse?.user?.username)

            var LandingPageIntent = Intent(activity,LandingPage::class.java)
            startActivity(LandingPageIntent)

            view?.progressBar?.visibility = View.GONE
            view?.register?.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERNAME,view?.username?.text.toString())
        outState.putString(PASSWORD,view?.password?.text.toString())
        outState.putString(CONFIRM_PASSWORD,view?.confirm_password?.text.toString())
        outState.putString(WHATSAPP,view?.whatsapp?.text.toString())
        outState.putString(EMAIL,view?.email?.text.toString())
    }

}