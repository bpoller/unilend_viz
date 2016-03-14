package io.nowave.api.dto.vhx;

public class VhxVideo {

    String id;
    String title;

    private VhxVideo() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static Trailer toTrailer(VhxVideo vhxVideo) {
        return new Trailer("<iframe src=\"https://embed.vhx.tv/videos/"
                + vhxVideo.getId()
                + "?buy_btn=0&sharing=0&title=0\" width=\"640\" height=\"360\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>");
    }

    public String playerHtml() {
        return "<iframe src=\"https://embed.vhx.tv/videos/"
                + getId() + "?buy_btn=0&sharing=0&title=0&api=1\""
                + " id=\"" + "myvhxvideo" + "\""
                + " width=\"640\" height=\"360\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>";
    }
}
