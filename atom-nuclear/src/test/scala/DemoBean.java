public class DemoBean implements DemoInterface {
    String name;
    Integer age;
    Integer height;
    Integer weight;

    DemoBean(String name, Integer age, Integer height, Integer weight) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String demotest() {
        DemoInterface.staticDemo();
        //staticDemo(); //接口的静态方法只能通过类名.方法的手段调用
        return " demo test";
    }
}
