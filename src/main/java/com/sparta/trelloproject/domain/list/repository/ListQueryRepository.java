package com.sparta.trelloproject.domain.list.repository;

public interface ListQueryRepository {
    void updateSequence(long listId, int sequence);

    void updateSequenceIncrement(Integer sequence, Integer originSequence, long boardId);

    void updateSequenceDecrement(Integer sequence, Integer originSequence, long boardId);
}
