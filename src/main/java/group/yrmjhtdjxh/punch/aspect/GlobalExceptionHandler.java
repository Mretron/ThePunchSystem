package group.yrmjhtdjxh.punch.aspect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author dengg
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static String EXCEPTION_MSG_KEY = "Params are not valid,tips: ";


    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<Integer,String> handleValidException(MethodArgumentNotValidException e){
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        Map<Integer, String> map = new HashMap<>(8);
        map.put(400,EXCEPTION_MSG_KEY+e.getBindingResult().getFieldError().getDefaultMessage());
        return map;
    }
}
