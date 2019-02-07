package com.romanov.application.controllers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest httpRequest) {
        String errorMessage = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMessage = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMessage = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 403: {
                errorMessage = "Http Error Code: 403. Forbidden";
                break;
            }
            case 404: {
                errorMessage = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMessage = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        logger.error("Something went wrong: {}", errorMessage);
        return "error";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
