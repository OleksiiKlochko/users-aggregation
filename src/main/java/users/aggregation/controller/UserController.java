package users.aggregation.controller;

import org.openapitools.api.UsersApi;
import org.openapitools.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
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
        if (isEmpty(ids) || isEmpty(usernames) || isEmpty(names) || isEmpty(surnames)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request parameters are empty");
        }
        try {
            return ResponseEntity.ok(userService.findUsers(ids, usernames, names, surnames));
        } catch (Exception exception) {
            throw new ServerErrorException("Sorry, an error occurred, please try again later", exception);
        }
    }

    private boolean isEmpty(List<String> list) {
        return list != null && list.isEmpty();
    }
}
