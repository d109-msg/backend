package com.ssafy.msg.article.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailDto {
    // 유저 정보
    private String imageUrl;
    private String nickname;
    private String emailId;

    //게시글 관련
    private int articleId;
    private int userId;
    private String content;
    private String createTime;
    private int flagPrivate;
    private String modifyTime;
    private String roomId;
    private List<String> urls;

    // 좋아요
    private int likeCount;
    private int isLike;

    // 댓글 관련
    private List<CommentDto> commentList;

    // follow 관련
    private int isFollow;

}