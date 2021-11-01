package media.uqab.quranapi

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

object ThreadExecutor {
    private var executor = Executors.newSingleThreadExecutor()
    private var parallelExecutor = Executors.newFixedThreadPool(2)

    fun runOnUiThread(runnable: Runnable) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(runnable)
    }

    fun executeParallel(runnable: Runnable) {
        if(parallelExecutor.isTerminated) parallelExecutor = Executors.newFixedThreadPool(2)
        parallelExecutor.execute(runnable)
    }

    fun execute(runnable: Runnable) {
        if(executor.isTerminated) executor = Executors.newSingleThreadExecutor()
        executor.execute(runnable)
    }
}