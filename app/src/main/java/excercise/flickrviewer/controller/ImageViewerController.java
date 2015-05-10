package excercise.flickrviewer.controller;

import java.util.List;

import excercise.flickrviewer.dto.FlickrItem;
import lombok.Data;

public interface ImageViewerController {
    void updateImages();
    Model getModel();

    @Data
    class Model {
        private List<FlickrItem> images;
    }

    class Events {
        @Data
        public static class OnImageUpdateBegan {}

        @Data
        public static class OnImagesUpdated {
            private final List<FlickrItem> images;

            public OnImagesUpdated(List<FlickrItem> images) {
                this.images = images;
            }
        }

        @Data
        public static class OnImagesUpdateFailed {
            private final Exception exception;

            public OnImagesUpdateFailed(Exception exception) {
                this.exception = exception;
            }
        }
    }
}
