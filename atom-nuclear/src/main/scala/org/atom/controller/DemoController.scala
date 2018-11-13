package org.atom.controller

import org.atom.service.DemoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("/demo"))
class DemoController @Autowired()(
                                   val demoService: DemoService
                                 ) {

  @GetMapping(value = {Array("/listHttpSuiteTestCaseJson")})
  @ResponseBody
  def demoFunc(@RequestBody httpSuiteId: Integer) = {
    demoService.demoFunc
  }
}
