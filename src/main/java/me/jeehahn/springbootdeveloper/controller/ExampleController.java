package me.jeehahn.springbootdeveloper.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExampleController {

    @GetMapping("thymeleaf/example")
    public String thymeleafExample(Model model) {
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("Mimi Noordmans");
        examplePerson.setAge(29);
        examplePerson.setHobbies(List.of("football", "gym"));

        model.addAttribute("person", examplePerson);
        model.addAttribute("currentDate", LocalDate.now());

        return "example"; // the name of the Thymeleaf template
    }

}

@Data // Combines @Getter, @Setter, @EqualsAndHashCode, @ToString
class Person {
    private Long id;
    private String name;
    private int age;
    private List<String> hobbies;
}
