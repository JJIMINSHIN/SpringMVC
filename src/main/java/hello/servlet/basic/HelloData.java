package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HelloData {

    private String username;
    private int age;

    // getter and setter 단축키 : alt+insert
    /*
    lombok 활용하면 아래 내용 선언 필요없음

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    */

}
