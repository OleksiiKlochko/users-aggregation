package users.aggregation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import users.aggregation.data.User;
import users.aggregation.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO: need to clarify the strategy of API versioning is used in the project (path variable, header, request param...)
    @Operation(summary = "Get aggregated users from all databases")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found users",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameters are empty",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            )
    })
    @GetMapping("/users")
    public List<User> getUsers(
            @Parameter(description = "IDs of users to be searched") @RequestParam(required = false) List<String> ids,
            @Parameter(description = "Usernames of users to be searched") @RequestParam(required = false) List<String> usernames,
            @Parameter(description = "Names of users to be searched") @RequestParam(required = false) List<String> names,
            @Parameter(description = "Surnames of users to be searched") @RequestParam(required = false) List<String> surnames
    ) {
        if (isEmpty(ids) || isEmpty(usernames) || isEmpty(names) || isEmpty(surnames)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request parameters are empty");
        }
        try {
            return userService.findUsers(ids, usernames, names, surnames);
        } catch (Exception exception) {
            throw new ServerErrorException("Sorry, an error occurred, please try again later", exception);
        }
    }

    private boolean isEmpty(List<String> list) {
        return list != null && list.isEmpty();
    }
}
