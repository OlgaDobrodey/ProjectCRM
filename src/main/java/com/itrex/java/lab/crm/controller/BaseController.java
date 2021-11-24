package com.itrex.java.lab.crm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@RequestMapping("/crm")
@SessionAttributes("userActiv")
public class BaseController {

}
