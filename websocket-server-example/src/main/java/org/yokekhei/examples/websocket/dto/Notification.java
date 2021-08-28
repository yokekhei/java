package org.yokekhei.examples.websocket.dto;

public class Notification {

    public enum Type {
        OUT_OF_STOCK, NEW_ORDER, ANNOUNCEMENT
    };

    private Long id;
    private Integer type;
    private String title;
    private String shortDesc;
    private String content;
    private String dateTime;

    public Notification() {
    }

    public Notification(Integer type, String title, String shortDesc, String content, String dateTime) {
        this.type = type;
        this.title = title;
        this.shortDesc = shortDesc;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", type=" + type + ", title=" + title + ", shortDesc=" + shortDesc
                + ", content=" + content + ", dateTime=" + dateTime + "]";
    }

}
