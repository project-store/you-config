package miao.you.meng.config.dashboard.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Controller
 */
public abstract class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected final String WEB_ROOT_DIR = System.getProperty("web.root.dir");

}
