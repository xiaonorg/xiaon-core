package studio.xiaoyun.core.service;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.dao.FeedbackDao;
import studio.xiaoyun.core.pojo.FeedbackDO;
import studio.xiaoyun.core.exception.InvalidParameterException;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

	@Resource
	private FeedbackDao feedbackDao;
	
	@Override
	public String saveFeedback(String title, String text) {
		if(text==null || text.trim().length()==0){
			throw new InvalidParameterException("内容不能为空!");
		}
		if(title==null || title.trim().length()==0){
			title = "网友";
		}
		FeedbackDO feedback = new FeedbackDO();
		feedback.setText(text.trim());
		feedback.setCreateDate(LocalDateTime.now());
		String id = feedbackDao.save(feedback);
        return id;
	}

}
