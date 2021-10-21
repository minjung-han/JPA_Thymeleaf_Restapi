package jpastudy.jpashop;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
    @Test
    public void stream(){
        List<User> users = List.of(new User("민정", 26), new User("Mongta", 58),
                new User("길동", 25),new User("Boot",18));
        //User 의 Name 만 추출해서 List<String> 으로 변환해서 출력
//        List<String> nameList =
//                users.stream() // =>return type : Stream<User>
//                     .map(user -> user.getName())
//                    //map 의 T는 input 타입을 의미하는데 여기서 input 타입은 User 이다
//                        // user의 Name을 가져오기 때문에 return type : Stream<String>
//                        //Stream 에 담겨있는 String을 List로 바꿔야 한다.
//                    .collect(Collectors.toList()); // => return type : List<String> 으로 변함

        //축약
        List<String> nameList = users.stream()
                                .map(User::getName).collect(Collectors.toList());


//        nameList.forEach(name -> System.out.println(name));
        //위의 출력문 축약
//        nameList.forEach(System.out::println);

        //20살 이상인 User의 name 추출해서 List<String>으로 변환해서 출력
        users.stream()
                .filter(user -> user.getAge() >= 20)
                .forEach(System.out::println);

        //20살 이상인 User의 name만 추출해서 List<String>으로 변환해서 출력
        users.stream()
                .filter(user -> user.getAge() >= 20)
                .forEach(user -> System.out.println(user.getName()));

        //20살 이상인 User의 name만 추출해서 List<String>으로 변환해서 출력
        List<String> names = users.stream()
                            .filter(user -> user.getAge() >= 20)
                            .map(user -> user.getName())
                            .collect(Collectors.toList());
//        names.forEach(System.out::println);


        //User 들의 나이 합계
        int sum = users.stream()
                .mapToInt(user -> user.getAge()) // return type : IntStream
                .sum();

        System.out.println("나이 합계 : "+sum);



    }

    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;
    }





}
