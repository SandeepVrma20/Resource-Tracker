/**
 * 
 */
package com.nl.abnamro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author C33129
 *
 */
@Controller
public class WebController {

  @RequestMapping(value = "/home", method = RequestMethod.GET)
  public String homepage() {
    return "index";
  }
}
