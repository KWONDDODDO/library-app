package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanHistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity //Spring이 User객체와 user테이블을 같은 것으로 바라본다
@Table(name = "\"user\"")
public class User { //이 객체를 통해 실제 user를 저장

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;    //mySqldml BigInt

    @Column(nullable = false, length = 20)
    private String name;
    //@Column(nullable = false, length = 20, name = "age")
    private Integer age;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User() {
    } //JPA를 사용하기 위해서는 기본 생성자가 꼭 필요하다

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(String bookName) {
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName) {
        UserLoanHistory targetHistory = this.userLoanHistories.stream().filter(history -> history.getBookName().equals(bookName)).findFirst().orElseThrow(IllegalAccessError::new);
        targetHistory.doReturn();
    }
}
