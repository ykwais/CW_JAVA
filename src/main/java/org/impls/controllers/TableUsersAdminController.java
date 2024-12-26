package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableUsersAdminController extends BaseController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<UserRow> tableView;

    @FXML
    private TableColumn<UserRow, String> loginColumn;

    @FXML
    private TableColumn<UserRow, String> emailColumn;

    @FXML
    private TableColumn<UserRow, String> realNameColumn;

    @FXML
    private TableColumn<UserRow, String> createdAtColumn;

    @FXML
    private TableColumn<UserRow, Integer> totalBookingsColumn;

    @FXML
    private TableColumn<UserRow, Void> actionColumn;

    public void initialize() {
        backButton.setOnAction(event -> handleBack());

        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        realNameColumn.setCellValueFactory(new PropertyValueFactory<>("realName"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        totalBookingsColumn.setCellValueFactory(new PropertyValueFactory<>("totalBookings"));

        setupActionColumn();

        Platform.runLater(() -> {
            mainController.getClient().sendGetUsersForAdminRequest(new StreamObserver<Rental.GetUsersForAdminResponse>() {
                @Override
                public void onNext(Rental.GetUsersForAdminResponse response) {
                    Platform.runLater(() -> {
                        for (Rental.DataUsersForAdmin user : response.getDataUsersList()) {
                            UserRow userRow = new UserRow(
                                    user.getUserId(),
                                    user.getLogin(),
                                    user.getEmail(),
                                    user.getRealName(),
                                    user.getCreatedAt(),
                                    user.getTotalBookings()
                            );
                            tableView.getItems().add(userRow);
                        }
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error fetching users: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("GetUsers request completed.");
                }
            });
        });
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<UserRow, Void> call(TableColumn<UserRow, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            UserRow user = getTableView().getItems().get(getIndex());
                            handleDeleteUser(user);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(deleteButton);
                            setGraphic(box);
                        }
                    }
                };
            }
        });
    }

    private void handleDeleteUser(UserRow user) {
        System.out.println("Deleting user: " + user);

        mainController.getClient().sendDeleteUserRequest(user.getUserId(), new StreamObserver<Rental.DeleteUserResponse>() {
            @Override
            public void onNext(Rental.DeleteUserResponse response) {
                Platform.runLater(() -> {
                    tableView.getItems().remove(user);
                    System.out.println("User deleted: " + response.getResult());
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error deleting user: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Delete user request completed.");
            }
        });
    }

    private void handleBack() {
        switchScene("admin_view.fxml");
    }

    public static class UserRow {
        private final long userId;
        private final String login;
        private final String email;
        private final String realName;
        private final String createdAt;
        private final long totalBookings;

        public UserRow(long userId, String login, String email, String realName, String createdAt, long totalBookings) {
            this.userId = userId;
            this.login = login;
            this.email = email;
            this.realName = realName;
            this.createdAt = createdAt;
            this.totalBookings = totalBookings;
        }

        public long getUserId() {
            return userId;
        }

        public String getLogin() {
            return login;
        }

        public String getEmail() {
            return email;
        }

        public String getRealName() {
            return realName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public long getTotalBookings() {
            return totalBookings;
        }
    }
}
