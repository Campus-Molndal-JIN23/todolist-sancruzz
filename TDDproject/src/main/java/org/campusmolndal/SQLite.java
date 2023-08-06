
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SQLite {




    public class SQLite {
        private Connection connection;
        private final String databaseName;
        private final String connectionString;

        public SQLite(String connectionString, String databaseName) {
            this.connectionString = connectionString;
            this.databaseName = databaseName;
            Logger.getLogger("org.sqlite").setLevel(Level.SEVERE);
            connectToDatabase();
        }

        private void connectToDatabase() {
            try {
                connection = DriverManager.getConnection(connectionString + databaseName);
                createTodoTableIfNotExists();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to SQLite.", e);
            }
        }

        private void createTodoTableIfNotExists() {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS todos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "text TEXT NOT NULL," +
                    "done INTEGER NOT NULL)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create 'todos' table.", e);
            }
        }

        public void createTodo(Todo todo) {
            String insertQuery = "INSERT INTO todos (text, done) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, todo.getText());
                preparedStatement.setInt(2, todo.isDone() ? 1 : 0);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create todo in SQLite.", e);
            }
        }


        public Todo getTodoById(String id) {
            String selectQuery = "SELECT * FROM todos WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Todo todo = new Todo(resultSet.getString("text"));
                        todo.setId(resultSet.getInt("id"));
                        if(resultSet.getInt("done") == 1) {
                            todo.setCompleted(true);
                        } else {
                            todo.setCompleted(false);
                        } return todo;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get todo by ID from SQLite.", e);
            }
            throw new IllegalArgumentException("Todo with ID " + id + " not found.");
        }

        public void markTodoAsDone(int id) {
            String updateQuery = "UPDATE todos SET done = 1 WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new IllegalArgumentException("Todo with ID " + id + " not found.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to mark todo as done in SQLite.", e);
            }
        }

        public void updateTodoText(int id, String updatedText) {
            String updateQuery = "UPDATE todos SET text = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, updatedText);
                preparedStatement.setInt(2, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new IllegalArgumentException("Todo with ID " + id + " not found.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update todo text in SQLite.", e);
            }
        }

        public void deleteTodoById(int id) {
            String deleteQuery = "DELETE FROM todos WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new IllegalArgumentException("Todo with ID " + id + " not found.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete todo by ID in SQLite.", e);
            }
        }

        public ArrayList<Todo> getAllTodos() {
            String selectAllQuery = "SELECT * FROM todos";
            ArrayList<Todo> allTodos = new ArrayList<>();
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectAllQuery)) {
                while (resultSet.next()) {
                    Todo todo = new Todo(resultSet.getString("text"));
                    todo.setId(resultSet.getInt("id"));
                    if(resultSet.getInt("done") == 1) {
                        todo.setCompleted(true);
                    } else {
                        todo.setCompleted(false);
                    }
                    allTodos.add(todo);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get all todos from SQLite.", e);
            }
            return allTodos;
        }

        public void closeConnection() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close SQLite connection.", e);
            }
        }
    }

}
