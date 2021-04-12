package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.UserService;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.MasterData;
import com.ebizcipta.ajo.api.util.ServiceSchedulerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/apu/v1")
@Api(value="Public Api")
public class HelloWorldController {
    private final MasterData masterData;
    private final ServiceSchedulerUtil serviceSchedulerUtil;
    private final UserService userService;

    public HelloWorldController(MasterData masterData, ServiceSchedulerUtil serviceSchedulerUtil, UserService userService) {
        this.masterData = masterData;
        this.serviceSchedulerUtil = serviceSchedulerUtil;
        this.userService = userService;
    }

    @ApiOperation(value = "Hello World")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully return Hello World"),
            @ApiResponse(code = 401, message = "Unauthorized user"),
            @ApiResponse(code = 403, message = "Forbidden user"),
    })

    @GetMapping("/testing-only")
    @PreAuthorize("@hasPrivilege.checkPrivilege('Home_READ')")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok().body("Hello World");
    }


    @PostMapping("/count")
    @ApiOperation("count")
    public ResponseEntity<Boolean> count(@RequestBody String username) throws URISyntaxException {
        Boolean result = userService.countDisabled(username);
        return ResponseEntity.created(new URI("/apu/v1/count"))
                .body(result);
    }

    @ApiOperation(value = "Insert Master Date")
    @GetMapping("/master/data")
    public ResponseEntity<String> insertMasterData(){
        masterData.insertMasterData();
        return ResponseEntity.ok().body("THIS STG SUCCESS");
    }

    @PostMapping("/reset-count-disabled")
    @ApiOperation("reset-count-disabled")
    public ResponseEntity<Boolean> resetCountDisabled(@RequestBody String username) throws URISyntaxException {
        Boolean result = userService.resetCountDisabled(username);
        return ResponseEntity.created(new URI("/apu/v1/reset-count-disabled"))
                .body(result);
    }


}
