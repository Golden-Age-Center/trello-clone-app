package com.sparta.trelloproject.domain.list.repository;

import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.list.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<Lists, Long>, ListQueryRepository {
    // 혹시나 QueryDsl에 문제가 있다면 해당 코드로 원복
    /*
    @Modifying
    @Query("UPDATE Lists SET sequence = sequence + 1 WHERE sequence >= ?1 AND sequence < ?2")
    void updateSequenceIncrement(int sequence, int originSequence);

    @Modifying
    @Query("UPDATE Lists SET sequence = sequence - 1 WHERE sequence <= ?1 AND sequence > ?2")
    void updateSequenceDecrement(int sequence, int originSequence);
    */

    default Lists findByListId(long listId) {
        return findById(listId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_LIST));
    }
}
