package com.example.cliphelper.controller;

import com.example.cliphelper.dto.ArticleModifyRequestDto;
import com.example.cliphelper.dto.ArticleRequestDto;
import com.example.cliphelper.dto.ArticleResponseDto;
import com.example.cliphelper.result.ResultCode;
import com.example.cliphelper.result.ResultResponse;
import com.example.cliphelper.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResultResponse registerArticle(@Valid @RequestBody ArticleRequestDto articleRequestDto) {
        articleService.createArticle(articleRequestDto);
        return ResultResponse.of(ResultCode.ARTICLE_CREATE_SUCCESS);
    }

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
    public ResultResponse findMyArticles(@RequestParam("userId") Long userId) {
        List<ArticleResponseDto> articleResponseDtos = articleService.findMyArticles(userId);
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