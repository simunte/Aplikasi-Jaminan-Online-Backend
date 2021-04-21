package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.RoleService;
import com.ebizcipta.ajo.api.service.UserService;
import com.ebizcipta.ajo.api.service.dto.RoleListDTO;
import com.ebizcipta.ajo.api.service.dto.UserCreateDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/apu/v1")
@Api(value="Public Api")
public class HelloWorldController {
    private final MasterData masterData;
    private final ServiceSchedulerUtil serviceSchedulerUtil;
    private final UserService userService;
    private final RoleService roleService;

    public HelloWorldController(MasterData masterData, ServiceSchedulerUtil serviceSchedulerUtil, UserService userService, RoleService roleService) {
        this.masterData = masterData;
        this.serviceSchedulerUtil = serviceSchedulerUtil;
        this.userService = userService;
        this.roleService = roleService;
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

    @GetMapping("/roles")
    @ApiOperation("get All Role")
    public ResponseEntity<List<RoleListDTO>> getAllRole(){
        List<RoleListDTO> result = roleService.findAllRole();
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping("/users")
    @ApiOperation("Add User")
    public ResponseEntity<Boolean> addUser(@Valid @RequestBody UserCreateDTO userCreateDTO) throws URISyntaxException {
        Boolean result = userService.saveUser(userCreateDTO);
        return ResponseEntity.created(new URI("/api/v1/users"+userCreateDTO.getUsername()))
                .body(result);
    }
}
