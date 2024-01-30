package backend.sculptor.domain.user.service;

import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void updateNickname(Users findUser, String newNickname) {
        findUser.setNickname(newNickname);
        userRepository.save(findUser);
    }

    public void updatePublic(Users findUser, Boolean isPublic) {
        findUser.setIs_public(isPublic);
        userRepository.save(findUser);
    }

    public void deleteUser(Users findUser) {
        userRepository.delete(findUser);
    }
}
