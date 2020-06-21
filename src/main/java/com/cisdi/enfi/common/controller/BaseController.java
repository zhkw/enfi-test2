package com.cisdi.enfi.common.controller;

import com.cisdi.enfi.common.utils.GlobalConfigHolder;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {
    protected ModelAndView createSingleView(String path) {
        ModelAndView view = new ModelAndView();
        view.addObject("basePath", GlobalConfigHolder.getProperty("basePath"));
        view.setViewName(path);
        return view;
    }

    protected ModelAndView createLayoutView(String path) {
        return createLayoutView(path, null);
    }

    protected ModelAndView createLayoutView(String path, String layout) {
        ModelAndView view = new ModelAndView();
        if (layout == null)
            view.setViewName("common/layout");
        else
            view.setViewName(layout);
        view.addObject("basePath", GlobalConfigHolder.getProperty("basePath"));
        view.addObject("resource", "common/resource.vm");
        view.addObject("header_path", "common/header.vm");
        view.addObject("footer_path", "common/footer.vm");
        view.addObject("content_path", path + ".vm");
        return view;
    }
}
