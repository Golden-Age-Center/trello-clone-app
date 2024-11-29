package com.sparta.trelloproject.domain.list.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trelloproject.common.config.JPAConfiguration;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.repository.BoardRepository;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import com.sparta.trelloproject.domain.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.trelloproject.data.board.BoardMockDataUtil.mockBoard;
import static com.sparta.trelloproject.data.user.UserMockDataUtil.mockAdmin;
import static com.sparta.trelloproject.data.user.UserMockDataUtil.mockUser;
import static com.sparta.trelloproject.data.workspace.WorkspaceMockDataUtil.mockUserWorkspace;
import static com.sparta.trelloproject.data.workspace.WorkspaceMockDataUtil.mockWorkspace;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(JPAConfiguration.class)
@ExtendWith(SpringExtension.class)
class ListRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserWorkSpaceRepository userWorkSpaceRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ListRepository listRepository;

    private User user;
    private User admin;
    private Workspace workspace;
    private UserWorkspace userWorkspace;
    private Board board;

    @BeforeEach
    void setup() {
        user = mockUser();
        userRepository.save(user);

        admin = mockAdmin();
        userRepository.save(admin);

        workspace = mockWorkspace();
        workspaceRepository.save(workspace);

        userWorkspace = mockUserWorkspace();
        userWorkSpaceRepository.save(userWorkspace);

        board = mockBoard();
        boardRepository.save(board);
    }

    @Test
    @DisplayName("리스트 순서 변경 > 더 작은 순서로")
    public void updateListSequence_lessThanOrEquals() {
        // given
        int sequence = 4;
        int originSequence = 7;

        List<Lists> savedLists = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Lists lists = Lists.of("title" + i, i, board);
            savedLists.add(listRepository.save(lists));
        }

        // when
        listRepository.updateSequenceIncrement(sequence, originSequence, board.getId());
        listRepository.updateSequence(savedLists.get(originSequence - 1).getId(), sequence);

        // then
        // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 이 있을 때
        // 7을 4로 옮기면
        // 1, 2, 3, 7, 4, 5, 6, 8, 9, 10 으로 변해야한다.

        List<String> expectedSequence =
                List.of("title1", "title2", "title3", "title7", "title4", "title5", "title6", "title8", "title9", "title10");
        List<String> actualSequence = listRepository.findAll(Sort.by("sequence"))
                .stream().map(Lists::getTitle).toList();

        assertEquals(expectedSequence, actualSequence);
    }

}