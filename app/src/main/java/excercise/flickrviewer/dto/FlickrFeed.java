package excercise.flickrviewer.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FlickrFeed {
    private String title;
    private String link;
    private String description;
    private Date modified;
    private String generator;
    private List<FlickrItem> items;
}
