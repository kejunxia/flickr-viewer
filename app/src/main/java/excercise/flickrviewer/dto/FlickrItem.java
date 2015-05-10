package excercise.flickrviewer.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FlickrItem {
    @Data
    public static class Media {
        private String m;
    }

    private String title;
    private String link;
    private Media media;
    private Date date;
    private String description;
    private Date published;
    private String author;
    private String authorId;
    private String tags;
}
