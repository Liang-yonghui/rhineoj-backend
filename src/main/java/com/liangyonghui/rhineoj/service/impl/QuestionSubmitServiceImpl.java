package com.liangyonghui.rhineoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liangyonghui.rhineoj.common.ErrorCode;
import com.liangyonghui.rhineoj.constant.CommonConstant;
import com.liangyonghui.rhineoj.exception.BusinessException;
import com.liangyonghui.rhineoj.judge.JudgeService;
import com.liangyonghui.rhineoj.mapper.QuestionSubmitMapper;
import com.liangyonghui.rhineoj.model.dto.question.QuestionQueryRequest;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangyonghui.rhineoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import com.liangyonghui.rhineoj.model.entity.User;
import com.liangyonghui.rhineoj.model.enums.QuestionSubmitLanguagEnum;
import com.liangyonghui.rhineoj.model.enums.QuestionSubmitStatusEnum;
import com.liangyonghui.rhineoj.model.enums.UserRoleEnum;
import com.liangyonghui.rhineoj.model.vo.QuestionSubmitVO;
import com.liangyonghui.rhineoj.model.vo.QuestionVO;
import com.liangyonghui.rhineoj.model.vo.UserVO;
import com.liangyonghui.rhineoj.service.QuestionService;
import com.liangyonghui.rhineoj.service.QuestionSubmitService;
import com.liangyonghui.rhineoj.service.UserService;
import com.liangyonghui.rhineoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 梁永辉
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-06-11 19:23:44
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //TODO 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguagEnum languagEnum = QuestionSubmitLanguagEnum.getEnumByValue(language);
        if (languagEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        //TODO 设置初始状态
        questionSubmit.setStatus(0);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // todo 执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        //questionSubmit = judgeService.doJudge(questionSubmitId);
        CompletableFuture.runAsync(()->{
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
        // 锁必须要包裹住事务方法
        //QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
        //synchronized (String.valueOf(userId).intern()) {
        //    return questionSubmitService.doQuestionSubmitInner(userId, questionId);
        //}
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已提交题目
        if (oldQuestionSubmit != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 提交题目数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未提交题目
            result = this.save(questionSubmit);
            if (result) {
                // 提交题目数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }


    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionQueryRequest.getLanguage();
        Integer status = questionQueryRequest.getStatus();
        Long questionId = questionQueryRequest.getQuestionId();
        Long userId = questionQueryRequest.getUserId();
        int current = questionQueryRequest.getCurrent();
        int pageSize = questionQueryRequest.getPageSize();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();



        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit question, User loginUser) {
        QuestionSubmitVO questionVO = QuestionSubmitVO.objToVo(question);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和 登录用户 id不同）提交代码的答案，提交代码
        //User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        Long userId = loginUser.getId();
        // 处理脱敏
        if (userId != question.getUserId() && !userService.isAdmin(loginUser)){
            questionVO.setCode(null);
        }
        return questionVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
    
}




