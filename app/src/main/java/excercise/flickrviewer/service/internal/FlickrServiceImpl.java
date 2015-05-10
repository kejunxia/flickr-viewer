package excercise.flickrviewer.service.internal;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import excercise.flickrviewer.dto.FlickrFeed;
import excercise.flickrviewer.service.FlickrService;

public class FlickrServiceImpl implements FlickrService{
    private HttpClient httpClient;
    private Gson gson;

    public FlickrServiceImpl() {
        httpClient = new DefaultHttpClient();
        gson = new Gson();
    }

    @Override
    public FlickrFeed getFeed() throws IOException {
        String url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json";
        HttpGet get = new HttpGet(url);
        HttpResponse resp = httpClient.execute(get);
        String responseStr = EntityUtils.toString(resp.getEntity());
        //Strip off jsonFlickrFeed and brackets
        responseStr = responseStr.replace("jsonFlickrFeed(", "");
        responseStr = responseStr.substring(0, responseStr.length() - 1);
        return gson.fromJson(responseStr, FlickrFeed.class);
    }
}
