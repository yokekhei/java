package org.yokekhei.examples.websocket.dto;

public class Comment {

    public enum Type {
        NEW, REPLY
    };

    private Long id;
    private Long postId;
    private Integer type;
    private String commenter;
    private String message;
    private String dateTime;

    public Comment() {
    }

    public Comment(Long postId, Integer type, String commenter, String message, String dateTime) {
        this.postId = postId;
        this.type = type;
        this.commenter = commenter;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", postId=" + postId + ", type=" + type + ", commenter=" + commenter + ", message="
                + message + ", dateTime=" + dateTime + "]";
    }

}
