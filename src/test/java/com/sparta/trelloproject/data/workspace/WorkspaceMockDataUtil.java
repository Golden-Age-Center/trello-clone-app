package com.sparta.trelloproject.data.workspace;

import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import org.springframework.test.util.ReflectionTestUtils;

import static com.sparta.trelloproject.data.user.UserMockDataUtil.mockAdmin;

public class WorkspaceMockDataUtil {
    public static Workspace mockWorkspace() {
        Workspace workspace =  Workspace.from("workspaceName", "workspaceDescription", mockAdmin());
        ReflectionTestUtils.setField(workspace, "id", Long.valueOf(1));

        return workspace;
    }

    public static UserWorkspace mockUserWorkspace() {
        UserWorkspace userWorkspace = UserWorkspace.from(mockWorkspace(), mockAdmin(), WorkSpaceUserRole.ROLE_EDIT_USER);
        ReflectionTestUtils.setField(userWorkspace, "id", 1L);

        return userWorkspace;
    }
}
