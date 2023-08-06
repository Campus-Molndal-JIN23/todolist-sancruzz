package org.campusmolndal;



    public class Todo { // OK 22.17

        private int id;
        private String text;
        private boolean done;
        public Todo(String text) {
            this.text = text;
            done = false;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isDone() {
            return done;
        }

        public void setCompleted(boolean done) {
            this.done = done;
        }
    }

