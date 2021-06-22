package org.platonos.fox;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/hello-world")
@RequestMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }
}
