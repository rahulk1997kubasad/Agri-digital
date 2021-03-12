package project.farmvisor;

public class WebLinkObject {

    String webLink;
    String kanString;
    String engString;

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getKanString() {
        return kanString;
    }

    public void setKanString(String kanString) {
        this.kanString = kanString;
    }

    public String getEngString() {
        return engString;
    }

    public void setEngString(String engString) {
        this.engString = engString;
    }

    public WebLinkObject(String webLink, String kanString, String engString) {
        this.webLink = webLink;
        this.kanString = kanString;
        this.engString = engString;
    }
}
