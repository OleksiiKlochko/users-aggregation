package users.aggregation.service;

import org.openapitools.model.User;
import org.springframework.stereotype.Service;
import users.aggregation.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final List<UserRepository> repositories;

    public UserService(List<UserRepository> repositories) {
        this.repositories = repositories;
    }

    public List<User> findUsers(List<String> ids, List<String> usernames, List<String> names, List<String> surnames) {
        // it looks like parallelization makes sense since each repository connects to its own database
        return repositories.parallelStream()
                .flatMap(repository -> repository.findUsers(ids, usernames, names, surnames))
                .toList();
    }
}
