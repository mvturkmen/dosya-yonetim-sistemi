package com.mehmetvasfi.controller;

import java.util.Map;

public interface IRequestController {

    public boolean refuseRequest(Map<String, Object> payload);

    public boolean acceptRequest(Map<String, Object> payload);
}
