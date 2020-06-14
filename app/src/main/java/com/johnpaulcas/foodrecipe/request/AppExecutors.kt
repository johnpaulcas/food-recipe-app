package com.johnpaulcas.foodrecipe.request

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Created by johnpaulcas on 12/06/2020.
 */
class AppExecutors {

    private val _networkIO = Executors.newScheduledThreadPool(3)

    val networkIO: ScheduledExecutorService
        get() = _networkIO

    companion object {
        private var instance: AppExecutors? = null

        fun getInstance(): AppExecutors {
            if (instance == null) {
                instance = AppExecutors()
            }

            return instance as AppExecutors
        }
    }
}