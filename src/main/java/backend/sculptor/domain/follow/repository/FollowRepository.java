package backend.sculptor.domain.follow.repository;

import backend.sculptor.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {
    Long countByToUserId(UUID userId);    // 팔로워 수 (follower)
    Long countByFromUserId(UUID userId);  // 팔로우 수 (following)

    //List<FollowSimpleListDto> findAllByFromUser(UUID userId); // 사용자가 팔로우한 관계를 가져옴

//    @Query("SELECT new backend.sculptor.domain.follow.dto.FollowSimpleListDto(f.id, f.nickname, f.representStoneId) " +
//            "FROM Follow f WHERE f.fromUser = :userId")
//    List<FollowSimpleListDto> findAllByFromUser(@Param("userId") UUID userId);
    //List<FollowSimpleListDto> findAllByToUser(UUID userId);	 // 사용자를 팔로우하는 관계를 가져옴

    List<Follow> findAllByFromUserId(UUID userId);
    List<Follow> findAllByToUserId(UUID toUser);
    Optional<Follow> findByFromUserIdAndToUserId(UUID fromUserId, UUID toUserId);
    boolean existsByFromUserIdAndToUserId(UUID followerId, UUID followeeId);
}
