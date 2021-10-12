package media.uqab.quranapi

import java.util.concurrent.Executors

object ThreadExecutor {
    private var executor = Executors.newSingleThreadExecutor()
    private var parallelExecutor = Executors.newFixedThreadPool(2)

    fun executeParallel(runnable: Runnable) {
        if(parallelExecutor.isTerminated) parallelExecutor = Executors.newFixedThreadPool(2)
        parallelExecutor.execute(runnable)
    }

    fun execute(runnable: Runnable) {
        if(executor.isTerminated) executor = Executors.newSingleThreadExecutor()
        executor.execute(runnable)
    }
}