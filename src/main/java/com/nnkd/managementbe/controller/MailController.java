package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.MailSendRequest;
import com.nnkd.managementbe.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mails")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {
    MailService mailService;

    @PostMapping
    ApiResponse sendMail(@RequestBody MailSendRequest request) {
         return mailService.sendMail(request);
    }
}
