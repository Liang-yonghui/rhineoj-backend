package com.liangyonghui.rhineoj.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liangyonghui.rhineoj.common.BaseResponse;
import com.liangyonghui.rhineoj.common.ErrorCode;
import com.liangyonghui.rhineoj.common.ResultUtils;
import com.liangyonghui.rhineoj.exception.BusinessException;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import com.liangyonghui.rhineoj.model.entity.User;
import com.liangyonghui.rhineoj.model.vo.QuestionSubmitVO;
import com.liangyonghui.rhineoj.service.QuestionSubmitService;
import com.liangyonghui.rhineoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.device.BaseResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/liliangyonghui">程序员鱼皮</a>
 * @from <a href="https://liangyonghui.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    //@PostMapping("/")
    //public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
    //        HttpServletRequest request) {
    //    if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
    //        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    //    }
    //    // 登录才能点赞
    //    final User loginUser = userService.getLoginUser(request);
    //    long questionId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
    //    return ResultUtils.success(questionId);
    //}
    //
    //
    //public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmit(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
    //                                                               HttpServletRequest request) {
    //    int current = questionSubmitQueryRequest.getCurrent();
    //    int size = questionSubmitQueryRequest.getPageSize();
    //    Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
    //            questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
    //    User loginUser = userService.getLoginUser(request);
    //    return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    //}

}
