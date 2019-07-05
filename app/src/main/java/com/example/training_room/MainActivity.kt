package com.example.training_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.NoteAdapter
import com.example.entity.Note
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.dao.NoteDao
import com.example.database.DataBaseManager
import com.example.database.MyDataBaseHelper
import com.example.subscriber.BaseSubscriber
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class MainActivity : AppCompatActivity() {

    private var mNoteRv: RecyclerView? = null

    companion object {
        val MENU_ITEM_VIEW = 111
        val MENU_ITEM_EDIT = 222
        val MENU_ITEM_DELETE = 444
        val MY_REQUEST_CODE = 1000
    }


    private var noteList = ArrayList<Note>()
    lateinit var listViewAdapter: NoteAdapter
    var db: NoteDao? = null

    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNoteRv = findViewById(R.id.listView)

        this.listViewAdapter = NoteAdapter(this, this.noteList)

        this.mNoteRv!!.layoutManager = LinearLayoutManager(this)

        this.mNoteRv!!.adapter = this.listViewAdapter

        val dividerItemDecoration = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        mNoteRv!!.addItemDecoration(dividerItemDecoration)

        registerForContextMenu(mNoteRv)

        setSupportActionBar(findViewById(R.id.main_toolbar))


    }

    override fun onStart() {
        super.onStart()
        db = DataBaseManager.getNoteDaoInstance(this)
        mDisposable.add(
            db?.getAllNotes()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(GetAllNoteSubscriber())
        )
    }


    fun onHandleAddBtn(view: View) {
        val intent = Intent(this, AddEditNoteActivity::class.java)

        this.startActivityForResult(intent, MY_REQUEST_CODE)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var selected: Boolean = false
        when (item?.itemId) {
            R.id.action_refresh -> {
//                refreshList()
                selected = true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return selected
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_toolbar, menu)
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            val needRefresh = data!!.getBooleanExtra("needRefresh", true)
            if (needRefresh) {
//                refreshList()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mDisposable.clear()
    }


    inner class GetAllNoteSubscriber : BaseSubscriber() {
        override fun onHandleError(t: Throwable) {
            Toast.makeText(applicationContext, "No data", Toast.LENGTH_SHORT).show()

        }

        override fun onHandleResult(t: List<Note>) {
            listViewAdapter.updateListNote(t)
            Toast.makeText(applicationContext, "Have data", Toast.LENGTH_SHORT).show()
        }

    }

}
