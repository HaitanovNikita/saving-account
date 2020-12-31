package com.camunda.savingsaccount;

import com.camunda.savingsaccount.process.model.InputData;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/process")
public class StartProcessRestController {
    @Autowired
    private ProcessEngine processEngine;

    @RequestMapping(value = "/start-prescoring", method = RequestMethod.POST)
    public void startProcess(@RequestBody InputData inputData){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("inputData", inputData);
        this.processEngine.getRuntimeService().startProcessInstanceByMessage("startSavingsMessage", variables);
        log.info("StartProcessRestController");
    }

}
