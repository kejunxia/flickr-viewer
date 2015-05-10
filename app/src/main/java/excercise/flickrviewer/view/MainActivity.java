package excercise.flickrviewer.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import excercise.flickrviewer.R;
import excercise.flickrviewer.view.fragments.FlickrViewerFragment;

public class MainActivity extends ActionBarActivity {
    private static final String FLICKR__FRAGMENT_TAG = "FlikrFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlickrViewerFragment fragment = (FlickrViewerFragment) getSupportFragmentManager()
                .findFragmentByTag(FLICKR__FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new FlickrViewerFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, fragment)
                    .commit();
        }
    }
}
