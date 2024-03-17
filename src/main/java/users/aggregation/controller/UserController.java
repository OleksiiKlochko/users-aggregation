package users.aggregation.controller;

import org.openapitools.api.UsersApi;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import users.aggregation.service.UserService;

import java.util.List;

@RestController
public class UserController implements UsersApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<User>> getUsers(
            List<String> ids,
            List<String> usernames,
            List<String> names,
            List<String> surnames
    ) {
        return ResponseEntity.ok(userService.findUsers(ids, usernames, names, surnames));
    }
}
