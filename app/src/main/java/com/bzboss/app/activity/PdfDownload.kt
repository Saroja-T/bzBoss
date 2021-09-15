package com.bzboss.app.activity

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bzboss.app.R
import com.bzboss.app.custom.FileDownloader
import kotlinx.android.synthetic.main.activity_pdf_download.*
import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.*
import java.io.File


class PdfDownload : AppCompatActivity() {
    var REQUEST_WRITE_PERMISSION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_download)
        Glide.with(this@PdfDownload)
            .load("https://demo.emeetify.com:4500/uploads/stafflog/f67986f81647479880bd4279f7aaa5be1624821482172.png")
            .into(img)

    //        setContentView(R.layout.activity_main);
        //requestPermission();
        /* Request user permissions in runtime */
        /*ActivityCompat.requestPermissions(this@PdfDownload, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 100)*/
        //download()
       // requestPermission()
    }
     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
         if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             // download()
         }
     }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        } else {
            download()

        }
    }
    fun download() {
        Log.d("Download complete", "----------")
        var file_path : String =""
        var uri: Uri
        class DownloadFile : AsyncTask<String?, Void?, Void?>(){

            var uploading: ProgressDialog? = null

            override fun onPreExecute() {
                super.onPreExecute()
                uploading =
                    ProgressDialog.show(
                        this@PdfDownload,
                        "Please Wait",
                        "Downloading...",
                        false,
                        false
                    )
            }
            override fun doInBackground(vararg strings: String?): Void? {
                uploading?.dismiss()
                val fileUrl: String = strings.get(0)!! // -> http://maven.apache.org/maven-1.x/maven.pdf

                val fileName: String = strings.get(1)!! // -> maven.pdf

                val localStorage = getExternalFilesDir(null)
//            if (localStorage == null) { return; }
                //            if (localStorage == null) { return; }
                val storagePath = localStorage!!.absolutePath
                val rootPath = "$storagePath/test15"
//            String fileName = "/test.zip";

                //            String fileName = "/test.zip";
                val root = File(rootPath)
                if (!root.mkdirs()) {
                    Log.i("Test", "This path is already exist: " + root.absolutePath)
                }

                val file = File(rootPath + fileName)
                try {
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        this@PdfDownload,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        if (!file.createNewFile()) {
                            Log.i("Test", "This file is already exist: " + file.absolutePath)
                        }
                        file_path = file.absolutePath


                        uri = if (Build.VERSION.SDK_INT < 24) {
                            Uri.fromFile(file)
                        } else {
                            Uri.parse(file.path) // My work-around for new SDKs, worked for me in Android 10 using Solid Explorer Text Editor as the external editor.
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                /* String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
                    .toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.getParentFile().mkdirs();
            Log.d(TAG, "doInBackground: "+folder.toString());
            File pdfFile = new File(folder, fileName);
            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }*/

                /* String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
                    .toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.getParentFile().mkdirs();
            Log.d(TAG, "doInBackground: "+folder.toString());
            File pdfFile = new File(folder, fileName);
            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }*/
                FileDownloader.downloadFile(fileUrl, file)
                return null
            }
            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                if(file_path!=""){
                    val intentShareFile = Intent(Intent.ACTION_SEND)
                    val fileWithinMyDir: File = File(file_path)
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.type = "application/pdf"
                        intentShareFile.putExtra(
                                Intent.EXTRA_STREAM,
                                Uri.parse("content://$file_path")
                        )
                        intentShareFile.putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Sharing File..."
                        )
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intentShareFile, "Share File"))
                    }
                }
            }
        }

        DownloadFile().execute(
            "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
            "maven.pdf"
        )
        Log.d("Download complete", "----------")
    }

/*
     class DownloadFile : AsyncTask<String?, Void?, Void?>() {

         override fun doInBackground(vararg strings: String?): Void? {
             val fileUrl = strings[0] // -> http://maven.apache.org/maven-1.x/maven.pdf
             val fileName = strings[1] // -> maven.pdf

             */
/*val extStorageDirectory = Environment.getExternalStorageDirectory().absolutePath
                     .toString()
             val folder = File(extStorageDirectory, "testthreepdf")
             folder.parentFile.mkdirs()
             val pdfFile = File(folder, fileName)
             try {
                 pdfFile.createNewFile()
             } catch (e: IOException) {
                 e.printStackTrace()
             }*//*



             val localStorage: File = Environment.getExternalStorageDirectory()
             //            if (localStorage == null) { return; }
             val storagePath = localStorage.absolutePath
             val rootPath = "$storagePath/test11"
             //            String fileName = "/test.zip";
             val root = File(rootPath)
             if (!root.mkdirs()) {
                 Log.i("Test", "This path is already exist: " + root.absolutePath)
             }
             val file = File(rootPath + fileName)
             */
/*
             try {
                 val permissionCheck = ContextCompat.checkSelfPermission(
                         this@PdfDownload,
                         Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                     if (!file.createNewFile()) {
                         Log.i("Test", "This file is already exist: " + file.absolutePath)
                     }
                 }
             } catch (e: Exception) {
                 e.printStackTrace()
             }*//*


             FileDownloader.downloadFile(fileUrl, file)
             return null
         }
     }
*/
}


