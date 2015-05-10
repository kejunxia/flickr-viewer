package excercise.flickrviewer.service;

import java.io.IOException;

import excercise.flickrviewer.dto.FlickrFeed;

public interface FlickrService {
    FlickrFeed getFeed() throws IOException;
}
