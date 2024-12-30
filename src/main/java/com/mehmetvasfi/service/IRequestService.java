package com.mehmetvasfi.service;

import java.util.List;

import com.mehmetvasfi.model.Request;

public interface IRequestService {

    public boolean RequestPasswordCahnge(String username, String newPasword);

    public List<Request> getPendingRequests(boolean approved);

    public boolean refuseRequest(Integer id);

    public boolean acceptRequest(Integer id);
}
