package com.mylstech.product.service;

import com.mylstech.product.dto.request.ContactRequest;
import com.mylstech.product.dto.response.ContactResponse;

public interface ContactService {
    ContactResponse getContactInfo();
    ContactResponse updateContactInfo(ContactRequest request);
}