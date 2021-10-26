package ie.wit.tennisapp.main

import android.app.Application
import ie.wit.tennisapp.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var matches: ResultStore
    lateinit var members: MemberStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        matches = ResultsMemStore()
        members = MemberJSONStore(applicationContext)
        i("Tennis App started")
    }
}