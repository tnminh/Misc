package com.home.controller;

import com.home.request.PoolData;
import com.home.request.QuantileRequest;
import com.home.service.PoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    PoolService quantileService;

    @GetMapping("/")
    String home() {
        return "Hello World!";
    }

    @PostMapping("/pool")
    Object addPool(@Validated  @RequestBody PoolData data) {
        Integer result=quantileService.addValues(data.poolId, data.poolValues);
        if(result==1)
            return "inserted";
        else if(result==2)
            return "appended";
        return new ResponseEntity("failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/quantile")
    Integer[] getQuantile(@Validated @RequestBody QuantileRequest bd) {
        Integer[] quantile = quantileService.getQuantile(bd.poolId, bd.percentile);
        return quantile;
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity handleException(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }
}
