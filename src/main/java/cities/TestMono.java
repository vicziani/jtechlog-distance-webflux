package cities;

import reactor.core.publisher.Mono;

import java.util.Optional;

public class TestMono {

    public static void main(String[] args) {
        new TestMono().withNameAndOptionalAddress(Mono.just("John Doe"), Mono.empty())
            .subscribe(System.out::println);
    }


    public Mono<Employee> withNameAndOptionalAddress(Mono<String> name, Mono<String> address) {
        return name.zipWith(address.map(Optional::of).defaultIfEmpty(Optional.empty()),
                (nameParam, addrParam) -> new Employee(nameParam, addrParam.orElse(null)));

    }

    public static class Employee {
        String name;

        String address;

        public Employee(String name, String address) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
