package excercise.flickrviewer.controller.internal;

import android.util.Log;

import java.util.ArrayList;

import excercise.flickrviewer.controller.ImageViewerController;
import excercise.flickrviewer.dto.FlickrItem;
import excercise.flickrviewer.service.FlickrService;
import excercise.flickrviewer.service.internal.FlickrServiceImpl;

public class ImageViewerControllerImpl extends BaseControllerImpl implements ImageViewerController {
    private Model model;
    private FlickrService flickrService;

    public ImageViewerControllerImpl() {
        model = new Model();
        model.setImages(new ArrayList<FlickrItem>());
        flickrService = new FlickrServiceImpl();
    }

    @Override
    public void updateImages() {
        postEvent(new Events.OnImageUpdateBegan());
        runTask(new TaskRunner() {
            @Override
            public void run() throws Exception {
                model.setImages(flickrService.getFeed().getItems());
                Events.OnImagesUpdated successEvent = new Events.OnImagesUpdated(model.getImages());
                postEvent(successEvent);
            }
        }, new TaskExceptionHandler() {
            @Override
            public void handle(Exception e) {
                Log.w(getClass().getSimpleName(), e.getMessage());
                postEvent(new Events.OnImagesUpdateFailed(e));
            }
        });
    }

    @Override
    public Model getModel() {
        return model;
    }
}
