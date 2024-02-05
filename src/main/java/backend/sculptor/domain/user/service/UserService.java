package backend.sculptor.domain.user.service;

import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

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

    public void deleteUser(Users findUser) {
        userRepository.delete(findUser);
    }

    public Users findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    //닉네임, 사용자 실명 둘다로 검색 가능
    public List<Users> searchUser(String username) {
        List<Users> findUserList = userRepository.findByNameOrNickname(username, username);
        return findUserList;
    }
}
