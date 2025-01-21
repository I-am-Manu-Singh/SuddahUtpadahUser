package com.neatroots.suddahutpadah.utils

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.DialogProgressBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun navigate(view: View, id: Int) {
        findNavController(view).navigate(id)
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.inVisible() {
        visibility = View.INVISIBLE
    }

    fun showMessage(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(context: Context): AlertDialog {
        val progress = AlertDialog.Builder(context, R.style.CustomAlertDialogTrance).create()
        val processLayout = DialogProgressBinding.inflate(LayoutInflater.from(context))
        progress.setView(processLayout.root)
        progress.setCancelable(false)
        return progress
    }

    fun calculateDiscount(offerPrice: Int, originalPrice: Int): Double {
        val discount = originalPrice - offerPrice
        return (discount / originalPrice.toDouble()) * 100
    }

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun openWhatsAppChat(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$phoneNumber")
        context.startActivity(intent)
    }

    fun openDialer(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

    fun rateUs(context: Context) {
        try {
            val marketUri = Uri.parse("market://details?id=${context.packageName}")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            context.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            val marketUri =
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            context.startActivity(marketIntent)
        }
    }

    fun shareText(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
        context.startActivity(Intent.createChooser(shareIntent, "Share Text"))
    }


    fun openEmailClient(context: Context, emailAddress: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))

        }
        context.startActivity(intent)


    }

}