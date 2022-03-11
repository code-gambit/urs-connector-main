package com.urlshortner.urlshortner.Controller;

import com.urlshortner.urlshortner.Model.OperationResult;
import com.urlshortner.urlshortner.Service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "test")
public class CounterController {

    @Autowired
    CounterService counterService;

    @GetMapping("")
    String test() {
        OperationResult<Void, String> result =
                counterService.insertCounterRange(1, 5);
        if (result.isFailed()) {
            return result.getFailureData();
        }
        return "Success";
    }

    @GetMapping("get")
    String getCounterValue() {
        OperationResult<Long, String> result = counterService.getCounterValue();
        if(result.isFailed()) {
            return result.getFailureData();
        } else if (result.isRangeExhausted()) {
            return "Reset: " + resetCounterValue();
        }
        return "Counter Value: " + result.getSuccessData();
    }

    @GetMapping("reset")
    String resetCounterValue() {
        OperationResult<Void, String> result = counterService.resetCounter(6, 10);
        if (result.isFailed()) {
            return result.getFailureData();
        }
        return "Success";
    }

}
