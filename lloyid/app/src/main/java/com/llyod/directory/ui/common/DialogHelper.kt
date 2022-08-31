package com.llyod.directory.ui.common

import android.app.AlertDialog
import android.content.Context
import com.llyod.directory.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DialogHelper @Inject constructor(@ActivityContext val context: Context?) {

   private  var alertDialog: AlertDialog? = null

    fun showDialog( errorMessage: String ) {

        dismissDialog()
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(context?.getString(R.string.app_name))
        alertDialogBuilder.setMessage(errorMessage)
        alertDialogBuilder.setNegativeButton(
            context?.getString(R.string.cancel_button)
        ) { dialog, id ->
            dialog.cancel()
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    fun dismissDialog(){
        alertDialog?.dismiss()
    }
}