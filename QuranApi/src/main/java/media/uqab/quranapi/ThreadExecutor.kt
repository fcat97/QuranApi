package media.uqab.quranapi

import java.util.concurrent.Executors

object ThreadExecutor {
    private var executors = Executors.newFixedThreadPool(2)

    fun execute(runnable: Runnable) {
        if(executors.isTerminated) executors = Executors.newFixedThreadPool(2)
        executors.execute(runnable)
    }
}