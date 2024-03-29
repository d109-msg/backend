package com.ssafy.msg.article.controller;


import com.ssafy.msg.article.model.dto.*;
import com.ssafy.msg.article.model.service.ArticleService;
import com.ssafy.msg.game.model.service.GameService;
import com.ssafy.msg.user.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "Article", description = "게시물 관련 API")
public class ArticleController {
    private final ArticleService articleService;
    private final GameService gameService;

    // 게시물 작성
    @PostMapping(value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시물 작성", description = "이미지추가와 내용 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시물 작성 성공"),
            @ApiResponse(responseCode = "400", description = "게시물 작성 실패", content = @Content)})
    public ResponseEntity<?> createArticle(@ModelAttribute ArticleCreateDto articleCreateDto, HttpServletRequest request) {
        log.info("(controller) create Start");

        int id = (int) request.getAttribute("id");
        ArticleDto articleDto = ArticleDto.builder()
                .userId(id)
                .articleImageList(articleCreateDto.getArticleImageList())
                .content(articleCreateDto.getContent())
                .roomId(articleCreateDto.getRoomId())
                .build();

        try {
            articleService.createArticle(articleDto); // 클라이언트로부터 받은 정보를 서비스에 입력
            log.info("(controller) articleService.createArticle 호출 -> Success");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("(controller) articleService.createArticle 호출 에러", e);
            return new ResponseEntity<>("게시물 작성 실패", HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(controller) -> End");
        }
    }

    // 게시물 수정
    @PatchMapping("")
    @Operation(summary = "게시물 수정", description = "게시물 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 수정 성공"),
            @ApiResponse(responseCode = "400", description = "게시물 수정 실패", content = @Content)})
    public ResponseEntity<?> updateArticle(@RequestBody UpdateArticleDto updateArticleDto, HttpServletRequest request) {

        int userId = (int) request.getAttribute("id");

        UpdateArticleDto updateArticleDto1 = UpdateArticleDto.builder()
                .content(updateArticleDto.getContent())
                .articleId(updateArticleDto.getArticleId())
                .userId(userId)
                .build();

        try {
            articleService.updateArticle(updateArticleDto1);
            log.info("(ArticleController) 게시물 수정 성공");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 게시물 수정 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 게시물 수정 끝");
        }
    }

    // 게시물 삭제
    @DeleteMapping("")
    @Operation(summary = "게시물 삭제", description = "게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "게시물 삭제 실패", content = @Content)})
    public ResponseEntity<?> deleteArticle(@RequestParam("articleId") int articleId, HttpServletRequest request) {

        int userId = (int) request.getAttribute("id");

        DeleteArticleDto deleteArticleDto = DeleteArticleDto.builder()
                .userId(userId)
                .articleId(articleId)
                .build();

        try {
            articleService.deleteArticle(deleteArticleDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.info("(controller) 게시물 삭제 실패 에러");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("controller 게시물 삭제 끝");
        }
    }


    // 마이페이지에서 프로필 보기
    @GetMapping(value = "/profile")
    @Operation(summary = "프로필 게시물", description = "프로필 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 리스트 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleWithUrlDto.class))}),
            @ApiResponse(responseCode = "400", description = "게시물 리스트 조회 실패", content = @Content)})
    public ResponseEntity<?> getArticles(@RequestParam("userId") int userId) {

        try {
            List<ArticleWithUrlDto> articleWithUrlDtoList = articleService.getArticles(userId);

            log.info("(ArticleController) 프로필 조회 성공");
            return new ResponseEntity<>(articleWithUrlDtoList, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("(Exception) ", e);
            return new ResponseEntity<>("프로필 조회 실패", HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) getArticles -> end");
        }

    }

    // 게시물 상세 보기 (댓글 포함(대댓글 미포함))
    @GetMapping(value = "/detail")
    @Operation(summary = "게시물 상세", description = "게시물 상세 내용 보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 상세 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDetailDto.class))}),
            @ApiResponse(responseCode = "400", description = "게시물 상세 조회 실패", content = @Content)})
    public ResponseEntity<?> getArticleDetail(@RequestParam("articleId") int articleId,
                                              HttpServletRequest request) {
        log.info("(ArticleController) 게시물 상세보기 시작");

        int id = (int) request.getAttribute("id");

        ArticleDto articleDto = ArticleDto.builder()
                .id(articleId)
                .build();

        try {
            ArticleDetailDto articleDetailDto = articleService.getArticleDetail(articleDto, id); // 게시물 상세 내용 가져오기

            log.info("(ArticleController) 게시물 상세조회 성공");

            return new ResponseEntity<>(articleDetailDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 게시물 상세 조회 실패", e);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {

            log.info("(ArticleController) getArticleDetail end");
        }

    }

    @GetMapping("/feed/room")
    @Operation(summary = "게임방 피드", description = "게임방 피드에 보여줄 게시물 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoomFeedResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content)})
    public ResponseEntity<?> getFeedByRoomId(HttpServletRequest request,
                                             @Parameter(description = "게임방Id") @RequestParam(value = "roomId", required = true, defaultValue = "room4") String roomId,
                                             @Parameter(description = "마지막으로 로딩한 타겟") @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                             @Parameter(description = "페이지당 타겟 개수") @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit) {

//        try {
//            String currentUrl = request.getRequestURL().toString();
//            RoomFeedResponseDto result = articleService.getFeedByRoomId(new ArticleByRoomIdDto(roomId, limit, offset, currentUrl));
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("getFeedByRoomId() -> error : {}", e);
//
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } finally {
//            log.info("getFeedByRoomId() end");
//        }

        if (offset == 0) {
            offset = Integer.MAX_VALUE;
        }

        ArticleByRoomIdDto articleByRoomIdDto = ArticleByRoomIdDto.builder()
                .roomId(roomId)
                .offset(offset)
                .limit(limit)
                .build();

        try {
            RoomFeedResponseDto result = articleService.getFeedByRoomId(articleByRoomIdDto);

            //  보여줄 피드가 없을 때 조건 넣어주기
            if (result == null || result.getArticles().isEmpty()) {
                return null;
            }

            int lastId = result.getArticles().get(result.getArticles().size() - 1).getArticleId();

            String nextUrl = "?roomId=" + roomId + "&offset=" + lastId + "&limit=" + limit;

            result.setNextUrl(nextUrl);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("getFeedByRoomId() -> error : {}", e);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("getFeedByRoomId() end");
        }
    }

    // 메인페이지에서 피드 조회 하기 ( 나 포함, 내가 팔로우 하고 있는 사람만 )
    @GetMapping(value = "/feed")
    @Operation(summary = "피드 게시물 리스트", description = "피드에 보여줄 게시물 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 게시물 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FeedResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "피드 게시물 조회 실패", content = @Content)})
    public ResponseEntity<?> getFeedArticleList(HttpServletRequest request,
                                                @Parameter(description = "마지막으로 로딩한 타겟") @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                @Parameter(description = "페이지당 타겟 개수") @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        log.info("getFollowList() -> Start");

        int userId = (Integer) request.getAttribute("id");

        if (offset == 0) {
            offset = Integer.MAX_VALUE;
        }

        FeedParamDto feedParamDto = FeedParamDto.builder()
                .userId(userId)
                .offset(offset)
                .limit(limit)
                .build();

        try {
            List<ArticleDetailDto> articleDetailDtos = articleService.getFeedArticleList(feedParamDto);

            String currentUrl = request.getRequestURL().toString();
            //  보여줄 피드가 없을 때 조건 넣어주기
            if (articleDetailDtos == null || articleDetailDtos.isEmpty()) {
                return null;
            }

            int lastId = articleDetailDtos.get(articleDetailDtos.size() - 1).getArticleId();
            log.info("(offset) {}", offset);
            log.info("(articleDetailDtos) {}", articleDetailDtos);
            log.info("(lasteId) {}", lastId);


            String nextUrl = "?offset=" + lastId + "&limit=" + limit;
            log.info("(ArticleController) nextUrel {}", nextUrl);
            FeedResponseDto feedResponseDto = FeedResponseDto.builder()
                    .articleDetailDtos(articleDetailDtos)
                    .nextUrl(nextUrl)
                    .build();
            log.info("(ArticleController) getFeedArticleList() 성공");

            return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 피드 게시물 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) getFeedArticleList end");
        }
    }

    // 비회원 메인 페이지
    @GetMapping("/guest")
    @Operation(summary = "비회원 피드", description = "비로그인 시 게스트로 볼 수 있는 피드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게스트 피드 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuestArticleResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "게스트 피드 조회 실패", content = @Content)})
    public ResponseEntity<?> getGuestFeed(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        log.info("(ArticleController) 게스트 피드 리스트");

//        FeedParamDto feedParamDto = FeedParamDto.builder().offset(offset).limit(limit).build();
//        try {
//            GusetFeedResponseDto result = articleService.getGuestFeed(feedParamDto);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("(ArticleController) 게시물 피드 조회 실패", e);
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } finally {
//            log.info("(ArticleController) 게스트 피드 조회 끝");
//        }

        if (offset == 0) {
            offset = Integer.MAX_VALUE;
        }

        FeedParamDto feedParamDto = FeedParamDto.builder()
                .offset(offset)
                .limit(limit)
                .build();

        try {
            GusetFeedResponseDto result = articleService.getGuestFeed(feedParamDto);

            //  보여줄 피드가 없을 때 조건 넣어주기
            if (result == null || result.getArticles().isEmpty()) {
                return null;
            }

            int lastId = result.getArticles().get(result.getArticles().size() - 1).getArticleId();

            String nextUrl = "?offset=" + lastId + "&limit=" + limit;
            result.setNextUrl(nextUrl);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 게시물 피드 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 게스트 피드 조회 끝");
        }

    }

    // 인기 게시물 -> 안쓰나?
    // 비회원 메인 페이지
    @GetMapping("/popular-feed")
    @Operation(summary = "인기 피드", description = "내 피드 모두 조회 후 인기 피드 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인기 피드 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GuestArticleResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "인기 피드 조회 실패", content = @Content)})
    public ResponseEntity<?> getPopularFeed(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
                                            HttpServletRequest request) {
        log.info("(ArticleController) 인기 피드 리스트");

        int userId = (Integer) request.getAttribute("id");
//        FeedParamDto feedParamDto = FeedParamDto.builder().userId(userId).offset(offset).limit(limit).build();
//
//        try {
//            GusetFeedResponseDto result = articleService.getGuestFeed(feedParamDto);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("(ArticleController) 인기 피드 조회 실패", e);
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } finally {
//            log.info("(ArticleController) 인기 피드 조회 끝");
//        }

        if (offset == 0) {
            offset = Integer.MAX_VALUE;
        }

        FeedParamDto feedParamDto = FeedParamDto.builder()
                .userId(userId)
                .offset(offset)
                .limit(limit)
                .build();

        try {
            GusetFeedResponseDto result = articleService.getGuestFeed(feedParamDto);

//            //  보여줄 피드가 없을 때 조건 넣어주기
            if (result == null || result.getArticles().isEmpty()) {
                return null;
            }

            int lastId = result.getArticles().get(result.getArticles().size() - 1).getArticleId();

            String nextUrl = "?offset=" + lastId + "&limit=" + limit;
            result.setNextUrl(nextUrl);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 인기 피드 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 인기 피드 조회 끝");
        }

    }

    // 게시물 좋아요 누르기
    @PostMapping(value = "/like")
    @Operation(summary = "게시물 좋아요", description = "게시물 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleLikeDto.class))}),
            @ApiResponse(responseCode = "400", description = "좋아요 실패", content = @Content)})
    public ResponseEntity<?> articleLike(@RequestParam("articleId") int articleId, HttpServletRequest request) {
        log.info("(ArticleController) articleLike() -> 게시물 좋아요 시작");

        int userId = (Integer) request.getAttribute("id");

        ArticleDto articleDto = ArticleDto.builder().
                userId(userId).
                id(articleId).
                build();

        try {
            articleService.articleLike(articleDto);
            log.info("(ArticleController) 좋아요 누르기 성공");
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 좋아요 누르기 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 좋아요 누르기 끝");
        }

    }

    // 게시물 좋아요 누른 유저 리스트 조회
    @GetMapping(value = "/like-userlist")
    @Operation(summary = "좋아요 유저 리스트", description = "좋아요 누른 유저 리스트를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 리스트 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LikeUserListDto.class))}),
            @ApiResponse(responseCode = "400", description = "좋아요 리스트 조회 실패", content = @Content)})
    public ResponseEntity<?> getLikeUserList(@RequestParam("articleId") int articleId) {
        log.info("(ArticleController) likeUserList 조회 시작");

        try {
            articleService.getLikeUserList(articleId);
            return new ResponseEntity<>(articleService.getLikeUserList(articleId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 좋아요 유저 리스트 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) getLikeUserList end");
        }
    }


    // 댓글 쓰기
    @PostMapping(value = "/comment")
    @Operation(summary = "댓글 작성", description = "댓글 작성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패", content = @Content)})
    public ResponseEntity<?> createComment(HttpServletRequest request,
                                           @RequestBody CreateCommentDto createCommentDto) {

        CommentDto commentDto = CommentDto.builder()
                .userId((Integer) request.getAttribute("id"))
                .articleId(createCommentDto.getArticleId())
                .content(createCommentDto.getContent())
                .parentCommentId(createCommentDto.getCommentId() > 0 ? createCommentDto.getCommentId() : null) // 유효한 ID가 아니라면 null을 할당
                .build();
        log.info("(controller) createComment() 댓글 작성 시작 {}", commentDto);

        try {
            articleService.createComment(commentDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("(controller) 댓글 작성 실패", e);
            return new ResponseEntity<>(commentDto, HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 끝");
        }

    }

    // 댓글 수정
    @PatchMapping("/comment")
    @Operation(summary = "댓글 수정", description = "댓글 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content)})
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentDto updateCommentDto, HttpServletRequest request) {
        log.info("(ArticleController) 댓글 수정 시작");

        int userId = (int) request.getAttribute("id");

        UpdateCommentDto updateCommentDto1 = UpdateCommentDto.builder()
                .commentId(updateCommentDto.getCommentId())
                .content(updateCommentDto.getContent())
                .userId(userId)
                .build();

        try {
            articleService.updateComment(updateCommentDto1);
            log.info("(ArticleController) 댓글 수정 성공");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 댓글 수정 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 댓글 수정 끝");
        }

    }

    // 댓글 삭제
    @DeleteMapping("/comment")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "게시물 삭제 실패", content = @Content)})
    public ResponseEntity<?> deleteComment(@RequestParam("commentId") int commentId, HttpServletRequest request) {

        int userId = (int) request.getAttribute("id");

        DeleteCommentDto deleteCommentDto = DeleteCommentDto.builder()
                .commentId(commentId)
                .userId(userId)
                .build();

        try {
            articleService.deleteComment(deleteCommentDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.info("(controller) 게시물 삭제 실패 에러", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("controller 게시물 삭제 끝");
        }
    }

    // 댓글 조회
    @GetMapping("/comment")
    @Operation(summary = "댓글 목록 조회", description = "댓글 목록 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = @Content)})
    public ResponseEntity<?> getComments(@RequestParam("articleId") int articleId,
                                         @RequestParam(value = "parentCommentId", required = false) Integer parentCommentId,
                                         HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("id");
        CommentDto commentDto = CommentDto.builder()
                .userId(userId)
                .articleId(articleId)
                .parentCommentId(parentCommentId)
                .build();

        try {
            List<CommentDto> commentDtos = articleService.getComments(commentDto);
            log.info("(ArticleController) 댓글 조회 성공");
            return new ResponseEntity<>(commentDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) getComments", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) getComments 댓글 조회 끝");
        }

    }

    // 대댓글 가져오기
    @GetMapping("/child-comment")
    @Operation(summary = "대댓글 목록 조회", description = "대댓글 목록 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대댓글 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))}),
            @ApiResponse(responseCode = "400", description = "대댓글 조회 실패", content = @Content)})
    public ResponseEntity<?> getChildCommentList(@RequestParam("commentId") int commentId,
                                                 @RequestParam("articleId") int articleId) {

        CommentDto commentDto = CommentDto.builder()
                .articleId(articleId)
                .parentCommentId(commentId)
                .build();

        try {
            List<CommentDto> commentDtos = articleService.getComments(commentDto);
            return new ResponseEntity<>(commentDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(대댓글 보기) 대댓글 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 대댓글 조회 성공");
        }
    }

    // 댓글 좋아요 누르기
    @PostMapping(value = "/comment-like")
    @Operation(summary = "댓글 좋아요", description = "게시물 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommentLikeDto.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 좋아요 실패", content = @Content)})
    public ResponseEntity<?> commentLike(@RequestParam("commentId") int commentId, HttpServletRequest request) {
        log.info("(ArticleController) articleLike() -> 게시물 좋아요 시작");

        int userId = (Integer) request.getAttribute("id");

        CommentLikeDto commentLikeDto = CommentLikeDto.builder().
                commentId(commentId).
                userId(userId).
                build();

        try {
            articleService.commentLike(commentLikeDto);
            log.info("(ArticleController) 좋아요 누르기 성공");
            return new ResponseEntity<>(commentLikeDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 좋아요 누르기 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 좋아요 누르기 끝");
        }

    }

    // 댓글 좋아요누른 유저 리스트
    @GetMapping(value = "/comment-like-userlist")
    @Operation(summary = "댓글 좋아요 유저 리스트", description = "댓글 좋아요 누른 유저 리스트를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 리스트 조회 성공", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LikeUserListDto.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 좋아요 리스트 조회 실패", content = @Content)})
    public ResponseEntity<?> getCommentLikeUserList(@RequestParam("commentId") int commentId) {
        log.info("(ArticleController) commentLikeUserList 조회 시작");

        try {
            articleService.getCommentLikeUserList(commentId);
            return new ResponseEntity<>(articleService.getCommentLikeUserList(commentId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 댓글 좋아요 유저 리스트 조회 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) getCommentLikeUserList end");
        }
    }

    @PostMapping(value = "/report")
    @Operation(summary = "게시물 신고", description = "게시물 신고하기 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 신고 성공"),
            @ApiResponse(responseCode = "400", description = "게시물 신고 실패", content = @Content)})
    public ResponseEntity<?> reportArticle(@RequestBody ArticleReportDto articleReportDto, HttpServletRequest request) {
        log.info("(ArticleController) 게시물 리폿 시작");

        int userId = (int) request.getAttribute("id");

        ArticleReportDto articleReportDto1 = ArticleReportDto.builder()
                .fromUserId(userId)
                .articleId(articleReportDto.getArticleId())
                .content(articleReportDto.getContent())
                .build();

        try {
            articleService.reportArticle(articleReportDto1);
            log.info("(ArticleController) 게시물 리폿 성공");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("(ArticleController) 게시물 리폿 실패", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 게시물 리폿 끝");
        }

    }

    @GetMapping(value = "/report")
    @Operation(summary = "신고리스트 조회", description = "관리자에 한에서 신고목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "신고 리스트 조회 실패", content = @Content)})
    public ResponseEntity<?> getArticleReports(HttpServletRequest request) {

        int userId = (int) request.getAttribute("id");

        try {

            return new ResponseEntity<>(articleService.getArticleReports(userId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("신고 리스트 보기 ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            log.info("(ArticleController) 신고 리스트 확인 끝");
        }
    }

}
