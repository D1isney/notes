package com.redis04_lock;

import com.redis04_lock.entity.Person;
import com.redis04_lock.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Redis04LockApplicationTests {

    @Test
    void contextLoads() {

        Person p = new Student();

        p.run();

        Student student = (Student) new Person();

        student.run();

        //  封装 继承 多态
        student.run("123");

    }

}
