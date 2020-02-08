package com.muklas.dictionarylite

import android.content.*
import android.os.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.TRANSLATE
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.WORD
import com.muklas.dictionarylite.database.EnToIdHelper
import com.muklas.dictionarylite.database.IdToEnHelper
import com.muklas.dictionarylite.service.DataManagerService
import com.muklas.dictionarylite.service.DataManagerService.Companion.CANCEL_MESSAGE
import com.muklas.dictionarylite.service.DataManagerService.Companion.FAILED_MESSAGE
import com.muklas.dictionarylite.service.DataManagerService.Companion.PREPARATION_MESSAGE
import com.muklas.dictionarylite.service.DataManagerService.Companion.SUCCESS_MESSAGE
import com.muklas.dictionarylite.service.DataManagerService.Companion.UPDATE_MESSAGE
import com.muklas.dictionarylite.service.HandlerCallback
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), HandlerCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var idToEnHelper: IdToEnHelper
    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_translate, R.id.nav_history, R.id.nav_word,
                R.id.nav_about, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setup()
        serviceSetup()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setup() {
        idToEnHelper = IdToEnHelper(this)
        idToEnHelper.open()
    }

    private fun serviceSetup(){
        val mBoundServiceIntent = Intent(this@MainActivity, DataManagerService::class.java)
        val mActivityMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    override fun onPreparation() {
    }

    override fun updateProgress(progress: Long) {
    }

    override fun loadSuccess() {
    }

    override fun loadFailed() {
    }

    override fun loadCancel() {
    }

    private class IncomingHandler(callback: HandlerCallback) : Handler() {
        private var weakCallback: WeakReference<HandlerCallback> = WeakReference(callback)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PREPARATION_MESSAGE -> weakCallback.get()?.onPreparation()
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallback.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> weakCallback.get()?.loadSuccess()
                FAILED_MESSAGE -> weakCallback.get()?.loadFailed()
                CANCEL_MESSAGE -> weakCallback.get()?.loadCancel()
            }
        }
    }
}