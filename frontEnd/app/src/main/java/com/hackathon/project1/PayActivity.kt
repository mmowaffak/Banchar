package com.hackathon.project1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.braintreepayments.cardform.view.CardForm
import com.hackathon.project1.helpers.AppStorage
import kotlinx.android.synthetic.main.payemnt_gate_way.*

class PayActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, PayActivity::class.java)
            context.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payemnt_gate_way)


        val cardForm = findViewById<CardForm>(R.id.card_form)
        cardForm?.cardRequired(true)
            ?.expirationRequired(true)
            ?.cvvRequired(true)
            ?.cardholderName(CardForm.FIELD_REQUIRED)
            ?.postalCodeRequired(true)
            ?.mobileNumberRequired(true)
            ?.mobileNumberExplanation("SMS is required on this number")
            ?.actionLabel("Purchase")
            ?.setup(this)


        buttonCash.setOnClickListener {
            finish()
        }
    }


}
