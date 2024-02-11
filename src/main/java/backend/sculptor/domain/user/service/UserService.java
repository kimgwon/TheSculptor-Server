package backend.sculptor.domain.user.service;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional
    public UUID setRepresentStone(UUID userId, Stone stone) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (user.getRepresentStone() != null && user.getRepresentStone().getId().equals(stone.getId())) {
            user.setRepresentStone(null);
            userRepository.save(user);
            return null;
        }

        // 사용자의 대표 돌을 설정합니다.
        user.setRepresentStone(stone);
        userRepository.save(user);

        return user.getRepresentStone().getId();
    }
}
