package ie.wit.tennisapp.main

import android.app.Application
import ie.wit.tennisapp.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var results: ResultStore
    lateinit var members: MemberStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        results = ResultsMemStore()
        members = MemberJSONStore(applicationContext)
        i("Tennis App started")
    }
}