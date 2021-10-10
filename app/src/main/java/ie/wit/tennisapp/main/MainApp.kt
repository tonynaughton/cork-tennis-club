package ie.wit.tennisapp.main

import android.app.Application
import ie.wit.tennisapp.models.MatchModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val matches = ArrayList<MatchModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Tennis App started")
        matches.add(MatchModel("Test Player", "Test Player", "2-0"))
        matches.add(MatchModel("Test Player", "Test Player", "0-2"))
        matches.add(MatchModel("Test Player", "Test Player", "3-1"))
    }
}