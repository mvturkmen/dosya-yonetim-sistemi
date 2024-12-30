package com.mehmetvasfi.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mehmetvasfi.controller.IRequestController;
import com.mehmetvasfi.service.impl.RequestService;

import java.util.Map;

@RestController
@RequestMapping("/rest/api/request")
public class RequestController implements IRequestController {

    @Autowired
    private RequestService requestService;

    @Override
    @PostMapping(path = "/delete")
    public boolean refuseRequest(@RequestBody Map<String, Object> payload) {
        Integer id = (Integer) payload.get("id");
        if (id == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
        return requestService.refuseRequest(id);
    }

    @Override
    @PostMapping(path = "/accept")
    public boolean acceptRequest(@RequestBody Map<String, Object> payload) {
        Integer id = (Integer) payload.get("id");
        if (id == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
        return requestService.acceptRequest(id);
    }
}
