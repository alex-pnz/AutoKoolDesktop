package com.fullstackhub.autokool.sevices;

import com.fullstackhub.autokool.models.User;


import com.fullstackhub.autokool.utils.FXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest{// extends ApplicationTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @Mock
    private ActionEvent actionEvent;
    @Mock
    private FXUtils fxUtils;
    @InjectMocks
    LoginService loginService;

    private MockedStatic<DriverManager> driverManagerMockedStatic;
    private static final int USER_ID = 1;

//    @Override
//    public void start(Stage stage) throws Exception {
//        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
//        Parent root = fxmlLoader.load();
//
//        stage.setScene(new Scene(root));
//        stage.show();
//
//
//    }


    @BeforeEach
    public void setDataSource(){
        try {
            driverManagerMockedStatic = mockStatic(DriverManager.class);
            when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void allDone() throws Exception{

        driverManagerMockedStatic.close();

//        FxToolkit.hideStage();
//        release(new KeyCode[]{});
//        release(new MouseButton[]{});
    }

    @Test
    void testUserLogin() throws SQLException, IOException {

        User user = new User("Alex","al111");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(true);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id")).thenReturn(USER_ID);
        when(resultSet.getString("username")).thenReturn("Alex");
        when(resultSet.getString("password")).thenReturn("al111");
        when(resultSet.getString("role")).thenReturn("USER");


//        clickOn("#textFieldUsername").write("Alex");
//        clickOn("#textFieldPassword").write("al111");
//        clickOn("#buttonLogin");

        boolean b = loginService.logUserIn(actionEvent, user);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(fxUtils,atLeastOnce()).showScreen(any(), argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertTrue(b);

        Assertions.assertThat(actualUser.getId()).isEqualTo(USER_ID);
        Assertions.assertThat(actualUser.getRole()).isEqualTo(User.Role.USER);

    }

    @Test
    void testAdminLogin() throws SQLException, IOException {

        User user = new User("Alex","al111");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(true);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id")).thenReturn(USER_ID);
        when(resultSet.getString("username")).thenReturn("Alex");
        when(resultSet.getString("password")).thenReturn("al111");
        when(resultSet.getString("role")).thenReturn("ADMIN");

        boolean b = loginService.logUserIn(actionEvent, user);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(fxUtils,atLeastOnce()).showScreen(any(), argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertTrue(b);

        Assertions.assertThat(actualUser.getId()).isEqualTo(USER_ID);
        Assertions.assertThat(actualUser.getRole()).isEqualTo(User.Role.ADMIN);

    }

    @Test
    void testUserNotFound() throws SQLException {

        User user = new User("Alex","al111");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(false);

        boolean b = loginService.logUserIn(actionEvent, user);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(fxUtils,atLeastOnce()).showAlert(argumentCaptor.capture());
        String actualMessage = argumentCaptor.getValue();

        assertFalse(b);
        Assertions.assertThat(actualMessage).isEqualTo("User not found!");

    }

    @Test
    void testWrongPass() throws SQLException {

        User user = new User("Alex","wrong");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(true);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id")).thenReturn(USER_ID);
        when(resultSet.getString("username")).thenReturn("Alex");
        when(resultSet.getString("password")).thenReturn("al111");
        when(resultSet.getString("role")).thenReturn("USER");

        boolean b = loginService.logUserIn(actionEvent, user);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(fxUtils,atLeastOnce()).showAlert(argumentCaptor.capture());
        String actualMessage = argumentCaptor.getValue();

        assertFalse(b);
        Assertions.assertThat(actualMessage).isEqualTo("Wrong password!");

    }

    @Test
    void testIOException() throws SQLException, IOException {

        User user = new User("Alex","al111");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.isBeforeFirst()).thenReturn(true);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("id")).thenReturn(USER_ID);
        when(resultSet.getString("username")).thenReturn("Alex");
        when(resultSet.getString("password")).thenReturn("al111");
        when(resultSet.getString("role")).thenReturn("USER");

        doAnswer(
                invocation -> {
                    throw new IOException();
                }).when(fxUtils).showScreen(any(),any());

        boolean b = loginService.logUserIn(actionEvent, user);

        assertFalse(b);

    }

    @Test
    void testSQLException() throws SQLException {

        User user = new User("Alex","al111");

        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        boolean b = loginService.logUserIn(actionEvent, user);

        assertFalse(b);
    }


}