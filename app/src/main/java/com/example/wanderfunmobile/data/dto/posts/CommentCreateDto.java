package com.example.wanderfunmobile.data.dto.posts;

public class CommentCreateDto {
    private String content;
    private Long parentId;

    public CommentCreateDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
