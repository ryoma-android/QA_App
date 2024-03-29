package com.example.qa_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var mDataBaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val name = sp.getString(NameKEY, "")
        nameText.setText(name)

        mDataBaseReference = FirebaseDatabase.getInstance().reference

        title = getString(R.string.setting_title)

        changeButton.setOnClickListener { v ->

            val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {

                Snackbar.make(v, getString(R.string.no_login_user), Snackbar.LENGTH_LONG).show()

            } else {

                val name2 = nameText.text.toString()   //fireBaseの保存方法
                val userRef = mDataBaseReference.child(UsersPATH).child(user.uid)
                val data = HashMap<String, String>()
                data["name"] = name2
                userRef.setValue(data)
            }

            val sp2 =
                PreferenceManager.getDefaultSharedPreferences(applicationContext) //Preferenceの保存方法
            val editor = sp2.edit()
            editor.putString(NameKEY, name)
            editor.commit()

            Snackbar.make(v, getString(R.string.change_disp_name), Snackbar.LENGTH_LONG).show()

        }

        logoutButton.setOnClickListener { v ->
            FirebaseAuth.getInstance().signOut()
            nameText.setText("")
            Snackbar.make(v, getString(R.string.logout_complete_message), Snackbar.LENGTH_LONG)
                .show()

        }
    }
}

