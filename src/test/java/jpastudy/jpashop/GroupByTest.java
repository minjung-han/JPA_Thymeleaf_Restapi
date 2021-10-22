package jpastudy.jpashop;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByTest {
    @Test
    public void groupby(){
        List<Dish> dishList = Arrays.asList(
                new Dish("pork", 357, Type.MEAT),
                new Dish("pasta", 515, Type.NOODLE),
                new Dish("salad", 152, Type.VEG),
                new Dish("onion", 29, Type.VEG),
                new Dish("우럭 회", 250, Type.FISH)
        );
        //Dish의 이름만 출력하기
        List<String> nameList = dishList.stream()
                                        .map(Dish::getName)
                                        .collect(Collectors.toList());
        nameList.forEach(dishName -> System.out.println(dishName));

        //Dish 의 이름을 구분자를 포함한 문자열로 출력하기
        dishList.stream()
                .map(dish -> dish.getName())
                .collect(Collectors.joining("/"));

        //Dish 칼로리 합계 / 평균
        Integer totalCalory = dishList.stream()
                .collect(Collectors.summingInt(dish -> dish.getCalory()));
        System.out.println("totalCalory : "+totalCalory);

        //statistics
        IntSummaryStatistics statistics = dishList.stream()
                .collect(Collectors.summarizingInt(Dish::getCalory));
        System.out.println("statistics : "+statistics);

//      Dish 의 Type별로 그룹핑 하기
        Map<Type, List<Dish>> groupbyType = dishList.stream()
                .collect(Collectors.groupingBy(dish -> dish.getType()));
        System.out.println("그룹핑 : "+groupbyType);

    }



    static class Dish{
        String name;
        int calory;
        Type type;

        public Dish(String name, int calory, Type type) {
            this.name = name;
            this.calory = calory;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getCalory() {
            return calory;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Dish{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }



    enum Type {
        MEAT, FISH, NOODLE, VEG
    }


}
