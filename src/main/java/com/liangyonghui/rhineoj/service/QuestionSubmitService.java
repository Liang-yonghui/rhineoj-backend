package com.liangyonghui.rhineoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liangyonghui.rhineoj.model.dto.question.QuestionQueryRequest;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import com.liangyonghui.rhineoj.model.entity.User;
import com.liangyonghui.rhineoj.model.vo.QuestionSubmitVO;
import com.liangyonghui.rhineoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 梁永辉
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-06-11 19:23:44
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionSubmitInner(long userId, long questionId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取帖子封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

}
