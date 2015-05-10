package excercise.flickrviewer.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.InjectView;
import excercise.flickrviewer.R;
import excercise.flickrviewer.controller.ImageViewerController;
import excercise.flickrviewer.controller.internal.ImageViewerControllerImpl;
import excercise.flickrviewer.dto.FlickrItem;

public class FlickrViewerFragment extends BaseFragment {
    @InjectView(R.id.toolbar)
    Toolbar toolBar;
    @InjectView(R.id.gallery)
    ViewPager gallery;
    private GalleryAdapter galleryAdapter;
    @InjectView(R.id.roller)
    RecyclerView roller;
    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter rollerAdapter;

    private ImageViewerController imageViewerController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewerController = new ImageViewerControllerImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flickr_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        roller.setLayoutManager(layoutManager);

        toolBar.inflateMenu(R.menu.tool_bar_menu);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_refresh) {
                    imageViewerController.updateImages();
                }
                return true;
            }
        });
        initImages();

        setSync();
    }

    private void setSync() {
        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                layoutManager.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        roller.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastState = RecyclerView.SCROLL_STATE_IDLE;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(lastState != newState && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    gallery.setCurrentItem(layoutManager.findFirstCompletelyVisibleItemPosition(), false);
                }
                lastState = newState;
            }
        });
    }

    private void initImages() {
        List<FlickrItem> images = imageViewerController.getModel().getImages();
        if (images == null || images.isEmpty()) {
            imageViewerController.updateImages();
        }
    }

    private void refreshGallery() {
        galleryAdapter = new GalleryAdapter(this);
        gallery.setAdapter(galleryAdapter);
    }

    private void refreshRoller() {
        rollerAdapter = new RollerAdapter(this);
        roller.setAdapter(rollerAdapter);
    }

    public void onEvent(ImageViewerController.Events.OnImageUpdateBegan event) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onEvent(ImageViewerController.Events.OnImagesUpdated event) {
        refreshGallery();
        refreshRoller();
        progressBar.setVisibility(View.GONE);
    }

    public void onEvent(ImageViewerController.Events.OnImagesUpdateFailed event) {
        String msg = String.format(getString(R.string.flickr_error), event.getException().getMessage());
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    static class GalleryAdapter extends PagerAdapter {
        private FlickrViewerFragment flickrViewerFragment;
        private LayoutInflater layoutInflater;

        GalleryAdapter(FlickrViewerFragment flickrViewerFragment) {
            this.flickrViewerFragment = flickrViewerFragment;
            layoutInflater = LayoutInflater.from(flickrViewerFragment.getActivity());
        }

        @Override
        public int getCount() {
            return flickrViewerFragment.imageViewerController.getModel().getImages().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.gallery_item, null);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.gallery_image);
            FlickrItem flickrItem = flickrViewerFragment.imageViewerController.getModel()
                    .getImages().get(position);
            Context context = flickrViewerFragment.getActivity().getApplicationContext();
            Picasso.with(context).load(flickrItem.getMedia().getM()).into(imageView);
            container.addView(itemView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

    }

    static class RollerAdapter extends RecyclerView.Adapter<RollerHolder> {
        private FlickrViewerFragment flickrViewerFragment;

        RollerAdapter(FlickrViewerFragment flickrViewerFragment) {
            this.flickrViewerFragment = flickrViewerFragment;
        }

        @Override
        public RollerHolder onCreateViewHolder(ViewGroup parent, int i) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roller_item, parent, false);
            RollerHolder holder = new RollerHolder(convertView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            return holder;
        }

        @Override
        public void onBindViewHolder(RollerHolder holder, final int i) {
            FlickrItem flickrItem = flickrViewerFragment.imageViewerController.getModel()
                    .getImages().get(i);
            Context context = flickrViewerFragment.getActivity().getApplicationContext();
            Picasso.with(context).load(flickrItem.getMedia().getM()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return flickrViewerFragment.imageViewerController.getModel().getImages().size();
        }
    }

    static class RollerHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public RollerHolder(View itemView) {
            super(itemView);
        }
    }
}
