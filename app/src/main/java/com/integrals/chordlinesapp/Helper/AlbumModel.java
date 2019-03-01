package com.integrals.chordlinesapp.Helper;

public class AlbumModel {
    private String CoverPic;
    private String YouTubeLink;
    private String PublishedON;
    private String AlbumName;
    private String Details;
    private String DownloadLink;
    //
    public AlbumModel(String coverPic,
                      String youTubeLink,
                      String publishedON,
                      String albumName,
                      String details,
                      String downloadLink) {
        CoverPic = coverPic;
        YouTubeLink = youTubeLink;
        PublishedON = publishedON;
        AlbumName = albumName;
        Details = details;
        DownloadLink = downloadLink;
    }


    public String getCoverPic() {
        return CoverPic;
    }

    public void setCoverPic(String coverPic) {
        CoverPic = coverPic;
    }

    public String getYouTubeLink() {
        return YouTubeLink;
    }

    public void setYouTubeLink(String youTubeLink) {
        YouTubeLink = youTubeLink;
    }

    public String getPublishedON() {
        return PublishedON;
    }

    public void setPublishedON(String publishedON) {
        PublishedON = publishedON;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDownloadLink() {
        return DownloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        DownloadLink = downloadLink;
    }
}
