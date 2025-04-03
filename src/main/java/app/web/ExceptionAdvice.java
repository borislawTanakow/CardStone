package app.web;

import app.exception.BuyCardException;
import app.exception.EmailAlreadyExistException;
import app.exception.UsernameAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    // 1. (First) POST HTTP Request -> /register -> redirect:/register
    // 2. (Second) GET HTTP Request -> /register -> register.html view

    // ВАЖНО: При редирект не връщаме @ResponseStatus(...)!!!
    // 1
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExist(RedirectAttributes redirectAttributes, UsernameAlreadyExistException exception) {


        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("usernameAlreadyExistMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public String handleEmailAlreadyExist(RedirectAttributes redirectAttributes, EmailAlreadyExistException exception) {


        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("emailAlreadyExistMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(BuyCardException.class)
    public String handleBuyCardException(BuyCardException exception, RedirectAttributes redirectAttributes) {
        log.error("Error when purchasing card: {}", exception.getMessage(), exception);

        redirectAttributes.addFlashAttribute("buyCardErrorMessage", exception.getMessage());
        return "redirect:/shop";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundExceptions() {

        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;
    }
}