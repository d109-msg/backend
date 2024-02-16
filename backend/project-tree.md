
```
backend
├─ .classpath
├─ .gradle
│  ├─ 8.5
│  │  ├─ checksums
│  │  │  ├─ checksums.lock
│  │  │  ├─ md5-checksums.bin
│  │  │  └─ sha1-checksums.bin
│  │  ├─ dependencies-accessors
│  │  │  ├─ dependencies-accessors.lock
│  │  │  └─ gc.properties
│  │  ├─ executionHistory
│  │  │  ├─ executionHistory.bin
│  │  │  └─ executionHistory.lock
│  │  ├─ fileChanges
│  │  │  └─ last-build.bin
│  │  ├─ fileHashes
│  │  │  ├─ fileHashes.bin
│  │  │  ├─ fileHashes.lock
│  │  │  └─ resourceHashesCache.bin
│  │  ├─ gc.properties
│  │  └─ vcsMetadata
│  ├─ buildOutputCleanup
│  │  ├─ buildOutputCleanup.lock
│  │  ├─ cache.properties
│  │  └─ outputFiles.bin
│  ├─ file-system.probe
│  └─ vcs-1
│     └─ gc.properties
├─ .idea
│  ├─ compiler.xml
│  ├─ gradle.xml
│  ├─ jarRepositories.xml
│  ├─ misc.xml
│  ├─ modules
│  │  └─ backend.main.iml
│  ├─ modules.xml
│  ├─ vcs.xml
│  └─ workspace.xml
├─ .project
├─ build
│  ├─ classes
│  │  └─ java
│  │     └─ main
│  │        └─ com
│  │           └─ ssafy
│  │              └─ msg
│  │                 ├─ aop
│  │                 │  └─ LogAspect.class
│  │                 ├─ article
│  │                 │  ├─ controller
│  │                 │  │  └─ ArticleController.class
│  │                 │  ├─ model
│  │                 │  │  ├─ dto
│  │                 │  │  │  ├─ ArticleByRoomIdDto$ArticleByRoomIdDtoBuilder.class
│  │                 │  │  │  ├─ ArticleByRoomIdDto.class
│  │                 │  │  │  ├─ ArticleCreateDto$ArticleCreateDtoBuilder.class
│  │                 │  │  │  ├─ ArticleCreateDto.class
│  │                 │  │  │  ├─ ArticleDetailDto$ArticleDetailDtoBuilder.class
│  │                 │  │  │  ├─ ArticleDetailDto.class
│  │                 │  │  │  ├─ ArticleDto$ArticleDtoBuilder.class
│  │                 │  │  │  ├─ ArticleDto.class
│  │                 │  │  │  ├─ ArticleImageDto$ArticleImageDtoBuilder.class
│  │                 │  │  │  ├─ ArticleImageDto.class
│  │                 │  │  │  ├─ ArticleLikeDto$ArticleLikeDtoBuilder.class
│  │                 │  │  │  ├─ ArticleLikeDto.class
│  │                 │  │  │  ├─ ArticleReportDto$ArticleReportDtoBuilder.class
│  │                 │  │  │  ├─ ArticleReportDto.class
│  │                 │  │  │  ├─ ArticleWithUrlDto$ArticleWithUrlDtoBuilder.class
│  │                 │  │  │  ├─ ArticleWithUrlDto.class
│  │                 │  │  │  ├─ CommentCreateDto$CommentCreateDtoBuilder.class
│  │                 │  │  │  ├─ CommentCreateDto.class
│  │                 │  │  │  ├─ CommentDto$CommentDtoBuilder.class
│  │                 │  │  │  ├─ CommentDto.class
│  │                 │  │  │  ├─ CommentLikeDto$CommentLikeDtoBuilder.class
│  │                 │  │  │  ├─ CommentLikeDto.class
│  │                 │  │  │  ├─ CommentLikeUserListDto$CommentLikeUserListDtoBuilder.class
│  │                 │  │  │  ├─ CommentLikeUserListDto.class
│  │                 │  │  │  ├─ CreateCommentDto$CreateCommentDtoBuilder.class
│  │                 │  │  │  ├─ CreateCommentDto.class
│  │                 │  │  │  ├─ DeleteArticleDto$DeleteArticleDtoBuilder.class
│  │                 │  │  │  ├─ DeleteArticleDto.class
│  │                 │  │  │  ├─ DeleteCommentDto$DeleteCommentDtoBuilder.class
│  │                 │  │  │  ├─ DeleteCommentDto.class
│  │                 │  │  │  ├─ FeedParamDto$FeedParamDtoBuilder.class
│  │                 │  │  │  ├─ FeedParamDto.class
│  │                 │  │  │  ├─ FeedResponseDto$FeedResponseDtoBuilder.class
│  │                 │  │  │  ├─ FeedResponseDto.class
│  │                 │  │  │  ├─ GuestArticleResponseDto$GuestArticleResponseDtoBuilder.class
│  │                 │  │  │  ├─ GuestArticleResponseDto.class
│  │                 │  │  │  ├─ GuestArticleResultDto.class
│  │                 │  │  │  ├─ GusetFeedResponseDto$GusetFeedResponseDtoBuilder.class
│  │                 │  │  │  ├─ GusetFeedResponseDto.class
│  │                 │  │  │  ├─ LikeUserListDto$LikeUserListDtoBuilder.class
│  │                 │  │  │  ├─ LikeUserListDto.class
│  │                 │  │  │  ├─ RoomArticleResponseDto$RoomArticleResponseDtoBuilder.class
│  │                 │  │  │  ├─ RoomArticleResponseDto.class
│  │                 │  │  │  ├─ RoomArticleResultDto$RoomArticleResultDtoBuilder.class
│  │                 │  │  │  ├─ RoomArticleResultDto.class
│  │                 │  │  │  ├─ RoomFeedResponseDto$RoomFeedResponseDtoBuilder.class
│  │                 │  │  │  ├─ RoomFeedResponseDto.class
│  │                 │  │  │  ├─ UpdateArticleDto$UpdateArticleDtoBuilder.class
│  │                 │  │  │  ├─ UpdateArticleDto.class
│  │                 │  │  │  ├─ UpdateCommentDto$UpdateCommentDtoBuilder.class
│  │                 │  │  │  └─ UpdateCommentDto.class
│  │                 │  │  ├─ mapper
│  │                 │  │  │  └─ ArticleMapper.class
│  │                 │  │  └─ service
│  │                 │  │     ├─ ArticleService.class
│  │                 │  │     └─ ArticleServiceImpl.class
│  │                 │  └─ util
│  │                 │     ├─ OpenAiUtil.class
│  │                 │     └─ S3Util.class
│  │                 ├─ BackendApplication.class
│  │                 ├─ chat
│  │                 │  ├─ controller
│  │                 │  │  └─ ChatController.class
│  │                 │  └─ model
│  │                 │     ├─ dto
│  │                 │     │  ├─ CreateRoomDto$CreateRoomDtoBuilder.class
│  │                 │     │  ├─ CreateRoomDto.class
│  │                 │     │  ├─ OpponentDto$OpponentDtoBuilder.class
│  │                 │     │  ├─ OpponentDto.class
│  │                 │     │  ├─ RoomDto$RoomDtoBuilder.class
│  │                 │     │  ├─ RoomDto.class
│  │                 │     │  ├─ RoomResponseDto$RoomResponseDtoBuilder.class
│  │                 │     │  └─ RoomResponseDto.class
│  │                 │     ├─ mapper
│  │                 │     │  └─ ChatMapper.class
│  │                 │     └─ service
│  │                 │        ├─ ChatService.class
│  │                 │        └─ ChatServiceImpl.class
│  │                 ├─ config
│  │                 │  ├─ AsyncConfig.class
│  │                 │  ├─ DatabaseConfig.class
│  │                 │  ├─ FCMConfig.class
│  │                 │  ├─ JasyptConfig.class
│  │                 │  ├─ MessageConfig.class
│  │                 │  ├─ RedisConfig.class
│  │                 │  ├─ S3Config.class
│  │                 │  ├─ SecurityConfig.class
│  │                 │  ├─ SwaggerConfig.class
│  │                 │  └─ WebConfig.class
│  │                 ├─ game
│  │                 │  ├─ controller
│  │                 │  │  └─ GameController.class
│  │                 │  ├─ exception
│  │                 │  │  ├─ GroupRoomDuplicateException.class
│  │                 │  │  ├─ GroupRoomFullException.class
│  │                 │  │  └─ GroupRoomNotFoundException.class
│  │                 │  ├─ model
│  │                 │  │  ├─ dto
│  │                 │  │  │  ├─ AbilityTargetResponseDto$AbilityTargetResponseDtoBuilder.class
│  │                 │  │  │  ├─ AbilityTargetResponseDto.class
│  │                 │  │  │  ├─ AiResultDto$AiResultDtoBuilder.class
│  │                 │  │  │  ├─ AiResultDto.class
│  │                 │  │  │  ├─ AliveParticipantDto$AliveParticipantDtoBuilder.class
│  │                 │  │  │  ├─ AliveParticipantDto.class
│  │                 │  │  │  ├─ ApplyGroupRoomDto$ApplyGroupRoomDtoBuilder.class
│  │                 │  │  │  ├─ ApplyGroupRoomDto.class
│  │                 │  │  │  ├─ CompleteMissionDto$CompleteMissionDtoBuilder.class
│  │                 │  │  │  ├─ CompleteMissionDto.class
│  │                 │  │  │  ├─ CreateMsgArticleDto$CreateMsgArticleDtoBuilder.class
│  │                 │  │  │  ├─ CreateMsgArticleDto.class
│  │                 │  │  │  ├─ EnterGroupRoomDto$EnterGroupRoomDtoBuilder.class
│  │                 │  │  │  ├─ EnterGroupRoomDto.class
│  │                 │  │  │  ├─ GetAbilityParamDto$GetAbilityParamDtoBuilder.class
│  │                 │  │  │  ├─ GetAbilityParamDto.class
│  │                 │  │  │  ├─ GetAbilityResultDto$GetAbilityResultDtoBuilder.class
│  │                 │  │  │  ├─ GetAbilityResultDto.class
│  │                 │  │  │  ├─ GetRoomVoteResult$GetRoomVoteResultBuilder.class
│  │                 │  │  │  ├─ GetRoomVoteResult.class
│  │                 │  │  │  ├─ MissionResultDto$MissionResultDtoBuilder.class
│  │                 │  │  │  ├─ MissionResultDto.class
│  │                 │  │  │  ├─ MyVoteDto$MyVoteDtoBuilder.class
│  │                 │  │  │  ├─ MyVoteDto.class
│  │                 │  │  │  ├─ NewMissionDto$NewMissionDtoBuilder.class
│  │                 │  │  │  ├─ NewMissionDto.class
│  │                 │  │  │  ├─ OpenAiApiResponseDto$Choice$Message.class
│  │                 │  │  │  ├─ OpenAiApiResponseDto$Choice.class
│  │                 │  │  │  ├─ OpenAiApiResponseDto$Usage.class
│  │                 │  │  │  ├─ OpenAiApiResponseDto.class
│  │                 │  │  │  ├─ ParticipantDto$ParticipantDtoBuilder.class
│  │                 │  │  │  ├─ ParticipantDto.class
│  │                 │  │  │  ├─ ParticipantIdDto$ParticipantIdDtoBuilder.class
│  │                 │  │  │  ├─ ParticipantIdDto.class
│  │                 │  │  │  ├─ ParticipantReceiveDto$ParticipantReceiveDtoBuilder.class
│  │                 │  │  │  ├─ ParticipantReceiveDto.class
│  │                 │  │  │  ├─ ParticipantResponseDto$ParticipantResponseDtoBuilder.class
│  │                 │  │  │  ├─ ParticipantResponseDto.class
│  │                 │  │  │  ├─ ParticipantWithFollowDto$ParticipantWithFollowDtoBuilder.class
│  │                 │  │  │  ├─ ParticipantWithFollowDto.class
│  │                 │  │  │  ├─ RandomNameDto.class
│  │                 │  │  │  ├─ RoomStartReceiveDto.class
│  │                 │  │  │  ├─ UpdateParticipantDto$UpdateParticipantDtoBuilder.class
│  │                 │  │  │  ├─ UpdateParticipantDto.class
│  │                 │  │  │  ├─ UserEndGameDto$UserEndGameDtoBuilder.class
│  │                 │  │  │  ├─ UserEndGameDto.class
│  │                 │  │  │  ├─ UserGameRateResultDto$UserGameRateResultDtoBuilder.class
│  │                 │  │  │  ├─ UserGameRateResultDto.class
│  │                 │  │  │  ├─ VoteReceiveDto.class
│  │                 │  │  │  ├─ VoteResponseDto$VoteResponseDtoBuilder.class
│  │                 │  │  │  ├─ VoteResponseDto.class
│  │                 │  │  │  ├─ VoteResultDto$VoteResultDtoBuilder.class
│  │                 │  │  │  └─ VoteResultDto.class
│  │                 │  │  ├─ mapper
│  │                 │  │  │  └─ GameMapper.class
│  │                 │  │  └─ service
│  │                 │  │     ├─ GameService.class
│  │                 │  │     └─ GameServiceImpl.class
│  │                 │  └─ util
│  │                 │     └─ GameUtil.class
│  │                 ├─ handler
│  │                 │  ├─ BlueGreenExceptionHandler.class
│  │                 │  └─ LocalExceptionHandler.class
│  │                 ├─ interceptor
│  │                 │  └─ UserInterceptor.class
│  │                 ├─ message
│  │                 │  ├─ controller
│  │                 │  │  └─ MessageController.class
│  │                 │  ├─ interceptor
│  │                 │  │  └─ FilterChannelInterceptor.class
│  │                 │  ├─ model
│  │                 │  │  ├─ dto
│  │                 │  │  │  ├─ LastMessageUpdateDto$LastMessageUpdateDtoBuilder.class
│  │                 │  │  │  ├─ LastMessageUpdateDto.class
│  │                 │  │  │  ├─ LastMessageUpdateRequestDto$LastMessageUpdateRequestDtoBuilder.class
│  │                 │  │  │  ├─ LastMessageUpdateRequestDto.class
│  │                 │  │  │  ├─ MessageIdDto$MessageIdDtoBuilder.class
│  │                 │  │  │  ├─ MessageIdDto.class
│  │                 │  │  │  ├─ MessageImageDto$MessageImageDtoBuilder.class
│  │                 │  │  │  ├─ MessageImageDto.class
│  │                 │  │  │  ├─ MessageRequestDto$MessageRequestDtoBuilder.class
│  │                 │  │  │  ├─ MessageRequestDto.class
│  │                 │  │  │  ├─ MessageResponseDto$MessageResponseDtoBuilder.class
│  │                 │  │  │  ├─ MessageResponseDto.class
│  │                 │  │  │  ├─ MessageScrollResponseDto$MessageScrollResponseDtoBuilder.class
│  │                 │  │  │  ├─ MessageScrollResponseDto.class
│  │                 │  │  │  ├─ StompPrincipalDto$StompPrincipalDtoBuilder.class
│  │                 │  │  │  └─ StompPrincipalDto.class
│  │                 │  │  ├─ entity
│  │                 │  │  │  ├─ MessageEntity$MessageEntityBuilder.class
│  │                 │  │  │  ├─ MessageEntity.class
│  │                 │  │  │  ├─ MessageImageEntity$MessageImageEntityBuilder.class
│  │                 │  │  │  └─ MessageImageEntity.class
│  │                 │  │  ├─ mapper
│  │                 │  │  │  └─ MessageMapper.class
│  │                 │  │  ├─ repo
│  │                 │  │  │  └─ MessageRepository.class
│  │                 │  │  └─ service
│  │                 │  │     ├─ MessageService.class
│  │                 │  │     └─ MessageServiceImpl.class
│  │                 │  └─ util
│  │                 │     └─ DateTimeUtil.class
│  │                 ├─ notification
│  │                 │  ├─ controller
│  │                 │  │  └─ NotificationController.class
│  │                 │  └─ model
│  │                 │     ├─ dto
│  │                 │     │  ├─ ArticleInfoDto$ArticleInfoDtoBuilder.class
│  │                 │     │  ├─ ArticleInfoDto.class
│  │                 │     │  ├─ CommentInfoDto$CommentInfoDtoBuilder.class
│  │                 │     │  ├─ CommentInfoDto.class
│  │                 │     │  ├─ NotificationIdDto$NotificationIdDtoBuilder.class
│  │                 │     │  ├─ NotificationIdDto.class
│  │                 │     │  ├─ NotificationResponseDto$NotificationResponseDtoBuilder.class
│  │                 │     │  ├─ NotificationResponseDto.class
│  │                 │     │  ├─ NotificationUserIdDto$NotificationUserIdDtoBuilder.class
│  │                 │     │  └─ NotificationUserIdDto.class
│  │                 │     ├─ entity
│  │                 │     │  ├─ NotificationEntity$NotificationEntityBuilder.class
│  │                 │     │  └─ NotificationEntity.class
│  │                 │     ├─ mapper
│  │                 │     │  └─ NotificationMapper.class
│  │                 │     ├─ repo
│  │                 │     │  └─ NotificationRepository.class
│  │                 │     └─ service
│  │                 │        ├─ NotificationService.class
│  │                 │        └─ NotificationServiceImpl.class
│  │                 ├─ scheduler
│  │                 │  ├─ controller
│  │                 │  │  └─ SchedulerController.class
│  │                 │  └─ model
│  │                 │     ├─ dto
│  │                 │     │  ├─ JudgeResultDto$JudgeResultDtoBuilder.class
│  │                 │     │  ├─ JudgeResultDto.class
│  │                 │     │  ├─ RandomMafiaDto$RandomMafiaDtoBuilder.class
│  │                 │     │  ├─ RandomMafiaDto.class
│  │                 │     │  ├─ RoomIdTitleDto$RoomIdTitleDtoBuilder.class
│  │                 │     │  ├─ RoomIdTitleDto.class
│  │                 │     │  ├─ UpdateWinFlagDto$UpdateWinFlagDtoBuilder.class
│  │                 │     │  └─ UpdateWinFlagDto.class
│  │                 │     ├─ mapper
│  │                 │     │  └─ SchedulerMapper.class
│  │                 │     └─ service
│  │                 │        ├─ SchedulerService.class
│  │                 │        └─ SchedulerServiceImpl.class
│  │                 ├─ test
│  │                 │  ├─ controller
│  │                 │  │  └─ TestController.class
│  │                 │  └─ model
│  │                 │     └─ service
│  │                 │        ├─ TestService.class
│  │                 │        └─ TestServiceImpl.class
│  │                 ├─ user
│  │                 │  ├─ controller
│  │                 │  │  └─ UserController.class
│  │                 │  ├─ exception
│  │                 │  │  ├─ FollowException.class
│  │                 │  │  ├─ IdentifierException.class
│  │                 │  │  ├─ PasswordNotMatchException.class
│  │                 │  │  ├─ TokenExpiredException.class
│  │                 │  │  ├─ TokenInvalidException.class
│  │                 │  │  ├─ UserDuplicateException.class
│  │                 │  │  ├─ UserNotFoundException.class
│  │                 │  │  └─ UserUnauthorizedException.class
│  │                 │  ├─ model
│  │                 │  │  ├─ dto
│  │                 │  │  │  ├─ AccessTokenDto$AccessTokenDtoBuilder.class
│  │                 │  │  │  ├─ AccessTokenDto.class
│  │                 │  │  │  ├─ BioDto$BioDtoBuilder.class
│  │                 │  │  │  ├─ BioDto.class
│  │                 │  │  │  ├─ EmailDto$EmailDtoBuilder.class
│  │                 │  │  │  ├─ EmailDto.class
│  │                 │  │  │  ├─ FollowDetailDto$FollowDetailDtoBuilder.class
│  │                 │  │  │  ├─ FollowDetailDto.class
│  │                 │  │  │  ├─ FollowDto$FollowDtoBuilder.class
│  │                 │  │  │  ├─ FollowDto.class
│  │                 │  │  │  ├─ FollowFindDto.class
│  │                 │  │  │  ├─ FollowParamDto$FollowParamDtoBuilder.class
│  │                 │  │  │  ├─ FollowParamDto.class
│  │                 │  │  │  ├─ FollowResponseDto$FollowResponseDtoBuilder.class
│  │                 │  │  │  ├─ FollowResponseDto.class
│  │                 │  │  │  ├─ FollowUserDto$FollowUserDtoBuilder.class
│  │                 │  │  │  ├─ FollowUserDto.class
│  │                 │  │  │  ├─ GetBioDto$GetBioDtoBuilder.class
│  │                 │  │  │  ├─ GetBioDto.class
│  │                 │  │  │  ├─ IdentifierDto$IdentifierDtoBuilder.class
│  │                 │  │  │  ├─ IdentifierDto.class
│  │                 │  │  │  ├─ NicknameDto$NicknameDtoBuilder.class
│  │                 │  │  │  ├─ NicknameDto.class
│  │                 │  │  │  ├─ Oauth2Dto$Oauth2DtoBuilder.class
│  │                 │  │  │  ├─ Oauth2Dto.class
│  │                 │  │  │  ├─ ProfileImageDto$ProfileImageDtoBuilder.class
│  │                 │  │  │  ├─ ProfileImageDto.class
│  │                 │  │  │  ├─ ResetPasswordDto$ResetPasswordDtoBuilder.class
│  │                 │  │  │  ├─ ResetPasswordDto.class
│  │                 │  │  │  ├─ SearchedUserDto$SearchedUserDtoBuilder.class
│  │                 │  │  │  ├─ SearchedUserDto.class
│  │                 │  │  │  ├─ SearchUsersParamDto$SearchUsersParamDtoBuilder.class
│  │                 │  │  │  ├─ SearchUsersParamDto.class
│  │                 │  │  │  ├─ SearchUsersResponseDto$SearchUsersResponseDtoBuilder.class
│  │                 │  │  │  ├─ SearchUsersResponseDto.class
│  │                 │  │  │  ├─ SignInDto$SignInDtoBuilder.class
│  │                 │  │  │  ├─ SignInDto.class
│  │                 │  │  │  ├─ SignUpDto$SignUpDtoBuilder.class
│  │                 │  │  │  ├─ SignUpDto.class
│  │                 │  │  │  ├─ TargetUserInfoDto$TargetUserInfoDtoBuilder.class
│  │                 │  │  │  ├─ TargetUserInfoDto.class
│  │                 │  │  │  ├─ TokenDto$TokenDtoBuilder.class
│  │                 │  │  │  ├─ TokenDto.class
│  │                 │  │  │  ├─ UpdatePasswordDto$UpdatePasswordDtoBuilder.class
│  │                 │  │  │  ├─ UpdatePasswordDto.class
│  │                 │  │  │  ├─ UserDto$UserDtoBuilder.class
│  │                 │  │  │  ├─ UserDto.class
│  │                 │  │  │  ├─ UserInfoDto$UserInfoDtoBuilder.class
│  │                 │  │  │  └─ UserInfoDto.class
│  │                 │  │  ├─ entity
│  │                 │  │  │  ├─ RefreshTokenEntity$RefreshTokenEntityBuilder.class
│  │                 │  │  │  └─ RefreshTokenEntity.class
│  │                 │  │  ├─ mapper
│  │                 │  │  │  └─ UserMapper.class
│  │                 │  │  ├─ repo
│  │                 │  │  │  └─ RefreshTokenRepository.class
│  │                 │  │  └─ service
│  │                 │  │     ├─ UserService.class
│  │                 │  │     └─ UserServiceImpl.class
│  │                 │  └─ util
│  │                 │     ├─ EmailUtil.class
│  │                 │     ├─ GoogleUtil.class
│  │                 │     ├─ JwtUtil.class
│  │                 │     ├─ KakaoUtil.class
│  │                 │     ├─ NaverUtil.class
│  │                 │     ├─ Oauth2Util.class
│  │                 │     └─ PasswordUtil.class
│  │                 └─ webpush
│  │                    ├─ controller
│  │                    │  └─ WebPushController.class
│  │                    └─ model
│  │                       ├─ dto
│  │                       │  ├─ FCMTokenDto$FCMTokenDtoBuilder.class
│  │                       │  ├─ FCMTokenDto.class
│  │                       │  ├─ FCMTokenOnlyDto$FCMTokenOnlyDtoBuilder.class
│  │                       │  ├─ FCMTokenOnlyDto.class
│  │                       │  ├─ WebPushDto$WebPushDtoBuilder.class
│  │                       │  └─ WebPushDto.class
│  │                       ├─ mapper
│  │                       │  └─ WebPushMapper.class
│  │                       └─ service
│  │                          ├─ WebPushService.class
│  │                          └─ WebPushServiceImpl.class
│  ├─ generated
│  │  └─ sources
│  │     ├─ annotationProcessor
│  │     │  └─ java
│  │     │     └─ main
│  │     └─ headers
│  │        └─ java
│  │           └─ main
│  ├─ resources
│  │  └─ main
│  │     ├─ application-secret.yml
│  │     ├─ application.yml
│  │     ├─ mapper
│  │     │  ├─ article.xml
│  │     │  ├─ chat.xml
│  │     │  ├─ game.xml
│  │     │  ├─ message.xml
│  │     │  ├─ notification.xml
│  │     │  ├─ scheduler.xml
│  │     │  ├─ user.xml
│  │     │  └─ webpush.xml
│  │     └─ service-account.json
│  └─ tmp
│     └─ compileJava
│        ├─ compileTransaction
│        │  ├─ backup-dir
│        │  └─ stash-dir
│        │     └─ GameServiceImpl.class.uniqueId0
│        └─ previous-compilation-data.bin
├─ build.gradle
├─ Dockerfile
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ ssafy
   │  │        └─ msg
   │  │           ├─ aop
   │  │           │  └─ LogAspect.java
   │  │           ├─ article
   │  │           │  ├─ controller
   │  │           │  │  └─ ArticleController.java
   │  │           │  ├─ model
   │  │           │  │  ├─ dto
   │  │           │  │  │  ├─ ArticleByRoomIdDto.java
   │  │           │  │  │  ├─ ArticleCreateDto.java
   │  │           │  │  │  ├─ ArticleDetailDto.java
   │  │           │  │  │  ├─ ArticleDto.java
   │  │           │  │  │  ├─ ArticleImageDto.java
   │  │           │  │  │  ├─ ArticleLikeDto.java
   │  │           │  │  │  ├─ ArticleReportDto.java
   │  │           │  │  │  ├─ ArticleWithUrlDto.java
   │  │           │  │  │  ├─ CommentCreateDto.java
   │  │           │  │  │  ├─ CommentDto.java
   │  │           │  │  │  ├─ CommentLikeDto.java
   │  │           │  │  │  ├─ CommentLikeUserListDto.java
   │  │           │  │  │  ├─ CreateCommentDto.java
   │  │           │  │  │  ├─ DeleteArticleDto.java
   │  │           │  │  │  ├─ DeleteCommentDto.java
   │  │           │  │  │  ├─ FeedParamDto.java
   │  │           │  │  │  ├─ FeedResponseDto.java
   │  │           │  │  │  ├─ GuestArticleResponseDto.java
   │  │           │  │  │  ├─ GuestArticleResultDto.java
   │  │           │  │  │  ├─ GusetFeedResponseDto.java
   │  │           │  │  │  ├─ LikeUserListDto.java
   │  │           │  │  │  ├─ RoomArticleResponseDto.java
   │  │           │  │  │  ├─ RoomArticleResultDto.java
   │  │           │  │  │  ├─ RoomFeedResponseDto.java
   │  │           │  │  │  ├─ UpdateArticleDto.java
   │  │           │  │  │  └─ UpdateCommentDto.java
   │  │           │  │  ├─ mapper
   │  │           │  │  │  └─ ArticleMapper.java
   │  │           │  │  └─ service
   │  │           │  │     ├─ ArticleService.java
   │  │           │  │     └─ ArticleServiceImpl.java
   │  │           │  └─ util
   │  │           │     ├─ OpenAiUtil.java
   │  │           │     └─ S3Util.java
   │  │           ├─ BackendApplication.java
   │  │           ├─ chat
   │  │           │  ├─ controller
   │  │           │  │  └─ ChatController.java
   │  │           │  └─ model
   │  │           │     ├─ dto
   │  │           │     │  ├─ CreateRoomDto.java
   │  │           │     │  ├─ OpponentDto.java
   │  │           │     │  ├─ RoomDto.java
   │  │           │     │  └─ RoomResponseDto.java
   │  │           │     ├─ mapper
   │  │           │     │  └─ ChatMapper.java
   │  │           │     └─ service
   │  │           │        ├─ ChatService.java
   │  │           │        └─ ChatServiceImpl.java
   │  │           ├─ config
   │  │           │  ├─ AsyncConfig.java
   │  │           │  ├─ DatabaseConfig.java
   │  │           │  ├─ FCMConfig.java
   │  │           │  ├─ JasyptConfig.java
   │  │           │  ├─ MessageConfig.java
   │  │           │  ├─ RedisConfig.java
   │  │           │  ├─ S3Config.java
   │  │           │  ├─ SecurityConfig.java
   │  │           │  ├─ SwaggerConfig.java
   │  │           │  └─ WebConfig.java
   │  │           ├─ game
   │  │           │  ├─ controller
   │  │           │  │  └─ GameController.java
   │  │           │  ├─ exception
   │  │           │  │  ├─ GroupRoomDuplicateException.java
   │  │           │  │  ├─ GroupRoomFullException.java
   │  │           │  │  └─ GroupRoomNotFoundException.java
   │  │           │  ├─ model
   │  │           │  │  ├─ dto
   │  │           │  │  │  ├─ AbilityTargetResponseDto.java
   │  │           │  │  │  ├─ AiResultDto.java
   │  │           │  │  │  ├─ AliveParticipantDto.java
   │  │           │  │  │  ├─ ApplyGroupRoomDto.java
   │  │           │  │  │  ├─ CompleteMissionDto.java
   │  │           │  │  │  ├─ CreateMsgArticleDto.java
   │  │           │  │  │  ├─ EnterGroupRoomDto.java
   │  │           │  │  │  ├─ GetAbilityParamDto.java
   │  │           │  │  │  ├─ GetAbilityResultDto.java
   │  │           │  │  │  ├─ GetRoomVoteResult.java
   │  │           │  │  │  ├─ MissionResultDto.java
   │  │           │  │  │  ├─ MyVoteDto.java
   │  │           │  │  │  ├─ NewMissionDto.java
   │  │           │  │  │  ├─ OpenAiApiResponseDto.java
   │  │           │  │  │  ├─ ParticipantDto.java
   │  │           │  │  │  ├─ ParticipantIdDto.java
   │  │           │  │  │  ├─ ParticipantReceiveDto.java
   │  │           │  │  │  ├─ ParticipantResponseDto.java
   │  │           │  │  │  ├─ ParticipantWithFollowDto.java
   │  │           │  │  │  ├─ RandomNameDto.java
   │  │           │  │  │  ├─ RoomStartReceiveDto.java
   │  │           │  │  │  ├─ UpdateParticipantDto.java
   │  │           │  │  │  ├─ UserEndGameDto.java
   │  │           │  │  │  ├─ UserGameRateResultDto.java
   │  │           │  │  │  ├─ VoteReceiveDto.java
   │  │           │  │  │  ├─ VoteResponseDto.java
   │  │           │  │  │  └─ VoteResultDto.java
   │  │           │  │  ├─ mapper
   │  │           │  │  │  └─ GameMapper.java
   │  │           │  │  └─ service
   │  │           │  │     ├─ GameService.java
   │  │           │  │     └─ GameServiceImpl.java
   │  │           │  └─ util
   │  │           │     └─ GameUtil.java
   │  │           ├─ handler
   │  │           │  ├─ BlueGreenExceptionHandler.java
   │  │           │  └─ LocalExceptionHandler.java
   │  │           ├─ interceptor
   │  │           │  └─ UserInterceptor.java
   │  │           ├─ message
   │  │           │  ├─ controller
   │  │           │  │  └─ MessageController.java
   │  │           │  ├─ interceptor
   │  │           │  │  └─ FilterChannelInterceptor.java
   │  │           │  ├─ model
   │  │           │  │  ├─ dto
   │  │           │  │  │  ├─ LastMessageUpdateDto.java
   │  │           │  │  │  ├─ LastMessageUpdateRequestDto.java
   │  │           │  │  │  ├─ MessageIdDto.java
   │  │           │  │  │  ├─ MessageImageDto.java
   │  │           │  │  │  ├─ MessageRequestDto.java
   │  │           │  │  │  ├─ MessageResponseDto.java
   │  │           │  │  │  ├─ MessageScrollResponseDto.java
   │  │           │  │  │  └─ StompPrincipalDto.java
   │  │           │  │  ├─ entity
   │  │           │  │  │  ├─ MessageEntity.java
   │  │           │  │  │  └─ MessageImageEntity.java
   │  │           │  │  ├─ mapper
   │  │           │  │  │  └─ MessageMapper.java
   │  │           │  │  ├─ repo
   │  │           │  │  │  └─ MessageRepository.java
   │  │           │  │  └─ service
   │  │           │  │     ├─ MessageService.java
   │  │           │  │     └─ MessageServiceImpl.java
   │  │           │  └─ util
   │  │           │     └─ DateTimeUtil.java
   │  │           ├─ notification
   │  │           │  ├─ controller
   │  │           │  │  └─ NotificationController.java
   │  │           │  └─ model
   │  │           │     ├─ dto
   │  │           │     │  ├─ ArticleInfoDto.java
   │  │           │     │  ├─ CommentInfoDto.java
   │  │           │     │  ├─ NotificationIdDto.java
   │  │           │     │  ├─ NotificationResponseDto.java
   │  │           │     │  └─ NotificationUserIdDto.java
   │  │           │     ├─ entity
   │  │           │     │  └─ NotificationEntity.java
   │  │           │     ├─ mapper
   │  │           │     │  └─ NotificationMapper.java
   │  │           │     ├─ repo
   │  │           │     │  └─ NotificationRepository.java
   │  │           │     └─ service
   │  │           │        ├─ NotificationService.java
   │  │           │        └─ NotificationServiceImpl.java
   │  │           ├─ scheduler
   │  │           │  ├─ controller
   │  │           │  │  └─ SchedulerController.java
   │  │           │  └─ model
   │  │           │     ├─ dto
   │  │           │     │  ├─ JudgeResultDto.java
   │  │           │     │  ├─ RandomMafiaDto.java
   │  │           │     │  ├─ RoomIdTitleDto.java
   │  │           │     │  └─ UpdateWinFlagDto.java
   │  │           │     ├─ mapper
   │  │           │     │  └─ SchedulerMapper.java
   │  │           │     └─ service
   │  │           │        ├─ SchedulerService.java
   │  │           │        └─ SchedulerServiceImpl.java
   │  │           ├─ test
   │  │           │  ├─ controller
   │  │           │  │  └─ TestController.java
   │  │           │  └─ model
   │  │           │     └─ service
   │  │           │        ├─ TestService.java
   │  │           │        └─ TestServiceImpl.java
   │  │           ├─ user
   │  │           │  ├─ controller
   │  │           │  │  └─ UserController.java
   │  │           │  ├─ exception
   │  │           │  │  ├─ FollowException.java
   │  │           │  │  ├─ IdentifierException.java
   │  │           │  │  ├─ PasswordNotMatchException.java
   │  │           │  │  ├─ TokenExpiredException.java
   │  │           │  │  ├─ TokenInvalidException.java
   │  │           │  │  ├─ UserDuplicateException.java
   │  │           │  │  ├─ UserNotFoundException.java
   │  │           │  │  └─ UserUnauthorizedException.java
   │  │           │  ├─ model
   │  │           │  │  ├─ dto
   │  │           │  │  │  ├─ AccessTokenDto.java
   │  │           │  │  │  ├─ BioDto.java
   │  │           │  │  │  ├─ EmailDto.java
   │  │           │  │  │  ├─ FollowDetailDto.java
   │  │           │  │  │  ├─ FollowDto.java
   │  │           │  │  │  ├─ FollowFindDto.java
   │  │           │  │  │  ├─ FollowParamDto.java
   │  │           │  │  │  ├─ FollowResponseDto.java
   │  │           │  │  │  ├─ FollowUserDto.java
   │  │           │  │  │  ├─ GetBioDto.java
   │  │           │  │  │  ├─ IdentifierDto.java
   │  │           │  │  │  ├─ NicknameDto.java
   │  │           │  │  │  ├─ Oauth2Dto.java
   │  │           │  │  │  ├─ ProfileImageDto.java
   │  │           │  │  │  ├─ ResetPasswordDto.java
   │  │           │  │  │  ├─ SearchedUserDto.java
   │  │           │  │  │  ├─ SearchUsersParamDto.java
   │  │           │  │  │  ├─ SearchUsersResponseDto.java
   │  │           │  │  │  ├─ SignInDto.java
   │  │           │  │  │  ├─ SignUpDto.java
   │  │           │  │  │  ├─ TargetUserInfoDto.java
   │  │           │  │  │  ├─ TokenDto.java
   │  │           │  │  │  ├─ UpdatePasswordDto.java
   │  │           │  │  │  ├─ UserDto.java
   │  │           │  │  │  └─ UserInfoDto.java
   │  │           │  │  ├─ entity
   │  │           │  │  │  └─ RefreshTokenEntity.java
   │  │           │  │  ├─ mapper
   │  │           │  │  │  └─ UserMapper.java
   │  │           │  │  ├─ repo
   │  │           │  │  │  └─ RefreshTokenRepository.java
   │  │           │  │  └─ service
   │  │           │  │     ├─ UserService.java
   │  │           │  │     └─ UserServiceImpl.java
   │  │           │  └─ util
   │  │           │     ├─ EmailUtil.java
   │  │           │     ├─ GoogleUtil.java
   │  │           │     ├─ JwtUtil.java
   │  │           │     ├─ KakaoUtil.java
   │  │           │     ├─ NaverUtil.java
   │  │           │     ├─ Oauth2Util.java
   │  │           │     └─ PasswordUtil.java
   │  │           └─ webpush
   │  │              ├─ controller
   │  │              │  └─ WebPushController.java
   │  │              └─ model
   │  │                 ├─ dto
   │  │                 │  ├─ FCMTokenDto.java
   │  │                 │  ├─ FCMTokenOnlyDto.java
   │  │                 │  └─ WebPushDto.java
   │  │                 ├─ mapper
   │  │                 │  └─ WebPushMapper.java
   │  │                 └─ service
   │  │                    ├─ WebPushService.java
   │  │                    └─ WebPushServiceImpl.java
   │  └─ resources
   │     ├─ application-secret.yml
   │     ├─ application.yml
   │     ├─ mapper
   │     │  ├─ article.xml
   │     │  ├─ chat.xml
   │     │  ├─ game.xml
   │     │  ├─ message.xml
   │     │  ├─ notification.xml
   │     │  ├─ scheduler.xml
   │     │  ├─ user.xml
   │     │  └─ webpush.xml
   │     └─ service-account.json
   └─ test
      └─ java
         └─ com
            └─ ssafy
               └─ msg
                  └─ test
                     └─ controller
                        └─ TestControllerTest.java

```