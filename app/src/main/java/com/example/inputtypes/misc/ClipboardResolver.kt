package com.example.inputtypes.misc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context

object ClipboardResolver {
    fun getClipboardText(context: Context): String {
        val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // val contentResolver: ContentResolver = context.contentResolver
        if(clipboardManager.hasPrimaryClip()){
           return clipboardManager.primaryClip!!.getItemAt(0).text.toString()
        }

        return ""
    }
}