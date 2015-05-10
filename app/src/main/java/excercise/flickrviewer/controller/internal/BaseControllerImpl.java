package excercise.flickrviewer.controller.internal;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import de.greenrobot.event.EventBus;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class BaseControllerImpl {
    private static Handler handler = new Handler();
    private static EventBus eventBus = new EventBus();

    private ExecutorService mExecutorService;

    /**
     * Event bus to deliver events from controllers to views
     * @return
     */
    public static EventBus getEventBus() {
        return eventBus;
    }

    public BaseControllerImpl(){
        mExecutorService = Executors.newFixedThreadPool(10, new ThreadFactory() {
            @Override
            public Thread newThread(final @NonNull Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, "ControllerBackGroundThread");
            }
        });
    }

    /**
     * Run tasks asynchronously.
     * @param taskRunner Task runner
     * @param exceptionHandler Error handler
     */
    protected void runTask(final TaskRunner taskRunner, final TaskExceptionHandler exceptionHandler) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    taskRunner.run();
                } catch (Exception e) {
                    exceptionHandler.handle(e);
                }
            }
        });
    }

    /**
     * Post events to view and make sure it's going to be running on UI thread
     * @param event
     */
    protected void postEvent(final Object event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getEventBus().post(event);
            }
        });
    }
}
