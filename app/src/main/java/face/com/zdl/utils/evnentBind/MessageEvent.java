package face.com.zdl.utils.evnentBind;

import java.util.List;

public class MessageEvent {


    private String message;

    List<Bc> bcs;


    public List<Bc> getBcs() {
        return bcs;
    }

    public void setBcs(List<Bc> bcs) {
        this.bcs = bcs;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static class Bc {


        private String name;

        private int age;

        public Bc(String name, int age) {
            this.name = name;
            this.age = age;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
