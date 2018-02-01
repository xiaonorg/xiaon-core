package studio.xiaoyun.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import studio.xiaoyun.core.constant.Permission;
import studio.xiaoyun.core.dao.FeedbackDao;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.dto.FeedbackDTO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.FeedbackDO;
import studio.xiaoyun.core.query.FeedbackQuery;
import studio.xiaoyun.core.service.FeedbackService;
import studio.xiaoyun.security.annotation.RequirePermission;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.web.WebResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 意见反馈的控制器
 * @author 岳正灵
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/feedback")
public class FeedbackController {
	@Resource
	private FeedbackDao feedbackDao;
	@Resource
	private DTOConverter resourceUtil;
	@Resource
	private FeedbackService feedbackService;
	
	/**
	 * 创建一个意见反馈
	 * @param title 标题
	 * @param text 文字
	 * @return 新创建的意见反馈
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public FeedbackDTO saveFeedback(String title,String text){
		String id = feedbackService.saveFeedback(title, text);
		FeedbackDO feedback = feedbackDao.getById(id);
		FeedbackDTO resource = resourceUtil.toDto(feedback,null,FeedbackDTO.class);
		return resource;
	}
	
	/**
	 * 根据Id删除数据
	 * @param feedbackId 意见反馈的Id，json格式的数组
	 */
	@RequirePermission(Permission.FEEDBACK_DELETE_ALL)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public void deleteFeedback(String feedbackId){
		if(StringUtils.isBlank(feedbackId)){
			throw new InvalidParameterException("参数不能为空！");
		}
		List<String> list = JSON.parseArray(feedbackId,String.class);
		feedbackDao.deleteFeedback(list);
	}
	
	/**
	 * 根据ID获得意见反馈
	 * @param request HTTP请求
	 * @param feedbackId ID
	 * @return 意见反馈
	 * @throws InvalidParameterException 如果根据id没有找到，则抛出异常
	 */
	@RequestMapping(value = "/{Id:\\S{32}}", method = RequestMethod.GET)
	public FeedbackDTO getFeedbackByID(HttpServletRequest request, @PathVariable("Id") String feedbackId)throws InvalidParameterException{
		FeedbackQuery param = ParameterUtil.getParameter(request, FeedbackQuery.class);
		FeedbackDO feedback = feedbackDao.getById( feedbackId);
		FeedbackDTO resource = resourceUtil.toDto(feedback,param,FeedbackDTO.class);
		return resource;
	}
	
	/**
	 * 根据参数获得意见反馈的列表
	 * @param request http请求
	 * @return 意见反馈的列表
	 * @see FeedbackQuery 意见反馈的参数类
	 */
	@RequirePermission(Permission.FEEDBACK_GET_ALL)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public WebResult getFeedbackByParameter(HttpServletRequest request){
		FeedbackQuery param = ParameterUtil.getParameter(request, FeedbackQuery.class);
		long count = feedbackDao.countFeedbackByParameter(param);
		List<FeedbackDO> feedbackList = feedbackDao.listFeedbackByParameter(param);
		List<FeedbackDTO> resourceList = resourceUtil.toDto(feedbackList,param,FeedbackDTO.class);
		return new WebResult(count,resourceList);
	}

}
