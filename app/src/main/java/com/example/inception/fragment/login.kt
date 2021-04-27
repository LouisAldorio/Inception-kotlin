package com.example.inception.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.LoginMutation
import com.example.inception.R
import com.example.inception.activity.LandingPage
import com.example.inception.activity.hideKeyboard
import com.example.inception.api.apolloClient
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.fragment_login.view.*


class login : Fragment() {

    val objectFromRegister = register()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
//            inflater, R.layout.fragment_login, container, false)
        // Inflate the layout for this fragment

        var objectView = inflater.inflate(R.layout.fragment_login, container, false)
        return objectView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.login.setOnClickListener {
            it.hideKeyboard()

            val username = view.username_login.text.toString()
            if(username.trim() == ""){

                toastInvalidInput("Username must not be empty")
                return@setOnClickListener
            }

            val password = view.password_login.text.toString()
            if(password.trim() == ""){
                toastInvalidInput("Password must not be empty!")
                return@setOnClickListener
            }

            view.findViewById<ProgressBar>(R.id.progress_bar_login).visibility = View.VISIBLE
            it.visibility = View.GONE

            login(username,password)
        }
    }

    fun toastInvalidInput(text: String){

        var toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun login(username : String, password: String){
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient(requireContext()).mutate(LoginMutation(
                    username = username,
                    password = password
                )).await()
            }catch (e : ApolloException){
                Log.i("error",e.toString())
                null
            }

            val loginResponse = response?.data?.user?.login
            if (loginResponse == null || response.hasErrors()) {
                view?.progress_bar_login?.visibility = View.GONE
                view?.login?.visibility = View.VISIBLE

                Toast.makeText(activity,response?.errors?.get(0)?.message.toString(),Toast.LENGTH_SHORT).show()
                return@launchWhenResumed
            }

            val access_token = loginResponse?.access_token
            User.setToken(requireContext(),access_token)
            User.setUsername(requireContext(),loginResponse?.user?.username)

            var LandingPageIntent = Intent(activity,LandingPage::class.java)
            startActivity(LandingPageIntent)

            view?.progress_bar_login?.visibility = View.GONE
            view?.login?.visibility = View.VISIBLE
        }
    }
}