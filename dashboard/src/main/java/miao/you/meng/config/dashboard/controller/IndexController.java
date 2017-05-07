package miao.you.meng.config.dashboard.controller;

import miao.you.meng.config.service.IAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by miaoyoumeng on 2017/3/25.
 */

@Controller
@RequestMapping("/maintain")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private IAppService appService;

    @RequestMapping("index")
    public String index(){
        logger.info("demo request ...", this.appService.countApp());
        return "/main/index";
    }
}
