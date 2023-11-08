package com.example.cliphelper.domain.article.controller;

import com.example.cliphelper.domain.article.dto.ArticleModifyRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;
import com.example.cliphelper.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping(value = "/articles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResultResponse registerArticle(@ModelAttribute ArticleRequestDto articleRequestDto) {
        if (articleRequestDto.getTitle().isBlank()) {
            throw new BusinessException(ErrorCode.ARTICLE_TITLE_IS_BLANK);
        }

        articleService.createArticle(articleRequestDto);
        return ResultResponse.of(ResultCode.ARTICLE_CREATE_SUCCESS);
    }

    //======================

    // 관리자 전용 API
    @GetMapping("/admin/articles")
    public ResultResponse findAllArticles() {
        List<ArticleResponseDto> articleResponseDtos = articleService.findAllArticles();
        return ResultResponse.of(ResultCode.ALL_ARTICLES_FIND_SUCCESS, articleResponseDtos);
    }

    @GetMapping("/articles/{articleId}")
    public ResultResponse findArticle(@PathVariable("articleId") Long articleId) {
        ArticleResponseDto articleResponseDto = articleService.findArticle(articleId);
        return ResultResponse.of(ResultCode.ARTICLE_FIND_SUCCESS, articleResponseDto);
    }

    @GetMapping("/articles")
    public ResultResponse findMyArticles() {
        List<ArticleResponseDto> articleResponseDtos = articleService.findMyArticles();
        return ResultResponse.of(ResultCode.ALL_ARTICLES_FIND_SUCCESS, articleResponseDtos);
    }

    @PatchMapping("/articles/{articleId}")
    public ResultResponse modifyArticle(@PathVariable("articleId") Long articleId, @RequestBody ArticleModifyRequestDto articleModifyRequestDto) {
        articleService.modifyArticle(articleId, articleModifyRequestDto);
        return ResultResponse.of(ResultCode.ARTICLE_MODIFY_SUCCESS);
    }

    // 특정 아티클이 속해 있는 컬렉션 편집
    @PatchMapping("/articles/{articleId}/collections")
    public ResultResponse modifyArticleListOfCollection(@PathVariable("articleId") Long articleId, @RequestBody List<Long> collectionIdList) {
        articleService.modifyArticleListOfCollection(articleId, collectionIdList);
        return ResultResponse.of(ResultCode.ARTICLE_MODIFY_SUCCESS);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResultResponse deleteArticle(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return ResultResponse.of(ResultCode.ARTICLE_DELETE_SUCCESS);
    }
}
