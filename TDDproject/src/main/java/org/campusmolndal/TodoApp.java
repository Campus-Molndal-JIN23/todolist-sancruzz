package org.campusmolndal;






    public class TodoApp {
        public static void main(String[] args) {
            String databaseName = "todos.db"; // Byt ut detta mot önskad SQLite-databasfil
            String connectionString = "jdbc:sqlite:";

            SQLite sqlite = new SQLite(connectionString, databaseName);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Välkommen till TodoApp!");

            while (true) {
                System.out.println("\nVälj en åtgärd:");
                System.out.println("1. Lägg till en ny todo");
                System.out.println("2. Markera todo som klar");
                System.out.println("3. Uppdatera todo-text");
                System.out.println("4. Ta bort todo");
                System.out.println("5. Visa alla todos");
                System.out.println("0. Avsluta");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumes the newline character after reading the integer

                switch (choice) {
                    case 1:
                        System.out.println("Ange todo-text:");
                        String text = scanner.nextLine();
                        Todo newTodo = new Todo(text);
                        sqlite.createTodo(newTodo);
                        System.out.println("Todo har skapats.");
                        break;
                    case 2:
                        System.out.println("Ange ID för todo som ska markeras som klar:");
                        int doneId = scanner.nextInt();
                        scanner.nextLine(); // Consumes the newline character after reading the integer
                        sqlite.markTodoAsDone(doneId);
                        System.out.println("Todo har markerats som klar.");
                        break;
                    case 3:
                        System.out.println("Ange ID för todo som ska uppdateras:");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consumes the newline character after reading the integer
                        System.out.println("Ange den uppdaterade todo-texten:");
                        String updatedText = scanner.nextLine();
                        sqlite.updateTodoText(updateId, updatedText);
                        System.out.println("Todo har uppdaterats.");
                        break;
                    case 4:
                        System.out.println("Ange ID för todo som ska tas bort:");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // Consumes the newline character after reading the integer
                        sqlite.deleteTodoById(deleteId);
                        System.out.println("Todo har tagits bort.");
                        break;
                    case 5:
                        System.out.println("Alla todos:");
                        printAllTodos(sqlite);
                        break;
                    case 0:
                        System.out.println("Avslutar TodoApp.");
                        sqlite.closeConnection();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Ogiltigt val. Försök igen.");
                }
            }
        }

        private static void printAllTodos(SQLite sqlite) {
            for (Todo todo : sqlite.getAllTodos()) {
                System.out.println(todo.getId() + ". " + todo.getText() + " - " + (todo.isDone() ? "Klar" : "Ej klar"));
            }
        }
    }

