package spittr.web;

import org.springframework.web.bind.annotation.ExceptionHandler;

import spittr.config.annotation.WebControllerAdvice;

@WebControllerAdvice
public class AppWideExceptionHandler {

  @ExceptionHandler(DuplicateSpittleException.class)
  public String handleDuplicateSpittle() {
    return "error/duplicate";
  }
  
  @ExceptionHandler(SpittleNotFoundException.class)
  public String handleNotFound() {
    return "error/notFound";
  }

}
