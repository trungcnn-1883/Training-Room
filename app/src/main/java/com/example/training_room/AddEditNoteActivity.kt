package com.example.training_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.database.DataBaseManager
import com.example.database.MyDataBaseHelper
import com.example.entity.Note
import com.example.subscriber.BaseObserver
import com.example.subscriber.BaseSubscriber
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AddEditNoteActivity : AppCompatActivity() {

    var note: Note? = Note("A", "A")
    private val MODE_CREATE = 1
    private val MODE_EDIT = 2

    private var mode: Int = 0
    private var textTitle: EditText? = null
    private var textContent: EditText? = null

    private var needRefresh: Boolean = false
    private val mAddDisposable = CompositeDisposable()
    private val mUpdateDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        this.textTitle = this.findViewById(R.id.note_title_edt)
        this.textContent = this.findViewById(R.id.note_content_edt)

        val intent = this.intent
        if (intent.getSerializableExtra("note") != null) {
            this.note = intent.getSerializableExtra("note") as Note
            Log.d("Update", note!!.id.toString())
            this.mode = MODE_EDIT
            this.textTitle!!.setText(note!!.title)
            this.textContent!!.setText(note!!.content)
        } else this.mode = MODE_CREATE

    }


    fun buttonSaveClicked(view: View) {
        val db = DataBaseManager.getNoteDaoInstance(this)
        val title = this.textTitle!!.text.toString()
        val content = this.textContent!!.text.toString()

        if (title == "" || content == "") {
            Toast.makeText(
                this,
                "Please enter title & content", Toast.LENGTH_LONG
            ).show()
            return
        }

        if (mode == MODE_CREATE) {
            this.note = Note(title, content)
            mAddDisposable.add(
                db.addNote(note!!).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(AddNoteSubscriber())
            )

        } else {
            this.note!!.title = title
            this.note!!.content = content
            mUpdateDisposable.add(
                db.updateNote(note!!).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(UpdateNoteSubscriber())
            )
        }

        this.needRefresh = true
        this.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        mAddDisposable.clear()
        mUpdateDisposable.clear()
    }

    fun buttonCancelClicked(view: View) {
        this.onBackPressed()
    }

    override fun finish() {

        val data = Intent()
        data.putExtra("needRefresh", needRefresh)
        this.setResult(Activity.RESULT_OK, data)
        super.finish()
    }

    inner class AddNoteSubscriber : BaseObserver<Long>() {
        override fun onHandleError(t: Throwable) {
            Toast.makeText(applicationContext, "Success False", Toast.LENGTH_SHORT).show()
        }

        override fun onHandleResult(t: Long) {
            Toast.makeText(applicationContext, "Success with new row id = " + t.toInt(), Toast.LENGTH_SHORT).show()
        }

    }

    inner class UpdateNoteSubscriber : BaseObserver<Int>() {
        override fun onHandleError(t: Throwable) {
            Toast.makeText(applicationContext, "updateNote False" + t, Toast.LENGTH_SHORT).show()

        }

        override fun onHandleResult(t: Int) {
            Toast.makeText(applicationContext, "Success updateNote: " + t, Toast.LENGTH_SHORT).show()
        }

    }
}
