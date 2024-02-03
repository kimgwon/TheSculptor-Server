package backend.sculptor.domain.follow.service;

import backend.sculptor.domain.follow.dto.FollowSimpleListDto;
import backend.sculptor.domain.follow.entity.Follow;
import backend.sculptor.domain.follow.repository.FollowRepository;
import backend.sculptor.domain.stone.dto.StoneDTO;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.user.entity.Users;
import backend.sculptor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final StoneRepository stoneRepository;
    private final FollowRepository followRepository;
    public List<FollowSimpleListDto> getFollowingList(UUID userId){
        return followRepository.findAllByFromUser(userId);
    }

    public StoneDTO searchStone(UUID representStoneId) {
        Stone stone = stoneRepository.findById(representStoneId).orElseThrow(NoSuchElementException::new);
        StoneDTO stoneInfo = new StoneDTO(stone.getId(), stone.getStoneName(), stone.getCategory(),stone.getStoneGoal());
        return stoneInfo;
    }
}
