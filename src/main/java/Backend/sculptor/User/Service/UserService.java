package Backend.sculptor.User.Service;

import Backend.sculptor.User.Entity.Users;
import Backend.sculptor.User.Repository.UserRepository;
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
        findUser.setIsPublic(isPublic);
        userRepository.save(findUser);
    }
}
